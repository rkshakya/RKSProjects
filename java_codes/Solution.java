import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int arr[][] = new int[6][6];

       //System.out.println(arr.length);
       //System.out.println(arr[0].length);

        
        for(int i=0; i < 6; i++){
            for(int j=0; j < 6; j++){
                arr[i][j] = in.nextInt();
            }
        }
        

        int rowSize = arr.length;
        int colSize = arr[0].length;

        if (rowSize < 3 || colSize < 3){
        	System.out.println("Hour glass not possible with array size < 3");
        	return;
        }

        int rowHGlass = rowSize - 2;
        int colHGlass = colSize - 2;

        //System.out.println("Possible hourglasses: " + (rowHGlass) * (colHGlass));

        int sum = 0;
        int max = Integer.MIN_VALUE;
        for(int m = 0; m < rowHGlass; m++){
            for (int n = 0; n < colHGlass; n++){
                sum = (arr[m][n] + arr[m][n+1] + arr[m][n+2] + arr[m+1][n+1] + arr[m+2][n] + arr[m+2][n+1] + arr[m+2][n+2]);
                if (sum > max)  {
                    max = sum;
                }
            }
        }

        System.out.println(max);

    }
} 