import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

/*******************************************************************************
 * EXMMesgUnits.java - <comment>
 *
 * Inputs: <load file> <CSDocid-uri map file>
 *
 * Outputs: Output file with PARENT CHILDREN URIs
 *
 * Usage: java EXMMesgUnits <load file> <CSDocid-uri map file>
 *
 * Version : 1.0 (Apr 7, 2009)
 *
 * Author : RavikishoreS@Custom Work Group(TS-India).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ******************************************************************************
 */

public class EXMMesgUnits {
	private static final String DELIMITER = "\t";
	private static final int DEFAULT_CAPACITY = 100000;
	private static final String DOT = ".";
	private static final String EXTENSION = "mht";
	private static final int DEFAULT_CAPACITY_CHILDS = 10;
	private static final String OUTPUT_HEADER = "PARENT\tCHILDREN";
	private static final String JOIN_DELIMITER = ",";
	private static final String NOT_FOUND = "CSDOCID_NOT_FOUND";

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out
					.println("Usage java EXMMesgUnits <load file> <CSDocid-uri map file>");
			return;
		}

		String mapFile = args[1];
		String loadFile = args[0];

		LinkedHashMap pathMap = null;
		LinkedHashMap loadEntries = new LinkedHashMap(DEFAULT_CAPACITY);
		// map to hold indID - cdicid map for parent entries
		LinkedHashMap idcidMap = new LinkedHashMap(DEFAULT_CAPACITY);

		String errFile = "exceptions_" + System.currentTimeMillis() + ".txt";
		BufferedWriter bwErr = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(errFile)));

		// load mapFile entries
		try {
			pathMap = loadMapFile(mapFile, bwErr);
		} catch (IOException e) {
			System.err
					.println("Error loading mapping file. Terminating prog execution..");
			e.printStackTrace();
		}

		// now process the load file - meat of the program
		try {
			processLoadFile(pathMap, loadEntries, loadFile, idcidMap, bwErr);
		} catch (IOException e) {
			System.err
					.println("Error processing load file. Terminating prog execution..");
			e.printStackTrace();
		}

		if (pathMap == null) System.out.println("Could not generate path Map");
		if (idcidMap == null) System.out.println("Could not generate idcidMap");

		// spit out the result entries
		try {
			printResults(loadEntries, pathMap, idcidMap, bwErr);
		} catch (IOException e) {
			System.err.println("Error generating results..");
			e.printStackTrace();
		}

		bwErr.close();
		bwErr = null;

		System.out.println("Prog execution completed.");

	}

	private static void printResults(LinkedHashMap loadEntries,
			LinkedHashMap pathMap, LinkedHashMap idcidMap, BufferedWriter bwErr) throws IOException {
		System.out.println("Printing results..");
		String outFile = "out_" + System.currentTimeMillis() + ".txt";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile)));
		Iterator itr = loadEntries.entrySet().iterator();

		String key = null;
		Object value = null;
		long value1 = 0L;
		String value2 = null;
		String valChild = null;

		bw.write(OUTPUT_HEADER);
		bw.newLine();

		while (itr.hasNext()) {
			value1 = 0L; value2 = null;
			valChild = null;

			Map.Entry me = (Map.Entry) itr.next();
			key = (String) me.getKey();
			value = me.getValue();

			//if these do not exist, log it in exception list and skip whole unit
			try{
				value1 = ((Long)idcidMap.get(key)).longValue();
			}catch(Exception ex){
				bwErr.write("cdocid mapping unresolved for parent, skipping: " + key);
				bwErr.newLine();
				continue;
			}

			try{
				value2 = (String) pathMap.get(value1);
			}catch(Exception ex1){
				bwErr.write("uri mapping unresolved for parent, skipping: " + key + " cdocid: " + value1);
				bwErr.newLine();
				continue;
			}

			if(value1 == 0 || value2 == null){
				bwErr.write("cdocid mapping unresolved for parent, skipping: " + key);
				bwErr.newLine();
				continue;
			}


			if(null != value){
				//if mail with attachs
				//get the concatenated child string
				valChild = join((ArrayList)value, JOIN_DELIMITER);

				//if any of the child string contains NOT_FOUND string, log that and skip whole unit
				if(valChild.indexOf(NOT_FOUND) >= 0 || valChild.indexOf("null") >= 0){
					bwErr.write("Uri mapping unresolved for child. Skipping unit with parent id.." + key);
					bwErr.newLine();
					continue;
				}else if(valChild.indexOf(value2) >= 0){
					bwErr.write("Parent - Child Cyclical relationship detected for " + key);
					bwErr.newLine();
					continue;
				}else{
					//these are valid ones, spit out in o/p files
					bw.write(value2 + DELIMITER + valChild);
					bw.newLine();
				}

			}else{
				//if standalone mail
				bw.write(value2);
				bw.newLine();
			}



			/*if (null != value2) {
				// 2 step resolution here to get parent uri
				bw.write(value2);
			} else {
				System.err.println("Key:" + key + ";value:" + value);
			}
			if (null != value) {
				bw.write(DELIMITER + join((ArrayList)value, JOIN_DELIMITER));
				bw.newLine();
			}*/


		}
		System.out.println("Closing writer...");
		bw.close();
		bw = null;
	}

	private static void processLoadFile(LinkedHashMap pathMap,
			LinkedHashMap loadEntries, String loadFile, LinkedHashMap idcidmap, BufferedWriter bwErr)
			throws IOException {
		System.out.println("Processing load file..");

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(loadFile))));


		String line = null;
		StringTokenizer tokenizer = null;
		String indID = null;
		String scdocID = null;
		String attachID = null;
		ArrayList childs = null;
		long cdocID = 0L;
		String childURI = null;


		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line, DELIMITER);
			indID = tokenizer.nextToken().trim();
			scdocID = tokenizer.nextToken().trim();
			attachID = tokenizer.nextToken().trim();
			childURI = null;

			//doing it to accomodate ununiformity in load files in CS_DocID values
			try{
			cdocID = Long.parseLong(scdocID);
			}catch(Exception ex){
				bwErr.write("Problematic csdocid value " + scdocID);
				bwErr.newLine();
			}

			// put all entries into parcidmap
			idcidmap.put(indID, cdocID);

			if (attachID.equals("0")) {
				// check to see if it represents .mht files
				// if yes put it as standalone mails
				// else standalone loose files
				if (checkMHT((String) pathMap.get(cdocID))) {
					if (!loadEntries.containsKey(indID)) {
						loadEntries.put(indID, null);
					} else {
						bwErr.write("LoadEntries map already contains " + indID);
						bwErr.newLine();
					}

				} else {
					//log the identified loose files
					bwErr.write("LOOSE FILE INDENTIFIED\t" + indID
							+ "\t" + cdocID + "\t" + pathMap.get(cdocID));
					bwErr.newLine();
				}
			} else if (indID.equals(attachID)) {
				// this is parent mail
				if (!loadEntries.containsKey(attachID)) {
					childs = new ArrayList(DEFAULT_CAPACITY_CHILDS);
					loadEntries.put(attachID, childs);
				}

			} else {
				// this is attach entries
				if (loadEntries.containsKey(attachID)) {
					// get the child list and put this one into it
					childs = (ArrayList) loadEntries.get(attachID);
					try {
						childURI = (String) pathMap.get(cdocID);
					} catch (Exception ex) {
						childURI = NOT_FOUND;
						bwErr.write("Mapping resolution not found for CS_docID: " + cdocID);
						bwErr.newLine();
					}

					if(childURI == null){
						bwErr.write("Mapping resolution not found for CS_docID: " + cdocID);
						bwErr.newLine();
					}

					childs.add(childURI);
					loadEntries.put(attachID, childs);

				} else {
					// this is new one so put this as new entry
					childs = new ArrayList(DEFAULT_CAPACITY_CHILDS);
					try {
						childURI = (String) pathMap.get(cdocID);
					} catch (Exception ex) {
						childURI = NOT_FOUND;
						bwErr.write("Mapping resolution not found for CS_docID: " + cdocID);
						bwErr.newLine();
					}

					if(childURI == null){
						bwErr.write("Mapping resolution not found for CS_docID: " + cdocID);
						bwErr.newLine();
					}

					childs.add(childURI);
					loadEntries.put(attachID, childs);
				}

			}

		}

		br.close();
		br = null;

		System.out.println("Processed Load file...");
	}

	private static boolean checkMHT(String uri) {
		// TODO Auto-generated method stub
		boolean flag = false;
		String extension = null;
		if (uri != null) {
			// get file extension from the URI
			extension = uri.substring(uri.lastIndexOf(DOT) + 1);
			flag = (extension.equalsIgnoreCase(EXTENSION)) ? true : false;
		}
		return flag;
	}

	private static LinkedHashMap loadMapFile(String mapFile, BufferedWriter bwErr) throws IOException {
		System.out.println("Loading path map file..");
		LinkedHashMap tempMap = new LinkedHashMap(DEFAULT_CAPACITY);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(mapFile))));
		String line = null;
		long lcdocid = 0L;
		String dpath = null;
		String cdocid = null;

		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, DELIMITER);
			while (st.hasMoreTokens()) {
				try{
				cdocid = st.nextToken().trim();
				dpath = st.nextToken().trim();
				lcdocid = Long.parseLong(cdocid);
				tempMap.put(lcdocid, dpath);

				}catch(Exception ex){
					bwErr.write("loadMapFile : cdocid not in proper format, Skipping cdocid " +  cdocid);
					bwErr.newLine();
					continue;
				}
			}
		}

		br.close();
		br = null;

		System.out.println(tempMap.size()
				+ " entries loaded into pathMap hash.");

		return tempMap;
	}

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


