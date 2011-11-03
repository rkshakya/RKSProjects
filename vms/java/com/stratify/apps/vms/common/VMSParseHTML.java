/*
 * VMSParseHTML.java
 *
 * Created on May 22, 2008, 2:25 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common;


import com.stratify.apps.vms.common.exceptions.VMSSysException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import com.stratify.common.logging.Logger;

import org.htmlparser.beans.StringBean;

public class VMSParseHTML {
    String mText = null; // text extracted from the response to the POST
    // request
    String location = null;
    String serverType = null;
    private static Logger logger = Logger.getLogger(VMSParseHTML.class.getName());
    
    public VMSParseHTML(String location, String serverType) throws VMSSysException{
        URL url;
        HttpURLConnection connection;
        StringBuffer buffer;
        PrintWriter out;
        StringBean bean;        
        
        // this.location = location;
        this.serverType = serverType;
        
        // form the appr url based on server type
        if (serverType.equalsIgnoreCase("searchserver")) {
            // http://ldsdts01.lds.asp.stratify.com:7041/ftsearchserver/
            if (location.endsWith("//")) {
                location += "ftsearchserver";
            } else {
                location += "/ftsearchserver";
            }
            logger.info("VMSParseHTML: URL passed: " + location);
            
        } else if (serverType.equalsIgnoreCase("redactionserver")) {
            // http://redact06.lds.asp.stratify.com:82/redactsrv/htdocs/SystemDiagnostics.jsp
            if (location.endsWith("//")) {
                location += "redactsrv/htdocs/SystemDiagnostics.jsp";
            } else {
                location += "/redactsrv/htdocs/SystemDiagnostics.jsp";
            }
            
        } else if (serverType.equalsIgnoreCase("pdfserver")) {
            // http://redact06.lds.asp.stratify.com:81/pdfconvertsrv/htdocs/SystemDiagnostics.jsp
            if (location.endsWith("//")) {
                location += "pdfconvertsrv/htdocs/SystemDiagnostics.jsp";
                
            } else {
                location += "/pdfconvertsrv/htdocs/SystemDiagnostics.jsp";
            }
        }
        
        this.location = location;
        
        try {
            
            url = null;
            url=  new URL(location);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            
            connection.setRequestProperty("Accept-Charset", "*");
            connection.setRequestProperty("Referer", location);
            connection
                    .setRequestProperty("User-Agent", "VMSParseHTML.java/1.0");
            
            bean = new StringBean();
            bean.setConnection(connection);
            mText = bean.getStrings();
            logger.info("VMSParseHTML: mText: " + mText);
        } catch (Exception e) {
            logger.error("VMSParseHTML: Error: " , e);
            throw new VMSSysException(VMSStaticParams.SEVERITY_1, VMSStaticParams.ERROR, "Unable to get server version info", location);
        }
        
    }
    
    public String getText() throws Exception {
        String retString = null;
        int start = 0;
        String partStrRed[], partStrPDF[];
        try{
            if (serverType.equalsIgnoreCase("searchserver")) {
                start = mText.indexOf("Version :") + "Version :".length();
                logger.info("getText: start index : " + start + " End index" + mText.indexOf("\n", start));
                retString = mText.substring(start, mText.indexOf("\n", start))
                .trim();
            } else if (serverType.equalsIgnoreCase("redactionserver")) {
                partStrRed = mText.split("\n");
                retString = partStrRed[8].trim();
            } else if (serverType.equalsIgnoreCase("pdfserver")) {
                partStrPDF = mText.split("\n");
                retString = partStrPDF[8].trim();
            }
        }catch(Exception ex){
           logger.warn("getText: Error: ", ex); 
        }
        return retString;
    }
    
}

