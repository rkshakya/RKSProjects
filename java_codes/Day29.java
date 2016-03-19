import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Day29 {

	private static int appfun(char s1, char s2){
		//int i1 = s1;
		//int i2 = s2;
		return Math.abs( (int)s1- (int)s2 ) ;
	}

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc = new Scanner(System.in);
        int n = Integer.valueOf(sc.nextLine());

        for(int i = 1; i <= n; i++){ 
            String wrd = sc.nextLine();
            String revword = new StringBuilder(wrd).reverse().toString();

            char[] arrorig = wrd.toCharArray();
            char[] arrrev = revword.toCharArray();

            //System.out.println(arrorig[1]);
            //System.out.println(arrrev[1]);
            
            for (int c = 1 ; c < arrorig.length ; c++){
            	//System.out.println(wrd[c]);
            	
            	if ( appfun(arrorig[c], arrorig[c-1]) != appfun(arrrev[c], arrrev[c-1]) ){
            		System.out.println("Not Funny");
            		break;
            	} else if (c == arrorig.length - 1){
            		 System.out.println("Funny"); 
            	}
            }

           
        }
    }
}