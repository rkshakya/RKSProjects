/*
 * VMSConfigurationParser.java
 *
 * Created on January 9, 2008, 9:24 PM
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
public class VMSConfigurationParser {
    static String filename = null;
    static Document dom = null;
    private static Logger logger = Logger.getLogger(VMSConfigurationParser.class.getName());
       
    /**
     * Creates a new instance of VMSConfigurationParser
     */
    public VMSConfigurationParser(String filename) {
        this.filename = filename;
    }
          
    public static ArrayList getTS() throws VMSSysException, Exception{
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        ArrayList retList = new ArrayList();
                
        try {            
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            logger.info("getTS: XML location passed: " + filename);            
            //parse using builder to get DOM representation of the XML file
            dom = db.parse(filename);                        
        }catch(Exception ex){            
            logger.error("getTS: Error parsing Entry Server xml: " , ex);
            throw new VMSSysException(ex);
        }
        
        //get the root elememt
        Element docEle = dom.getDocumentElement();        
        //get a nodelist of 'site' elements
        NodeList nl = docEle.getElementsByTagName(VMSStaticParams.SITE_TAG);
        
        if(nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength();i++) {
                String tsuri = "";
                //get the site element
                Element e = (Element)nl.item(i);
                if( (e.getAttribute(VMSStaticParams.TERMINAL_SERVER_ALIAS) != null) && (e.getAttribute(VMSStaticParams.TERMINAL_SERVER_ALIAS).length() != 0) && (!e.getAttribute(VMSStaticParams.TERMINAL_SERVER_ALIAS).equals("")) ){
                    logger.info("getTS: " + e.getAttribute(VMSStaticParams.TERMINAL_SERVER_ALIAS));
                    retList.add(e.getAttribute(VMSStaticParams.TERMINAL_SERVER_ALIAS).trim());
                }
            } 
        }
        
        return retList;
        
    }
    
    
    
}
