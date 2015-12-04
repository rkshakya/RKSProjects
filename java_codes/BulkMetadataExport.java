import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*******************************************************************************
 * BulkMetadataExport.java - <comment>
 *
 * Inputs:a) Input File with (FolderName - FolderID - Created_Date - Doc_Count -
 * OutputFileName) TAB DELIMITED (Default)
 *
 * Outputs:
 *
 * Usage: java
 *
 * Version : 1.0 (Jan 22, 2009)
 *
 * Author : RavikishoreS@Custom Work Group(TS-India).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ******************************************************************************
 */

public class BulkMetadataExport {
	// **** PARAMS SPECIFIC TO EXPORT***************//
	// query to be used for export
	private static final String QRY_METADATA_EXPORT = "SELECT "
		+ " ISNULL(ISNULL(DBADMIN.dsf_getProductionValues(19, DME.GID, 'DS_Prefix', ';'), '') + "
		+ " ISNULL(DBADMIN.dsf_getProductionValues(19, DME.GID, 'DS_StartBates', ';'), '')+ "
		+ " ISNULL(DBADMIN.dsf_getProductionValues(19, DME.GID, 'DS_Suffix', ';'), ''), '') AS STARTBATES, "
		+ " ISNULL(ISNULL(DBADMIN.dsf_getProductionValues(19, DME.GID, 'DS_Prefix', ';'), '') + "
		+ " ISNULL(DBADMIN.dsf_getProductionValues(19, DME.GID, 'DS_EndBates', ';'), '')+ "
		+ " ISNULL(DBADMIN.dsf_getProductionValues(19, DME.GID, 'DS_Suffix', ';'), ''), '') AS ENDBATES, "
		+ " ISNULL(DME.PUBLISHED_AT, '') AS DOC_DATE, "
		+ " ISNULL(DBADMIN.csf_getExtendedRawValues(19, DME.GID, 'DS_Sender'),'') AS SENDER,"
		+ " ISNULL(DBADMIN.csf_getExtendedRawValues(19, DME.GID, 'DS_Receiver'), '') AS RECIPIENTS, "
		+ " ISNULL(DBADMIN.dsf_getExtendedValues(19, DME.GID, 'DS_Custodian'), '') AS CUSTODIAN, "
		+ " DBADMIN.csf_hasAttachments(DME.GID) AS HAS_ATTACHMENTS "
		+ " FROM DS_DOCUMENT_METADATA_ENTRY DME(NOLOCK), DS_CLASSIFICATION CLS(NOLOCK)"
		+ " WHERE DME.GID = CLS.DOCUMENT_METADATA_ENTRY_GID "
		+ " AND CLS.TOPIC_ID = ? "
		+ " ORDER BY DBADMIN.dsf_getProductionValues(19, DME.GID, 'DS_StartBates', ';')";

	// delimiter to be used in export file
	private static final String DEST_DELIMITER = "þþ";
	// header to be used in export file
	private static final String HEADER = "þSTARTBATESþþENDBATESþþDOC_DATEþþSENDERþþRECIPIENTSþþ"
		+ "CUSTODIANþþHAS_ATTACHMENTSþ";
	private static final String BEGIN_MARKER = "þ";
	private static final String END_MARKER = "þ";


	// **** SANITY RELATED ***********************//
	private static final String QRY_FOLDER_SANITY = "SELECT T.[NAME], T.TOPIC_ID, T.HIERARCHY_VERSION_GID,  COUNT(CLS.GID) AS CNT "
			+ " FROM DS_TOPIC T(NOLOCK), DS_CLASSIFICATION CLS(NOLOCK)"
			+ " WHERE CLS.TOPIC_ID = ? "
			+ " AND CLS.TOPIC_ID = T.TOPIC_ID"
			+ " GROUP BY T.[NAME], T.TOPIC_ID, T.HIERARCHY_VERSION_GID";

	// *****CONSTANTS RELATED TO SQLSERVER*************//
	static final String CONNECTION_STRING = "net.sourceforge.jtds.jdbc.Driver";
	static final String DB_URL_PREFIX = "jdbc:jtds:sqlserver://";
	static final String MSSQL_USERNAME = "DBADMIN";
	static final String MSSQL_PASSWORD = "DBADMIN";
	static final String DOMAIN_SUFFIX = ".lds.asp.stratify.com";
	static final int MSSQL_PORT = 1433;

	// *****RELATED TO INPUT FILE *********************//
	static final String SRC_DELIMITER = "\t";

	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out
					.println("Usage : java BulkMetadataExport <FEDBNAME> <FEDBSERVER> <INPUTFILE>");
			System.exit(0);
		}

		String feDb = args[0];
		String feDbServer = args[1];
		String inputFileName = args[2];

		// display the input params
		displayMessage("---------INPUT PARAMS SUPPLIED-----------");
		displayMessage("FE DB NAME: " + feDb);
		displayMessage("FE DB SERVER: " + feDbServer);
		displayMessage("INPUT FILE:" + inputFileName);

		// get connection to the DB
		// append domain prefix
		if (feDbServer.toLowerCase().indexOf(DOMAIN_SUFFIX) < 0) {
			feDbServer += DOMAIN_SUFFIX;
		}

		// get dburl
		String dbUrl = getUrl(feDb, feDbServer);
		displayMessage("DB URL : " + dbUrl);

		// get db connection
		Connection con = null;

		try {
			displayMessage("Connecting to the DB....");
			con = getDBConnection(CONNECTION_STRING, dbUrl, MSSQL_USERNAME,
					MSSQL_PASSWORD);

			// read the input file
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(inputFileName)));

			String line, folderName, folderId, docCount, outFileName;
			String folderNameDB;
			int rowCount = 0;
			int docCountDB = 0;
			int hierarchyGid = 0;
			int exportCount = 0;
			PreparedStatement pstmtSanity, pstmtMetadata;
			ResultSet rsSanity, rsMetadata;
			StringTokenizer tokenizer;


			while ((line = br.readLine()) != null) {
				rowCount++;
				folderName = null;
				folderId = null;
				docCount = null;
				outFileName = null;
				pstmtSanity = null;
				docCountDB = 0;
				folderNameDB = null;
				hierarchyGid = 0;
				exportCount = 0;

				tokenizer = new StringTokenizer(line, SRC_DELIMITER);

				folderName = tokenizer.nextToken().trim();
				folderId = tokenizer.nextToken().trim();
				tokenizer.nextToken();
				//replace , in docCount if present
				if(tokenizer.hasMoreTokens()){
				docCount = tokenizer.nextToken().trim().replaceAll(",", "");
				}
				if(tokenizer.hasMoreTokens()){
				outFileName = tokenizer.nextToken().trim();
				}

				displayMessage("DOCCOUNT: " + docCount);
				displayMessage("OUTFILENAME: " + outFileName);

				System.out.println();

				if((null == folderId) || (folderId.length() == 0)){
					displayMessage("ATTENTION: Folder ID missing for entry. Skipping entry no: " + rowCount );
					continue;
				}

				// and if all the expected fields exist
				if (null == outFileName) {
					// give the default output filename if it not exists
					outFileName = "output_" + rowCount + ".dat";
				}

				// do some sanity checks for each entry -- topic name, topic
				// exists, counts etc
				pstmtSanity = con.prepareStatement(QRY_FOLDER_SANITY);
				pstmtSanity.setInt(1, Integer.parseInt(folderId));
				rsSanity = pstmtSanity.executeQuery();

				while(rsSanity.next()){
					folderNameDB = rsSanity.getString(1);
					hierarchyGid = rsSanity.getInt(3);
					docCountDB = rsSanity.getInt(4);
				}
				cleanResource(pstmtSanity, rsSanity);

				// display topic info
				displayMessage("FolderNameFile: " + folderName + " DocCountFile: " + docCount);
				displayMessage("FolderNameDB: " + folderNameDB + " DocCountDB: " + docCountDB);
				displayMessage("Hierarchy GID: " + hierarchyGid);

				System.out.println();

				if(!(folderName.equalsIgnoreCase(folderNameDB))&&(Integer.parseInt(docCount)!= docCountDB)){
					displayMessage("ATTENTION: Either FolderName or DOC_COUNT does not match. Please check.");
				}


				//all checks passed - now do the metadata export
				BufferedWriter bw = new BufferedWriter(new FileWriter(outFileName));

				displayMessage("Output File created : " + outFileName);

				// run the query and export metadata -- use concordance delimiters
				// newline at the end

				pstmtMetadata = con.prepareStatement(QRY_METADATA_EXPORT);
				pstmtMetadata.setInt(1, Integer.parseInt(folderId));
				rsMetadata = pstmtMetadata.executeQuery();

				bw.write(HEADER);
				bw.newLine();

				while(rsMetadata.next()){
					bw.write(BEGIN_MARKER + rsMetadata.getString(1) + DEST_DELIMITER);
					bw.write(rsMetadata.getString(2) + DEST_DELIMITER);
					bw.write(rsMetadata.getString(3) + DEST_DELIMITER);
					bw.write(rsMetadata.getString(4) + DEST_DELIMITER);
					bw.write(rsMetadata.getString(5) + DEST_DELIMITER);
					bw.write(rsMetadata.getString(6) + DEST_DELIMITER);
					bw.write(rsMetadata.getInt(7) + END_MARKER);
					bw.newLine();
					exportCount++;
				}


				cleanResource(pstmtMetadata, rsMetadata);

				bw.close();
				bw = null;

				// print the exported counts
				displayMessage("File: " + outFileName + " No of entries exported: " + exportCount);

				displayMessage("----------------------------------");

			}
			br.close();
			br = null;

		} catch (Exception e) {
			displayMessage("Error: Connection to DB Failed. Bailing out...");
			e.printStackTrace();
		} finally {
			try {
				closeCon(con);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void cleanResource(PreparedStatement pstmtSanity,
			ResultSet rsSanity) throws SQLException {
		// TODO Auto-generated method stub
		pstmtSanity.close();
		pstmtSanity = null;
		rsSanity.close();
		rsSanity = null;
	}

	private static String getUrl(String dbname, String dbserver) {
		String url;
		url = DB_URL_PREFIX + dbserver + ":" + MSSQL_PORT + "/" + dbname;
		return url;
	}

	private static Connection getDBConnection(String conString, String dbURL,
			String uName, String passWd) throws ClassNotFoundException,
			SQLException {
		Connection con;
		Class.forName(conString);
		displayMessage("URL: " + dbURL + "UNAME: " + uName + "PASSWD: "
				+ passWd);
		con = DriverManager.getConnection(dbURL, uName, passWd);
		return con;
	}

	private static void closeCon(Connection con) throws SQLException {
		if (con != null) {
			con.close();
			con = null;
		}

	}

	private static void displayMessage(String string) {
		System.out.println(string);

	}

}
