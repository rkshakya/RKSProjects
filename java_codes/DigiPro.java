import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class DigiPro {
	static long digipro(long num){

		long retval=0L;

		if (num < 10){
			retval = num;
		} else{
			long prod = 1L;

			while (num > 0) {
    			prod *= ( num % 10);
    			num = num / 10;
			}

			retval = prod;
		}
		return retval;
	}


    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

          Scanner sc=new Scanner(System.in);
          int T = sc.nextInt();
          for (int i = 1; i <= T; i++ ){
              long A = sc.nextLong();
              long B = sc.nextLong();
              long prod = sc.nextLong();

              long counter = 0L;

              for (long m = A; m <= B; m++ ){
              	if (digipro(m) == prod){
              		counter++;
              	}

              }

              System.out.println("Case " + i + ": " + (int)(counter % (Math.pow(10, 9) + 7)) ) ;


          }
    }
}