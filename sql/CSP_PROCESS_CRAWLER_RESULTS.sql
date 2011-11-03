-- ================================================
-- proc to populate 'CS_ExceptionKeyword' metadata from crawler results
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Ravikishores@TS-India
-- Create date: 9 March 2009
-- Description:	
-- Assumptions: 1. CS_KEYWORD_REGEX_MAPPING table is present in SCRIBBLE DB
--				2. Crawler results are stored in the temp table named @TEMP_RES_TABLE
-- =============================================
ALTER PROCEDURE DBADMIN.CSP_PROCESS_CRAWLER_RESULTS 	
	@TEMP_RES_TABLE NVARCHAR(500), --temp table where crawler results were uploaded in SCRIBBLE DB	
	@SELECT_BIT BIT = 1			--debug bit to display error info
AS
BEGIN		
	--DECLARATIONS
	DECLARE @COUNT_CHK INT
	DECLARE @CURR_DB VARCHAR(200)
	DECLARE @TEMP_CURSOR CURSOR 	
	DECLARE @KEYWORDVAL NVARCHAR(500)
	DECLARE @REGEX NVARCHAR(500)
	DECLARE @KEYWORD_EMD_GID INT
	DECLARE @SQL_STRING NVARCHAR(1000)
	DECLARE @ERRORCODE INT
	DECLARE @ERRORLABEL NVARCHAR(500)	
	DECLARE @COUNT_INSERTED INT
	
	SET @CURR_DB = db_name()
	PRINT ' Current DB : ' + @CURR_DB

	--VALIDATIONS
	--check if mapping table exists 
	SELECT @COUNT_CHK = COUNT(name) FROM dbo.sysobjects       
	WHERE id = object_id('DBADMIN.CS_KEYWORD_REGEX_MAPPING')       
      
	IF (@COUNT_CHK = 0)      
	  BEGIN      
	  SET @ERRORCODE = -20000       
	  SET @ERRORLABEL = 'Table ' + @CURR_DB + '.DBADMIN.CS_KEYWORD_REGEX_MAPPING does not exist. Terminating proc execution..'      
	  GOTO ERROR_LABEL      
	  END      

	--check if result temp table exists
	SELECT @COUNT_CHK = COUNT(name) FROM dbo.sysobjects       
	WHERE id = object_id('DBADMIN.'+@TEMP_RES_TABLE)       
      
	IF (@COUNT_CHK <= 0)      
	  BEGIN      
	  SET @ERRORCODE = -20001       
	  SET @ERRORLABEL = 'Table '+ @CURR_DB + '.DBADMIN.' + @TEMP_RES_TABLE+' does not exist. Terminating proc execution..'      
	  GOTO ERROR_LABEL      
	  END      

	--HOUSEKEEPING STUFFS
	--drop staging table if exists
	IF EXISTS(SELECT * FROM dbo.sysobjects WHERE id = object_id(N'DBADMIN.TEMP_CRAWLER_STAGE'))
	BEGIN
	DROP TABLE DBADMIN.TEMP_CRAWLER_STAGE
	PRINT 'Staging table TEMP_CRAWLER_STAGE dropped.'
	END

	--create staging table
	CREATE TABLE DBADMIN.TEMP_CRAWLER_STAGE(
	DOC_GID INT,
	[VALUE] NVARCHAR(768) COLLATE Latin1_General_CI_AS
	)
	PRINT 'New Staging table TEMP_CRAWLER_STAGE created.'	

	--loop thru entries in mapping table
	--execute queries to populate into temp table
	SET @TEMP_CURSOR = CURSOR LOCAL FAST_FORWARD FOR                  
	SELECT [Exception Keyword Value] , Regex FROM DBADMIN.CS_KEYWORD_REGEX_MAPPING
	                   
	OPEN @TEMP_CURSOR                  
	                   
	WHILE (1=1)                  
	BEGIN                  
	  	FETCH FROM @TEMP_CURSOR INTO @KEYWORDVAL, @REGEX
	                   
  		IF (@@FETCH_STATUS<>0) BREAK 
		--here we ll need to use dynamic sql		
		
		SET @SQL_STRING = 'INSERT INTO DBADMIN.TEMP_CRAWLER_STAGE '
		+ 'SELECT DOCUMENT_METADATA_ENTRY_GID,''' + @KEYWORDVAL + ''' FROM'
		+ ' PWC005.DBADMIN.DS_DOCUMENT_HASH_ID(NOLOCK) WHERE CONTENT_HASH_ID IN ('
		+ ' SELECT CONTENT_HASH_ID FROM PWC005.DBADMIN.DS_DOCUMENT_HASH_ID(NOLOCK) WHERE'
		+ ' DOCUMENT_METADATA_ENTRY_GID IN ('
		+ ' SELECT RES.GID FROM  SCRIBBLE.DBADMIN.'
		+ @TEMP_RES_TABLE 
		+ ' RES WHERE RES.RESULT = ''searched'''
		+ ' AND [' + @REGEX  + '] IS NOT NULL));  SELECT @COUNT_INSERTED_1 = @@ROWCOUNT;'

	PRINT 'Query Used: ' + @SQL_STRING

	EXEC sp_executesql  @SQL_STRING, N'@COUNT_INSERTED_1 INT OUTPUT ', @COUNT_INSERTED OUTPUT

	IF @@ERROR <> 0
	BEGIN               
	   SET @ERRORCODE = -20002              
	   SET @ERRORLABEL = 'FAILED : WHILE POPULATING INTO STAGE TABLE'               
	   GOTO ERROR_LABEL              
	END

		PRINT 'Num of rows populated for regex :' + @REGEX + ' is ' + CAST(@COUNT_INSERTED AS VARCHAR(30))		
	END
	
	--clean up the mess created
	CLOSE @TEMP_CURSOR                  
	DEALLOCATE @TEMP_CURSOR  

	SELECT @KEYWORD_EMD_GID  = GID FROM PWC005.DBADMIN.DS_METADATA_PROPERTIES(NOLOCK)
	WHERE HIERARCHY_GID = (SELECT GID FROM PWC005.DBADMIN.DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL)
	AND [NAME] = 'CS_ExceptionKeyword'

	--try creating the metadata if its not already created
	IF @KEYWORD_EMD_GID IS NULL
	BEGIN 
		PRINT 'CS_ExceptionKeyword metadata is not yet created. Trying to create..'
		
		INSERT INTO PWC005.DBADMIN.DS_METADATA_PROPERTIES VALUES
		(19, 'CS_ExceptionKeyword', 'ON',0, 'customer_specific_property', 0, 0, 'Indicates the exception keyword which hit the document. See SII-11317 for details', 'DS_EXTENDED_METADATA_CUSTOM' )
		
		SELECT @KEYWORD_EMD_GID  = GID FROM PWC005.DBADMIN.DS_METADATA_PROPERTIES(NOLOCK)
		WHERE HIERARCHY_GID = (SELECT GID FROM PWC005.DBADMIN.DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL)
		AND [NAME] = 'CS_ExceptionKeyword'
	END
		
	--populate metadata into EMC table
	INSERT INTO PWC005.DBADMIN.DS_EXTENDED_METADATA_CUSTOM(DOCUMENT_METADATA_ENTRY_GID, [VALUE], EXTENDED_PROPERTY_GID)
	SELECT DISTINCT DOC_GID, [VALUE],@KEYWORD_EMD_GID FROM  
	SCRIBBLE.DBADMIN.TEMP_CRAWLER_STAGE RES(NOLOCK)
	WHERE NOT EXISTS (
		SELECT 'X' FROM PWC005.DBADMIN.DS_EXTENDED_METADATA_CUSTOM CUST(NOLOCK)
		WHERE CUST.DOCUMENT_METADATA_ENTRY_GID = RES.DOC_GID
		AND CUST.[VALUE] = RES.[VALUE]
		AND CUST.EXTENDED_PROPERTY_GID = @KEYWORD_EMD_GID
	)
    
	PRINT ' Entries ' + CAST(@@ROWCOUNT AS VARCHAR(200)) + ' populated into EMC table.' 
	RETURN

	--error block
	ERROR_LABEL:   
	SET @ERRORLABEL = 'Error: ' + @ERRORLABEL     
	IF(@SELECT_BIT = 1) SELECT @ERRORCODE, @ERRORLABEL   	
END
GO
