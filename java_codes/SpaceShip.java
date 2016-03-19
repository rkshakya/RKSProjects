import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.util.Arrays;
import java.util.Collections;

public class SpaceShip {

	public static boolean useLoop(int[] arr, int targetValue) {
		for(int i: arr){
			if(i == targetValue)
				return true;
		}
		return false;
	}

	public static boolean useList(int[] arr, int targetValue) {
	return Arrays.asList(arr).contains(targetValue);
}

	public static Integer[] convert(int[] chars) {
        Integer[] copy = new Integer[chars.length];
        for(int i = 0; i < copy.length; i++) {
            copy[i] = Integer.valueOf(chars[i]);
        }
        return copy;
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int c[] = new int[m];
        int dist[] = new int[n];

        for(int c_i=0; c_i < m; c_i++){
            c[c_i] = in.nextInt();
        }

        for(int a = 0; a < n; a++){
        	int temp[] = new int[c.length];
        	if (!useList(c, a)){
        		for(int b = 0; b < c.length; b++){
        			temp[b] = Math.abs(a - c[b]);
        		}
        	}
        	//Integer[] intTemp = convert(temp);
        	//System.out.println(Collections.min(Arrays.asList(intTemp)));
        	//dist[a] = Collections.min(Arrays.asList(intTemp));
            Arrays.sort(temp);
            dist[a] = temp[0];
        }
        Integer[] distTemp = convert(dist);
        System.out.println(Collections.max(Arrays.asList(distTemp)));
        //Arrays.sort(dist);
        //System.out.println(dist[dist.length - 1]);
    }
}

