import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.util.StringTokenizer;

public class Day20 {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();

        StringTokenizer st = new StringTokenizer(str, "[\\s!,?.\\_'@]+" , false);
  

        System.out.println(st.countTokens());
        while (st.hasMoreTokens()){
        	System.out.println(st.nextToken());
        }
    }
}

