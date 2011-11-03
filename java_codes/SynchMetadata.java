import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/*******************************************************************************
 * SynchMetadata.java - program to generate reports for Source/Custodian mismatch info
 *						comparing vis-a-vis FE DB and BE DB for a case.
 *
 * Inputs:  5 parameters  viz <casename> <bedbserver> <fedbserver> <actionflag> <drop_table_needed>
 * 			Please run the program sans parameters for more info.
 *
 * Outputs: a) 2 report files (custodian and source wise),
 *			b) a backup file,
 * 			c) query output file,
 *			d) bcp dump file.
 *
 * Usage: java  SynchMetadata <casename> <bedbserver> <fedbserver> <actionflag> <drop_table_needed>
 *
 * Compatibility: a) Requires JDK/JRE 1.5 ,
 * 				  b) needs bcp.exe and bcp.rll in current folder,
 *				  c) jtds.jar must be included in classpath
 *
 * Version : 1.0 (Jul 15, 2009)
 *
 * Author : RavikishoreS@Custom Work Group(TS-India).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ******************************************************************************
 */


public class SynchMetadata{
    private static final int ARGS_COUNT = 5;
    private static final String AFFIRM_FLAG = "y";

    //utils related
    private static final String CURR_DIR = ".";
    private static final String BCP_EXE = "bcp.exe";
    private static final String BCP_RLL = "bcp.rll";

    //domain and db related
    private static final String BE_SUFFIX = ".sdp.stratify.com";
    private static final String FE_SUFFIX = ".lds.asp.stratify.com";
    private static final String NA = "NA";
    private static final String USERNAME = "DBADMIN";
    private static final String PASSWORD = "DBADMIN";
	private static final String CONNECTION_STRING = "net.sourceforge.jtds.jdbc.Driver";
	private static final String DB_URL_PREFIX = "jdbc:jtds:sqlserver://";
	private static final int MSSQL_PORT = 1433;

	//headers related
	private static final String OUTPUT_HEADER = "BATCH\tINFERENCE\tMEDIANAME\tCOUNT";
	private static final String OUTPUT_DELIMITER = "\t";


    public static void main(String[] args) {
		if (args.length != ARGS_COUNT) {
			display("Usage java SynchMetadata <casename> <bedbserver> <fedbserver> <actionflag> <drop_table_needed>");
			display("-----------------------------------------------------------------------");
			display(" actionflag = 0 for DEBUG MODE: No changes will be done in the BE DB. ");
			display(" actionflag = 1 for INSERT MODE: New source/custodian entries will be inserted in the EMD_BE table in BE DB. ");
			display(" actionflag = 2 for COMPLETE SYNCH MODE: New source/custodian entries will be inserted in the EMD_BE table in BE DB.");
			display("					 BE DB source/custodian entries will be updated as per FE DB source/custodian entries.");
			display("					 Extra source/custodian entries from EMD_BE from BE DB will be eliminated.");
			display("");
			display("drop_table_needed = 0 if staging table is to be retained in BE DB.");
			display("drop_table_needed = 1 if staging table is to be dropped from BE DB.");
			display("-----------------------------------------------------------------------");

			return;
		}

                String casename = args[0];
                String bedbserver = args[1];
                String fedbserver = args[2];
                int actionflag = Integer.parseInt(args[3]);
                int dropTableFlag = Integer.parseInt(args[4]);

                Connection con = null;
                String dbUrl = null;

                if(!bedbserver.endsWith(BE_SUFFIX)){
                	bedbserver += BE_SUFFIX;
                }
                if(!fedbserver.endsWith(FE_SUFFIX)){
                	fedbserver += FE_SUFFIX;
                }

              //check if reqd bcp utils are present in current folder
                int retval = 0;
				try {
					retval = validateCompos();
				} catch (IOException e) {
					e.printStackTrace();
				}


                if(retval == 1){
                	display(BCP_EXE + " not present in current folder. Exiting..");
                	return;
                }else if(retval == 2){
                	display(BCP_RLL + " not present in current folder. Exiting..");
                	return;
                }else if(retval == 12){
                	display(BCP_RLL + " and " + BCP_EXE + " are not present in current folder. Exiting..");
                	return;
                }else if(retval == 3){
                	display("Current folder is not writable. Exiting..");
                	return;
                }

                //display info to user for confirmation
                printinfo(casename, bedbserver, fedbserver, actionflag, dropTableFlag);

                String exportQueryFile = null;
                String outFile = null;

                Scanner scanner = new Scanner(System.in);
                if(scanner.next().equalsIgnoreCase(AFFIRM_FLAG)){
                	//dump query to a file
                	try {
						exportQueryFile = outputExportQuery(casename);
					} catch (IOException e) {
						e.printStackTrace();
					}

                	//bcp out src/custodian values into a file
                	outFile = exportFromSource(casename, fedbserver, exportQueryFile);

                	if(outFile.equalsIgnoreCase(NA)){
                		display("Error generating output file. Exiting..");
                		return;
                	}

                	try{
                	dbUrl = getUrl(casename, bedbserver);
                	con = getDBConnection(CONNECTION_STRING, dbUrl, USERNAME,
        					PASSWORD);

                	String tablename = "TEMP_SYNCH_" + System.currentTimeMillis();

                	//do the housekeeping on the dest DB - create table
                	 createTempTable(con, tablename);

                	//bcp into temp table in dest BE DB
                	 if(importIntoDest(casename, bedbserver, outFile, tablename)){
                		 display("BCP in SUCCESS.");
                	 }else{
                		 display("BCP in FAILURE.");
                	 }

                	//identify docs to be inserted, deleted, updated in the BE
                	//and generate reports
                	identifyDocsAndGenReports(tablename, con);

                	//bcp out this table as bkp
                	String bkpFile = genBackup(tablename, bedbserver, casename);

                	//make this file read-only to avoid accidental deletion
                	setReadOnly(bkpFile);

                	//this is not implemented for now
/*
                	//synchronize - this be done using DB Connections not from osql
                	//we want Xn
                	synchEntries(actionflag);
*/
                	//drop the temp table generated
                	if(dropTableFlag == 1){
                		int res = dropTable(con, tablename);
                		if(res == 0){
                		display("Table " + tablename + " dropped from BE DB.");
                		}
                	}

                	closeCon(con);

                	}catch(Exception ex){
                		ex.printStackTrace();
                	}

                }else{
                	display("Quitting script execution...");
                }

    }

    private static void setReadOnly(String bkpFile) throws IOException {
		// TODO Auto-generated method stub
    	File curdir = new File (CURR_DIR);
        display ("Current dir : " + curdir.getAbsolutePath());
        File bkpfile = new File(curdir.getAbsolutePath() + "\\" + bkpFile);
        if(bkpfile.setReadOnly()){
        	display("Bkp file attribute set to READ-ONLY.");
        }else{
        	display("Bkp file attribute could not be changed.");
        }

	}

	private static int dropTable(Connection con, String tablename) throws SQLException {
		int retval = 0;
		display("Dropping table " + tablename + "....");

		String qryDropTable = " DROP TABLE DBADMIN." + tablename;

		PreparedStatement pstmtDrop = con.prepareStatement(qryDropTable);
		retval = pstmtDrop.executeUpdate();

		return retval;
	}

	private static String genBackup(String tablename, String bedbserver, String casename) {
    	String retval = null;
		String bkpFile = "backup_" + System.currentTimeMillis() + ".txt";

		tablename = casename + ".DBADMIN." + tablename;
    	display("Generating backup file for table : " + tablename + " into " + bkpFile);

			String cmdOp = "";
			char[] readArr = new char[1];
			boolean success = false;
			String commands = null;
			Process prc = null;
			InputStream cmdIn = null;
			int read = 0;

			try {
				commands = "cmd.exe /C bcp \"" + tablename + "\" out \"" + bkpFile + "\" -c -U " + USERNAME + " -P " + PASSWORD + " -S " + bedbserver;
				display ("bcp command:" + commands);
				prc = Runtime.getRuntime().exec(commands);
				cmdIn = prc.getInputStream();
				while ((read = cmdIn.read()) != -1) {
					readArr[0] = (char) read;
					cmdOp += new String(readArr);
				}
				cmdOp = cmdOp.trim();
				display(cmdOp);

				if (cmdOp.indexOf("rows copied") > 0) {
					success = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		retval = (success == true)? bkpFile: NA ;

		display("Generated backup file  " + bkpFile);

		return retval;
	}

	private static void identifyDocsAndGenReports(String tablename, Connection contemp) throws SQLException {
    	display("Identifying docs for updation/deletion/insertion..");
		//create index on the temp table
    	String qryIndex = "CREATE INDEX idx_temp ON " + tablename + "(DOC_GID, EXT_GID, FLAG)";
    	PreparedStatement pstmtIndex = contemp.prepareStatement(qryIndex);
    	int ret = pstmtIndex.executeUpdate();
    	if(ret == 0){
    		display("Index idx_temp created SUCCESSFULLY on table " + tablename);
    	}else{
    		display("Creation of index idx_temp FAILED on table " + tablename);
    	}

    	cleanup(pstmtIndex);

    	//identify docs to be inserted, deleted, updated in the BE

    	//identify rows to insert
    	String qryInsert = "UPDATE TEMP SET TEMP.FLAG = 'TO_BE_INSERTED'"
    		+ " FROM " + tablename + " TEMP"
    		+ " WHERE NOT EXISTS ("
    		+ "  SELECT 'X' FROM DS_EXTENDED_METADATA_BE EMB(NOLOCK)"
    		+ "  WHERE TEMP.DOC_GID = EMB.DOCUMENT_METADATA_ENTRY_GID"
    		+ "  AND TEMP.EXT_GID = EMB.EXTENDED_PROPERTY_GID"
    		+ ")";
    	PreparedStatement pstmtToInsert = contemp.prepareStatement(qryInsert);
    	int cntInsert = pstmtToInsert.executeUpdate();
    	display(cntInsert + " new rows to insert.");

    	cleanup(pstmtToInsert);

    	//identify and bkp rows to update
    	String qryUpdate = "UPDATE TEMP"
    		+ " SET TEMP.FLAG = 'TO_BE_UPDATED', TEMP.OLDVAL = EMB.[VALUE]"
    		+ " FROM " + tablename + " TEMP, DS_EXTENDED_METADATA_BE EMB"
    		+ " WHERE TEMP.DOC_GID = EMB.DOCUMENT_METADATA_ENTRY_GID"
    		+ " AND TEMP.EXT_GID = EMB.EXTENDED_PROPERTY_GID"
    		+ " AND TEMP.[VALUE] <> EMB.[VALUE]";

		PreparedStatement pstmtToUpdate = contemp.prepareStatement(qryUpdate);
		int cntUpdate = pstmtToUpdate.executeUpdate();
		display(cntUpdate + " rows to be updated in EMD_BE table.");

		cleanup(pstmtToUpdate);

		//identify and bkp rows to delete from EMD_BE table
		String qryDelete = "INSERT INTO " + tablename + " (DOC_GID, EXT_GID, MNAME, FLAG, OLDVAL)"
			+ " SELECT EMB.DOCUMENT_METADATA_ENTRY_GID, EMB.EXTENDED_PROPERTY_GID, EMB1.[VALUE],'TO_BE_DELETED', EMB.[VALUE]"
			+ " FROM DS_EXTENDED_METADATA_BE EMB(NOLOCK), DS_EXTENDED_METADATA_BE EMB1(NOLOCK)"
			+ " WHERE NOT EXISTS("
			+ " 	SELECT 'X' FROM " + tablename + " TEMP"
			+ " 	WHERE TEMP.DOC_GID = EMB.DOCUMENT_METADATA_ENTRY_GID"
			+ " 	AND TEMP.EXT_GID = EMB.EXTENDED_PROPERTY_GID"
			+ " )"
			+ " AND EMB.DOCUMENT_METADATA_ENTRY_GID = EMB1.DOCUMENT_METADATA_ENTRY_GID"
			+ " AND EMB1.EXTENDED_PROPERTY_GID = ("
			+ " 	SELECT GID FROM DS_METADATA_PROPERTIES(NOLOCK) WHERE HIERARCHY_GID = "
			+ "		(SELECT GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL)"
			+ " 	AND NAME IN ('DS_MediaName')"
			+ " )"
			+ " AND EMB.EXTENDED_PROPERTY_GID IN ("
			+ "		SELECT GID FROM DS_METADATA_PROPERTIES(NOLOCK) WHERE HIERARCHY_GID = "
			+ " 	(SELECT GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL)"
			+ " 	AND NAME IN ('DS_Custodian', 'DS_Source')"
			+ " )";

		PreparedStatement pstmtToDelete = contemp.prepareStatement(qryDelete);
		int cntDelete = pstmtToDelete.executeUpdate();
		display(cntDelete + " rows to be deleted from EMD_BE table.");

		cleanup(pstmtToDelete);

		//generate reports
		try {
			generateReports(contemp, tablename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void generateReports(Connection contemp, String tablename) throws IOException, SQLException {

		//generate custodian report
		display("Generating Custodian report...");
		String custReport = "custodian_report_" + System.currentTimeMillis() + ".txt";
		BufferedWriter bwCust = new BufferedWriter(new FileWriter(custReport));

		bwCust.write(OUTPUT_HEADER);
		bwCust.newLine();

		String qryCust = "SELECT DME.SOURCE_ACRONYM, TEMP.FLAG, TEMP.MNAME, COUNT(TEMP.DOC_GID) AS COUNTS"
		+ " FROM " + tablename + " TEMP(NOLOCK), DS_DOCUMENT_METADATA_ENTRY DME(NOLOCK)"
		+ " WHERE TEMP.DOC_GID = DME.GID"
		+ " AND TEMP.EXT_GID = ("
		+ "		SELECT GID FROM DS_METADATA_PROPERTIES(NOLOCK) WHERE HIERARCHY_GID = ("
		+ "		SELECT GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL )"
		+ " 	AND NAME = 'DS_Custodian')"
		+ " AND TEMP.FLAG <> 'NOCHANGE' "
		+ " GROUP BY DME.SOURCE_ACRONYM, TEMP.FLAG, TEMP.MNAME"
		+ " ORDER BY CAST(DME.SOURCE_ACRONYM AS INT)";

		PreparedStatement pstmtCust = contemp.prepareStatement(qryCust);
		ResultSet rsCust = pstmtCust.executeQuery();

		while(rsCust.next()){
			bwCust.write(rsCust.getString(1) + OUTPUT_DELIMITER);
			bwCust.write(rsCust.getString(2) + OUTPUT_DELIMITER);
			bwCust.write(rsCust.getString(3) + OUTPUT_DELIMITER);
			bwCust.write(rsCust.getString(4));
			bwCust.newLine();
		}

		cleanUp(pstmtCust, rsCust);

		bwCust.close();
		bwCust = null;

		//generate Source report

		display("Generating Source report...");
		String srcReport = "source_report_" + System.currentTimeMillis() + ".txt";
		BufferedWriter bwSrc = new BufferedWriter(new FileWriter(srcReport));

		bwSrc.write(OUTPUT_HEADER);
		bwSrc.newLine();

		String qrySrc = "SELECT DME.SOURCE_ACRONYM, TEMP.FLAG, TEMP.MNAME, COUNT(TEMP.DOC_GID) AS COUNTS"
		+ " FROM " + tablename + " TEMP(NOLOCK), DS_DOCUMENT_METADATA_ENTRY DME(NOLOCK)"
		+ " WHERE TEMP.DOC_GID = DME.GID"
		+ " AND TEMP.EXT_GID = ("
		+ "		SELECT GID FROM DS_METADATA_PROPERTIES(NOLOCK) WHERE HIERARCHY_GID = ("
		+ "		SELECT GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL )"
		+ " 	AND NAME = 'DS_Source')"
		+ " AND TEMP.FLAG <> 'NOCHANGE' "
		+ " GROUP BY DME.SOURCE_ACRONYM, TEMP.FLAG, TEMP.MNAME"
		+ " ORDER BY CAST(DME.SOURCE_ACRONYM AS INT)";

		PreparedStatement pstmtSrc = contemp.prepareStatement(qrySrc);
		ResultSet rsSrc = pstmtSrc.executeQuery();

		while(rsSrc.next()){
			bwSrc.write(rsSrc.getString(1) + OUTPUT_DELIMITER);
			bwSrc.write(rsSrc.getString(2) + OUTPUT_DELIMITER);
			bwSrc.write(rsSrc.getString(3) + OUTPUT_DELIMITER);
			bwSrc.write(rsSrc.getString(4));
			bwSrc.newLine();
		}

		cleanUp(pstmtSrc, rsSrc);

		bwSrc.close();
		bwSrc = null;

	}

	private static void cleanUp(PreparedStatement pstmtCust, ResultSet rsCust) throws SQLException {
		// TODO Auto-generated method stub
		pstmtCust.close();
		pstmtCust = null;
		rsCust.close();
		rsCust = null;

	}

	private static boolean importIntoDest(String casename, String bedbserver,
			String outFile, String tablename) {
    	display("BCPing into table : " + casename + ".DBADMIN." + tablename + " in " + bedbserver);

		String cmdOp = "";
		char[] readArr = new char[1];
		boolean success = false;
		String commands = null;
		Process prc = null;
		InputStream cmdIn = null;
		int read = 0;
		String table = casename + ".DBADMIN." + tablename;

		try {
			commands = "cmd.exe /C bcp " + table + " in " + outFile + " -c -U " + USERNAME + " -P " + PASSWORD + " -S " + bedbserver;
			display ("bcp command:" + commands);
			prc = Runtime.getRuntime().exec(commands);
			cmdIn = prc.getInputStream();
			while ((read = cmdIn.read()) != -1) {
				readArr[0] = (char) read;
				cmdOp += new String(readArr);
			}
			cmdOp = cmdOp.trim();
			display(cmdOp);

			if (cmdOp.toLowerCase().indexOf("error") < 0) {
				success = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	return success;

	}

	private static void createTempTable(Connection con, String tablename) throws SQLException {
		// TODO Auto-generated method stub
    	int retval = 0;
    	String qryCreate = "CREATE TABLE DBADMIN." + tablename + " ( "
    	+ " DOC_GID INT, "
    	+ " VALUE NVARCHAR(768) COLLATE Latin1_General_CI_AS, "
    	+ " EXT_GID INT, "
    	+ " MNAME NVARCHAR(768) COLLATE Latin1_General_CI_AS, "
    	+ " FLAG VARCHAR(20), "
    	+ " OLDVAL NVARCHAR(768) COLLATE Latin1_General_CI_AS "
    	+ ")";
    	PreparedStatement pstmtcreatetable = con.prepareStatement(qryCreate);
    	retval = pstmtcreatetable.executeUpdate();

    	cleanup(pstmtcreatetable);
	}

	private static void cleanup(PreparedStatement pstmtcreatetable) throws SQLException {
		// TODO Auto-generated method stub
		pstmtcreatetable.close();
		pstmtcreatetable = null;

	}

	private static Connection getDBConnection(String conString, String dbURL,
			String uName, String passWd) throws ClassNotFoundException,
			SQLException {
		Connection con;
		Class.forName(conString);
		display("URL: " + dbURL + "UNAME: " + uName + "PASSWD: "
				+ passWd);
		con = DriverManager.getConnection(dbURL, uName, passWd);
		return con;
	}

    private static String getUrl(String dbname, String dbserver) {
		String url;
		url = DB_URL_PREFIX + dbserver + ":" + MSSQL_PORT + "/" + dbname;
		return url;
	}

    private static void closeCon(Connection con) throws SQLException {
		if (con != null) {
			con.close();
			con = null;
		}

	}

	private static String outputExportQuery(String casename) throws IOException {
    	String outFile = "outquery_" + System.currentTimeMillis() + ".sql";
    	display("Dumping output query to file : " + outFile);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile)));
		String outqry = generateQuery(casename);
		bw.write(outqry);

		bw.close();
		bw = null;

		display("Dumped output query to file : " + outFile);
		return outFile;
	}



	private static String generateQuery(String casename) {
		String retqry = "SELECT EMB.DOCUMENT_METADATA_ENTRY_GID, EMB.[VALUE], EMB.EXTENDED_PROPERTY_GID,"
		    + " EMB1.[VALUE] AS MNAME, 'NOCHANGE' AS FLAG, 'NA' AS OLDVAL"
		    + " FROM " + casename + ".DBADMIN.DS_EXTENDED_METADATA_BE EMB(NOLOCK)," + casename + ".DBADMIN.DS_EXTENDED_METADATA_BE EMB1(NOLOCK)"
		    + " WHERE EMB.DOCUMENT_METADATA_ENTRY_GID = EMB1.DOCUMENT_METADATA_ENTRY_GID"
		    + " AND EMB.EXTENDED_PROPERTY_GID IN ("
		    + " SELECT GID FROM " + casename + ".DBADMIN.DS_METADATA_PROPERTIES(NOLOCK) WHERE HIERARCHY_GID = ("
		    + " 	SELECT GID FROM " + casename + ".DBADMIN.DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL"
		    + "	)"
		    + " AND NAME IN ('DS_Custodian', 'DS_Source')"
		    + " )"
		    + " AND EMB1.EXTENDED_PROPERTY_GID = ("
		    + " SELECT GID FROM " + casename + ".DBADMIN.DS_METADATA_PROPERTIES(NOLOCK) WHERE HIERARCHY_GID = ("
		    + "		SELECT GID FROM " + casename + ".DBADMIN.DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL"
		    + "	)"
		    + " AND NAME IN ('DS_MediaName')"
		    + " )"
		    + " ORDER BY EMB.EXTENDED_PROPERTY_GID, EMB1.[VALUE]";
		display("Out query returned: " + retqry);
		return retqry;
	}

	private static String exportFromSource(String casename, String fedbserver, String exportQueryFile) {
		String retval = null;
		String outFile = "outresults_" + System.currentTimeMillis() + ".txt";
    	display("Dumping output to file : " + outFile);

			String cmdOp = "";
			char[] readArr = new char[1];
			boolean success = false;
			String commands = null;
			Process prc = null;
			InputStream cmdIn = null;
			String qry = generateQuery(casename);
			int read = 0;

			try {
				commands = "cmd.exe /C bcp \"" + qry + "\" queryout \"" + outFile + "\" -c -U " + USERNAME + " -P " + PASSWORD + " -S " + fedbserver;
				display ("bcp command:" + commands);
				prc = Runtime.getRuntime().exec(commands);
				cmdIn = prc.getInputStream();
				while ((read = cmdIn.read()) != -1) {
					readArr[0] = (char) read;
					cmdOp += new String(readArr);
				}
				cmdOp = cmdOp.trim();
				display(cmdOp);

				if (cmdOp.indexOf("rows copied") > 0) {
					success = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		retval = (success == true)? outFile: NA ;

		return retval;
	}

	private static int validateCompos() throws IOException {
		// TODO Auto-generated method stub
    	int retval = 0;
    	File curdir = new File (CURR_DIR);
        display ("Current dir : " + curdir.getCanonicalPath());

        if(!curdir.canWrite()){
        	retval = 3;
        }

    	if(!new File(curdir.getCanonicalPath() + "\\" + BCP_EXE).exists()){
    		retval = 1;
    	}

    	if(!new File(curdir.getCanonicalPath() + "\\" + BCP_RLL).exists()){
    		retval = 2;
    	}

    	if(!new File(curdir.getCanonicalPath() + "\\" + BCP_RLL).exists() && !new File(curdir.getCanonicalPath() + "\\" + BCP_EXE).exists()){
    		retval = 12;
    	}

		return retval;
	}

	//displays informative messages
	private static void printinfo(String casename, String bedbserver, String fedbserver, int actionflag, int dropTableFlag) {
		// TODO Auto-generated method stub
		display("----------------------------------");
		display("INPUT PARAMS SUPPLIED");
		display("CASENAME : " + casename);
		display("BACKEND DBSERVER: " + bedbserver );
		display("FRONTEND DBSERVER: " + fedbserver);
		display("");

		switch(actionflag) {
		case 0: display("DEBUG MODE: No changes will be done in the BE DB.");
				break;
		case 1: display("INSERT MODE: New entries will be inserted in the BE DB.");
				break;
		case 2: display("COMPLETE SYNCH MODE: New entries will be inserted in the BE DB.");
				display("BE DB entries will be updated as per FE DB entries.");
				display("Extra entries from BE DB will be eliminated.");
		}

		switch(dropTableFlag){
		case 0: display("Staging table WILL NOT BE DROPPED.");
				break;
		case 1: display("Staging table WILL BE DROPPED.");
		}

		display("-----------------------------------");
		display("Plz confirm supplied input parameters are correct(Press y for Yes , n otherwise)");


	}

	private static void display(String s){
		System.out.println(s);
	}

}