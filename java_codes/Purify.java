import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

//define bean
class Node{
	String header = null;
	String value = null;
	public Node(String header, String value) {
		super();
		this.header = header;
		this.value = value;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}

public class Purify{
	private static final String OUTDELIMITER = ",";
	private static final String INDELIMITER = ",";
	
    public static void main (String args[]) throws IOException{
        if (args.length != 1) {
            System.out.println("Incorrect number of argument");
            return;
        }
        
        String inFile = args[0];
        String outFileName = "out_" + System.currentTimeMillis() + ".csv";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile))));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outFileName));
        
        String line = null;
        String[] toks = new String[200];
        
       //put headers in HashMap (index as key, Header as value)
        String line0 = br.readLine();
        String[] heads = new String[200];
        LinkedHashMap<Integer, String> headmap = new LinkedHashMap<Integer, String>();
        
        heads = line0.split(INDELIMITER);
        
        for(int cnt = 0; cnt < heads.length; cnt++){
        	headmap.put(cnt, heads[cnt]);
        }
        
        //now process recs
        Queue<Node> qprice;
        Queue<Node> qarea;
        
      //generate header in out file
        bw.write(headmap.get(0) + OUTDELIMITER + headmap.get(1)  + OUTDELIMITER + headmap.get(2) + OUTDELIMITER + headmap.get(3) + OUTDELIMITER + headmap.get(4) + OUTDELIMITER + headmap.get(5) + OUTDELIMITER + headmap.get(6) + OUTDELIMITER + "\"MISC\"" + OUTDELIMITER + "\"PRICE\"" + OUTDELIMITER + "\"SQFT\"" + OUTDELIMITER + headmap.get(157) + OUTDELIMITER + headmap.get(158) + OUTDELIMITER + headmap.get(159) + OUTDELIMITER + headmap.get(160));
        bw.newLine();
        
        while((line = br.readLine()) != null){
        	toks = line.split(INDELIMITER);
        	
        	qprice = new LinkedList<Node>();
        	qarea = new LinkedList<Node>();
        	
        	Node price;
        	//collect price entries in q
        	for(int cnt = 7; cnt <= 81; cnt++){
        		if (toks[cnt].trim().length() != 0) {
        			//create node
        			price = new Node(headmap.get(cnt), toks[cnt].trim());
        			//insert into q
        			qprice.add(price);
        		}
        	}
        	
        	Node area;
        	//collect area entries into q
        	for(int c = 82; c <= 156; c++ ){
        		if(toks[c].trim().length() != 0){
        			//create node
        			area = new Node(headmap.get(c), toks[c].trim());
        			//add to q
        			qarea.add(area);
        		}
        	}
        	
        	//now attempt to generate outputs
        	Iterator<Node> itprice = qprice.iterator();
        	Iterator<Node> itarea = qarea.iterator();
        	
        	Node nprice, narea;
        	
        	while(itprice.hasNext()){
        		bw.write(toks[0].trim() + OUTDELIMITER + toks[1].trim() + OUTDELIMITER + toks[2].trim() + OUTDELIMITER + toks[3].trim() + OUTDELIMITER + toks[4].trim() + OUTDELIMITER + toks[5].trim() + OUTDELIMITER + toks[6].trim() + OUTDELIMITER);
        		
        		nprice = itprice.next();
        		narea = itarea.next();
        		
        		bw.write(nprice.getHeader() + OUTDELIMITER + nprice.getValue() + OUTDELIMITER + narea.getValue() + OUTDELIMITER);
        		bw.write(toks[157].trim() + OUTDELIMITER + toks[158].trim() + OUTDELIMITER + toks[159].trim() + OUTDELIMITER + toks[160]) ;
        		bw.newLine();
        	}
        	
        	/*for(String s : toks){
        		System.out.println(s);
        	}*/
        }
        
        bw.close();
        bw = null;
        
        br.close();
        br= null;
    }
}
