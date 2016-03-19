import java.util.Scanner;
import java.util.regex.*;

public class Day28
{
   public static void main(String[] args){
      Scanner in = new Scanner(System.in);
      int testCases = Integer.parseInt(in.nextLine());
      while(testCases>0){
         String pattern = in.nextLine();
         try{
         Pattern r = Pattern.compile(pattern);
         System.out.println("Valid");
     	} catch (PatternSyntaxException pe){
     		System.out.println("Invalid");
     	}

         testCases--;
      }
   }
}

