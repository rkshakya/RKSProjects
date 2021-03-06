import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/*******************************************************************************
 * EGLReport.java - <comment>
 *
 * Inputs:
 *
 * Outputs:
 *
 * Usage: java
 *
 * Version : 1.0 (July  13, 2009)
 *
 * Author : RavikishoreS@Custom Work Group(TS-India).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ******************************************************************************
 */

public class EGLReport_17488 {
	private static final String DELIMITER = "\t";
	private static final int DEFAULT_CAPACITY = 500000;
	private static final String OUTPUT_HEADER = "BATCH\tMEDIANAME\tPST";
	private static final String JOIN_DELIMITER = ";";
	private static final int DEFAULT_CAPACITY_CHILDS = 10000;


	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage java EGLReport <load file>");
			return;
		}

		String loadFile = args[0];

		LinkedHashMap mnamemap = new LinkedHashMap(DEFAULT_CAPACITY);
		LinkedHashMap pstmap = new LinkedHashMap(DEFAULT_CAPACITY);

		try {
			processLoadFiles(mnamemap, pstmap, loadFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			printOutput(mnamemap, pstmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	private static void printOutput(LinkedHashMap mnamemap, LinkedHashMap pstmap) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Printing results..");
		String outFile = "out_" + System.currentTimeMillis() + ".txt";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile)));
		Set keys = mnamemap.keySet();
		//Iterator itr = keys.iterator();
		String arrkeys[] = (String[])keys.toArray(new String[0]);

		String key = null;
		TreeSet value = null;
		TreeSet value1 = null;

		bw.write(OUTPUT_HEADER);
		bw.newLine();

		for(int count = 0; count < arrkeys.length; count++) {
			value = null; value1 = null;

			//Map.Entry me = (Map.Entry) itr.next();
			key = arrkeys[count];
			value = (TreeSet) mnamemap.get(key);

			if(pstmap.containsKey(key)){
				value1 = (TreeSet) pstmap.get(key);
			}else{
				value1 = new TreeSet();
				value1.add("NA");
			}

			bw.write(key + DELIMITER + join(value, JOIN_DELIMITER) + DELIMITER + join(value1, JOIN_DELIMITER));
			bw.newLine();
		}

		System.out.println("Closing writer...");
		bw.close();
		bw = null;

	}


	private static void processLoadFiles(LinkedHashMap mnamemap,
			LinkedHashMap pstmap, String loadFile) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Processing load file..");

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(loadFile))));

		String batch = null;
		String medianame = null;
		String pst = null;
		StringTokenizer tokenizer = null;
		String line;
		TreeSet media = null;
		TreeSet psts = null;

		while ((line = br.readLine()) != null) {
			media = null; psts = null;

			//System.out.println(line);

			tokenizer = new StringTokenizer(line, DELIMITER);
			batch = tokenizer.nextToken().trim();
			medianame = tokenizer.nextToken().trim();
			pst = tokenizer.nextToken().trim();

			if(mnamemap.containsKey(batch)){
				//fetch treeset and add this medianame
				media = (TreeSet)mnamemap.get(batch);
				media.add(medianame);
				mnamemap.put(batch, media);

			}else{
				//create new treeset, add this medianame and add to map
				TreeSet tsmedia = new TreeSet();
				tsmedia.add(medianame);
				mnamemap.put(batch, tsmedia);
			}

			if(pstmap.containsKey(batch)){
				//fetch treeset and add this pst(if this is pst)
				if(isPST(pst)){
					//fetch treeset and add this pst
					psts = (TreeSet)pstmap.get(batch);
					psts.add(pst);
					pstmap.put(batch, psts);

				}

			}else{
				//create new treeset, add this pst(if it is pst) and add to map
				if(isPST(pst)){
					TreeSet setpst = new TreeSet();
					setpst.add(pst);
					pstmap.put(batch, setpst);

				}
			}
		}

		br.close();
		br = null;

		System.out.println("Processed Load file...");

	}


	private static boolean isPST(String pst) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(pst.toLowerCase().endsWith(".pst")){
			flag = true;
		}
		return flag;
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




















































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































