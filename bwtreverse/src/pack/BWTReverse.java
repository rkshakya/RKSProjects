package pack;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/*---------------------------------------------------------------------------------------------------------
# BWTReverse.java - program to generate original string used in BWT (implementation of reverse BWT)
# i/p -  an ASCII txt file with row number <space> BWT scrambled output
# o/p - original string used for BWT
# author - Ravi Kishor Shakya
# JDK 1.6
#----------------------------------------------------------------------------------------------------------
*/

//class to represent a row read from input file
class Row{
	private int rownum = 0;
	private String inputString = "";
	
	public Row(int rownum, String inputString) {
		super();
		this.rownum = rownum;
		this.inputString = inputString;
	}
	
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public String getInputString() {
		return inputString;
	}
	public void setInputString(String inputString) {
		this.inputString = inputString;
	}
	
	
}

class Worker implements Runnable{
	private final int D_SIZE = 200;
	int low = 0;
	int up = 0;
	ArrayList<Row> al;
	ArrayList<String> res = new ArrayList<String> (D_SIZE);
		
	Thread th;
	
	 Worker(int i, int split, ArrayList<Row> elements) {
		// TODO Auto-generated constructor stub
		 this.low = i;
		 this.up = split;
		 this.al = elements;
		 th = new Thread(this, "Child Thread");
	     th.start(); // Start the thread
	}
	 
	 public void run() {
	     
				int count = 0;
				int indxStart = -1;
				char schar;
				int row = -1;
				int counter = 1;
				int result = -1;
				char[] chrs;
				char[] sortedchrs;
				HashMap<Character, Integer> inter = new HashMap<Character, Integer>(D_SIZE);
				HashMap<String, Integer> lookup = new HashMap<String, Integer>(D_SIZE);	 
				String finalString = "";
				Row r;
				
				//iterate for the set of elements
				for(int cntr = low; cntr <= up; cntr++){	
					r = al.get(cntr);
					
					
				//break the string to chars contains last col of the matrix
				chrs = r.getInputString().toCharArray();
				sortedchrs = new char[chrs.length];				
				System.arraycopy(chrs, 0, sortedchrs, 0, chrs.length);
				//sorted array contains first col of the matrix
				Arrays.sort(sortedchrs);				
				
				//get the lookup hash ready
				count = 0;
				
				
					for(int indx = 0; indx < chrs.length; indx++){
						if(inter.containsKey(chrs[indx])){
							count = inter.get(chrs[indx]);
							count++;
							inter.put(chrs[indx], count);
							lookup.put(chrs[indx] + "-" + count, indx);
						}
						if(!inter.containsKey(chrs[indx])){
							inter.put(chrs[indx], new Integer(1));
							lookup.put(chrs[indx] + "-" + "1", indx);
							
						}
						
					}
					
					indxStart = r.getRownum();
					//first char of the final string- we already know
					finalString += sortedchrs[indxStart];
					
					//get the intermediate chars - meat of the program
					//start with the first char c, given row number say R					
					schar = sortedchrs[indxStart];
					row = indxStart;
					counter = 1;
					
					//loop till all chars are processed
					while(counter < chrs.length - 1){
						result = 0;
						
						//count how many times this char comes before current row R, say i times	
						for(int cnt = 0; cnt < row; cnt++){
							if(sortedchrs[cnt] == schar){
								result++;
							}
						}
						
						row = lookup.get(schar + "-" + (result + 1));
						
						//get the corresp first char in the row - this char is the next char in the sequence	
						//append in the final string						
						finalString += sortedchrs[row];
						schar = sortedchrs[row];
						
						counter++;
					}
					//last char - also
					if(chrs.length != 1){
						finalString += chrs[indxStart];
					}
					
					//put into fin list
					res.add(finalString);
					
					//reset the global vars, arrays, maps
					indxStart = -1;
					schar = ' ';
					row = -1;
					indxStart = 0;
					chrs = null;
					sortedchrs = null;
					finalString = "";
					inter.clear();
					lookup.clear();					
					
				}
	
	 }	

	public ArrayList<String> getRes() {
		return res;
	}

	public void setRes(ArrayList<String> res) {
		this.res = res;
	}
	
}

public class BWTReverse{
	private final static int DS_SIZE = 1000;  //default size of DS
	private final static int THRESHOLD = 1000; //no of rows in I/P file that determines if to use mutithreading
	
	public static void main(String[] args) throws Exception{
	if(args.length != 1){
		System.out.println("Wrong usage");		
		return;
	}
	
	String inFile = args[0];
	String line = null;	
	String[] parts;
	Row rw;
	int split = 0;
	ArrayList<Row> elements = new ArrayList<Row>(DS_SIZE);
	
	//read input file 
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(inFile)));
			
			
			while ((line = br.readLine()) != null) {
				line = line.trim();
				//get the BWT values now, 0-index, 1-string
				parts = line.split(" ");	
				
				//create new objs and populate into a List and Map
				rw = new Row(Integer.parseInt(parts[0]), parts[1]);
				elements.add(rw);
				}
			
			br.close();
			br = null;			
		}catch(Exception e){
			e.getMessage();
		}
		
		int elemsize = 0;
		elemsize = elements.size();
		
		//if rows are too many, then only use threading
		if(elemsize >= THRESHOLD){
			//now split the inputs and pass them to multiple threads
			split = (int)Math.floor(elements.size()/2);
			
			Worker wk1 = new Worker(0, split, elements);
			Worker wk2 = new Worker((split + 1), (elemsize - 1), elements);
			
			//wait for workers to complete
			wk1.th.join();
			wk2.th.join();
			
			ArrayList<String> al1 = wk1.getRes();
			ArrayList<String> al2 = wk2.getRes();
			
			for(String a : al1){
				System.out.println(a);
			}
			
			for(String a: al2){
				System.out.println(a);
			}			
		}else if(elemsize < THRESHOLD){
			Worker wk5 = new Worker(0, elemsize-1, elements);
			
			wk5.th.join();
			
			ArrayList<String> al5 = wk5.getRes();
			
			for(String a: al5){
				System.out.println(a);
			}
		}
		
	    
	}	
	
}