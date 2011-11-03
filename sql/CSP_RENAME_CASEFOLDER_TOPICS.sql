    
-- =============================================    
-- Author:  ravikishores@TS-India    
-- Create date: 12 June 2009    
-- Description: This proc renames the Concept Folder topics as per requirements of SII-16759    
-- Usage Info: You may wish to take backup of DS_TOPIC table before running this proc for safety.    
--  a) Running this proc with 0 as the input parameter will run it in NON-DEBUG mode    
--   and updates the values in DS_TOPIC table.    
--  b) Running it in any other way will be in DEBUG mode and won't update values in DS_TOPIC table    
-- =============================================    
alter PROCEDURE [DBADMIN].[CSP_RENAME_CASEFOLDER_TOPICS_MAY014_16759]    
 -- Add the parameters for the stored procedure here    
 @DEBUG_BIT BIT = 1     
AS    
BEGIN     
 --declarations    
 DECLARE @HIERARCHY_GID INT    
 DECLARE @TOPIC_TYPE BIT    
 DECLARE @UDT_COUNT1 INT, @UDT_COUNT2 INT, @UDT_COUNT3 INT, @UDT_COUNT4 INT, @UDT_COUNT5 INT    
 --concept folder    
 SET @TOPIC_TYPE = 0    
    
 SELECT @HIERARCHY_GID = GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL    
 PRINT ' HIERARCHY_GID : ' + CAST(@HIERARCHY_GID AS VARCHAR(10))     
     
 BEGIN TRY    
    
 --validations    
 IF NOT EXISTS(SELECT * FROM dbo.sysobjects WHERE id = object_id(N'DBADMIN.CS_CASEFOLDER_RENAME_INFO'))    
 BEGIN    
  PRINT 'CS_CASEFOLDER_RENAME_INFO table does not exist. Creating it...'    
  CREATE TABLE DBADMIN.CS_CASEFOLDER_RENAME_INFO(    
  ID INT IDENTITY(1, 1),    
  TOPIC_ID INT,    
  OLD_NAME NVARCHAR(510) COLLATE Latin1_General_BIN,    
  NEW_NAME NVARCHAR(510) COLLATE Latin1_General_BIN,    
  UPDATE_DATE DATETIME     
  )    
      
  --create index    
  CREATE INDEX idx_topic ON CS_CASEFOLDER_RENAME_INFO(TOPIC_ID)    
 END     
  
BEGIN TRAN T1

	--updated entries for already existing topics but for whom criteria do not match
UPDATE REN
SET REN.OLD_NAME = T.[NAME], REN.UPDATE_DATE = NULL
FROM CS_CASEFOLDER_RENAME_INFO REN, DS_TOPIC T
WHERE  T.HIERARCHY_VERSION_GID = @HIERARCHY_GID    
 AND T.TOPIC_TYPE = @TOPIC_TYPE  
 AND T.TOPIC_ID <> 0    
 AND  EXISTS (    
 SELECT 'X' FROM CS_CASEFOLDER_RENAME_INFO CS(NOLOCK)    
 WHERE CS.TOPIC_ID = T.TOPIC_ID    
 )    
 AND (    
   T.[NAME] NOT LIKE '(LP)%' AND T.[NAME] NOT LIKE '(PJ)%'    
   AND T.[NAME] NOT LIKE '(MB)%' AND T.[NAME] NOT LIKE '(CLP)%'    
   AND T.[NAME] NOT LIKE '(MLP)%' AND T.[NAME] NOT LIKE '(YTD%'    
 ) 
AND REN.TOPIC_ID = T.TOPIC_ID

SET @UDT_COUNT4 = @@ROWCOUNT
    
 PRINT ' Updated ' + CAST(@UDT_COUNT4 AS VARCHAR(20)) + ' old entries in CS_CASEFOLDER_RENAME_INFO table.'    
  
 --populate new concept folder names that do not start with LP, PJ, MB, CLP, MLP, YTD in this table    
 INSERT INTO DBADMIN.CS_CASEFOLDER_RENAME_INFO(TOPIC_ID, OLD_NAME)    
 SELECT T.TOPIC_ID, T.[NAME] FROM DS_TOPIC T(NOLOCK) WHERE T.HIERARCHY_VERSION_GID = @HIERARCHY_GID    
 AND T.TOPIC_TYPE = @TOPIC_TYPE    
 AND T.TOPIC_ID <> 0    
 AND NOT EXISTS (    
 SELECT 'X' FROM CS_CASEFOLDER_RENAME_INFO CS(NOLOCK)    
 WHERE CS.TOPIC_ID = T.TOPIC_ID    
 )    
 AND (    
   T.[NAME] NOT LIKE '(LP)%' AND T.[NAME] NOT LIKE '(PJ)%'    
   AND T.[NAME] NOT LIKE '(MB)%' AND T.[NAME] NOT LIKE '(CLP)%'    
   AND T.[NAME] NOT LIKE '(MLP)%' AND T.[NAME] NOT LIKE '(YTD%'    
 )   

 SET @UDT_COUNT3 = @@ROWCOUNT
    
 PRINT ' Populated ' + CAST(@UDT_COUNT3 AS VARCHAR(20)) + ' new entries into CS_CASEFOLDER_RENAME_INFO table.'    
    
 --update folder names    
 UPDATE CS    
 SET CS.NEW_NAME = '(YTD ' + CAST(CS.ID AS NVARCHAR(20)) + ')' + CS.OLD_NAME    
 FROM CS_CASEFOLDER_RENAME_INFO CS    
 WHERE CS.UPDATE_DATE IS NULL 

SET @UDT_COUNT5 = @@ROWCOUNT

PRINT ' Updated ' + CAST(@UDT_COUNT5 AS VARCHAR(20)) + ' new names old entries in CS_CASEFOLDER_RENAME_INFO table.'        

IF(@UDT_COUNT5 = @UDT_COUNT4 + @UDT_COUNT3)    
  BEGIN    
   COMMIT TRAN T1   
   PRINT 'Updation completed for new names.'    
  END    
  ELSE    
  BEGIN     
   ROLLBACK TRAN T1 
   PRINT 'Updation counts mismatch. Transaction T1 rolled back. Plz investigate the problem.'    
  END  

 --now update based on debug bit     
 IF(@DEBUG_BIT = 0)    
 BEGIN    
  PRINT 'Proc is run in NON-DEBUG mode. Values WILL be updated in DS_TOPIC table. '        
    
  BEGIN TRAN     
    
  --disable trigger DS_TOPIC_TUI here     
  ALTER TABLE DS_TOPIC DISABLE TRIGGER DS_TOPIC_TUI        
      
  UPDATE T    
  SET T.[NAME] = CS.NEW_NAME    
  FROM DS_TOPIC T, CS_CASEFOLDER_RENAME_INFO CS    
  WHERE T.TOPIC_ID = CS.TOPIC_ID    
  AND T.TOPIC_TYPE = @TOPIC_TYPE    
  AND CS.UPDATE_DATE IS NULL    
    
  SET @UDT_COUNT1 = @@ROWCOUNT    
    
  --enable trigger here    
  ALTER TABLE DS_TOPIC ENABLE TRIGGER DS_TOPIC_TUI     
    
      
  PRINT ' Updated Names for ' + CAST(@UDT_COUNT1 AS VARCHAR(20)) + ' new entries in DS_TOPIC table.'    
    
  UPDATE CS    
  SET CS.UPDATE_DATE = getdate()    
  FROM CS_CASEFOLDER_RENAME_INFO CS    
  WHERE UPDATE_DATE IS NULL    
    
  SET @UDT_COUNT2 = @@ROWCOUNT    
  PRINT ' Updated dates for ' + CAST(@UDT_COUNT2 AS VARCHAR(20)) + ' entries in CS_CASEFOLDER_RENAME_INFO table.'    
    
  IF(@UDT_COUNT1 = @UDT_COUNT2)    
  BEGIN    
   COMMIT TRAN    
   PRINT 'Updation completed.'    
  END    
  ELSE    
  BEGIN     
   ROLLBACK TRAN    
   PRINT 'Update counts mismatch. Transaction rolled back. Plz investigate the problem.'    
  END        
        
 END    
 ELSE    
 BEGIN     
PRINT 'Proc is run in DEBUG mode. Values WILL NOT be updated in DS_TOPIC table. '    
  SELECT * FROM CS_CASEFOLDER_RENAME_INFO WHERE UPDATE_DATE IS NULL       
 END     
    
 END TRY    
 BEGIN CATCH        
  IF(@@TRANCOUNT > 0)    
  BEGIN     
   ROLLBACK TRAN    
   --enable trigger here just to be sure    
   ALTER TABLE DS_TOPIC ENABLE TRIGGER DS_TOPIC_TUI    
  END    
 END CATCH    
     
END    