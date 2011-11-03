import java.io.*;
import java.util.*;

class Element{
	int docCount = 0;
	int dupCount = 0;

    Element(){
    }

    Element(int i, int j){
		docCount = i;
		dupCount = j;

	}

	 void setDocCount(int m){
		docCount = m;

	}

	 void setDupCount(int n){
		dupCount = n;
	}

	 int getDocCount(){
		return docCount;
	}

	 int getDupCount(){
		return dupCount;
	}
}

public class FormatReportV7 {

	private static ArrayList list1 = new ArrayList();
	private static HashMap hm = new HashMap();

	 //function to find a pattern string and replace with replacement
	 private static String myreplace(String str, String pattern, String replace) {
	        int s = 0;
	        int e = 0;
	        StringBuffer result = new StringBuffer();

	        while ((e = str.indexOf(pattern, s)) >= 0) {
	            result.append(str.substring(s, e));
	            result.append(replace);
	            s = e+pattern.length();
	        }
	        result.append(str.substring(s));
	        return result.toString();
    }

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println("Usage: java FormatReportV7 modelFile ClusteringLogFile modelfileSummary"); //modelfile is Boolean model file
			return;
		}

		//populate all the search terms
		populateList(args[0]);

		//populate all the terms that got hits
		populateValues(args[2]);

		writeReport(args[1]);
	}

	//function to populate hit entries into hashmap
	private static void populateValues(String name) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(name))));
		String line = null;


		String exp = "";
		int doCount = 0;
		int duCount = 0;

		while ((line = br.readLine()) != null) {

			int start = 0;
			int end = 0;

			if(line.indexOf("<rule hash=\"0\">") > -1){
				break;
			}

			if(line.indexOf("<expression>") > -1){
				start = line.indexOf("<expression>") + "<expression>".length();
				end = line.indexOf("</expression>");
				exp = line.substring(start, end);

				System.out.println("EXP: " + exp);
			}

			if(line.indexOf("<docCount>") > -1){
				start = line.indexOf("<docCount>") + "<docCount>".length();
				end = line.indexOf("</docCount");
				doCount = Integer.parseInt(line.substring(start, end));

				System.out.println("DOCOUNT: " + doCount);
			}

			if(line.indexOf("<duplicateDocCount>") > -1){
				start = line.indexOf("<duplicateDocCount>") + "<duplicateDocCount>".length();
				end = line.indexOf("</duplicateDocCount>");
				duCount = Integer.parseInt(line.substring(start, end));

				System.out.println("DUCOUNT: " + duCount);
			}

			if(line.indexOf("</rule>") > -1){
				Element el = new Element(doCount, duCount);
				hm.put(exp, el);
			}

		}

		br.close();
		br = null;
	}


	//gather all the search terms(hits + non hits) into ArrayList
	private static void populateList(String name) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(name))));
		String line = null;

		while ((line = br.readLine()) != null) {

			if (line.indexOf("<rule ") > -1) {

				if (line.indexOf("<expression>") < 0) {
					line = br.readLine();
				}
				int start = line.indexOf("<expression>") + "<expression>".length();

				if (start - "<expression>".length() < 0) {
					continue;
				}

				int end = line.indexOf("</expression>");

				//System.out.println("START: " + start + " END : " + end + " LINE: " + line);

				//replace &quot; with "
				String exp = line.substring(start, end).replaceAll("&quot;", "\"");

				//replace &apos; with '
				exp = exp.replaceAll("&apos;", "\'");

				System.out.println("\nEXPRESSION: " + exp);

				list1.add(exp);
			}
		}
		br.close();
		br = null;

		System.out.println("Added " + list1.size() + " entries to expressions list");

	}

	private static void writeReport(String name) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(name))));

		String line = null;

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("report_"+System.currentTimeMillis()+".html"))));

		boolean start = false;
		float totaldocs = 0;
		float remove = 0;
		float remain = 0;
		int mylen = 0;
		String retVal = "";

		while ((line = br.readLine()) != null) {
			line = line.trim();

			if (line.length() < 2) {
				continue;
			}
			if (line.toLowerCase().indexOf("total documents in input corpus") > -1) {
				String number = line.substring(line.indexOf(":")+1).trim();
				totaldocs = Float.parseFloat(number);
				System.out.println("Found total: " + totaldocs);
				continue;
			}
			if (line.toLowerCase().indexOf("documents with unsupported extensions") > -1) {
				String number = line.substring(line.indexOf(":")+1).trim();
				remove = Float.parseFloat(number);
				System.out.println("Found filtered: " + remove);
				remain = totaldocs-remove;
				System.out.println("REMAIN: " + remain);
				continue;
			}
		}

		br.close();
		br = null;

		bw.write("Total docs in input corpus: " + (int)totaldocs + "<br>");
		bw.newLine();
		bw.write("No of docs with unsupported extension: " + (int)remove);
		bw.newLine();
		bw.write("<html><head></head><body><table border=\"1\" cellpadding=\"1\">");
		bw.newLine();
		bw.write("<tr><td width=\"60%\"><b>Expression</b></td><td><b>Total Hits</b></td><td><b>%age of Input Corpus</b></td><td><b>Matches</b></td><td><b>Content Duplicates</b></td></tr>");
		bw.newLine();

		Set keys = hm.keySet();
		Iterator iter = keys.iterator();

		while (iter.hasNext()) {
			String key   = (String) iter.next();

			System.out.println("\nKEY: " + key);
			Element values = (Element) hm.get(key);

			//remove the entries that got hits from the Arraylist
			if(list1.contains(key)){
				list1.remove(key);
				System.out.println("\nNON-ZERO HITS: " + key);
			}

			int total = values.getDocCount() + values.getDupCount();
			bw.write("<tr><td>");
			bw.write(key);
			bw.write("</td><td>");

			bw.write(total + "</td><td>" + ((total/remain)*100)+"%</td><td>"+ values.getDocCount() + "</td><td>" + values.getDupCount() + "</td></tr>");
			bw.newLine();
		}

		//print out the 0 hits
		for (int m = 0; m < list1.size(); m++){
			System.out.println("\nNO HITS: " + list1.get(m));
			bw.write("<tr><td>" + list1.get(m) + "</td><td>0</td></tr>");
			bw.newLine();
		}

		bw.close();
		bw = null;

	}
}
