import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/*******************************************************************************
 * ResultScan.java - a script to generate output for post processing results of image scan
 * 
 * Inputs: a) input load file (sample given in project description)
 * b) debug flag (= true for debug stmts (default), = false otherwise)
 *
 * Output: displays on the console total counts and errorwise count breakdown for each application
 *
 * Usage: java ResultScan <input load file> <flag>
 *
 * Version : 1.0
 *
 * Author : Ravi
 *
 * Organization: 
 * ****************************************************************************************************
 */
class CustomDS {
	//custom DS to represent errorwise description for each application
	HashMap<Integer, Integer> hmErrorCount; //key - errortype, value - counts
	Set<Integer> sErrorPresent; //holds distinct errorTypes for each application	
	
	CustomDS(){
		this.hmErrorCount = new HashMap<Integer, Integer>();  		
	}
}

class Image{	
	// a custom class to represent each image entry
	int iImageId;	
	CustomDS ds1;   //for results of app1 - these can be dynamically varied based on no of applications as input 	
	CustomDS ds2;  	//for results of app2
	int iInputResult;  // result flag read from input file
	boolean validImageEntry;  //indicate if image entry is valid
	
	Image(){
		this.iImageId = 0;
		this.ds1 = new CustomDS();
		this.ds2 = new CustomDS();
		this.iInputResult = 9999;
		this.validImageEntry = true;
	}

	public int getIInputResult() {
		return iInputResult;
	}

	public void setIInputResult(int inputResult) {
		iInputResult = inputResult;
	}

	public boolean isValidImageEntry() {
		return validImageEntry;
	}

	public void setValidImageEntry(boolean validImageEntry) {
		this.validImageEntry = validImageEntry;
	}

	public int getIImageId() {
		return iImageId;
	}

	public void setIImageId(int imageId) {
		iImageId = imageId;
	}
}

public class ResultScan {
	//config parameters - can ask from user interactively
	private static final int NO_OF_APPLICATIONS = 2;
	private static final int COMPONENTS_IN_APP1 = 5;
	private static final int COMPONENTS_IN_APP2 = 3;
	private static boolean DEBUG = true; //controls display of debug stmts
	
	private static final int ARGS_COUNT = 2;
	private static final String EMPTY_STRING = "";
	private static final String DELIMITER_EQUALS = "=";
	private static final String DELIMITER_COMMA = ",";
	private static final String TAB = "\t";
	
	//to distinguish calls for various apps
	private static final int INDICATOR1 = 1;
	private static final int INDICATOR2 = 2;
	private static String[] errorTypes = {"0", "1", "2"};
	
	
	//identifiers
	private static final String IMAGE = "image_id";
	private static final String APP_1 = "app_1";
	private static final String APP_2 = "app_2";
	private static final String RESULT = "result";
	
	//counters  - first element - total count, second element - app1 count, third element - app2 count
	static ArrayList<Integer> pass = new ArrayList<Integer> (Arrays.asList(0,0,0));
	static ArrayList<Integer> FailType1 = new ArrayList<Integer> (Arrays.asList(0,0,0));
	static ArrayList<Integer> FailType2 = new ArrayList<Integer> (Arrays.asList(0,0,0));
	static ArrayList<Integer> FailType3 = new ArrayList<Integer> (Arrays.asList(0,0,0));
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		if(args.length != ARGS_COUNT){
			dispMesg("Wrong number of parameters supplied for program execution!");
			dispMesg("Usage: java ResultScan <input file name> <debug flag>");
			return;
		}
		
		String inFile = args[0];
		String debugFlag = args[1];
		
		if(debugFlag.equalsIgnoreCase("FALSE")){
			DEBUG = false;
		}
		
		if(DEBUG){ 
			dispMesg("Input file: " + inFile);		
		}
		
		//read/process input file
		try {
			readInputFile(inFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//output results
		//general o/p
		dispMesg("INFERENCE" + TAB + "TOTAL" + TAB + "APP 1" + TAB + "APP 2");
		dispMesg("PASS" + TAB + join(pass, TAB));
		dispMesg("FAIL" + TAB + (FailType1.get(0) + FailType2.get(0) + FailType3.get(0)) + TAB + (FailType1.get(1) + FailType2.get(1) + FailType3.get(1)) + TAB + (FailType1.get(2) + FailType2.get(2) + FailType3.get(2)));
		dispMesg("-----------------------------------------");
		
		
		//detailed o/p
		dispMesg("INFERENCE" + TAB + "TOTAL" + TAB + "APP 1" + TAB + "APP 2");
		dispMesg("PASS" + TAB + join(pass, TAB));
		dispMesg("FAIL1" + TAB + join(FailType1, TAB));
		dispMesg("FAIL2" + TAB + join(FailType2, TAB ));
		dispMesg("FAIL3" + TAB + join(FailType3, TAB));
	}


	private static String join(ArrayList<Integer> s, String tab2) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(tab2);
			}
		}
		return buffer.toString();
	}


	private static void readInputFile(String inFile) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFile)));
		String line = null;		
		Image refImage = null; //image obj pointer		
		
		while ((line = br.readLine()) != null) {
			line = line.trim();
			
			//skip empty lines
			if(line.length() == 0 || line.equals(EMPTY_STRING)){
				refImage = null; //reset the prev image obj pointer
				continue;
			}
			
			if(DEBUG){
				dispMesg("------------------------------");
				dispMesg("Processing line: " + line);
			}
			
			if(line.startsWith(IMAGE)){
				refImage = processImageLine(line);				
			}else if(line.startsWith(APP_1)){
				processAppData(line, INDICATOR1, refImage);
			}else if(line.startsWith(APP_2)){
				processAppData(line, INDICATOR2, refImage);
			}else if(line.startsWith(RESULT)){
				processResult(line, refImage);
				//update total counters
				if(refImage.iInputResult == 3){
					//update the count only
					int count3 = FailType3.get(0);
					if(DEBUG){ dispMesg("Count3 " + count3); }
					FailType3.set(0, ++count3);										
				}else if(refImage.iInputResult == 2){
					//update the count only
					int count2 = FailType2.get(0);
					FailType2.set(0, ++count2);										
				}else if(refImage.iInputResult == 1){
					//update the count only
					int count1 = FailType1.get(0);
					FailType1.set(0, ++count1);										
				}else if(refImage.iInputResult == 0){
					int count0 = pass.get(0);
					pass.set(0, ++count0);
				}
				
				if(DEBUG){
				dispMesg("sErrorPresent1 " + refImage.ds1.sErrorPresent);
				dispMesg("sErrorPresent2 " + refImage.ds2.sErrorPresent);
				}
				//update app1 counters
				if(refImage.ds1.sErrorPresent.contains("1") && refImage.ds1.sErrorPresent.contains("2")){
					//update app1 ctr for err3
					int count31 = FailType3.get(1);
					if(DEBUG) {dispMesg("Count31 " + count31);}
					FailType3.set(1, ++count31);
					if(DEBUG) {dispMesg("FT3 " + FailType3);}
				}else if(refImage.ds1.sErrorPresent.contains("2") && !refImage.ds1.sErrorPresent.contains("1")){
					//update app1 err2
					int count21 = FailType2.get(1);
					FailType2.set(1, ++count21);
				}else if(refImage.ds1.sErrorPresent.contains("1") && !refImage.ds1.sErrorPresent.contains("2")){
					//update app1 err1
					int count11 = FailType1.get(1);
					FailType1.set(1, ++count11);
				}else{
					//update app1 pass
					int count01 = pass.get(1);
					pass.set(1, ++count01);
				}
				
				//update app2 counters
				if(refImage.ds2.sErrorPresent.contains("1") && refImage.ds2.sErrorPresent.contains("2")){
					//update app2 ctr for err3
					int count32 = FailType3.get(2);
					FailType3.set(2, ++count32);
				}else if(refImage.ds2.sErrorPresent.contains("2") && !refImage.ds2.sErrorPresent.contains("1")){
					//update app2 err2
					int count22 = FailType2.get(2);
					FailType2.set(2, ++count22);
				}else if(refImage.ds2.sErrorPresent.contains("1") && !refImage.ds2.sErrorPresent.contains("2")){
					//update app2 err1
					int count12 = FailType1.get(2);
					FailType1.set(2, ++count12);
				}else{
					//update app2 pass
					int count02 = pass.get(2);
					pass.set(2, ++count02);
				}
				
				
			}
			
			
			
		}
		
		br.close();
		br = null;
	}


	private static void processResult(String line, Image refImage) {
		// TODO Auto-generated method stub
		String result = null;	
		result = tokenizeElements(line);				
		//populate into obj
		refImage.setIInputResult(Integer.parseInt(result));
	}


	private static void processAppData(String line, int i, Image refImage) {
		// TODO Auto-generated method stub
		String tokens = tokenizeElements(line); //got tokenized by =
		tokens = tokens.trim();
		HashMap tempMap = null;
		Set tempSet = null;
		
		//tokenize by commas
		String[] parts = tokens.split(DELIMITER_COMMA);
		
		//some validations
		if(i == INDICATOR1){
			if(parts.length != COMPONENTS_IN_APP1){							
				refImage.setValidImageEntry(false);
				dispMesg("Token counts mismatch : " + line);
			} else {
				tempMap = refImage.ds1.hmErrorCount;
				tempSet = refImage.ds1.sErrorPresent;
			}
		} else if(i == INDICATOR2){
			if(parts.length != COMPONENTS_IN_APP2){
				refImage.setValidImageEntry(false);
				dispMesg("Token counts mismatch : " + line);
			} else {
				tempMap = refImage.ds2.hmErrorCount;
				tempSet = refImage.ds2.sErrorPresent;
			}
		}
		
		int currentValue = 0;
		
		for(int count = 0; count < parts.length; count++){
			if(!tempMap.containsKey(parts[count])){
				//initially set the count to 1
				tempMap.put(parts[count], 1);
			}else{
				//increment counter if already existing
				currentValue = (Integer)tempMap.get(parts[count]);
				tempMap.put(parts[count], ++currentValue);
			}
		}
		
		//get all unique error flags
		tempSet = tempMap.keySet();
		
		//set temp data structs to the main ones
		if(i == INDICATOR1){
			refImage.ds1.hmErrorCount = tempMap;
			refImage.ds1.sErrorPresent = tempSet;
		}else if(i == INDICATOR2){
			refImage.ds2.hmErrorCount = tempMap;
			refImage.ds2.sErrorPresent = tempSet;
		}
			
	}


	

	private static Image processImageLine(String line) {
		Image imgNewImage = null;		
		String imageid = null;
		
		//create new obj
		imgNewImage = new Image();
		
		//tokenize info
		imageid = tokenizeElements(line);		
		
		//populate into obj
		imgNewImage.setIImageId(Integer.parseInt(imageid));
		
		//assign image obj pointer
		return imgNewImage;
	}


	private static String tokenizeElements(String line) {
		// TODO Auto-generated method stub
		String retToken = null;
		StringTokenizer tokenizer = new StringTokenizer(line, DELIMITER_EQUALS);
		while(tokenizer.hasMoreTokens()){
			tokenizer.nextToken();
			retToken = tokenizer.nextToken().trim();					
		}
		if(DEBUG) { dispMesg("retToken: " + retToken); }
		return retToken;
	}


	private static void dispMesg(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);
		
	}	

}
