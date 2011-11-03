import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.Collection;

/*******************************************************************************
 * GenMsgUnitTabDelimited.java - a script to generate msg unit tab delimited
 * file (supports multilevel msg units)
 *
 * Inputs: a) load file (tab delimited parent - children uris (multiple children
 * comma separated))
 * b) flag_slds (= 1 for 8.0 cases, = 0 for older version cases)
 * c) bedbserver
 * d) bedbname
 * e) <flag = 1 for medianamewise export, 2 for batchwise export, 3 for current un-numbered batch>
 * f) <batch_num or medianame, should be 0 if flag = 3 in e) above>
 *
 * Output: a txt file with tabe delimited uri and email props
 *
 * Usage: java GenMsgUnitTabDelimited <load file> <flag slds> <bedbserver> <bedbname> <flag> <batch_num or medianame>
 *
 * Tech details : The program works in 2 passes - Pass 1 parses the load file
 * and constructs the trees. Pass 2 populates and prints the metadata values.
 *
 * Version : 2.0 (2009 April 21), rev 6 July 2009
 *
 * Author : RaviKishores@Custom Work Group(TS).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ****************************************************************************************************
 */

// a class template to mimick a node of tree
class Node {
	// props to relate to tree struct
	String uri = null; // data path associated with this node
	Node parentnode = null; // pointer to the parent node of this node
	ArrayList children = null; // pointers to the child nodes

	Node() {

	}

	Node(String ur) {
		this.uri = ur;
	}

	Node(String ur, Node par) {
		this.uri = ur;
		this.parentnode = par;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Node getParentnode() {
		return parentnode;
	}

	public void setParentnode(Node parentnode) {
		this.parentnode = parentnode;
	}

	public ArrayList getChildren() {
		return children;
	}

	public void setChildren(ArrayList children) {
		this.children = children;
	}

	public int getChildrenLength() {
		return children.size();
	}

}

// main program

public class GenMsgUnitTabDelimited {
	// change this as required
	private static final int HASH_CAPACITY = 20000; // Default Hash Map size
	private static final String DELIMETER = "\t"; // default delimiter
	private static final String SLASH = "//";
	private static final String COMMA = ",";
	private static final String FILE_PREFIX = "file:";
	// expected in input/load
	// files

	// !!TRESPASSERS BELOW THIS LINE WILL SUFFER :)
	private static final String ALGORITHM = "MD5"; // checksum generation algo
	// to use
	private static final String EMAIL = "email";
	private static final String ATTACHMENT = "attachment";
	private static final String DATAFOLDERNAME = "data";

	private static final String PLACEHOLDER = "NULL";

	// ALIASES for metadata props
	private static final String EMAILTYPE = "DS_EmailType";
	private static final String ANCESTOR_ID = "DS_AncestorID";
	private static final String ANCESTOR_OF_ID = "DS_AncestorOfID";
	private static final String ATTACHMENT_ID = "DS_AttachmentID";
	private static final String ATTACHMENT_OF_ID = "DS_AttachmentOfID";
	private static final String MSG_UNIT_CHASH = "DS_MsgUnit_ContentHash";
	private static final String MSG_UNIT_CNT = "DS_MsgUnit_DocumentCount";

	//sqlserver related
	static final String CONNECTION_STRING = "net.sourceforge.jtds.jdbc.Driver";
	static final String DB_URL_PREFIX = "jdbc:jtds:sqlserver://";
	static final String MSSQL_USERNAME = "DBADMIN";
	static final String MSSQL_PASSWORD = "DBADMIN";
	static final String DOMAIN_SUFFIX = ".sdp.stratify.com";
	static final int MSSQL_PORT = 1433;

	// header info
	private static final String LOADFILE_HEADER = "PARENT";

	public static void main(String args[]) {
		if (args.length != 6) {
			System.out
					.println("Usage: java GenMsgUnitTabDelimited <load file> <flag slds> <bedbserver> <bedbname> <flag> <batch_num or medianame>");
			System.out
					.println("flag slds = 1 for 8.0 cases and 0 for earlier version cases");
			System.out.print("flag = 1, to generate uri-chash for medialabel");
			System.out.print("flag = 2, to generate uri-chash for a batch");
			System.out.print("flag = 3, to generate uri-chash for currently un-numbered batch");
			return;
		}

		int flag = 0;
		flag = Integer.parseInt(args[1]);

		String bedbserver = args[2];

		if(bedbserver.toLowerCase().indexOf(DOMAIN_SUFFIX)<= 0){
			bedbserver += DOMAIN_SUFFIX;
		}

		String bedbname = args[3];
		String mbatch = args[5];

		System.out
				.println("***************************************************");
		System.out.println("Scheme used for hash calculation: " + ALGORITHM);
		System.out.println("Default size of Data Structures: " + HASH_CAPACITY);
		System.out.println("BE DB SERVER: " + bedbserver);
		System.out.println("BE DB : " + bedbname);

		if (flag == 1) {
			System.out.println("You chose to use the script for 8.0 cases.");
		} else {
			System.out.println("You chose to use the script for < 8.0 cases.");
		}

		HashMap chas = new HashMap(HASH_CAPACITY);
		String qryExport = null;

		//get url
		String dbUrl = getUrl(bedbname, bedbserver);
		displayMessage("DB URL : " + dbUrl);

		switch (Integer.parseInt(args[4])) {
		case 1:
			System.out
					.println("Uri-chash map file will be generate for media : "
							+ mbatch);
				qryExport = "SELECT DME.PATH, EMB.VALUE AS CHASH "
						+ " FROM DS_DOCUMENT_METADATA_ENTRY DME(NOLOCK), DS_EXTENDED_METADATA_BE EMB(NOLOCK)"
						+ " WHERE DME.GID = EMB.DOCUMENT_METADATA_ENTRY_GID"
						+ " AND EMB.EXTENDED_PROPERTY_GID IN ("
						+ " SELECT GID FROM DS_METADATA_PROPERTIES MP(NOLOCK) WHERE MP.HIERARCHY_GID IN ("
						+ " SELECT GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL"
						+ " )"
						+ " AND NAME = 'DS_ContentHash'"
						+ " )"
						+ " AND EMB.DOCUMENT_METADATA_ENTRY_GID IN ("
						+ " SELECT DOCUMENT_METADATA_ENTRY_GID FROM DS_EXTENDED_METADATA_BE EM(NOLOCK)"
						+ " WHERE EM.VALUE = ?"
						+ " AND EM.EXTENDED_PROPERTY_GID IN ("
						+ " SELECT GID FROM DS_METADATA_PROPERTIES MP(NOLOCK) WHERE MP.HIERARCHY_GID IN ("
						+ " SELECT GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL"
						+ " )"
						+ " AND NAME = 'DS_MediaName'"
						+ ")"
						+ ")";

				loadChash(qryExport, chas, dbUrl, mbatch);
			break;
		case 2:
			System.out
					.println("Uri-chash map file will be generate for batch : "
							+ mbatch);
			qryExport = "SELECT DME.PATH, EMB.VALUE AS CHASH"
					+ " FROM DS_DOCUMENT_METADATA_ENTRY DME(NOLOCK), DS_EXTENDED_METADATA_BE EMB(NOLOCK)"
					+ " WHERE DME.GID = EMB.DOCUMENT_METADATA_ENTRY_GID"
					+ " AND EMB.EXTENDED_PROPERTY_GID IN ("
					+ " SELECT GID FROM DS_METADATA_PROPERTIES MP(NOLOCK) WHERE MP.HIERARCHY_GID IN ("
					+ " SELECT GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL"
					+ " )"
					+ " AND NAME = 'DS_ContentHash'"
					+ " )"
					+ " AND DME.SOURCE_ACRONYM = ?";
			// call appr fn
			loadChash(qryExport, chas, dbUrl, mbatch);
			break;
			//default: System.out
			//.println("Invalid flag param. Quitting..");
			//System.exit(0);

			case 3:
			System.out
					.println("Uri-chash map file will be generated for current un-numbered batch : "
							+ mbatch);
			qryExport = "SELECT DME.PATH, EMB.VALUE AS CHASH"
					+ " FROM DS_DOCUMENT_METADATA_ENTRY DME(NOLOCK), DS_EXTENDED_METADATA_BE EMB(NOLOCK)"
					+ " WHERE DME.GID = EMB.DOCUMENT_METADATA_ENTRY_GID"
					+ " AND EMB.EXTENDED_PROPERTY_GID IN ("
					+ " SELECT GID FROM DS_METADATA_PROPERTIES MP(NOLOCK) WHERE MP.HIERARCHY_GID IN ("
					+ " SELECT GID FROM DS_HIERARCHY(NOLOCK) WHERE RELEASE_VERSION IS NOT NULL"
					+ " )"
					+ " AND NAME = 'DS_ContentHash'"
					+ " )"
					+ " AND DME.SOURCE_ACRONYM IS NULL AND NOT_IN_MDT = ?";
			// call appr fn
			loadChash(qryExport, chas, dbUrl, mbatch);
			break;
			default: System.out
			.println("Invalid flag param. Quitting..");
			System.exit(0);

		}

		// to hold root Nodes
		ArrayList rootNodes = new ArrayList();

		// process load file - construct tree struct
		try {
			processLoadFile(args[0], rootNodes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		/*
		 * Set set = uris.entrySet(); Iterator i = set.iterator(); // Display
		 * Elements while (i.hasNext()) { Map.Entry me = (Map.Entry) i.next();
		 * System.out.println((String) me.getKey()); }
		 */

		// to hold contenthash
		//HashMap chas = new HashMap(HASH_CAPACITY);

		// load chash in hashmaps
		/*try {
			loadValues(args[1], chas);
		} catch (Exception ex) {
			ex.printStackTrace();
		}*/



		// traverse tree then populate and print values
		try {
			populatenprintValues(rootNodes, chas, flag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void loadChash(String qryExport, HashMap chas, String dbUrl, String mbatch) {
		// TODO Auto-generated method stub
		// get db connection
		Connection con = null;
		String outFileName = "uri_chash_map_" + System.currentTimeMillis() + ".txt";
		PreparedStatement pstmtMetadata = null;
		ResultSet rsMetadata = null;
		int exportCount = 0;

		displayMessage("Query : " + qryExport + " Param " + mbatch);

		try {
			displayMessage("Connecting to the DB....");
			con = getDBConnection(CONNECTION_STRING, dbUrl, MSSQL_USERNAME,
					MSSQL_PASSWORD);

			//now do the export
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFileName));

			displayMessage("Output File created : " + outFileName);

			pstmtMetadata = con.prepareStatement(qryExport);
			pstmtMetadata.setString(1, mbatch);
			rsMetadata = pstmtMetadata.executeQuery();

			while(rsMetadata.next()){
				chas.put(rsMetadata.getString(1), rsMetadata.getString(2));
				bw.write(rsMetadata.getString(1) + DELIMETER + rsMetadata.getString(2));
				bw.newLine();
				exportCount++;
			}


			cleanResource(pstmtMetadata, rsMetadata);

			bw.close();
			bw = null;

			// print the exported counts
			displayMessage("File: " + outFileName + " No of entries exported: " + exportCount);
			displayMessage("No of entries loaded in HashMap : " + chas.size());
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("loadChash: "  + ex.getMessage());
			System.exit(0);
		}finally{
			try {
				closeCon(con);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	// fn to load content hash and mediapath into hashmaps
	private static void loadValues(String string, HashMap chas)
			throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(string))));
		String line = null;

		while ((line = br.readLine()) != null) {
			String uri = null;
			String ch = null;

			// System.out.println("LINE:" + line);
			StringTokenizer st = new StringTokenizer(line, "\t");

			uri = st.nextToken().trim();
			ch = st.nextToken().trim();

			// validation/manipulations
			if (!uri.startsWith(SLASH)) {
				uri = SLASH + uri;
			}

			chas.put(uri, ch);

			/*
			 * System.out.println("URI " + uri); System.out.println("CHASH " +
			 * ch); System.out.println("MPATH " +
			 * mp.substring(mp.indexOf(MEDIAFOLDERNAME) + 6));
			 */

		}

		br.close();
		br = null;

	}

	private static void printXMLHeader(BufferedWriter bw) {
		String header = "<?xml version='1.0' encoding='UTF-8' ?><hierarchy xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://www.stratify.com/hierarchy'  xsi:schemaLocation='http://www.stratify.com/hierarchy  http://agni:7001/schema/hierarchy.xsd'  id='13' name='lds_trials01' description='metadata taxonomy' language='en-US' type='public' owner='lds'>\n"
				+ "<namespaces>\n"
				+ "	<namespace name='reference.stratify.com' id='0'/>\n"
				+ "</namespaces>\n"
				+ "<hierarchyVersion version='1.0'/>\n"
				+ " <metadata>\n";
		try {
			bw.write(header);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void printXMLEntryStart(BufferedWriter bw, String iuri) {
		String uri = "file:" + iuri;
		try {
			bw.write("<docmetadata uri=\"" + uri
					+ "\" nexpire=\"1\" impt=\"50\" >\n");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void printXMLEntryEnd(BufferedWriter bw) {
		try {
			bw.write("</docmetadata>\n");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void printMetadataValue(BufferedWriter bw, String name,
			String value) {
		String toPrint = "<extension name=\"" + name + "\">\n"
				+ " <item val=\"" + value + "\" />\n" + "</extension>\n";
		try {
			bw.write(toPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void printXMLClose(BufferedWriter bw) {
		// String footer = "</metadata>\n" + "</hierarchy>\n";
		try {
			// bw.write(footer);

			bw.close();
			bw = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// fn that traverses the tree and populate/print values
	private static void populatenprintValues(ArrayList rootNodes, HashMap chas,
			int flag) throws Exception {
		// output xml file name
		String outFile = "out_" + System.currentTimeMillis() + ".txt";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outFile))));

		// printXMLHeader(bw);

		// print headers
		bw.write("uri" + DELIMETER + EMAILTYPE + DELIMETER + ANCESTOR_ID
				+ DELIMETER + ANCESTOR_OF_ID + DELIMETER + ATTACHMENT_ID
				+ DELIMETER + ATTACHMENT_OF_ID + DELIMETER + MSG_UNIT_CHASH
				+ DELIMETER + MSG_UNIT_CNT);
		bw.newLine();

		// do it for all root nodes
		while (!rootNodes.isEmpty()) {
			Node root = null;
			String rhash = null;
			String rdpath = null;
			String contentHash = null;

			// list to hold chash of all constituents
			ArrayList chashs = new ArrayList();
			// counter for constituents of the unit
			int ucount = 0;

			// holds the subtasks to iterate over
			ArrayList subtasks = new ArrayList();

			root = (Node) rootNodes.get(0);

			System.out.println("---------------------");
			System.out.println("ROOT NODE: " + root.getUri());

			// start with root node
			rdpath = (String) root.getUri();
			rdpath = rdpath.substring(rdpath.indexOf(DATAFOLDERNAME));
			contentHash = (String) chas.get(root.getUri());

			rhash = calculateHash(rdpath);
			System.out.println("RHASH for node " + root.getUri() + " RPATH "
					+ rdpath + ": " + calculateHash(rdpath));

			chashs.add(contentHash);
			ucount++;

			subtasks.add(root);

			while (!subtasks.isEmpty()) {
				Node thisTask = null;
				String ihash = null;
				String irdpath = null;

				thisTask = (Node) subtasks.get(0);
				irdpath = (String) thisTask.getUri();
				irdpath = irdpath.substring(irdpath.indexOf(DATAFOLDERNAME));

				ihash = calculateHash(irdpath);

				System.out.println("IHASH for node " + thisTask.getUri()
						+ " RPATH " + irdpath + ": " + calculateHash(irdpath));

				System.out.println(thisTask.getUri() + " removed");

				subtasks.remove(0);

				ArrayList nodeKids = thisTask.getChildren();

				// nodeKids will be null for standalone emails
				if (null != nodeKids) {
					System.out.println("SIZE LIST: " + nodeKids.size());

					for (int count = 0; count < nodeKids.size(); count++) {
						String desc = null;
						Node inode = null;
						String dhash = null;
						String dpath = null;

						desc = ((Node) nodeKids.get(count)).getUri();

						System.out.println("DESC: " + desc);

						dpath = (String) desc.substring(desc
								.indexOf(DATAFOLDERNAME));

						System.out.println("DPATH: " + dpath);

						dhash = calculateHash(dpath);

						inode = (Node) nodeKids.get(count);

						// if this node's kids are not null
						if (inode.getChildren() != null) {
							// add it to subtasks list
							System.out.println("Node: " + inode.getUri());

							chashs.add((String) chas.get(desc));
							ucount++;

							/*
							 * printXMLEntryStart(bw, inode.getUri());
							 * printMetadataValue(bw, ANCESTOR_OF_ID, rhash);
							 * printMetadataValue(bw, ATTACHMENT_OF_ID, ihash);
							 * printMetadataValue(bw, ATTACHMENT_ID, dhash);
							 * printMetadataValue(bw, EMAILTYPE, EMAIL);
							 * printXMLEntryEnd(bw);
							 */

							bw.write(inode.getUri() + DELIMETER + EMAIL
									+ DELIMETER + PLACEHOLDER + DELIMETER
									+ rhash + DELIMETER + dhash + DELIMETER
									+ ihash + DELIMETER + PLACEHOLDER
									+ DELIMETER + PLACEHOLDER);
							bw.newLine();

							subtasks.add(inode);

							System.out.println("INTER NODE: " + inode.getUri());
						} else {
							// print the value as it is leaf node

							chashs.add((String) chas.get(desc));
							ucount++;

							System.out.println("Node: " + inode.getUri());

							/*
							 * printXMLEntryStart(bw, inode.getUri());
							 * printMetadataValue(bw, ANCESTOR_OF_ID, rhash);
							 * printMetadataValue(bw, ATTACHMENT_OF_ID, ihash);
							 * printMetadataValue(bw, EMAILTYPE, ATTACHMENT);
							 * printXMLEntryEnd(bw);
							 */

							bw.write(inode.getUri() + DELIMETER + ATTACHMENT
									+ DELIMETER + PLACEHOLDER + DELIMETER
									+ rhash + DELIMETER + PLACEHOLDER
									+ DELIMETER + ihash + DELIMETER
									+ PLACEHOLDER + DELIMETER + PLACEHOLDER);
							bw.newLine();

						}
					}
				}

			}

			bw.write(root.getUri() + DELIMETER + EMAIL);

			// printXMLEntryStart(bw, root.getUri());

			// not needed for standalone parent email
			if (null != root.getChildren()) {
				/*
				 * printMetadataValue(bw, ANCESTOR_ID, rhash);
				 * printMetadataValue(bw, ATTACHMENT_ID, rhash);
				 */
				bw.write(DELIMETER + rhash + DELIMETER + PLACEHOLDER
						+ DELIMETER + rhash + DELIMETER + PLACEHOLDER);

			} else {
				bw.write(DELIMETER + PLACEHOLDER + DELIMETER + PLACEHOLDER
						+ DELIMETER + PLACEHOLDER + DELIMETER + PLACEHOLDER);
			}
			/* printMetadataValue(bw, EMAILTYPE, EMAIL); */
			// only for 8.0 cases and if email is not standalone
			if ((flag == 1) && (null != root.getChildren())) {
				// sort the chash list
				Collections.sort(chashs);

				for (int k = 0; k < chashs.size(); k++) {
					System.out.println("After sorting: " + chashs.get(k));
				}

				System.out.println("After concatenation: " + join(chashs, ","));

				/*
				 * printMetadataValue(bw, MSG_UNIT_CHASH,
				 * calculateHash(join(chashs, ""))); printMetadataValue(bw,
				 * MSG_UNIT_CNT, ucount + "");
				 */

				bw.write(DELIMETER + calculateHash(join(chashs, ""))
						+ DELIMETER + ucount);
			} else {
				bw.write(DELIMETER + PLACEHOLDER + DELIMETER + PLACEHOLDER);
			}

			bw.newLine();
			// printXMLEntryEnd(bw);

			// keep removing the roots that are done
			rootNodes.remove(0);

		}

		printXMLClose(bw);

	}

	// meat of the program -- parses the load file and constructs trees out of
	// it
	private static void processLoadFile(String string, ArrayList rn)
			throws Exception {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(string))));
		String line = null;
		String parent = null;
		String kids = null;
		// to hold uri - nodes mapping
		HashMap hm = new HashMap(HASH_CAPACITY);

		// all processing of the load file has to be within following block
		while ((line = br.readLine()) != null) {
			// System.out.println("LINE:" + line);
			// if there is header, skip that line
			if (line.indexOf(LOADFILE_HEADER) > -1) {
				continue;
			}

			StringTokenizer st = new StringTokenizer(line, DELIMETER);
			while (st.hasMoreTokens()) {

				Node pnode = null;
				parent = st.nextToken().trim();

				// add front slashes if not present
				if (!parent.startsWith(SLASH)) {
					parent = SLASH + parent;
				}

				if (!hm.containsKey(parent)) {
					// create node for parent
					pnode = new Node();
					rn.add(pnode);
				} else {
					// retrieve node from HashMap
					pnode = (Node) hm.get(parent);
				}

				if (st.hasMoreTokens()) {
					kids = st.nextToken().trim();
					// System.out.println(parent + DELIMETER + kids);
					String[] kid = null;
					ArrayList kidsNodes = new ArrayList();

					// if child entries are present
					if (kids.indexOf(COMMA) > -1) {
						kid = kids.split(COMMA);
					} else {
						kid = new String[1];
						kid[0] = kids;
					}

					// remove spurious spaces from these array entries
					for (int cnt = 0; cnt < kid.length; cnt++) {
						kid[cnt] = kid[cnt].trim();

						// add slashes if not present
						if (!kid[cnt].startsWith(SLASH)) {
							kid[cnt] = SLASH + kid[cnt];
						}
					}

					// create node for kids
					for (int count = 0; count < kid.length; count++) {
						Node kidNode = null;

						if (!hm.containsKey(kid[count])) {
							kidNode = new Node();
						} else {
							kidNode = (Node) hm.get(kid[count]);
						}

						kidNode.setUri(kid[count]);
						kidNode.setParentnode(pnode);

						// this node not in Map - this is leaf
						if (!hm.containsKey(kid[count])) {
							kidNode.setChildren(null);
						}

						// put these entries in HashMap
						hm.put(kid[count], kidNode);

						// List that will be used by Parent
						kidsNodes.add(kidNode);

						// if rootnode list contains this node means it's not
						// root
						// anymore
						if (rn.contains(kidNode)) {
							rn.remove(kidNode);
						}
					}

					// update kids list in parent node
					pnode.setChildren(kidsNodes);
				}

				pnode.setUri(parent);

				// only for standalone parent mail
				if (!hm.containsKey(parent)) {
					pnode.setParentnode(null);
				}

				// put parent entry in HashMap
				hm.put(parent, pnode);

			}
		}

		// free the map
		hm.clear();
		hm = null;

		br.close();
		br = null;

	}

	// fn to calculate hash - set ALGORITHM accordingly
	private static String calculateHash(String message) {
		String hex = "";
		try {
			byte[] buffer = message.getBytes();
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.update(buffer);
			byte[] digest = md.digest();

			for (int i = 0; i < digest.length; i++) {
				int b = digest[i] & 0xff;
				if (Integer.toHexString(b).length() == 1)
					hex += "0";
				hex += Integer.toHexString(b);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hex;
	}


	// fn that returns a joined String, given a collection and delimiter
	public static String join(Collection s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	private static void displayMessage(String string) {
		System.out.println(string);

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

	private static void cleanResource(PreparedStatement pstmtSanity,
			ResultSet rsSanity) throws SQLException {
		// TODO Auto-generated method stub
		pstmtSanity.close();
		pstmtSanity = null;
		rsSanity.close();
		rsSanity = null;
	}

	private static void closeCon(Connection con) throws SQLException {
		if (con != null) {
			con.close();
			con = null;
		}

	}
}