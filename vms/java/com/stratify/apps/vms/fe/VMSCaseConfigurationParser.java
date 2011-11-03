/*
 * VMSCaseConfigurationParser.java
 *
 * Created on June 4, 2008, 11:12 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.fe;


import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.stratify.common.logging.Logger;

/**
 *
 * @author ravikishores
 */
public class VMSCaseConfigurationParser {
    private String filename = null;
    private String caseName = null;
    private Document dom = null;
    private String primaryCaseShare = null;
    private ArrayList tempCaseShares = new ArrayList();
    private ArrayList caseShares = new ArrayList();
    private static Logger logger = Logger.getLogger(VMSCaseConfigurationParser.class.getName());
    
    /**
     * Creates a new instance of VMSConfigurationParser
     */
    public VMSCaseConfigurationParser(String caseName, String primCaseShare,
            String filename) {
        this.filename = filename;
        this.primaryCaseShare = primCaseShare;
        this.caseName = caseName;
    }
    
    public  void showValues() {
        for (int i = 0; i < caseShares.size(); i++) {
            System.out.print("From List: " + (String) caseShares.get(i) + "\n");
        }
    }
    
    public  ArrayList getSecondaryCaseShares() throws VMSSysException {
        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            logger.info("getSecondaryCaseShares: XML location passed: " + filename);
            // parse using builder to get DOM representation of the XML file
            dom = db.parse(filename);            
        } catch (Exception ex){
            logger.error("getSecondaryCaseShares: Error: ", ex);
            throw new VMSSysException(ex);
        }
        
        // get the root elememt
        Element docEle = dom.getDocumentElement();
        
        // get a nodelist of 'site' elements
        NodeList nl = docEle.getElementsByTagName(VMSStaticParams.SEC_CASESHARE_PREFIX);
        logger.info("getSecondaryCaseShares Node Size: " + nl.getLength());
        
        if (null != nl && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                String secCaseShare = null;
                // get the site element
                Element e = (Element) nl.item(i);
                if ((null != e.getTextContent())
                || ("".equals(e.getTextContent()))) {
                    try{
                        String nodeText = e.getTextContent().trim().toLowerCase();
                        caseName = caseName.toLowerCase();
                        if ((nodeText.startsWith("\\"))
                        && (nodeText.indexOf(caseName) > -1)) {
                            String trimVal = nodeText.substring(0, nodeText
                                    .indexOf(caseName) - 1);
                            if (!trimVal.equalsIgnoreCase(primaryCaseShare)) {
                                if (!tempCaseShares.contains(trimVal.toLowerCase())) {
                                    tempCaseShares.add(trimVal.toLowerCase());
                                    caseShares.add(trimVal + "\\" + caseName);
                                }
                            }
                        }
                    }catch(Exception ex){
                        logger.error("getSecondaryCaseShares: Error: ", ex);
                        continue;
                    }
                }
            }
        }
        return caseShares;
        
    }
    
}
