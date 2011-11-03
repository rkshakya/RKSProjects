import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*******************************************************************************
 * MediaLabelHitReport.java - <comment>
 *
 * Inputs: 1) booleanClassification xml file 2) uri-medialabel mapping file (tab
 * delimited)
 *
 * Outputs: 1) A html file with MediaLabel, Expression(Boolean Term), Count of
 * docs
 *
 * Usage: java java MediaLabelHitReport booleanClassification
 * uri-mlabelmapping(tab delimited)
 *
 * Version : 1.0 No 7 2007, (Rev May 29, 2009)
 *
 * Author : RavikishoreS@Custom Work Group(TS-India).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ******************************************************************************
 */

public class MediaLabelHitReport {
	private static final int DS_CAPACITY = 10000;
	private static final String DELIMITER = "\t";
	private static final String RULETAG = "rule";
	private static final String HASHATTRIB = "hash";
	private static final String EXPRESSIONTAG = "expression";
	private static final String DOCUMENTTAG = "document";
	private static final String ORIGINALLOCATIONATTRIB = "originalLocation";
	private static final String FILEPREFIX = "file:";
	private static final String SLASHES = "//";

	static Document dom = null;
	// holds uri as key and ArrayList of expressions as values
	static LinkedHashMap exprPath = new LinkedHashMap(DS_CAPACITY);
	// key - medialabel, uri list as value
	static LinkedHashMap mlabelPath = new LinkedHashMap(DS_CAPACITY);

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.out
					.println("Usage: java MediaLabelHitReport booleanClassification uri-mlabelmapping(tab delimited)");
			return;
		}
		// load medialabel-pathlist mapping
		populateMediaLabelMap(args[1]);

		// load path - expression list mapping after parsing xml file
		processXML(args[0]);

		// generateReports
		generateReports();

	}

	private static void populateMediaLabelMap(String custodianMapFile)
			throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(custodianMapFile))));
		String line = null;
		String path, medialabel;
		String[] fields = null;
		ArrayList tempArr;

		while ((line = br.readLine()) != null) {
			// first field - path , 2nd medialabel.
			path = null;
			medialabel = null;
			tempArr = null;

			fields = line.split(DELIMITER);

			path = fields[0].toLowerCase().trim();
			medialabel = fields[1].trim();

			if(!path.startsWith(SLASHES)){
				path = SLASHES + path;
			}

			if (mlabelPath.containsKey(medialabel)) {
				tempArr = (ArrayList) mlabelPath.get(medialabel);
				tempArr.add(path);
				mlabelPath.put(medialabel, tempArr);

			} else {
				ArrayList pathArr = new ArrayList();
				pathArr.add(path);
				mlabelPath.put(medialabel, pathArr);

			}

		}

		br.close();
		br = null;
		System.out.println("No of entries added to medialabel- uri map." + mlabelPath.size());
	}

	private static void processXML(String string) {
		parseXML(string);
		parseDocument();
	}

	private static void parseXML(String str) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(str);
			System.out
					.println("Boolean Classification XML parsed successfully.");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private static void parseDocument() {
		// get the root elememt
		Element docEle = dom.getDocumentElement();

		// get a nodelist of 'rule' elements
		NodeList nl = docEle.getElementsByTagName(RULETAG);

		if (nl != null && nl.getLength() > 0) {
			Element el;
			for (int i = 0; i < nl.getLength(); i++) {
				// get the rule element
				el = null;
				el = (Element) nl.item(i);
				String expression;

				// for only elems where has property is not 0
				if (!el.getAttribute(HASHATTRIB).equals("0")) {
					expression = null;
					// get expression info
					NodeList exprList = el.getElementsByTagName(EXPRESSIONTAG);
					expression = getTextValue(el, EXPRESSIONTAG).trim();

					// get doc uris
					NodeList docList = el.getElementsByTagName(DOCUMENTTAG);

					if (docList != null && docList.getLength() > 0) {
						String docuri;
						Element doc;
						for (int k = 0; k < docList.getLength(); k++) {
							docuri = null;
							doc = null;
							// get document element
							doc = (Element) docList.item(k);
							docuri = doc.getAttribute(ORIGINALLOCATIONATTRIB)
									.replaceFirst(FILEPREFIX, "").trim()
									.toLowerCase();

							if (exprPath.containsKey(docuri)) {
								ArrayList tempExps = null;
								tempExps = (ArrayList) exprPath.get(docuri);
								tempExps.add(expression);
								exprPath.put(docuri, tempExps);
							} else {
								ArrayList exps = new ArrayList();
								exps.add(expression);
								exprPath.put(docuri, exps);
							}

						}
					}

				}

			}
		}
		System.out
				.println("Info from BooleanClassification.xml loaded into internal DS.");
	}

	// returns the expression node value
	private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	private static void generateReports() throws FileNotFoundException,
			IOException {
		System.out.println("Generating Reports.....");
		String outFile = "out_" + System.currentTimeMillis() + ".html";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile)));

		Iterator itrMLabel = mlabelPath.keySet().iterator();

		bw
				.write("<html><head></head><body><table border=\"1\" cellpadding=\"1\">");
		bw.newLine();
		bw
				.write("<tr><td width=\"60%\"><b>MediaLabel</b></td><td><b>Expression</b></td><td><b>Count(Hits)</b></td></tr>");
		bw.newLine();

		String medialabel;
		ArrayList paths;
		LinkedHashMap results;

		// iterate thru medialabels
		while (itrMLabel.hasNext()) {
			medialabel = null;
			paths = null;
			results = new LinkedHashMap();

			medialabel = (String) itrMLabel.next();
			paths = (ArrayList) mlabelPath.get(medialabel);

			ArrayList exprArr;
			String thisPath;
			// iterate thru dpaths
			for (int m = 0; m < paths.size(); m++) {
				exprArr = null;
				thisPath = null;

				thisPath = (String) paths.get(m);
				exprArr = (ArrayList) exprPath.get(thisPath);

				if (null == exprArr) {
					continue;
				}

				String tempExpr;
				for (int n = 0; n < exprArr.size(); n++) {
					tempExpr = null;
					tempExpr = (String) exprArr.get(n);

					int tempCnt;
					if (results.containsKey(tempExpr)) {
						tempCnt = 0;
						tempCnt = (Integer) results.get(tempExpr);
						tempCnt++;
						results.put(tempExpr, tempCnt);

					} else {
						results.put(tempExpr, 1);
					}
				}

			}

			Iterator resItr = results.keySet().iterator();

			String exp;
			String count;

			while (resItr.hasNext()) {
				exp = null;
				count = null;

				exp = (String) resItr.next();
				count = ((Integer) results.get(exp)).toString();

				System.out.println(medialabel + ": " + exp + ": " + count);
				bw.write("<tr><td>" + medialabel + "</td><td>" + exp
						+ "</td><td>" + count + "</td></tr>");
				bw.newLine();
			}

		}

		bw.close();
		bw = null;

		System.out.println("Report generated. Script execution completed.");
	}
}
