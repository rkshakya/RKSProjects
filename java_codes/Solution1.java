import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution1 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int arr[][] = new int[6][6];
        for(int i=0; i < 6; i++){
            for(int j=0; j < 6; j++){
                arr[i][j] = in.nextInt();
            }
        }
        
        int sum = 0;
        int max = Integer.MIN_VALUE;
        for(m = 0; m <= 3; m++){
            for (n = 0; n <= 3; n++){
                sum = (arr[m][n] + arr[m][n+1] + arr[m][n+2] + arr[m+1][n+1] + arr[m+2][n] + arr[m+2][n+1] + arr[m+2][n+2]);
                if (sum > max)  {
                    max = sum;
                }
            }
        }

        System.out.println(max);

    }
}

