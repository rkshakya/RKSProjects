import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/*---------------------------------------------------------------------------------------------------------
# Topfive.java - program to generate top 5 search terms from log files
# i/p -  an input log file (txt format)
# o/p - top 5 search terms (based on frequency) displayed on console (sorted desc)
# @author - Ravi Kishor Shakya
# @date - 09 Dec 2011
# JDK 1.6
#----------------------------------------------------------------------------------------------------------
*/

public class Topfive {
	private static final int DEFAULT_CAPACITY = 5000;  //default size of Data Structs
	public static void main(String[] args) {	
		if(args.length != 1){
			System.out.println("Usage: java Topfive <input log file name>");
			return;
		}
		
		String inFile = args[0];
		HashMap<String, Integer> searchTerms = new HashMap<String, Integer>(DEFAULT_CAPACITY);
		
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFile)));
		String line;
		String term;
		int counter;
		
		while ((line = br.readLine()) != null) {
			line = line.trim();
			//get the query string
			term = line.substring(line.indexOf("query=") + 6, line.lastIndexOf("]"));
			
			//populate in map, set the counter
			if(searchTerms.containsKey(term)){
				counter = searchTerms.get(term);
				counter++;
				searchTerms.put(term, counter);
			}else{
				searchTerms.put(term, 1);
			}
			
		}
		
		br.close();
		br = null;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		//now display top 5 terms
		ValueComparator vc = new ValueComparator(searchTerms);
		TreeMap<String, Integer> sortMap = new TreeMap(vc);
		sortMap.putAll(searchTerms);
		
		//get rid of HashMap now
		searchTerms.clear();
		searchTerms = null;
		
		int ctr = 1;
		for (String key : sortMap.keySet()) {
            System.out.println(key);
            
            if(ctr == 5){
            	break;
            }
            
            ctr++;
        }

		//recycle TreeMap
		sortMap.clear();
		sortMap = null;
	}
}

//comparator class for sorting Map based on values
class ValueComparator implements Comparator {
	  Map base;
	  public ValueComparator(Map base) {
	      this.base = base;
	  }

	  public int compare(Object a, Object b) {
	    if((Integer)base.get(a) < (Integer)base.get(b)) {
	      return 1;
	    } else if((Integer)base.get(a) == (Integer)base.get(b)) {
	      return 0;
	    } else {
	      return -1;
	    }
	  }
	}
