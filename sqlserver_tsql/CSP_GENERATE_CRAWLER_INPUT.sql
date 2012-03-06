-- ================================================
-- proc to generate input file for crawler run
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Ravikishores@TS-India
-- Create date: 6 March 2009
-- Description:	
-- Usage : a) EXEC CSP_GENERATE_CRAWLER_INPUT 0 -- to get unnumbered docs with NOT_IN_MDT = 0
--		   b) EXEC CSP_GENERATE_CRAWLER_INPUT 1, <batch_num> --to get for specific batch
--		   c) EXEC CSP_GENERATE_CRAWLER_INPUT 1, <start_batch>, <end_batch> --to get for batch range			
-- =============================================
CREATE PROCEDURE CSP_GENERATE_CRAWLER_INPUT 
	-- Add the parameters for the stored procedure here
	@FLAG INTEGER = 0, -- by default get docs with BATCH NULL and NOT_IN_MDT = 0 
	@STARTBATCH INTEGER = 1, 
	@ENDBATCH INTEGER = 1,
	@SELECT_BIT BIT = 1 -- to display error codes	
AS
BEGIN			
	DECLARE @ERRORCODE INT,
			@ERRORLABEL VARCHAR(200),
			@STBATCH INT,
			@ENBATCH INT,
			@DOC_COUNT INT
	
	IF(@FLAG = 1)
	BEGIN 
	--get for specified batches
		
		SET @STBATCH = @STARTBATCH
		SET @ENBATCH = @ENDBATCH

		--validation checks	
		--if only 1 batch is specified set endbatch also same
		IF(@ENBATCH = 1) 
		BEGIN 
			SET @ENBATCH = @STBATCH
		END	
		
	END

/*
	In the following query, depending on value of @FLAG, one of the following condition will be executed
	a) @FLAG=0 AND DME.SOURCE_ACRONYM IS NULL AND DME.NOT_IN_MDT = 0 
	b) @FLAG=1 AND CAST(DME.SOURCE_ACRONYM AS INT) BETWEEN @STBATCH AND @ENBATCH
*/
	SELECT FTE.DOCUMENT_METADATA_ENTRY_GID, FTE.[PATH]
	INTO #TEMP_CRAWLER_LIST
	FROM DS_FULLTEXT_ENTRY FTE(NOLOCK) 
	WHERE FTE.DOCUMENT_METADATA_ENTRY_GID IN (
		SELECT DME.GID FROM DS_DOCUMENT_METADATA_ENTRY DME(NOLOCK) 
		WHERE (@FLAG=0 AND DME.SOURCE_ACRONYM IS NULL AND DME.NOT_IN_MDT = 0)
		OR (@FLAG=1 AND CAST(DME.SOURCE_ACRONYM AS INT) BETWEEN @STBATCH AND @ENBATCH)
	)

	SELECT @DOC_COUNT = @@ROWCOUNT

	IF @DOC_COUNT = 0
	BEGIN
		SET @ERRORCODE = 20001  		
		SET @ERRORLABEL = 'BATCH NUM SUPPLIED MAY NOT BE VALID OR THERE MAY BE NO REPRESENTATIVE DOCS WITH SOURCE_ACRONYM NULL.'  
		GOTO ERROR_LABEL  		    
	END		

	SELECT * FROM #TEMP_CRAWLER_LIST
	PRINT 'Total documents to be crawled: ' + CAST(@DOC_COUNT AS VARCHAR(100)) 
	RETURN

    --error block
	ERROR_LABEL:   
	SET @ERRORLABEL = 'Error: ' + @ERRORLABEL     
	IF(@SELECT_BIT = 1) SELECT @ERRORCODE, @ERRORLABEL   
	RETURN  
END
GO
