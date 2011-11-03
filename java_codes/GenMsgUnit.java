import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.Collection;

/*******************************************************************************
 * GenMsgUnit.java - a script to generate msg unit xml file
 * (supports multilevel msg units)
 *
 * Inputs: a) load file (tab delimited parent - children uris (multiple children
 * comma separated)) b) input file (uri-chash tab delimited)
 * c) flag (= 1 for 8.0 cases, = 0 for older version cases)
 *
 * Output: an xml file named 'out_<timestamp>.xml'
 *
 * Usage: java GenMsgUnit <load file> <uri-chash mapping file(tab
 * delimited)> <flag>
 *
 * Tech details : The program works in 2 passes - Pass 1 parses the load file
 * and constructs the trees. Pass 2 populates and prints the metadata values.
 *
 * Version : 1.0 (2008 April 3), 2.0 (2009 May 26)
 *
 * Author : Custom Work Group(TS).
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

public class GenMsgUnit {
	// change this as required
	private static final int HASH_CAPACITY = 20000; // Default Hash Map size
	private static final String DELIMETER = "\t"; // default delimiter
	private static final String SLASH = "//";
	private static final String COMMA = ",";
	private static final String FILE_PREFIX  = "file:";
	// expected in input/load
	// files

	// !!TRESPASSERS BELOW THIS LINE WILL SUFFER :)
	private static final String ALGORITHM = "MD5"; // checksum generation algo
	// to use
	private static final String EMAIL = "email";
	private static final String ATTACHMENT = "attachment";
	private static final String DATAFOLDERNAME = "data";

	// ALIASES for metadata props
	private static final String EMAILTYPE = "DS_EmailType";
	private static final String ANCESTOR_ID = "DS_AncestorID";
	private static final String ANCESTOR_OF_ID = "DS_AncestorOfID";
	private static final String ATTACHMENT_ID = "DS_AttachmentID";
	private static final String ATTACHMENT_OF_ID = "DS_AttachmentOfID";
	private static final String MSG_UNIT_CHASH = "DS_MsgUnit_ContentHash";
	private static final String MSG_UNIT_CNT = "DS_MsgUnit_DocumentCount";

	//header info
	private static final String LOADFILE_HEADER = "PARENT";

	public static void main(String args[]) {
		if (args.length != 3) {
			System.out
					.println("Usage: java GenMsgUnit <load file> <uri-chash mapping file(tab delimited)> <flag>");
			System.out
					.println("flag = 1 for 8.0 cases and 0 for earlier version cases");
			return;
		}

		int flag = 0;
		flag = Integer.parseInt(args[2]);

		System.out.println("***************************************************");
		System.out.println("Scheme used for hash calculation: " + ALGORITHM);
		System.out.println("Default size of Data Structures: " + HASH_CAPACITY);

		if (flag == 1) {
			System.out.println("You chose to use the script for 8.0 cases.");
		} else {
			System.out.println("You chose to use the script for < 8.0 cases.");
		}

		// to hold root Nodes
		ArrayList rootNodes = new ArrayList();

		// process load file - construct tree struct
		try {
			System.out.println("Processing load file....");
			processLoadFile(args[0], rootNodes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}


		// to hold contenthash
		HashMap chas = new HashMap(HASH_CAPACITY);

		// load chash in hashmaps
		try {
			System.out.println("Loading mapping file.....");
			loadValues(args[1], chas);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// traverse tree then populate and print values
		try {
			System.out.println("Generating output msg unit xml file....");
			populatenprintValues(rootNodes, chas,  flag);
		} catch (Exception ex) {
			ex.printStackTrace();
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

			StringTokenizer st = new StringTokenizer(line, DELIMETER);

			uri = st.nextToken().trim();
			ch = st.nextToken().trim();

			//validation/manipulations
			if(!uri.startsWith(SLASH)){
				uri = SLASH + uri;
			}

			chas.put(uri, ch);

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
		String uri = FILE_PREFIX + iuri;
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
		String footer = "</metadata>\n" + "</hierarchy>\n";
		try {
			bw.write(footer);

			bw.close();
			bw = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// fn that traverses the tree and populate/print values
	private static void populatenprintValues(ArrayList rootNodes, HashMap chas, int flag) throws Exception {
		// output xml file name
		String outFile = "out_" + System.currentTimeMillis() + ".xml";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(outFile))));

		printXMLHeader(bw);

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
					System.out.println("CHILD NODES SIZE: " + nodeKids.size());

					for (int count = 0; count < nodeKids.size(); count++) {
						String desc = null;
						Node inode = null;
						String dhash = null;
						String dpath = null;

						desc = ((Node) nodeKids.get(count)).getUri();

						System.out.println("DESC: " + desc);

						dpath = (String) desc.substring(desc.indexOf(DATAFOLDERNAME));

						System.out.println("DPATH: " + dpath);

						dhash = calculateHash(dpath);

						inode = (Node) nodeKids.get(count);

						// if this node's kids are not null
						if (inode.getChildren() != null) {
							// add it to subtasks list
							System.out.println("Node: " + inode.getUri());

							chashs.add((String) chas.get(desc));
							ucount++;

							printXMLEntryStart(bw, inode.getUri());
							printMetadataValue(bw, ANCESTOR_OF_ID, rhash);
							printMetadataValue(bw, ATTACHMENT_OF_ID, ihash);
							printMetadataValue(bw, ATTACHMENT_ID, dhash);
							printMetadataValue(bw, EMAILTYPE, EMAIL);
							printXMLEntryEnd(bw);

							subtasks.add(inode);

							System.out.println("INTERMEDIATE NODE: " + inode.getUri());
						} else {
							// print the value as it is leaf node

							chashs.add((String) chas.get(desc));
							ucount++;

							System.out.println("Node: " + inode.getUri());

							printXMLEntryStart(bw, inode.getUri());
							printMetadataValue(bw, ANCESTOR_OF_ID, rhash);
							printMetadataValue(bw, ATTACHMENT_OF_ID, ihash);
							printMetadataValue(bw, EMAILTYPE, ATTACHMENT);
							printXMLEntryEnd(bw);

						}
					}
				}

			}

			printXMLEntryStart(bw, root.getUri());
			// not needed for standalone parent email
			if (null != root.getChildren()) {
				printMetadataValue(bw, ANCESTOR_ID, rhash);
				printMetadataValue(bw, ATTACHMENT_ID, rhash);
			}
			printMetadataValue(bw, EMAILTYPE, EMAIL);
			// only for 8.0 cases and if the email is not standalone
			if ((flag == 1) && (null != root.getChildren())) {
				// sort the chash list
				Collections.sort(chashs);

				for (int k = 0; k < chashs.size(); k++) {
					System.out.println("After sorting: " + chashs.get(k));
				}

				System.out.println("After concatenation: " + join(chashs, ","));

				printMetadataValue(bw, MSG_UNIT_CHASH, calculateHash(join(
						chashs, "")));
				printMetadataValue(bw, MSG_UNIT_CNT, ucount + "");
			}
			printXMLEntryEnd(bw);

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
			//if there is header, skip that line
					if(line.indexOf(LOADFILE_HEADER) > -1){
						continue;
				}

			StringTokenizer st = new StringTokenizer(line, DELIMETER);
			while (st.hasMoreTokens()) {

				Node pnode = null;
				parent = st.nextToken().trim();

				//add front slashes if not present
				if(!parent.startsWith(SLASH)){
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

						//add slashes if not present
						if(!kid[cnt].startsWith(SLASH)){
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
}