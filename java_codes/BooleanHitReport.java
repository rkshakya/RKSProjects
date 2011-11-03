import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/*******************************************************************************
 * BooleanHitReport.java - generates hit report for the terms used in Boolean filtering
 *
 * Inputs: 3 input file in following order: <BooleanModelFileXML> <ClusteringLogFile> <BooleanSummaryFileXML>
 *
 * Outputs:a)A report in html format with counts + percentage of docs hit per term 
 *
 * Usage: java BooleanHitReport <BooleanModelFileXML> <ClusteringLogFile> <BooleanSummaryFileXML>
 *
 * Version : 3.0 (Feb 23, 2009)
 *
 * Author : RavikishoreS@Custom Work Group(TS-India).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ******************************************************************************
 */

/*bean used to store doc counts and duplicate counts*/
class Element {
	int docCount = 0; //docCount obtd from boolean summary xml file
	int dupCount = 0; //dupCount obtd from boolean summary xml file

	Element() {
	}

	Element(int i, int j) {
		docCount = i;
		dupCount = j;

	}

	void setDocCount(int m) {
		docCount = m;

	}

	void setDupCount(int n) {
		dupCount = n;
	}

	int getDocCount() {
		return docCount;
	}

	int getDupCount() {
		return dupCount;
	}
}

public class BooleanHitReport {
	private static final int DS_SIZE = 100;  //default capacity of Data Structs 
	private static final String ENCODING_SCHEME = "UTF-8"; //encoding scheme to use
	
	//input files specific
	private static final String EXPRESSION_TAG = "expression";//expression tag name in Boolean Model files
	private static final String RULE_TAG = "rule";//rule tag name in Boolean summary files
	private static final String ATTRIBUTE = "hash";//attrib to check in Boolean summary files
	private static final String DOCCOUNT_TAG = "docCount";//docCount tag in Boolean summary files
	private static final String DUPCOUNT_TAG = "duplicateDocCount";//dupCount tag in Boolean summary files
	private static final String COLON = ":";
	
	//output file specific
	private static final String OUTPUTFILENAME = "report_";
	private static final String OUTPUTFILEEXTENSION = ".html";
	
	//holds all the expressions obtd from booleanModel xml file
	private static ArrayList allterms = new ArrayList(DS_SIZE);
	//holds hit terms and their corresp counts
	private static LinkedHashMap hitterms = new LinkedHashMap(DS_SIZE);
		

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out
					.println("Usage: java BooleanHitReport <BooleanModelFileXML> <ClusteringLogFile> <BooleanSummaryFileXML>"); 
			return;
		}

		// populate all the boolean terms
		try{
		populateList(args[0]);
		}catch(Exception e){
			System.out.println("ERROR: populating boolean terms.");
			e.printStackTrace();
		}

		// populate all the terms that got hits
		try{
		populateValues(args[2]);
		} catch(Exception e){
			System.out.println("ERROR: populating hit terms.");
			e.printStackTrace();
		}

		try{
		writeReport(args[1]);
		}catch(Exception e){
			System.out.println("ERROR: generating reports.");
			e.printStackTrace();
		}
		
		//cleanUp the mess
		allterms.clear();
		allterms = null;
		hitterms.clear();
		hitterms = null;
	}
	

	// function to populate hit entries into map
	private static void populateValues(String name) throws Exception {
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			System.out
					.println("populateValues: Boolean Summary XML location passed: "
							+ name);
			// parse using builder to get DOM representation of the XML file
			dom = db.parse(name);
		} catch (Exception ex) {
			System.out.println("populateValues: Error: ");
			ex.printStackTrace();
		}

		// get a nodelist of 'rule' elements
		NodeList nl = dom.getElementsByTagName(RULE_TAG);
		System.out.println("populateValues:Rule Node Size: " + nl.getLength());
		
		Node parnode;

		if (null != nl && nl.getLength() > 0) {
			for (int rulecount = 0; rulecount < nl.getLength(); rulecount++) {
				// get each of these elements
				parnode = (Node) nl.item(rulecount);

				System.out.println("populateValues: Rule Node Name: " + parnode.getNodeName());
				
				String expression = null;
				String docCount = null;
				String dupCount = null;				

				//skip the node with <rule hash="0">
				 NamedNodeMap attributes = parnode.getAttributes();
			     Node hashValue = attributes.getNamedItem(ATTRIBUTE);
			     if(hashValue.getNodeValue().equals("0")){
			    	 continue;
			     }


				//fetch the childnodes
				NodeList childlist = parnode.getChildNodes();
				Node child = null;

				for(int childcnt = 0; childcnt < childlist.getLength(); childcnt++){
					child = (Node) childlist.item(childcnt);
					if(child.getNodeName().equalsIgnoreCase(EXPRESSION_TAG)){
						expression = child.getTextContent();
					}
					if(child.getNodeName().equalsIgnoreCase(DOCCOUNT_TAG)){
						docCount = child.getTextContent();
					}
					if(child.getNodeName().equalsIgnoreCase(DUPCOUNT_TAG)){
						dupCount = child.getTextContent();
					}
				}



				System.out.println("populateValues: Expression: " + expression + " docCount: " + docCount + " dupCount: " + dupCount);

				//populate in Map
				hitterms.put(expression, new Element(Integer.parseInt(docCount), Integer.parseInt(dupCount)));

			}
		}

		System.out.println("populateValues: Added " + hitterms.size() + " hit entries to hashmap");

	}



	// gather all the search terms(hits + non hits) into ArrayList	
	private static void populateList(String name) throws Exception {
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			System.out
					.println("populateList: Boolean Model XML location passed: "
							+ name);
			// parse using builder to get DOM representation of the XML file
			dom = db.parse(name);
		} catch (Exception ex) {
			System.out.println("populateList: Error");
			ex.printStackTrace();
		}

		// get a nodelist of 'expression' elements
		NodeList nl = dom.getElementsByTagName(EXPRESSION_TAG);
		System.out.println("populateList: Node Size: " + nl.getLength());
		
		String expression;
		Node node;

		if (null != nl && nl.getLength() > 0) {
			for (int expCount = 0; expCount < nl.getLength(); expCount++) {
				expression = null;
				// get the expression element
				node = (Node) nl.item(expCount);

				try {
					expression = node.getTextContent();
					System.out.println("populateList: EXPRESSION: " + expression);
				} catch (Exception ex) {
					System.out.println("populateList: Error: ");
					ex.printStackTrace();
				}

				allterms.add(expression);

			}
		}

		System.out.println("populateList: Added " + allterms.size()
				+ " entries to expressions Arraylist.");

	}



	private static void writeReport(String name) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(name)), ENCODING_SCHEME));
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(OUTPUTFILENAME
						+ System.currentTimeMillis() + OUTPUTFILEEXTENSION)), ENCODING_SCHEME));

		float totaldocs = 0;
		float removedDocs = 0;
		float remainingDocs = 0;			
		String line = null;
		String number;

		while ((line = br.readLine()) != null) {
			number = null;
			line = line.trim();

			if (line.length() < 2) {
				continue;
			}
			if (line.toLowerCase().indexOf("total documents in input corpus") > -1) {
				number = line.substring(line.indexOf(COLON) + 1).trim();
				totaldocs = Float.parseFloat(number);
				System.out.println("writeReport: Found total: " + totaldocs);
				continue;
			}
			if (line.toLowerCase().indexOf(
					"documents with unsupported extensions") > -1) {
				number = line.substring(line.indexOf(COLON) + 1).trim();
				removedDocs = Float.parseFloat(number);
				System.out.println("writeReport : Found filtered: " + removedDocs);
				remainingDocs = totaldocs - removedDocs;
				continue;
			}
		}

		br.close();
		br = null;

		bw.write("Total docs in input corpus: " + (int) totaldocs + "<br>");
		bw.newLine();
		bw.write("No of docs with unsupported extension: " + (int) removedDocs);
		bw.newLine();

		bw
				.write("<html><head><meta http-equiv=\"content-type\" content=\"text-html; charset=utf-8\"></head><body><table border=\"1\" cellpadding=\"1\">");
		bw.newLine();
		bw
				.write("<tr><td width=\"60%\"><b>Expression</b></td><td><b>Total Hits</b></td><td><b>%age of Input Corpus</b></td><td><b>Matches</b></td><td><b>Content Duplicates</b></td></tr>");
		bw.newLine();

		Set keys = hitterms.keySet();
		Iterator iter = keys.iterator();
		String key;
		int total = 0;

		while (iter.hasNext()) {
			key = (String) iter.next();

			System.out.println("writeReport: KEY: " + key);
			Element values = (Element) hitterms.get(key);

			// remove the entries that got hits from the Arraylist
			if (allterms.contains(key)) {
				allterms.remove(key);
				System.out.println("writeReport: REMOVED KEY : " + key);
			}

			total = values.getDocCount() + values.getDupCount();
			bw.write("<tr><td>");
			bw.write(key);
			bw.write("</td><td>");

			bw.write(total + "</td><td>" + ((total / remainingDocs) * 100)
					+ "%</td><td>" + values.getDocCount() + "</td><td>"
					+ values.getDupCount() + "</td></tr>");
			bw.newLine();
		}

		// print out the 0 hits terms
		System.out.println("writeReport: Size of 0 hits: " + allterms.size());
		for (int count = 0; count < allterms.size(); count++) {
			System.out.println("writeReport: NO HITS: " + allterms.get(count));
			bw.write("<tr><td>" + allterms.get(count) + "</td><td>0</td></tr>");
			bw.newLine();
		}

		bw.close();
		bw = null;

	}

}
