/*
 * VMSCommonUtils.java
 *
 * Created on June 2, 2008, 10:16 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common;

import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.dao.VMSMyDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import com.stratify.common.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author RavikishoreS
 */
public class VMSCommonUtils {
    static Logger logger = Logger.getLogger(VMSCommonUtils.class.getName());
    
    /** Creates a new instance of VMSCommonUtils */
    public VMSCommonUtils() {
    }
    
     //function to read caseShare script version from caseshare/scripts/common/version.txt
    public static String readVersion(String serverURL) throws Exception {
        String vers = "0.0.0.0";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(serverURL + VMSStaticParams.CASESHARE_VERSION)));
        logger.info("readVersion: Reading File At: " + serverURL + VMSStaticParams.CASESHARE_VERSION);
        String line = null;
        while((line = br.readLine()) != null){
            vers = line.trim();
        }
        br.close();
        br = null;
        
        return vers;
    }
    
     public static String join(Collection s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }
    
    
    private static Properties prop;
    
    private static void loadProperties(String str){
        logger.traceEntry("loadProperties");
        prop = new Properties();
        FileInputStream fis = null;
        try{
            logger.info("loadProperties: Loading Properties from mailerConfig file....");
            fis = new FileInputStream(new File(str));
            fis.read();
            prop.load(fis);
        }catch(IOException ioex){
            logger.error("loadProperties: File IO Error: " , ioex);            
        }catch(Exception e){
            logger.error("loadProperties: Error: " , e);
        }finally{
            if(null != fis){
                try{
                    fis.close();
                    fis=null;
                }catch(Exception ex){
                    logger.warn("loadProperties: Error: " , ex);
                }
            }
        }
        logger.traceExit("loadProperties");
    }
    
    public static Properties getPropertiesObject(String string){
        loadProperties(string);
        return prop;
    }
    
    public static String[] separateServerPort(String servURL) {
        logger.traceEntry("separateServerPort");
        String[] serverport = new String[2];
        String trimServerName = null;
        String port = null;
        
        //get the serverName part from http://xyz:7809
        if(servURL.indexOf(":") > -1){
            trimServerName = servURL.substring(servURL.indexOf(VMSStaticParams.HTTP) + 8, servURL.lastIndexOf(":"));
            trimServerName = servURL.substring(servURL.indexOf(VMSStaticParams.HTTP.toUpperCase()) + 8, servURL.lastIndexOf(":"));
            //get the port number
            port = servURL.substring(servURL.lastIndexOf(":") + 1);
            logger.info("separateServerPort: TRIMMED SERVERNAME/PORT : " + trimServerName + " : " +  port);
        }else if (servURL.startsWith("\\")){
            //only store \\bigxx\rootxx part as server, port will be caseName
            port = servURL.substring(servURL.lastIndexOf("\\") + 1);
            trimServerName = servURL.substring(0, servURL.lastIndexOf("\\"));
            logger.info("separateServerPort: TRIMMED SERVERNAME/PORT : " + trimServerName + " : " +  port);
        }else{
            trimServerName = servURL;
            logger.info("separateServerPort: TRIMMED SERVERNAME: " + trimServerName);
        }
        
        serverport[0] = trimServerName;
        serverport[1] = port;
        logger.traceExit("separateServerPort");
        return serverport;
    }
    
    //this fn will return an array with 1st element - caseServGid, 2nd element- serverGid
    public static int[] getMiscGIDs(int[] compoFlag, String domain, VMSMyDAO insMyDAO, String trimServerName) throws VMSDAOException {
        logger.traceEntry("getMiscGIDs");
        int[] retGids = {0, 0};
        int serverGID = 0;
        int caseServGID = 0;
        
        if((compoFlag[0] == 0) && (compoFlag[1] > 0) && (compoFlag[2] == 0)){
            //if server and caseserver mapping are not present
            logger.info("getMiscGIDs: CASE1 component exists : TrimServerName" + trimServerName);
            //insert server
            serverGID = insMyDAO.insServer(trimServerName, domain);
            logger.info("getMiscGIDs: SERVER INFO INSERTED GID: " + serverGID);
            //insert caseserver
            caseServGID = insMyDAO.insCaseServer(compoFlag[3], serverGID);
            logger.debug("getMiscGIDs COMPINS1: Server: " + trimServerName + " CASE: " + compoFlag[3] );
        }else if((compoFlag[0] > 0) && (compoFlag[1] > 0) && (compoFlag[2] == 0)){
            //insert caseserver
            logger.info("getMiscGIDs: CASE2");
            caseServGID = insMyDAO.insCaseServer(compoFlag[3], compoFlag[0]);
            logger.debug("getMiscGIDs: COMPINS2: Server: " + trimServerName + " CASE: " + compoFlag[3] + "CASESERVGID: " + caseServGID);
        }else if((compoFlag[0] > 0) && (compoFlag[1] > 0) && (compoFlag[2] > 0)){
            //all 3 present, do nothing
            logger.info("getMiscGIDs: CASE3");
            logger.debug("getMiscGIDs: COMPINS3: Server: " + trimServerName + " CASE: " + compoFlag[3] );
            caseServGID = compoFlag[2];
        }
        
        retGids[0] = caseServGID;
        retGids[1] = serverGID;
        
        logger.traceExit("getMiscGIDs");
        return retGids;
    }
    
    
    
}
