import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Mars {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();

        int slength = s.length();
        int nomesg = slength/3;

        String ini = "";
        for (int i = 1; i <= nomesg; i++){
        	ini += "SOS";
        }

        char[] arrorig = ini.toCharArray();
        char[] actual = s.toCharArray();
        int ctr = 0;

        for(int c = 0 ; c < arrorig.length; c++){
        	if (arrorig[c] != actual[c]){
        		ctr++;
        	}
        }

        System.out.println(ctr+"");
    }
}

