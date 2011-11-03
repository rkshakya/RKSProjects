
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


IF NOT EXISTS ( SELECT * FROM sysobjects 
		WHERE id = object_id(N'DBADMIN.CSP_APPLY_TAG_CONSTRAINT')) 
		BEGIN EXEC('CREATE PROCEDURE DBADMIN.CSP_APPLY_TAG_CONSTRAINT AS SELECT 1') 
END 
GO

/**************************************************************************************************
*	Stored Procedure Name: CSP_APPLY_TAG_CONSTRAINT
*	
*	Applicability: 5.x/6.0 cases
*	
*	Author:	Custom Work Group/Ravi	
*	
*	Date:			
*
*	Intent:	
*
*	Description:	This procedure applies mutually exclusive tag constraints for the child tag topics of
*			the parent topic whose topic GID is @PARENT_TOPIC_GID.
*			Uses 2 intermediate tables:
*			1) CREATE TABLE TEMP_TAGS(GID INTEGER IDENTITY(1, 1), TAG_TOPIC INTEGER) 
*			-- to hold the child tag topics that are going to be mutually exclusive
*			2) CREATE TABLE TEMP_CONSTRAINTS_LIST(TOPIC_ID_A INTEGER , TOPIC_ID_B INTEGER) 
*			--to hold the pairs of topics that are going to be mutually exclusive 
*			  and that are going to form a rule.
* 
*	Input Parameters: 1) @PARENT_TOPIC_GID - the topic GID of the parent topic.
*		         
*			
*
*	
*	Usage:	DECLARE @ERRORCODE INTEGER
*		DECLARE @ERRORLABEL VARCHAR(100)
*		EXEC CSP_APPLY_TAG_CONSTRAINT 49428, @ERRORCODE, @ERRORLABEL
*		PRINT @ERRORCODE
*		PRINT @ERRORLABEL
*
*				
*
*****************************************************************************************************/

ALTER PROCEDURE CSP_APPLY_TAG_CONSTRAINT(
	@PARENT_TOPIC_GID INTEGER,
	@ERRORCODE INTEGER OUTPUT, 
        @ERRORLABEL VARCHAR(254) OUTPUT
)
AS
BEGIN
	SET NOCOUNT ON                
	                  
	 IF EXISTS (SELECT 'X' FROM sysobjects WHERE name = 'TEMP_TAGS' AND type = 'U')                  
	 BEGIN                  
	  DROP TABLE TEMP_TAGS               
	 END                  
	                   
	 --create table to store child tag topics
 	CREATE TABLE TEMP_TAGS(GID INTEGER IDENTITY(1, 1), TAG_TOPIC INTEGER) 
 	
 	IF EXISTS (SELECT 'X' FROM sysobjects WHERE name = 'TEMP_CONSTRAINTS_LIST' AND type = 'U')                  
	BEGIN                  
		DROP TABLE TEMP_CONSTRAINTS_LIST              
	END                  
			                   
	--create table to store child tag topics
	CREATE TABLE TEMP_CONSTRAINTS_LIST(TOPIC_ID_A INTEGER , TOPIC_ID_B INTEGER) 

 	
 	INSERT INTO TEMP_TAGS(TAG_TOPIC)
 	SELECT TOPIC_ID FROM DS_TOPIC
	WHERE GID IN (
	SELECT CHILD_TOPIC_GID FROM DS_HIERARCHY_BRANCH
	WHERE PARENT_TOPIC_GID = @PARENT_TOPIC_GID)
	
	
	--to generate the list of tag topic that needs to be mutually exclusive
	INSERT INTO TEMP_CONSTRAINTS_LIST(TOPIC_ID_A ,TOPIC_ID_B)
	SELECT A.TAG_TOPIC, B.TAG_TOPIC
	FROM TEMP_TAGS A, TEMP_TAGS B
	WHERE A.GID < B.GID
	ORDER BY 1, 2
	
	 --declare variables
	 DECLARE @GID INTEGER
	 DECLARE @TAG_TOPIC_1 INTEGER
	 DECLARE @TAG_TOPIC_2 INTEGER
	 DECLARE @USERID INTEGER
	 DECLARE @HIERARCHYID INTEGER	 
	 DECLARE @TEMP_CURSOR CURSOR 
	 DECLARE @COUNTER INTEGER
	 
	 SET @COUNTER = 1
	 
	 --counter for rule number
	 DECLARE @RULE_COUNTER INTEGER 
	 
	 --get HIERARCHY GID
	 SELECT @HIERARCHYID = GID FROM DS_HIERARCHY WHERE RELEASE_VERION IS NOT NULL
	 
	 --get USER ID
	 SELECT @USERID = GID FROM DS_USER WHERE NAME = 'SYSTEM'

	 
	 --get the max rule number
	 SELECT @RULE_COUNTER =  MAX(RULE_NUMBER) + 1 from DS_CLS_CONSTRAINTS
	                   
	 SET @TEMP_CURSOR = CURSOR LOCAL FAST_FORWARD FOR                  
	 SELECT TOPIC_ID_A, TOPIC_ID_B FROM TEMP_CONSTRAINTS_LIST
	                   
	 OPEN @TEMP_CURSOR                  
	                   
	 WHILE (1=1)                  
	 BEGIN                  
	  	FETCH FROM @TEMP_CURSOR INTO @TAG_TOPIC_1, @TAG_TOPIC_2
	                   
  		IF (@@FETCH_STATUS<>0) BREAK 
  		
  		--increment rule counter
  		SET @RULE_COUNTER = @RULE_COUNTER + 1

  		--insert into DS_CLS_CONSTRAINT
  		
  		INSERT INTO DS_CLS_CONSTRAINTS(HIERARCHY_GID, RULE_NUMBER, TOPIC_ID, MODIFIED_BY, MODIFIED_AT)
		VALUES (@HIERARCHYID, @RULE_COUNTER, @TAG_TOPIC_1, @USERID, getdate())

  		INSERT INTO DS_CLS_CONSTRAINTS(HIERARCHY_GID, RULE_NUMBER, TOPIC_ID, MODIFIED_BY, MODIFIED_AT)
		VALUES (@HIERARCHYID, @RULE_COUNTER, @TAG_TOPIC_2, @USERID, getdate())
		
		SET @COUNTER = @COUNTER + 1
  		
  	END
  	
  	PRINT 'Successfully inserted ' + CAST(@COUNTER - 1 AS VARCHAR) + ' tag constraints.'
  	
  	--clean up resources
	 CLOSE @TEMP_CURSOR                  
	 DEALLOCATE @TEMP_CURSOR                  
	                
 	SET NOCOUNT OFF        

END

