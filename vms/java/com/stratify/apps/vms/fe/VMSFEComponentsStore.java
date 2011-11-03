/*
 * VMSFEComponentsStore.java
 *
 * Created on December 27, 2007, 3:03 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.fe;

import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import com.stratify.apps.vms.common.vos.VMSComponent;
import com.stratify.apps.vms.dao.VMSConnectionFactory;
import com.stratify.apps.vms.dao.common.VMSDAOQueries;
import com.stratify.apps.vms.dao.common.VMSDAOUtils;
import com.stratify.apps.vms.common.VMSParseHTML;
import com.stratify.apps.vms.common.VMSReadRegistry;
import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.common.logging.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.stratify.datahub.common.util.PyEnv;
import com.stratify.datahub.common.util.PyEnvLocal;

/**
 *
 * @author ravikishores
 */
public class VMSFEComponentsStore {
    static Logger logger = Logger.getLogger(VMSFEComponentsStore.class.getName());
    String caseName = null;
    String serverName = null;
    Connection conMSFE = null;
    
    
    public ArrayList getComponents() {
        ArrayList temp = new ArrayList();
        VMSComponent db = null;
        ArrayList koperniks = null;
        ArrayList caseshares = null;
        VMSComponent searchserver = null;
        VMSComponent annotationserver = null;
        ArrayList pdfredactserver = null;
        ArrayList ldsui = null;
        int major = 0;
        
        //fetch the componets here
        //FE DB Server info
        try{
            db = getDBServer(serverName);
            if(null != db){
                temp.add(db);
                logger.info("getComponents: FE DB info added.");
            }else{
                logger.info("getComponents: FE DB info could not be obtained.");
                System.exit(0);
            }
        }catch(VMSDAOException vde){
            logger.error("getComponents: Not able to get FE DB info: Skipping fetch of other compos: ", vde);
            System.exit(0);
        }
        
        major = Integer.parseInt(db.getVersion().substring(0,1));
        
        //if SLDS version >= 6, try getting Kopernik
        
        if( major >= 6){
            try{
                koperniks = getKoperniks();
                if(null != koperniks){
                    temp.addAll(koperniks);
                    logger.info("getComponets: Kopernik info added.");
                }else{
                    logger.info("getComponets: Kopernik info not obtained.");
                }
            }catch(VMSDAOException vde){
                logger.error("getComponents: Problem fetching Kopernik info : ", vde);
            }
            
        }
        
        
        
        //get the caseshares list
        try{
            caseshares = getCaseShare(major, caseName);
            if(null != caseshares){
                temp.addAll(caseshares);
                logger.info("getComponets: CaseShares(Primary/Secondary) info added.");
            }else{
                logger.info("getComponets: CaseShares(Primary/Secondary) not obtained.");
            }
        }catch(VMSDAOException vde){
            logger.error("getComponents: Problem fetching Caseshare info : ", vde);
        }catch(VMSSysException vse){
            logger.error("getComponents: Problem fetching Caseshare info from DB : ", vse);
        }
        
        
        //SLDS >= 8, get SearchServer
        if(major >= 8){
            try{
                searchserver = getSearchServer();
                if(null != searchserver){
                    temp.add(searchserver);
                    logger.info("getComponets: Search Server info added.");
                }else{
                    logger.info("getComponets: Search Server info not obtained.");
                }
            }catch(VMSDAOException vde){
                logger.error("getComponents: Problem fetching searchserver info : ", vde);
            }
        }
        
        //if SLDS version => 8, try getting Annotation Server
        if(major >= 8){
            try{
                annotationserver = getAnnotationServer();
                if(null != annotationserver){
                    temp.add(annotationserver);
                    logger.info("getComponets: Annotation Server info added.");
                }else{
                    logger.info("getComponets: Annotation Server info not obtained.");
                }
                
            }catch(VMSDAOException vde){
                logger.error("getComponents: Problem fetching annotationserver info : ", vde);
            }
            
        }
        
        try{
            pdfredactserver = getPDFRedactServer();
            if(null != pdfredactserver){
                temp.addAll(pdfredactserver);
                logger.info("getComponets: PDF/Redact info added.");
            }else{
                logger.info("getComponets: PDF/Redact info not obtained.");
            }
        }catch(VMSDAOException vde){
            logger.error("getComponents: Problem fetching PDF/Redact info : ", vde);
        }
        
        //removing this as cases have been moved to citrix server
//            ldsui = getLDSUI(caseName);
//            if(null != ldsui){
//                temp.addAll(ldsui);
//                logger.info("getComponets: LDSUI info added.");
//            }else{
//                logger.info("getComponets: LDSUI info not obtained.");
//            }
        
        return temp;
    }
    
    void clean() throws SQLException {
        if(null != conMSFE){
            conMSFE.close();
            conMSFE = null;
        }
    }
    
    private VMSComponent getDBServer(String serverName) throws VMSDAOException{
        VMSComponent tempdb = null;
        //create DAO
        VMSFEDAO dao = new VMSFEDAO(conMSFE);
        tempdb = dao.getDBServer(serverName);
        return tempdb;
    }
    
    private ArrayList getKoperniks()  throws VMSDAOException{
        ArrayList tempKopernik = null;
        VMSFEDAO dao = new VMSFEDAO(conMSFE);
        tempKopernik = dao.getKopernik();
        return tempKopernik;
    }
    
    private ArrayList getCaseShare(int major, String caseName) throws VMSDAOException, VMSSysException{
        VMSCaseShareList tempcaseshareDB = null;
        VMSFEDAO dao = new VMSFEDAO(conMSFE);
        String primaryCaseShare = null;
        String caseConfigPath = null;
        ArrayList secCaseShares = null;
        
        //get caseshares from DB
        try{
            tempcaseshareDB = dao.getCaseShareDB(major, caseName);
        }catch(VMSDAOException vde){
            logger.error("getCaseShare: Error: ", vde);
            throw new VMSSysException(vde);
        }
        
        //do these only for cases < SLDS 9.0        
        if(major < 9){
            //get  secondary caseShares, parsing caseconfig.xml file from the primary caseshare location
            //first find the primary caseshare
            for(int counterOut = 0; counterOut < tempcaseshareDB.size(); counterOut++){
                VMSComponent caseshares = (VMSComponent)tempcaseshareDB.get(counterOut);
                if( caseshares.getServerType().equalsIgnoreCase(VMSStaticParams.CASESHARE_PRIMARY) ){
                    primaryCaseShare = caseshares.getServerURL();
                    break;
                }
            }
            
            caseConfigPath = primaryCaseShare + VMSStaticParams.CASECONFIG_LOCATION;
            logger.info("getCaseShare:CaseConfig Path " + caseConfigPath);
            
            if( new File(caseConfigPath).exists() ){
                VMSCaseConfigurationParser caseconfigparser = new VMSCaseConfigurationParser(caseName, primaryCaseShare, caseConfigPath);
                try{
                    secCaseShares = caseconfigparser.getSecondaryCaseShares();
                    logger.info("getCaseShare: secCaseShare Size: " + secCaseShares.size());
                    
                    for(int counter = 0; counter < secCaseShares.size(); counter++){
                        VMSComponent secCaseShare = new VMSComponent();
                        
                        secCaseShare.setIsActive(1);
                        secCaseShare.setServerType(VMSStaticParams.CASESHARE_SECONDARY);
                        secCaseShare.setServerURL((String)secCaseShares.get(counter));
                        //secCaseShare.setVersion("0.0.0.0");
                        
                        if(!tempcaseshareDB.containsServer(secCaseShare)){
                            tempcaseshareDB.add(secCaseShare);
                        }
                    }
                }catch(Exception ex){
                    logger.error("getCaseShare: Error: " , ex);
                }
            }else{
                logger.info("getCaseShare: " + caseConfigPath + " does not exist.");
            }
        }else{
            //for 9.0 cases try to get sec caseshares from DS_CASECONFIG_UNIVERSE
            try{
                    secCaseShares = getSecondaryCaseShares9(caseName);
                    logger.info("getCaseShare: secCaseShare Size for 9: " + secCaseShares.size());
                    
                    for(int counter = 0; counter < secCaseShares.size(); counter++){
                        VMSComponent secCaseShare = new VMSComponent();
                        
                        secCaseShare.setIsActive(1);
                        secCaseShare.setServerType(VMSStaticParams.CASESHARE_SECONDARY);
                        secCaseShare.setServerURL((String)secCaseShares.get(counter));
                        //secCaseShare.setVersion("0.0.0.0");
                        
                        if(!tempcaseshareDB.containsServer(secCaseShare)){
                            tempcaseshareDB.add(secCaseShare);
                        }
                    }
                }catch(Exception ex){
                    logger.error("getCaseShare: Error: " , ex);
                }
        }
        return tempcaseshareDB;
    }
    
    private VMSComponent getSearchServer() throws VMSDAOException{
        VMSComponent searchserver = null;
        VMSFEDAO dao = new VMSFEDAO(conMSFE);
        searchserver = dao.getSearchServer();
        return searchserver;
    }
    
    private VMSComponent getAnnotationServer() throws VMSDAOException{
        VMSComponent annotationserver = null;
        VMSFEDAO dao = new VMSFEDAO(conMSFE);
        annotationserver = dao.getAnnotationServer();
        return annotationserver;
    }
    
    private ArrayList getPDFRedactServer() throws VMSDAOException{
        ArrayList pdfredacts = null;
        VMSFEDAO dao = new VMSFEDAO(conMSFE);
        pdfredacts = dao.getPDFRedactServer();
        return pdfredacts;
    }
    
    private ArrayList getLDSUI(String caseName) {
        ArrayList ldsuis = null;
        logger.info("getLDSUI: Entry Server Location is: " + VMSStaticParams.ENTRY_SERVER_FOLDER);
        
        try{
            //now getting LDSUI version info
            //first get TS machine list for this case -- parsing entry server xml
            ldsuis = new ArrayList();
            VMSConfigurationParser parser = new VMSConfigurationParser(VMSStaticParams.ENTRY_SERVER_FOLDER + "\\" + caseName.toUpperCase() + ".xml");
            ArrayList tsMachines = null;
            tsMachines = parser.getTS();
            HashMap ipmappings = new HashMap();
            String tsmachine;
            
            for(int m = 0; m < tsMachines.size(); m++){
                try{
                    tsmachine = null;
                    tsmachine = (String)tsMachines.get(m);
                    //08 Feb 2008 - commented to take care of IP addresses that can be present as TS name
                    
                    if((tsmachine.toLowerCase().startsWith("ts")) || (tsmachine.indexOf("ts") > -1)) {
                        //if first part starts with 'ts'
                        
                    }else{
                        //if not
                        //resolve the external IP address to ts machine name or internal IP address
                        ipmappings = readMapFile(System.getProperty(PyEnv.DATAHUB_ROOT_KEY) + VMSStaticParams.MAPPING_FILE);
                        tsmachine = (String) ipmappings.get(tsmachine);
                    }
                    
                    tsmachine = tsmachine.substring(0, tsmachine.indexOf(".") );
                    
                    logger.info("getLDSUI: TS MACHINE" + tsmachine);
                    
                    String servURL = null;
                    String servName = null;
                    String servVersion = null;
                    String serverType = VMSStaticParams.LDSUI;
                    String subKey = VMSStaticParams.LDSUI_STRING;
                    
                    VMSComponent ldsui = new VMSComponent();
                    
                    //now read registry of terminal server for getting version
                    VMSReadRegistry registry = new VMSReadRegistry(tsmachine);
                    
                    logger.info("getLDSUI: Registry Initialized");
                    
                    servVersion = registry.searchValue(subKey);
                    
                    logger.info("getLDSUI: LDSUI version obtd: " + servVersion);
                    
                    //set serverURL as TS machine url
                    servURL = tsmachine;
                    
                    //set the component values
                    ldsui.setIsActive(1);
                    ldsui.setServerType(serverType);
                    ldsui.setVersion(servVersion);
                    ldsui.setServerURL(servURL);
                    
                    ldsuis.add(ldsui);
                }catch(Exception ex){
                    logger.error("getLDSUI: error: " , ex);
                    continue;
                }
            }
        }catch(Exception ex){
            logger.error("getLDSUI: error: " , ex);
        }
        return ldsuis;
    }
    
    /**
     * Creates a new instance of VMSFEComponentsStore
     */
    public VMSFEComponentsStore(String caseName, String serverName) throws Exception {
        this.caseName = caseName;
        this.serverName = serverName;
        this.conMSFE = VMSConnectionFactory.getMSConnection(serverName, caseName, VMSStaticParams.FEALAIS);
    }
    
    public HashMap readMapFile(String fileLocation) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileLocation)));
        HashMap retMap = new HashMap();
        String line = null;
        String[] parts = null;
        while ((line = br.readLine()) != null) {
            logger.info("readMapFile: LINE READ: " + line);
            if (line.trim().length() > 0) {
                parts = line.split("=");
                retMap.put(parts[0].trim(), parts[1].trim());
            }
            
        }
        
        br.close();
        br = null;
        
        return retMap;
    }   

    private ArrayList getSecondaryCaseShares9(String caseName) throws VMSDAOException{
        ArrayList secCaseShares = null;
        VMSFEDAO dao = new VMSFEDAO(conMSFE);
        secCaseShares = dao.getSecondaryCaseShares(caseName);
        return secCaseShares;
    }
    
    
}
