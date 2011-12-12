import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//this is added

/*---------------------------------------------------------------------------------------------------------
# Gymnast.java - program to generate top 5 search terms from log files
# i/p -  an input log file (txt format)
# o/p - top 5 search terms (based on frequency) displayed on console (sorted desc)
# @author - Ravi Kishor Shakya
# @date - 09 Dec 2011
# JDK 1.6
#----------------------------------------------------------------------------------------------------------
*/

//comparator class for sorting Map based on values
class ValComparator implements Comparator<Candidate> {	
	  public int compare(Candidate a, Candidate b) {
		  int sumtotal1 = a.getSum();
		  int sumtotal2 = b.getSum();
		  
	    if( sumtotal1 < sumtotal2 ) {
	      return 1;
	    } else if (sumtotal1 > sumtotal2){
	      return -1;
	    } else {
	      return 0;
	    }
	 }
}

//class to represent each participant in gymnastics
class Candidate{
	private int height;
	private int weight;
	private int sum;
	
	public Candidate(int height, int weight) {
		super();
		this.height = height;
		this.weight = weight;
		this.sum = height + weight;
	}	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}	
}

public class Gymnast {
	private static final int DEFAULT_CAPACITY = 5000;  //default size of Data Structs
	public static void main(String[] args) {	
		if(args.length != 1){
			System.out.println("Usage: java Gymnast <input log file name>");
			return;
		}
		
		String inFile = args[0];
		ArrayList<Candidate> participants = new ArrayList<Candidate>(DEFAULT_CAPACITY);
		
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFile)));
		String line;
		String[] term;
		String[] finalTokens;
		int counter;
		
		while ((line = br.readLine()) != null) {
			line = line.trim();
			//split to get appr tokens, delim ) (
			term = line.split("\\)\\s\\(");
			
			//eliminate extra parentheses in first and last tokens
			term[0] = term[0].replace('(', ' ').trim();
			term[term.length - 1] = term[term.length - 1].replace(')', ' ').trim();
			
			//again tokenize on comma and populate into hash
			for(String s: term){
				//System.out.println(s);
				finalTokens = s.replaceAll(" ", "").split(",");
				//System.out.println(finalTokens[0] + ":" + finalTokens[1]);
				
				participants.add(new Candidate(Integer.parseInt(finalTokens[0]), Integer.parseInt(finalTokens[1])));
				
			}
							
		}
		
		br.close();
		br = null;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		//now sort based on sum(ht + wt) using Comparator
		Collections.sort(participants, new ValComparator());
		int candidates = 1; //holds the final result - no of possible candidates		
		int testHeight = participants.get(0).getHeight();
		int testWeight = participants.get(0).getWeight();
		int testSum = participants.get(0).getSum();
		
		for(Candidate c : participants){
			//System.out.println(c.getHeight() + ": " + c.getWeight() + ": " + c.getSum());
			//checking condition for sum is redundant as list is already sorted desc
			if((c.getHeight() < testHeight) && (c.getWeight() < testWeight) && (c.getSum() < testSum)){
				candidates++;
				testHeight = c.getHeight();
				testWeight = c.getWeight();
				testSum = c.getSum();
			}
			
		}	
		
		System.out.print(candidates);
		System.out.println();		
	}
}


