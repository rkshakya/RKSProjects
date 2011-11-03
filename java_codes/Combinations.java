import java.io.*;
import java.util.StringTokenizer;
import java.util.HashMap;
/*        Date : 5/9/2007
        Author: ravikishores
       Description: program to generate all possible combinations of First Name, Last Name(supplied in tab delimited format in <input file>)
       It also generates a set of queries to search for those names against raw and normalized tables.
       If there are chances of having shortened forms of FirstName, then they should be included
       in  the list <short name list>
 */

public class Combinations{
   public static void main(String args[]) throws IOException {
       if(args.length < 1){
           System.out.println("Usage: java Combinations <input file> [<short name list>]");
           System.out.println("entries inside [] indicate optional parameters.");
           return;
       }

       String inFile = args[0];
       HashMap shortNames = null;

       if(args.length == 2){
        String  shortFile = args[1];
           //populate map with Name - short name
           try{
           shortNames = populateMap(shortFile);
           }   catch (IOException io){
               System.err.println("Error: opening input file: " + io.getMessage());

       }
       }

       String outFile = "out_" + System.currentTimeMillis() + ".txt";
       //to output queries
       String qryFile = "qry_" + System.currentTimeMillis() + ".txt";
       String line = null;

       String firstName = null;
       String lastName = null;
       String domainNames = null;
       String firstInit = null;
       String lastInit = null;
       String var1 = null;
       String var2 = null;
       String var3 = null;
       String var4 = null;
       String var5 = null;


       String domains[] = new String[100];

       String shortName = null;
       boolean flag = false;


        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        BufferedWriter bwQry = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(qryFile)));

       while((line = br.readLine()) != null){
		   System.out.println(line);
           StringTokenizer st = new StringTokenizer(line, "\t");
          firstName = st.nextToken().trim();
          lastName = st.nextToken().trim();

			while(st.hasMoreTokens()){
				domainNames = st.nextToken().trim();
			}

			if(domainNames.indexOf(",") > -1){
				domains = domainNames.split(",");
			}

           firstInit = firstName.substring(0,1).toUpperCase();
           lastInit = lastName.substring(0,1).toUpperCase();


          bw.write("\"" + firstName + " " + lastName + "\"\n");
           bw.write("\"" + lastName + ", " + firstName + "\"\n");
           bw.write("\"" + firstName + lastName + "\"\n");

           var1 = firstInit + lastName;
           var2 = lastName + firstInit;
           var3 = firstName + lastInit;
           var4 = lastInit + firstName;
           var5 = firstName + "." + lastName;


           bw.write("\"" + var1 + "@\"\n");
           bw.write("\"" + var2 + "@\"\n");
           bw.write("\"" + var3 + "@\"\n");
           bw.write("\"" + var4 + "@\"\n");
           bw.write("\"" + var5 + "@\"\n");


           bw.write("\"" + var1.toLowerCase() +  "@\"\n");
		  bw.write("\"" + var2.toLowerCase() +  "@\"\n");
		   bw.write("\"" + var3.toLowerCase() + "@\"\n");
           bw.write("\"" + var4.toLowerCase() +  "@\"\n");
           bw.write("\"" + var5.toLowerCase() +  "@\"\n");

           for(int m = 0; m < domains.length; m++){
				bw.write("\"" + var1 + "@" + domains[m].trim() +"\"\n");
				bw.write("\"" + var2 + "@" + domains[m].trim() + "\"\n");
				bw.write("\"" + var3 + "@" + domains[m].trim() + "\"\n");
				bw.write("\"" + var4 + "@" + domains[m].trim() + "\"\n");
				bw.write("\"" + var5 + "@" + domains[m].trim() + "\"\n");

				bw.write("\"" + var1.toLowerCase() +  "@" + domains[m].trim() + "\"\n");
				bw.write("\"" + var2.toLowerCase() +  "@" + domains[m].trim() + "\"\n");
				bw.write("\"" + var3.toLowerCase() + "@" + domains[m].trim() + "\"\n");
           		bw.write("\"" + var4.toLowerCase() +  "@" + domains[m].trim() + "\"\n");
           		bw.write("\"" + var5.toLowerCase() +  "@" + domains[m].trim() + "\"\n");
		   }



           bwQry.write("SELECT VALUE FROM <replace with table name> WHERE \n");
           bwQry.write(" CHARINDEX(UPPER('" + firstName + " " + lastName + "', UPPER(VALUE))> 0\n");
           bwQry.write(" OR CHARINDEX(UPPER('" + lastName + ", " + firstName + "', UPPER(VALUE))> 0\n");
           bwQry.write(" OR CHARINDEX(UPPER('" + firstName + lastName + "', UPPER(VALUE))> 0\n");
            bwQry.write(" OR CHARINDEX(UPPER('" + firstInit + lastName + "@', UPPER(VALUE))> 0\n");
            bwQry.write(" OR CHARINDEX(UPPER('" + lastName + firstInit + "@', UPPER(VALUE))> 0\n");
            bwQry.write(" OR CHARINDEX(UPPER('" + firstName + lastInit + "@', UPPER(VALUE))> 0\n");
            bwQry.write(" OR CHARINDEX(UPPER('" + lastInit + firstName + "@', UPPER(VALUE))> 0\n");



           if(args.length == 2){
               if( shortNames.containsKey(firstName.toUpperCase())){
                    shortName = ( shortNames.get(firstName.toUpperCase()) ).toString();
                  flag = true;
               }
           }



           if(flag){
               bw.write("\""+ shortName + " " + lastName + "\"\n");
           bw.write("\""+ lastName + ", " + shortName + "\"\n");
           bw.write("\""+ shortName + lastName + "\"\n");

           bwQry.write(" OR CHARINDEX(UPPER('" + shortName + " " + lastName + "', UPPER(VALUE))> 0\n");
           bwQry.write(" OR CHARINDEX(UPPER('"+ lastName + ", " + shortName + "', UPPER(VALUE))> 0\n");
           bwQry.write(" OR CHARINDEX(UPPER('" + shortName + lastName + "', UPPER(VALUE))> 0\n");


           flag = false;

           }

            bwQry.newLine();


       }


       bwQry.close();
       bwQry = null;

       bw.close();
       bw = null;

       br.close();
       br = null;

   }

    private static HashMap populateMap(String shortFile) throws IOException {
        HashMap temp = new HashMap();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(shortFile)));
        String line = null;

        while((line =  br.readLine()) != null){
            StringTokenizer st = new StringTokenizer(line, "\t");
            String fName = st.nextToken();
            String sName = st.nextToken();

            temp.put(fName.toUpperCase(), sName);

        }

        br.close();
        br = null;

        return temp;
    }

}
