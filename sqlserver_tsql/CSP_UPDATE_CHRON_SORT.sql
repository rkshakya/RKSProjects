

--create stored proc
-- =============================================
-- Author:		TS-India
-- Create date: 23 March 2009
-- Description:	
-- Assumptions/Dependencies: 1. Function csf_getChronSort table is present in the DB
--				
-- =============================================
CREATE PROCEDURE DBADMIN.CSP_UPDATE_CHRON_SORT (
	@BATCH INT, 
	@SELECT_BIT BIT = 1			--debug bit to display error info
) 
AS 
BEGIN  
	DECLARE @EMD_GID INT,
			@HIERARCHY_GID INT,
			@COUNT_CHK INT
	DECLARE @ERRORCODE INT
	DECLARE @ERRORLABEL NVARCHAR(500)

	--VALIDATIONS
	--check if dependant function csf_getChronSort exists 
	SELECT @COUNT_CHK = COUNT(name) FROM dbo.sysobjects       
	WHERE id = object_id('DBADMIN.csf_getChronSort')       
      
	IF (@COUNT_CHK = 0)      
	  BEGIN  
		SET @ERRORCODE = -20000       
		SET @ERRORLABEL = 'Dependency function DBADMIN.csf_getChronSort does not exist in DB. Please create it and retry..'     
		GOTO ERROR_LABEL     	      
	  END
	  
	--get H_GID
	SELECT @HIERARCHY_GID = GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL
	PRINT 'HIERARCHY_GID obtained: ' + CAST(@HIERARCHY_GID AS VARCHAR(20))
	
	--get EMD_GID for ChronSort
	SELECT @EMD_GID = GID FROM DS_METADATA_PROPERTIES(NOLOCK) WHERE HIERARCHY_GID = @HIERARCHY_GID
	AND [NAME] = 'CS_ChronSort'
	
	--if not existing, create
	IF (@EMD_GID IS NULL)
	BEGIN 
		PRINT 'CS_ChronSort metadata is not yet created. Trying to create..'
		
		INSERT INTO DS_METADATA_PROPERTIES VALUES
		(@HIERARCHY_GID, 'CS_ChronSort', 'ON',0, 'customer_specific_property', 0, 0, 'Chron Sort values', 'DS_EXTENDED_METADATA_CUSTOM' )
		
		SELECT @EMD_GID = GID FROM DS_METADATA_PROPERTIES(NOLOCK) WHERE HIERARCHY_GID = @HIERARCHY_GID
		AND [NAME] = 'CS_ChronSort'
		
	END

	PRINT 'EMD_GID for ChronSort: ' + CAST(@EMD_GID AS VARCHAR(20))
	

	INSERT INTO DS_EXTENDED_METADATA_CUSTOM(DOCUMENT_METADATA_ENTRY_GID, [VALUE], EXTENDED_PROPERTY_GID)   
	SELECT DME.GID, DBADMIN.csf_getChronSort(DME.GID), @EMD_GID   
	FROM DS_DOCUMENT_METADATA_ENTRY DME(NOLOCK)  
	WHERE DME.SOURCE_ACRONYM = @BATCH 
	AND NOT EXISTS(
		SELECT 'x' FROM DS_EXTENDED_METADATA_CUSTOM EMC(NOLOCK)
		WHERE EMC.DOCUMENT_METADATA_ENTRY_GID = DME.GID
		AND EMC.EXTENDED_PROPERTY_GID  = @EMD_GID
		--commenting out below line 
		--AND EMC.[VALUE] = DBADMIN.csf_getChronSort(DME.GID)
	)
	 
	PRINT 'Populated ChronSort values for ' + CAST(@@ROWCOUNT AS VARCHAR(20)) + ' docs of batch ' + CAST(@BATCH AS VARCHAR(10))
	RETURN

	--error block
	ERROR_LABEL:   
	SET @ERRORLABEL = 'Error: ' + @ERRORLABEL     
	IF(@SELECT_BIT = 1) SELECT @ERRORCODE, @ERRORLABEL   
END