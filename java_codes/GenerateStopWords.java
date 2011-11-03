import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************************************************
 * GenerateStopWords.java - <comment>
 *
 * Inputs: input file with Sender/Receiver entries (one per line)
 *
 * Outputs: an output file with Stopwords and source input line tab delimited.
 *
 * Usage: java GenerateStopWords <InputFileWithSendersReceivers(one in each line)>
 *
 * Version : 1.0 (Mar 2, 2009)
 *
 * Author : RavikishoreS@Custom Work Group(TS-India).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ******************************************************************************
 */

public class GenerateStopWords {
	private final static String DEFAULT_ENCODING = "UTF-8"; //default encoding of I/O files
	//! DONT MEDDLE WITH THESE UNLESS U KNOW WHAT U R DNG
	private final static String EMAIL_REGEX = "[<|(][\\w\\.-]+@([\\w\\-]+\\.)(\\w+)[\\.\\w]+[>|)]"; //regex to detect mail addresses
	private final static String WORD_SEPARATOR_REGEX = "[,\\s]+";
	private final static String DELIMITER = "\t";
	private final static String SINGLE_QUOTES = "\'";
	private final static String DOUBLE_QUOTES = "\"";
	//output file specific
	private static final String OUTPUTFILENAME = "out_";
	private static final String OUTPUTFILEEXTENSION = ".txt";

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out
					.println("Usage : java GenerateStopWords <InputFileWithSendersReceivers(one in each line)>");
			System.exit(0);
		}

		String inFile = args[0];
		try {
			generateList(inFile);
		} catch (Exception ex) {
			System.out.println("generateList: Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private static void generateList(String inFile) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFile), DEFAULT_ENCODING));

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(OUTPUTFILENAME
						+ System.currentTimeMillis() + OUTPUTFILEEXTENSION)), DEFAULT_ENCODING));

		String line, prefix;
		Pattern patemail = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Pattern patsep = Pattern.compile(WORD_SEPARATOR_REGEX, Pattern.UNICODE_CASE);
		Matcher matcheremail;
		String[] results;

		while((line = br.readLine()) != null){
			matcheremail = patemail.matcher(line);

			if( matcheremail.find() && (matcheremail.start() != 0)){
				System.out.println("generateList: Email chars found in : " + line);
				//break, clean of quotes and then spit out the words found preceding email addresses
				prefix = line.substring(0, matcheremail.start() - 1).trim().replaceAll(SINGLE_QUOTES, "");
				prefix = prefix.replaceAll(DOUBLE_QUOTES, "");
				results = patsep.split(prefix);

				for(int count = 0 ; count < results.length; count++){
					bw.write(results[count] + DELIMITER + line);
					bw.newLine();
				}


			}else{
				System.out.println("generateList: No email chars found or there is no string preceding email address: " + line  );
				continue;
			}
		}

		bw.close();
		bw = null;

		br.close();
		br = null;
	}

}
