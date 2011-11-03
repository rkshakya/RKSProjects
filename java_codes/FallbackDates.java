import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

/*******************************************************************************
 * FallbackDates.java - a program to process input file with GID, SENTTIME,
 * RECEIVEDTIME, CREATEDTIME (in UTC format) and then generate the output file
 * with GID and VALID DATE in IST Date format. The fallback order for
 * determining valid date SENTTIME, RECEIVEDTIME, CREATEDTIME.
 *
 * Inputs: 1) Input File with GID, SENTTIME, RECEIVEDTIME, CREATEDTIME tab
 * delimited in UTC format
 *
 * Outputs: 1) A file with GID, SENTTIMEIST, RECEIVEDTIMEIST, CREATEDTIMEIST,
 * VALIDDATEIST. VALIDDATEIST would be the final valid IST date.
 *
 * Usage: java FallbackDates <input file>
 *
 * Version : 1.0 (Mar 19, 2009)
 *
 * Author : RavikishoreS@Custom Work Group(TS-India).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ******************************************************************************
 */

public class FallbackDates {
	private final static String DELIMITER = "\t"; // used in i/o files
	private final static String FORMATTER = "yyyy-MM-dd HH:mm:ss.SSS"; // final
	// date
	// format
	// expected
	private final static String FORMATTER_THRESHOLD = "yyyy-MM-dd";
	private final static String TIMEZONE_INDIA = "IST";
	// to check if date is valid
	private final static String THRESHOLD_DATE = "1970-01-02";

	public static void main(String[] args) throws IOException, ParseException {
		if (args.length != 1) {
			System.out.println("Usage: java FallbackDates <input file>");
			return;
		}

		String loadFile = args[0];
		String outFile = "out_" + System.currentTimeMillis() + ".txt";
		String gid;
		Date dtSent;
		Date dtRevd;
		Date dtCreated;
		Date dtThreshold;
		String stime;
		String rtime;
		String ctime;
		String vtime;
		String line = null;

		// initialize timezone and date formatter
		TimeZone tzIndia = TimeZone.getTimeZone(TIMEZONE_INDIA);
		SimpleDateFormat sdfIndia = new SimpleDateFormat(FORMATTER);
		sdfIndia.setTimeZone(tzIndia);

		// form the threshold date to validate against
		SimpleDateFormat sdfThres = new SimpleDateFormat(FORMATTER_THRESHOLD);
		sdfThres.setTimeZone(tzIndia);
		dtThreshold = sdfThres.parse(THRESHOLD_DATE);

		// I/O streams
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(loadFile)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

		// output headers
		bw.write("GID" + DELIMITER + "STIMEIST" + DELIMITER + "RTIMEIST"
				+ DELIMITER + "CTIMEIST" + DELIMITER + "VALIDDATEIST");
		bw.newLine();

		while ((line = br.readLine()) != null) {
			gid = null;
			stime = null;
			rtime = null;
			ctime = null;
			vtime = null;
			dtSent = null;
			dtRevd = null;
			dtCreated = null;

			StringTokenizer st = new StringTokenizer(line, DELIMITER);
			gid = st.nextToken().trim();
			stime = st.nextToken().trim();
			rtime = st.nextToken().trim();
			ctime = st.nextToken().trim();

			if (!stime.equalsIgnoreCase("NA")) {
				dtSent = new Date(new Long(stime));
				stime = sdfIndia.format(dtSent);
			}

			if (!rtime.equalsIgnoreCase("NA")) {
				dtRevd = new Date(new Long(rtime));
				rtime = sdfIndia.format(dtRevd);
			}

			if (!ctime.equalsIgnoreCase("NA")) {
				dtCreated = new Date(new Long(ctime));
				ctime = sdfIndia.format(dtCreated);
			}

			// determine valid date
			if((!stime.equalsIgnoreCase("NA"))&&(!sdfIndia.parse(stime).before(dtThreshold))){
				vtime = stime;
			}else if((!rtime.equalsIgnoreCase("NA"))&&(!sdfIndia.parse(rtime).before(dtThreshold))){
				vtime = rtime;
			}else if ((!ctime.equalsIgnoreCase("NA"))&&(!sdfIndia.parse(ctime).before(dtThreshold))){
				vtime = ctime;
			}else{
				System.out.println("No valid dates available for gid: " + gid);
				vtime = "INVALID DATE";
			}

			bw.write(gid + DELIMITER + stime + DELIMITER + rtime + DELIMITER
					+ ctime + DELIMITER + vtime);
			bw.newLine();
		}

		// clean up the mess

		bw.close();
		bw = null;

		br.close();
		br = null;

	}
}
