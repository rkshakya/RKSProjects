import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;


class Difference {
  	private int[] elements;
  	public int minimumDifference;
    HashMap <Integer, ArrayList> hm = new HashMap<Integer, ArrayList>();

  	Difference(int[] a){
  		this.elements = a;
  		Arrays.sort(elements);
  	}

    String join(Collection s, String delimiter) {
    StringBuffer buffer = new StringBuffer();
    Iterator iter = s.iterator();
    while (iter.hasNext()) {
      buffer.append(iter.next());
      if (iter.hasNext()) {
        buffer.append(delimiter);
      }
    }
    return buffer.toString();
  }

    void printPairs(int mindiff){
      ArrayList<Integer> lst = hm.get(mindiff) ;     
      System.out.println(join(lst, " "));
    }

  	void computeDifference(){
      //populate difference and pairs as key-val in map
      for(int m = 0; m < elements.length - 1 ; m++){
          int diff = elements[m+1] - elements[m];
          if(hm.containsKey(diff)){
            ArrayList<Integer> l = hm.get(diff);
            
            l.add(elements[m]);
            l.add(elements[m+1]);
            hm.put(diff, l) ;
          } else {
           
            ArrayList<Integer> li = new ArrayList<Integer> ();
            li.add(elements[m]);
            li.add(elements[m+1]);
            hm.put(diff, li);

          }
      }

  		int minDiff = elements[1]-elements[0];
      for(int i = 2; i != elements.length; i++){
          minDiff = Math.min(minDiff, elements[i]- elements[i-1]);
      }
      minimumDifference = minDiff;
  	}


  	} // End of Difference class

public class Day16 {



            public static void main(String[] args) {
                Scanner sc = new Scanner(System.in);
                int N = sc.nextInt();
                int[] a = new int[N];
                for (int i = 0; i < N; i++) {
                    a[i] = sc.nextInt();
                }

                Difference D = new Difference(a);

                D.computeDifference();

              	//System.out.print(D.minimumDifference);
                D.printPairs(D.minimumDifference);
            }
        }