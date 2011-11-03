/*
 * VMSMSDAO.java
 *
 * Created on November 27, 2007, 4:49 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.dao;


import com.stratify.apps.vms.common.VMSCommonUtils;
import com.stratify.apps.vms.common.VMSParseHTML;
import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import com.stratify.apps.vms.common.vos.VMSComponent;
import com.stratify.apps.vms.dao.common.*;
import com.stratify.apps.vms.dao.common.VMSDAOUtils;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.stratify.common.logging.Logger;

/**
 *
 * @author ravikishores
 */
public class VMSMSDAO {
    static Logger logger = Logger.getLogger(VMSMSDAO.class.getName());
    Connection con = null;
    /**
     * Creates a new instance of VMSMSDAO
     */
    public VMSMSDAO(Connection con1) {
        this.con = con1;
    }
   /*
    *Given the BE DB server name, gathers all BE component info and returns a list
    *of VMSComponent s
    */
    public ArrayList getComponents(String serverName, String caseName) {
        ArrayList temp = new ArrayList();
        PreparedStatement pstmtGetComponents = null;
        
        PreparedStatement pstmtGetDTSearch = null;
        PreparedStatement pstmtGetCaseShares = null;
        ResultSet rsGetComponents = null;
        
        ResultSet rsGetDTSearch = null;
        ResultSet rsGetCaseShares = null;
        String qryCaseShare = null;
        VMSComponent bedb = null;
        ArrayList becaseshares = null;
        ArrayList miscservers = null;
        ArrayList masterservers = null;
        VMSComponent searchserver;
        int major = 0;
        
        //get BE DB info
        try{
            bedb = getBEDBServer(serverName);
            temp.add(bedb);
        }catch(VMSDAOException vde){
            logger.error("getComponents: Problem getting BE DB server info" , vde);
        }
        
        major = Integer.parseInt(bedb.getVersion().substring(0,1));
        
        //get all BE CaseShares
        try{
            becaseshares = getBECaseShares(major, caseName);
            temp.addAll(becaseshares);
        }catch(VMSDAOException vde){
            logger.error("getComponents: Problem getting BE caseshares info" , vde);
        }
        
        //get Misc servers: Taxonomy server, Extraction Server, DataHub info
        if(major >= 6){
            try{
                miscservers = getMiscServers();
                temp.addAll(miscservers);
            }catch(VMSDAOException vde){
                logger.error("getComponents: Problem getting Miscellaneous BE servers info" , vde);
            }
        }
        
         //if version is >= 8.0, read info from DS_DATAHUB_PREF, get master servers URL and versions
        if(major >= 9){
            try{
                masterservers = getMasterServers();
                temp.addAll(masterservers);
            }catch(VMSDAOException vde){
                logger.error("getComponents: Problem getting Master server info" , vde);
            }
        }
        
        //if version is > 8.0, read info from DS_DATAHUB_PREF, get DTsearch server name
        if(major >= 8){
            try{
                searchserver = getSearchServer();
                temp.add(searchserver);
            }catch(VMSDAOException vde){
                logger.error("getComponents: Problem getting Search server info" , vde);
            }
        }
               
        
        return temp;
    }
    
    public void useDB(String caseNameBE) throws VMSDAOException {
        Statement stmtuseDB = null;
        int resFlag = 0;
        
        try{
            stmtuseDB = con.createStatement();
            resFlag = stmtuseDB.executeUpdate(VMSDAOQueries.USE_DB + caseNameBE);
        }catch(SQLException sqle){
            logger.error("useDB: DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("useDB: General error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            try{
                VMSDAOUtils.cleanUp(null, stmtuseDB);
            }catch(Exception ex){
                logger.error("useDB : Error: " , ex);
            }
        }
    }
    
    public ArrayList getComponetsFE(String serverName) {
        ArrayList temp = new ArrayList();
        PreparedStatement pstmtGetComponents = null;
        PreparedStatement pstmtGetDBserver = null;
        PreparedStatement pstmtGetDTSearch = null;
        PreparedStatement pstmtGetRedactPDF = null;
        ResultSet rsGetComponents = null;
        ResultSet rsGetDBserver = null;
        ResultSet rsGetDTSearch = null;
        ResultSet rsGetRedactPDF = null;
        
        try{
            //for the Kopernik info obtd from DS_SERVER
            //Query GET_BE_COMPONENTS can be used here instead of creating new query
            pstmtGetComponents = con.prepareStatement(VMSDAOQueries.GET_BE_COMPONENTS);
            logger.info("getComponetsFE: Query: " + VMSDAOQueries.GET_BE_COMPONENTS);
            rsGetComponents = pstmtGetComponents.executeQuery();
            while(rsGetComponents.next()){
                String serverType = "";
                String version = "";
                String serverURL = "";
                int active = 0;
                //component type is same as VMSComponent
                VMSComponent fecompo = new VMSComponent();
                
                serverType = rsGetComponents.getString("SERVER_TYPE");
                version = rsGetComponents.getString("VERSION");
                serverURL = rsGetComponents.getString("SERVER_URL");
                active = rsGetComponents.getInt("ACTIVE");
                
                
                fecompo.setServerType(serverType);
                fecompo.setVersion(version);
                fecompo.setServerURL(serverURL);
                fecompo.setIsActive(active);
                
                temp.add(fecompo);
            }
            
            //for FE DB server info obtd from DS_DB_PKG_BUILD
            //for DS_DB_PKG_BUILD info
            String serverType = "DatabaseServer_FE";
            String major = "";
            String minor = "";
            String build_number = "";
            String patch_number = "";
            String schema_number = "";
            //query is same for BE and FE DB
            pstmtGetDBserver = con.prepareStatement(VMSDAOQueries.GET_DB_INFO);
            logger.info("getComponetsFE: Query: " + VMSDAOQueries.GET_DB_INFO);
            rsGetDBserver = pstmtGetDBserver.executeQuery();
            if(rsGetDBserver.next()){
                
                VMSComponent becompo = new VMSComponent();
                
                major = "" + rsGetDBserver.getInt("MAJOR");
                minor = "" + rsGetDBserver.getInt("MINOR");
                build_number = "" + rsGetDBserver.getInt("BUILD_NUMBER");
                patch_number = "" + rsGetDBserver.getInt("PATCH_NUMBER");
                schema_number = "" + rsGetDBserver.getInt("SCHEMA_NUMBER");
                
                
                becompo.setServerType(serverType);
                becompo.setVersion(major + "." + minor + "." + build_number +"." + patch_number );
                becompo.setServerURL("http://" + serverName + ":1433");
                becompo.setIsActive(1);
                
                temp.add(becompo);                
            }
            

            
            
        }catch(SQLException sqle){
            logger.error("getComponetsFE: DB related error: " , sqle);            
        }catch(Exception ex){
            logger.error("getComponetsFE: DB related error: " , ex);
        }finally{
            VMSDAOUtils.freeUp(rsGetRedactPDF, pstmtGetRedactPDF);
            VMSDAOUtils.freeUp(rsGetDBserver, pstmtGetDBserver);
            VMSDAOUtils.freeUp(rsGetComponents, pstmtGetComponents);
        }
        return temp;
    }
    
    public int getHierarchy() throws VMSDAOException{
        logger.traceEntry("getHierarchy");
        PreparedStatement pstmtGetHierarchy = null;
        ResultSet rsGetHierarchy = null;
        int retVal = 0;
        
        try{
            pstmtGetHierarchy = con.prepareStatement(VMSDAOQueries.GET_HIERARCHY);
            rsGetHierarchy = pstmtGetHierarchy.executeQuery();
            logger.info("getHierarchy: Getting BE Hierarchy info: " + VMSDAOQueries.GET_HIERARCHY);
            
            while(rsGetHierarchy.next()){
                retVal = rsGetHierarchy.getInt(1);
            }
        }catch(SQLException sqle){
            logger.error("getHierarchy: DB related error: " + sqle.getMessage());
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error getting Hierarchy GID", VMSStaticParams.NA);
        }catch(Exception ex){
            logger.error("getHierarchy: Error: " + ex.getMessage());
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetHierarchy, pstmtGetHierarchy);
        }
        logger.traceExit("getHierarchy");
        return retVal;
    }
    
    private VMSComponent getBEDBServer(String serverName) throws VMSDAOException{
        logger.traceEntry("getBEDBServer");
        PreparedStatement pstmtGetDBserver = null;
        ResultSet rsGetDBserver = null;
        String serverType = VMSStaticParams.BEDB;
        String major = null;
        String minor = null;
        String build_number = null;
        String patch_number = null;
        String schema_number = null;
        VMSComponent becomponent = new VMSComponent();
        try{
            pstmtGetDBserver = con.prepareStatement(VMSDAOQueries.GET_DB_INFO);
            logger.info("getBEDBServer: " + VMSDAOQueries.GET_DB_INFO);
            rsGetDBserver = pstmtGetDBserver.executeQuery();
            if(rsGetDBserver.next()){
                major = "" + rsGetDBserver.getInt("MAJOR");
                minor = "" + rsGetDBserver.getInt("MINOR");
                build_number = "" + rsGetDBserver.getInt("BUILD_NUMBER");
                patch_number = "" + rsGetDBserver.getInt("PATCH_NUMBER");
                schema_number = "" + rsGetDBserver.getInt("SCHEMA_NUMBER");
                
                
                becomponent.setServerType(serverType);
                becomponent.setVersion(major + "." + minor + "." + build_number +"." + patch_number );
                becomponent.setServerURL(VMSStaticParams.HTTP + serverName + ":" + VMSStaticParams.MSSQL_PORT);
                becomponent.setIsActive(1);
            }
        }catch(SQLException sqle){
            logger.error("getBEDBServer : SQL related error: " + sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error getting BE DB server info for case", serverName);            
        }catch(Exception ex){
            logger.error("getBEDBServer :  error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetDBserver, pstmtGetDBserver);
        }
        logger.traceExit("getBEDBServer");
        return becomponent;
    }
    
    private ArrayList getBECaseShares(int major, String caseName) throws VMSDAOException{
        logger.traceEntry("getBECaseShares");
        ArrayList tempcaseshares = new ArrayList();
        String qryCaseShare = null;
        PreparedStatement pstmtGetCaseShares = null;
        ResultSet rsGetCaseShares = null;
        
        if(major >= 9){
            qryCaseShare = VMSDAOQueries.GET_BE_CASESHARES_9_ON;
        }else if(major > 6 && major <= 8){
            qryCaseShare = VMSDAOQueries.GET_BE_CASESHARES_6_ON;
        }else{
            qryCaseShare = VMSDAOQueries.GET_BE_CASESHARES_6_OFF;
        }
        
        logger.info("getBECaseShares: Case Version: " + major);
        try{
            pstmtGetCaseShares = con.prepareStatement(qryCaseShare);
            logger.info("getBECaseShares: Getting BE caseshare info using query: " + qryCaseShare);
            rsGetCaseShares = pstmtGetCaseShares.executeQuery();            
            
            while(rsGetCaseShares.next()){
                String servType = null;
                String version = null;
                String serverURL = null;
                int primary = 0;
                int indexOfLastSlash = 0;
                
                VMSComponent beCaseShare = new VMSComponent();
                
                serverURL = rsGetCaseShares.getString("CASESHARE_PATH").trim();
                
                //try to remove ending \ if any
                if(serverURL.endsWith("\\")){
                    indexOfLastSlash = serverURL.lastIndexOf("\\");
                    serverURL = serverURL.substring(0, indexOfLastSlash);
                }
                
                primary = rsGetCaseShares.getInt("IS_PRIMARY");
                if(primary == 0){
                    servType = VMSStaticParams.CASESHARE_SECONDARY_BE;
                }else{
                    servType = VMSStaticParams.CASESHARE_PRIMARY_BE;
                }
                
                //read the version info from caseshare/scripts/common/version.txt
                try{
                    version = VMSCommonUtils.readVersion(serverURL);
                }catch(FileNotFoundException fe){
                    logger.error("getBECaseShares: File related problem " + fe.getMessage());
                    new VMSSysException().logDeviation(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to read caseshare version info", caseName);
                }
                
                logger.info("getBECaseShares: BE CaseShare Version info obtained: " + version);
                //if version could not be read, set it to all 0
                if(null == version){
                    version = "0.0.0.0";
                }
                
                beCaseShare.setServerType(servType);
                beCaseShare.setServerURL(serverURL);
                beCaseShare.setVersion(version);
                
                tempcaseshares.add(beCaseShare);
                logger.info("getBECaseShares: BE CASE SHARE info added");
                
            }
        }catch(SQLException sqle){
            logger.error("getBECaseShares : SQL related error: " + sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to get BE caseshare info for case" , caseName);
        }catch(Exception ex){
            logger.error("getBECaseShares :  error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetCaseShares, pstmtGetCaseShares);
        }
        logger.traceExit("getBECaseShares");
        return tempcaseshares;
    }
    
    private ArrayList getMiscServers() throws VMSDAOException {
        logger.traceEntry("getMiscServers");
        ArrayList tempServers = new ArrayList();
        PreparedStatement pstmtGetComponents = null;
        ResultSet rsGetComponents = null;
        String servType ;
        String version;
        String serverURL;
        int active;
        
        //get BE components like ExtractionServer, TaxonomyServer, DataHub
        try{
            pstmtGetComponents = con.prepareStatement(VMSDAOQueries.GET_BE_COMPONENTS);
            logger.info("getMiscServers: Trying to fetch TS, ES, DH info using query: " + VMSDAOQueries.GET_BE_COMPONENTS );
            rsGetComponents = pstmtGetComponents.executeQuery();
            
            while(rsGetComponents.next()){
                servType = null;
                version = null;
                serverURL = null;
                active = 0;
                VMSComponent becomponent = new VMSComponent();
                
                servType = rsGetComponents.getString("SERVER_TYPE");
                version = rsGetComponents.getString("VERSION");
                serverURL = rsGetComponents.getString("SERVER_URL");
                active = rsGetComponents.getInt("ACTIVE");
                
                if(servType.equalsIgnoreCase(VMSStaticParams.SEARCH_SERVER_ALIAS)){
                    servType = VMSStaticParams.SEARCH_SERVER_BE;
                }
                
                becomponent.setServerType(servType);
                becomponent.setVersion(version);
                becomponent.setServerURL(serverURL);
                becomponent.setIsActive(active);
                
                tempServers.add(becomponent);
                logger.info("getMiscServers: Misc BE components info added");
            }
        }catch(SQLException sqle){
            logger.error("getMiscServers : SQL related error: " + sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to get Misc server info for case: " , VMSStaticParams.NA);
        }catch(Exception ex){
            logger.error("getMiscServers :  error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetComponents, pstmtGetComponents);
        }
        logger.traceExit("getMiscServers");
        return tempServers;
    }
    
    private VMSComponent getSearchServer() throws VMSDAOException {
        logger.traceEntry("getSearchServer");
        PreparedStatement pstmtGetDTSearch = null;
        ResultSet rsGetDTSearch = null;
        VMSComponent dtSearch;
        String servURL = null;
        String servVersion = null;
        String serverDT = null;
        String serverDTMod = null;
        String numPart = null;
        String searchKey = null;
        String serverType = VMSStaticParams.SEARCH_SERVER_BE;
        //read the version from ServerDiagnostic.jsp page and add it as BE compo
        //lds/ftSearchServer
        try{
            pstmtGetDTSearch = con.prepareStatement(VMSDAOQueries.GET_BE_DTSEARCH);
            logger.info("getSearchServer: Trying to get DTSearch Server info using query: " + VMSDAOQueries.GET_BE_DTSEARCH );
            rsGetDTSearch = pstmtGetDTSearch.executeQuery();
                                   
            dtSearch = new VMSComponent();
            
            if(rsGetDTSearch.next()){
                servURL = rsGetDTSearch.getString("VALUE").trim();
            }               
            
            //logic revised on May 21, 2008
            servVersion = new VMSParseHTML(servURL, VMSStaticParams.SEARCH_SERVER_ALIAS).getText().trim();
            
            logger.info("getSearchServer: DT search server version obtd: " + servVersion);
            
        }catch(SQLException sqle){
            logger.error("getSearchServer : SQL related error: " + sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error getting SS info", VMSStaticParams.NA);
        }catch(Exception ex){
            logger.error("getSearchServer :  error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetDTSearch, pstmtGetDTSearch);
        }
        
        //set the component values
        dtSearch.setIsActive(1);
        dtSearch.setServerType(serverType);
        dtSearch.setVersion(servVersion);
        dtSearch.setServerURL(servURL);
        
        logger.traceExit("getSearchServer");
        return dtSearch;
    }

    private ArrayList getMasterServers() throws VMSDAOException{
         logger.traceEntry("getMasterServers");
        ArrayList tempServers = new ArrayList();
        PreparedStatement pstmtGetComponents = null;
        ResultSet rsGetComponents = null;
        String servType ;
        String version;
        String serverURL;
        String serverName;
        int active;
        
        //get BE components like Master servers: DocProcessor, Extraction, Clustering
        try{
            pstmtGetComponents = con.prepareStatement(VMSDAOQueries.GET_MASTER_COMPONENTS);
            logger.info("getMasterServers: Trying to fetch Master Server info using query: " + VMSDAOQueries.GET_MASTER_COMPONENTS );
            rsGetComponents = pstmtGetComponents.executeQuery();
            
            while(rsGetComponents.next()){
                servType = null;
                version = null;
                serverURL = null;
                serverName = null;
                active = 0;
                VMSComponent becomponent = new VMSComponent();
                
                serverName = rsGetComponents.getString("SERVERCATEGORY");
                serverURL = rsGetComponents.getString("URL");
                                                
                //determine serverType based on ServerName                
                if(serverName.equalsIgnoreCase(VMSStaticParams.MASTER_EXTRACTION)){
                    servType = VMSStaticParams.VMS_MASTER_EXTRACTION;
                }else if(serverName.equalsIgnoreCase(VMSStaticParams.MASTER_DOCPROCESSOR)){
                    servType = VMSStaticParams.VMS_MASTER_DOCPROCESSOR;
                }else if(serverName.equalsIgnoreCase(VMSStaticParams.MASTER_CONCEPT_ORGANIZATION)){
                    servType = VMSStaticParams.VMS_MASTER_CONCEPT_ORGANIZATION;
                }
               
                //TODO: implement logic to get version info for masters - not yet finalized
                //TODO: till then assign version to be 0.0.0.0
                //version =  new VMSParseHTML(serverURL).getText().trim(); 
                version = "0.0.0.0";
                
                becomponent.setServerType(servType);
                becomponent.setVersion(version);
                becomponent.setServerURL(serverURL);
                becomponent.setIsActive(1);
                
                tempServers.add(becomponent);
                logger.info("getMasterServers: Master Server BE components info added");
            }
        }catch(SQLException sqle){
            logger.error("getMasterServers : SQL related error: " + sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to get Master server information", VMSStaticParams.NA);
        }catch(Exception ex){
            logger.error("getMasterServers :  error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetComponents, pstmtGetComponents);
        }
        logger.traceExit("getMasterServers");
        return tempServers;
    }
}
