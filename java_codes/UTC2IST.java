import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class UTC2IST {
	/*
	 * This script takes date in UTC(GMT) and converts it to date string in IST
	 * Usage: java UTC2IST input.txt Inputs:input.txt -- tab limited file of GID
	 * and UTC(GMT)
	 */
	private final static String DELIMITER = "\t";
	private final static String FORMATTER = "yyyy-MM-dd HH:mm:ss.SSS";
	private final static String TIMEZONE_INDIA = "IST";
	private final static String TIMEZONE_PST = "PST";
	private final static String TIMEZONE_GMT = "GMT";

	public static void main(String[] args) throws IOException, ParseException {

		String loadFile = args[0];
		String gid;
		String strDate;
		Date dt;
		TimeZone tzIndia = TimeZone.getTimeZone(TIMEZONE_INDIA);
		TimeZone tzPST = TimeZone.getTimeZone(TIMEZONE_PST);
		TimeZone tzGMT = TimeZone.getTimeZone(TIMEZONE_GMT);
		SimpleDateFormat sdfGMT = new SimpleDateFormat(FORMATTER);
		sdfGMT.setTimeZone(tzGMT);
		SimpleDateFormat sdfIndia = new SimpleDateFormat(FORMATTER);
		sdfIndia.setTimeZone(tzIndia);
		SimpleDateFormat sdfPST = new SimpleDateFormat(FORMATTER);
		sdfPST.setTimeZone(tzPST);

		String convDateIndia;
		String convDatePST;
		String convDateGMT;

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(loadFile)));
		String line = null;

		System.out.println( "GID\tDATEIST\tDATEPST\tDATEGMT");

		while ((line = br.readLine()) != null) {

			gid = null;
			strDate = null;
			dt = null;
			convDateIndia = null;
			convDatePST = null;
			convDateGMT = null;

			StringTokenizer st = new StringTokenizer(line, DELIMITER);
			gid = st.nextToken().trim();
			strDate = st.nextToken().trim();

			dt = new Date(new Long(strDate));
			convDateIndia = sdfIndia.format(dt);
			convDatePST = sdfPST.format(dt);
			convDateGMT = sdfGMT.format(dt);
			System.out.println(gid + "\t" + convDateIndia + "\t" + convDatePST + "\t" + convDateGMT);
		}

		br.close();
		br = null;

	}

}