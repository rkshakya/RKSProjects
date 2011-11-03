/*
 * CustodianReport.java
 *
 * Created on November 7, 2007, 3:51 AM
 *
 * Stratify Inc P Ltd.
 *
 */

import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class CustodianReport {

    static Document dom = null;
    static HashMap exprPath = new HashMap(10000);
    static  HashMap custodianPath = new HashMap(10000);

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println("Usage: java CustodianReport booleanClassification custodianMapping");
            return;
        }
        //load custodian-pathlist mapping
        populateCustodianMap(args[1]);

        //load path - expression list mapping after parsing xml file
        processXML(args[0]);

        //generateReports
        generateReports();


    }



    private static void populateCustodianMap(String custodianMapFile) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(custodianMapFile))));
        String line = null;

        String[] fields = null;

        while ((line = br.readLine()) != null) {
            //first field - path , 2nd custodian.
            String path = "";
            String custodian = "";
            fields = line.split("\t");

            path = fields[0].toLowerCase().trim();
            custodian = fields[1].trim();

            if(custodianPath.containsKey(custodian)){
                ArrayList tempArr = (ArrayList)custodianPath.get(custodian);
                tempArr.add(path);
                custodianPath.put(custodian, tempArr);

            }else{
                ArrayList pathArr = new ArrayList();
                pathArr.add(path);
                custodianPath.put(custodian, pathArr);

            }

        }

        br.close();
        br = null;
    }

    private static void processXML(String string) {
        parseXML(string);

        parseDocument();
    }

    private static void parseXML(String str) {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse(str);


        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        }catch(SAXException se) {
            se.printStackTrace();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void parseDocument() {
        //get the root elememt
        Element docEle = dom.getDocumentElement();

        //get a nodelist of 'rule' elements
        NodeList nl = docEle.getElementsByTagName("rule");

        if(nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength();i++) {

                //get the rule element
                Element el = (Element)nl.item(i);

                //for only elems where has property is not 0
                if(!el.getAttribute("hash").equals("0")){

                    String expression = "";

                    //get expression info
                    NodeList exprList = el.getElementsByTagName("expression");
                    //System.out.println("Expr Node length: " + exprList.getLength());

                    expression = getTextValue(el, "expression").trim();

                    //System.out.println("Expression Value: "+ expression);


                    //get doc uris
                    NodeList docList = el.getElementsByTagName("document");

                    //System.out.println("Doc Node length: " +  docList.getLength());
                    if(docList != null && docList.getLength() > 0){
                        for(int k = 0 ; k < docList.getLength();k++) {
                            String docuri = "";

                            //get document element
                            Element doc = (Element)docList.item(k);
                            docuri = doc.getAttribute("originalLocation").replaceFirst("file:", "").trim().toLowerCase();
                            //System.out.println("DOC URI: " + docuri);

                            if(exprPath.containsKey(docuri)){
                                ArrayList tempExps = null;
                                tempExps = (ArrayList)exprPath.get(docuri);

                                //System.out.println("TempArr Size:" + tempExps.size());

                                for(int g = 0; g < tempExps.size(); g++){
									//System.out.println("G: " + tempExps.get(g));
								}
                                //System.out.println("Expression2: " + expression);
                                tempExps.add(expression);
                                exprPath.put(docuri, tempExps);
                            }else{
                                ArrayList exps = new ArrayList();
                                exps.add(expression);
                                exprPath.put(docuri, exps);

                                //System.out.println("2nd insert");
                            }


                        }
                    }



                } //hash


            } //for
        }//if
    }

    private static String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            Element el = (Element)nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }

    private static void generateReports() throws FileNotFoundException, IOException {
        String outFile = "out_" + System.currentTimeMillis() + ".html";
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));

        Iterator itrCust = custodianPath.keySet().iterator();

          bw.write("<html><head></head><body><table border=\"1\" cellpadding=\"1\">");
				bw.newLine();
				bw.write("<tr><td width=\"60%\"><b>Custodian</b></td><td><b>Expression</b></td><td><b>Count(Hits)</b></td></tr>");
		bw.newLine();

        while(itrCust.hasNext()){
            String custodian = "";
            ArrayList paths = null;
            HashMap results = new HashMap();


            //Map.Entry entries = (Map.Entry)itrCust.next();
            custodian = (String)itrCust.next();
            paths = (ArrayList) custodianPath.get(custodian);

            //System.out.println("Custodian: " + custodian);
            //System.out.println("Paths Length: " + paths.size());


            for(int m = 0 ; m < paths.size(); m++ ){
                ArrayList exprArr = null;
                String thisPath = "";

                thisPath = (String)paths.get(m);
                exprArr = (ArrayList) exprPath.get(thisPath);

                if(null == exprArr){
				continue;
			}



                for(int n = 0; n < exprArr.size(); n++){
                    String tempExpr = "";
                    tempExpr = (String)exprArr.get(n);

                    if(results.containsKey(tempExpr)){
                        int tempCnt = (Integer)results.get(tempExpr);
                        tempCnt++;
                        results.put(tempExpr, tempCnt);

                    }else{
                        results.put(tempExpr, 1);
                    }
                }

            }



            //System.out.println("CUSTODIAN: " + custodian);
            Iterator resItr = results.keySet().iterator();
/*
            if(resItr.hasNext()){
                bw.write("<html><head></head><body><table border=\"1\" cellpadding=\"1\">");
		bw.newLine();
		bw.write("<tr><td width=\"60%\"><b>Custodian</b></td><td><b>Expression</b></td><td><b>Count(Hits)</b></td></tr>");
		bw.newLine();
            }
            */
            while(resItr.hasNext()){
               // Map.Entry res = (Map.Entry)resItr.next();
               String exp = "";
               String count = "";

               exp = (String)resItr.next();
               count = ( (Integer)results.get(exp) ).toString();

                System.out.println(custodian + ": " + exp + ": " + count);
                bw.write("<tr><td>" + custodian + "</td><td>" + exp + "</td><td>" + count + "</td></tr>");
                bw.newLine();
            }

        }

        bw.close();
        bw = null;
    }
}


