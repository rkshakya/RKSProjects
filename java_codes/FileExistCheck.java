/*******************************************************************************
 * FileExistCheck.java - <comment>
 *
 * Inputs: an input file contains the GID<tab delimited>URI path of the files to checked for existence
 *
 * Outputs: 1. FILE_EXISTING.txt - Will have UNC PATH of the files which exists 
 *	2. FILE_NOT_EXISTING.txt - Will have UNC PATH of the files which DO NOT exist 
 *
 * Usage: java FileExistCheck input.txt
 *
 * Version : 1.0 (Sep 22, 2008)
 *
 * Author : RavikishoreS@Custom Work Group(TS-India).
 *
 * Organization: Stratify, an Iron Mountain Company.
 * ******************************************************************************
 */

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class FileExistCheck {
	public static void main(String args[]) {
		int line_num = 0;
		int filesFound = 0;
		int filesNOTFound = 0;
		String line = null;
		String outFileExisting = "FILE_EXISTING.txt";
		String outFileNOTExisting = "FILE_NOT_EXISTING.txt";

		if (args.length != 1) {
			System.out
					.println("Usage: java FileExistCheck <input file with GID-URI tab delimited>");
			System.exit(0);
		}

		try {
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(
					new FileInputStream(args[0])));
			BufferedWriter bwExisting = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(outFileExisting)));
			BufferedWriter bwNOTExisting = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(
							outFileNOTExisting)));
			String gid, filepath_name;

			while ((line = reader1.readLine()) != null) {
				line_num++;
				StringTokenizer st = new StringTokenizer(line, "\t");
				gid = st.nextToken().trim();
				filepath_name = st.nextToken().trim();

				if (new File(filepath_name).exists()) {
					filesFound++;
					bwExisting.write(gid + "\t" + filepath_name);
					bwExisting.newLine();
				} else {
					filesNOTFound++;
					bwNOTExisting.write(gid + "\t" + filepath_name);
					bwNOTExisting.newLine();
				}

			}

			// WRITE COUNT STATUS TO ALL THE FILES
			bwExisting.write("Total Files Checked : " + line_num);
			bwExisting.newLine();
			bwExisting.write("Total Files FOUND : " + filesFound);

			bwNOTExisting.write("Total Files Checked : " + line_num);
			bwNOTExisting.newLine();
			bwNOTExisting.write("Total Files NOT FOUND : " + filesNOTFound);

			reader1.close();
			bwExisting.close();
			bwNOTExisting.close();
		} catch (Exception e) {
			System.out.println(e + "at line " + line_num);
		}
	}

}
