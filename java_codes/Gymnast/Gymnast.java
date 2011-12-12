import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.Collections;
import java.util.Comparator;

/*---------------------------------------------------------------------------------------------------------
# Gymnast.java - program to generate optimal number of gymnasts to form a tower
# i/p -  an input file with (height, weight) (height, weight) pair of gymnasts
# o/p - number of candidates for optimal solution
# @author - Ravi Kishor Shakya
# @date - 09 Dec 2011
# JDK 1.6
#----------------------------------------------------------------------------------------------------------
*/

//comparator class for sorting Map based on values
class ValComparator implements Comparator<Candidate> {  
	 int sumtotal1, sumtotal2;
      public int compare(Candidate a, Candidate b) {
          sumtotal1 = a.getSum();
    	  sumtotal2 = b.getSum();
		  
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
	public static void main(String[] args) {	
		if(args.length != 1){
			System.out.println("Usage: java Gymnast <input file name>");
			return;
		}
		
		String inFile = args[0];
		Vector<Candidate> participants = new Vector<Candidate>();
		
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFile)));
		String line;
		String[] term;
		String[] finalTokens;
		
		while ((line = br.readLine()) != null) {
			line = line.trim();
			//split to get appr tokens, delim ) (
			term = line.split("\\)\\s\\(");
			
			//eliminate extra parentheses in first and last tokens
			term[0] = term[0].replace('(', ' ').trim();
			term[term.length - 1] = term[term.length - 1].replace(')', ' ').trim();
			
			//again tokenize on comma and populate into list
			for(String s: term){
				finalTokens = s.replaceAll(" ", "").split(",");				
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
		
		for(Candidate c : participants){
			//checking condition for sum is redundant as list is already sorted desc
			if((c.getHeight() < testHeight) && (c.getWeight() < testWeight)){
				candidates++;
				testHeight = c.getHeight();
				testWeight = c.getWeight();				
			}			
		}			
		System.out.print(candidates);
		System.out.println();		
		
		participants.clear();
		participants = null;
	}
}
