CREATE PROCEDURE CSP_INDEX_TRANSLATED_DOCS (         
 @SRC_DOCS_TABLE NVARCHAR(512),
 @CATALOG_NAME VARCHAR(254), 
 @ERRORCODE INTEGER OUTPUT,     
 @ERRORLABEL VARCHAR(1000) OUTPUT,    
 @DEBUG_FLAG BIT = 1    
)     
AS        
BEGIN    
  
/**********************************************************************  
**Name:    CSP_INDEX_TRANSLATED_DOCS  
**Author:   R K Shakya 
**Creation Date: 16 January 2009  
**Version:   1.1  
**SLDS Compatibility: SLDS 8.0  
**Inputs:  A temp table(@SRC_DOCS_TABLE) containing DOCUMENT_METADATA_ENTRY_GID and PATH (of translated/converted docs)        
**Usage:   DECLARE @ERRORCODE INT, @ERRORLABEL NVARCHAR(1000)  
     EXEC CSP_INDEX_TRANSLATED_DOCS <SRC_DOCS_TABLE_NAME>, <CATALOG_NAME> 
      @ERRORCODE OUTPUT, @ERRORLABEL OUTPUT
	SELECT @ERRORCODE, @ERRORLABEL
**Prerequisites: 1) A table named CS_FULLTEXT_ENTRY (similar in structure to DS_FULLTEXT_ENTRY) should be present in the DB.
		Essentially it will be holding GID, PATH of translated docs that were indexed, CATALOG_NAME(e.g. FULLTEXTCAT_7) and TYPE(e.g. Translation). If not present, this proc
		will create it.
		2) Fulltext catalog that's to be used for indexing translated docs should have been created.
		
**Description:  
   This proc inserts the entries from the @SRC_DOCS_TABLE into CS_FULLTEXT_ENTRY and then index those docs.
  The following steps are performed by the procedure:  
  1. Do validity checks on the table names and schema  
  2. Inserts the entries into CS_FULLTEXT_ENTRY.   
  3. Docs from @SRC_DOCS_TABLE are sent for indexing. 
  4. If any entries(from translated universe) were inserted into DS_FULLTEXT_ENTRY by DSP_INDEX_DOCUMENTS
  delete them.
**********************************************************************/  
SET NOCOUNT ON    
   --var declarations  
	DECLARE @SQL_STRING_CS NVARCHAR(4000) 
	DECLARE @SQL_STRING NVARCHAR(4000)
	DECLARE @HIERARCHY_ID INTEGER
	DECLARE @LANGUAGE_ID VARCHAR(50)
	DECLARE @INDEX_TYPE VARCHAR(100)
	DECLARE @ACTION_TYPE BIT	
	DECLARE @COUNT_INS INTEGER
	DECLARE @COUNT_DEL INTEGER
        
 IF(@SRC_DOCS_TABLE IS NOT NULL OR LTRIM(RTRIM(@SRC_DOCS_TABLE)) <> '')    
 BEGIN 
	--get hierarchy gid
	SELECT @HIERARCHY_ID = GID FROM DS_HIERARCHY WHERE RELEASE_VERSION IS NOT NULL
	PRINT 'HIERARCHY_ID SUPPLIED: ' + CAST(@HIERARCHY_ID AS VARCHAR(200))
	PRINT 'CATALOG_NAME SUPPLIED(FOR TRANSLATED DOCS): ' + @CATALOG_NAME	
	
	--check if entry for this catalog is there in catalog_list table
	IF NOT EXISTS(SELECT 'X' FROM DS_FULLTEXT_CATALOG_LIST FCL WHERE FCL.CATALOG_NAME = @CATALOG_NAME)
	BEGIN    
		SET @ERRORCODE = -20004   
		SET @ERRORLABEL = 'Please check if translation catalog ' + @CATALOG_NAME + ' has been created and has entry in FULLTEXT_CATALOG_LIST table.'
		GOTO ERROR_LABEL    
	END
	
 
	-- check if CS_FULLTEXT_ENTRY exists, if not create
	IF NOT EXISTS (SELECT 'X' FROM sysobjects where name = 'CS_FULLTEXT_ENTRY' and xtype = 'U')          
  CREATE TABLE CS_FULLTEXT_ENTRY(GID INT IDENTITY(1,1),DOCUMENT_METADATA_ENTRY_GID INT,PATH nvarchar(880) COLLATE Latin1_General_CS_AS, CATALOG_NAME nvarchar(200) collate Latin1_General_CI_AS, TYPE nvarchar(200) collate Latin1_General_CI_AS)   
    
  SET @SQL_STRING_CS = N'INSERT INTO CS_FULLTEXT_ENTRY SELECT DOCUMENT_METADATA_ENTRY_GID, PATH, ''' + @CATALOG_NAME + ''','Translation' FROM ' + @SRC_DOCS_TABLE + ' SRC'   
	+ ' WHERE NOT EXISTS ( SELECT 'x' FROM CS_FULLTEXT_ENTRY DEST WHERE DEST.PATH = SRC.PATH AND DEST.DOCUMENT_METADATA_ENTRY_GID = SRC.DOCUMENT_METADATA_ENTRY_GID AND DEST.TYPE = 'Translation')'
  EXECUTE sp_executesql @SQL_STRING_CS   
    
  IF (@@ERROR <> 0)    
  BEGIN    
   SET @ERRORCODE = -20001    
   SET @ERRORLABEL = 'Unable to insert into CS_FULLTEXT_ENTRY documents from source table ' + @SRC_DOCS_TABLE    
   GOTO ERROR_LABEL    
  END
 
  --TEMP_DOCLIST_13428 will be used for indexing docs  
  IF EXISTS (SELECT 'X' FROM sysobjects where name = 'TEMP_DOCLIST_13428' and xtype = 'U')    
   DROP TABLE TEMP_DOCLIST_13428    
      
  CREATE TABLE TEMP_DOCLIST_13428 (DOCUMENT_METADATA_ENTRY_GID INT,PATH nvarchar(1024))    
    
  SET @SQL_STRING = N'INSERT INTO TEMP_DOCLIST_13428 SELECT DOCUMENT_METADATA_ENTRY_GID, PATH FROM ' + @SRC_DOCS_TABLE    
  EXECUTE sp_executesql @SQL_STRING    
    
  IF (@@ERROR <> 0)    
  BEGIN    
   SET @ERRORCODE = -20002    
   SET @ERRORLABEL = 'Unable to insert docs into TEMP_DOCLIST_13428 from source table ' + @SRC_DOCS_TABLE    
   GOTO ERROR_LABEL    
  END
  
  -- index docs from TEMP_DOCLIST_13428  			
	SET @LANGUAGE_ID='0'
	SET @INDEX_TYPE=''
	SET @ACTION_TYPE=1 --1 for indexing
	EXEC DSP_INDEX_DOCUMENTS @HIERARCHY_ID,@CATALOG_NAME,'TEMP_DOCLIST_13428',@LANGUAGE_ID,@INDEX_TYPE,@ACTION_TYPE,@ERRORCODE,@ERRORLABEL
	
	IF (@ERRORCODE <> 0)    
	BEGIN    
		GOTO ERROR_LABEL    
	END 

	--delete from DS_FULLTEXT_ENTRY any spurious entries
	--first get the counts of spurious entries that were inserted by DSP_INDEX_DOCUMENTS proc
	SELECT @COUNT_INS = COUNT(DFE.GID) FROM DS_FULLTEXT_ENTRY DFE WHERE 
	EXISTS (SELECT 'X' FROM TEMP_DOCLIST_13428 TEMP WHERE TEMP.PATH COLLATE Latin1_General_CS_AS = DFE.PATH )
	
	--now delete
	BEGIN TRAN
	DELETE FROM DS_FULLTEXT_ENTRY WHERE PATH IN 
	(SELECT PATH FROM TEMP_DOCLIST_13428)
	
	SELECT @COUNT_DEL = @@ROWCOUNT                          
                             
  IF (@COUNT_DEL = @COUNT_INS)                          
  BEGIN                          
   -- Success. Commit the Xn.                          
   PRINT 'Spurious entries' + CAST(@COUNT_DEL AS VARCHAR(20)) + ' were deleted successfully from DS_FULLTEXT_ENTRY.'                           
   SET @ERRORCODE = 0                           
   SET @ERRORLABEL = 'DONE SUCCESSFULLY'                           
   COMMIT TRAN                          
  END                          
  ELSE                          
  BEGIN                          
     -- An error occurred. Indicate which operation(s) failed                          
     -- and roll back the Xn.                                                  
   PRINT 'Error:mismatch in insertion and deletion counts from DS_FULLTEXT_ENTRY, Rolling back.. '                           
   ROLLBACK  TRAN                          
   SET @ERRORCODE = -20003                           
   SET @ERRORLABEL = 'FAILED : WHILE DELETING FROM DS_FULLTEXT_ENTRY '                          
   GOTO ERROR_LABEL                    
  END        
  
  ERROR_LABEL:      
	SET @ERRORLABEL = 'Error: ' + @ERRORLABEL     
    RETURN     
   END 
END  