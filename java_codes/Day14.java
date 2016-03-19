import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;


class Difference {
  	private int[] elements;
  	public int maximumDifference;

  	Difference(int[] a){
  		this.elements = a;
  		Arrays.sort(elements);
  	}

  	void computeDifference(){
  		int larger = elements[1];
  		int smallest = elements[0];
  		int largest_diff = Math.abs(elements[1] - elements[0]);

  		for(int i = 1; i < (elements.length - 1) ; i++){
  			if(elements[1] < smallest){
  				smallest = elements[1];
  			}

  			if(Math.abs(elements[i+1] - smallest) > largest_diff){
  				largest_diff = Math.abs(elements[i+1]) - smallest;
  				larger = elements[i+1]		;
  			}
  		}

  		maximumDifference = largest_diff;
  	}


  	} // End of Difference class

public class Day14 {

            public static void main(String[] args) {
                Scanner sc = new Scanner(System.in);
                int N = sc.nextInt();
                int[] a = new int[N];
                for (int i = 0; i < N; i++) {
                    a[i] = sc.nextInt();
                }

                Difference D = new Difference(a);

                D.computeDifference();

              	System.out.print(D.maximumDifference);
            }
        }