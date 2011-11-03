/*
 * VMSGatherHelper.java
 *
 * Created on November 19, 2007, 4:25 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.client.helper;

import com.stratify.apps.vms.common.VMSCommonUtils;
import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import com.stratify.apps.vms.common.vos.VMSCaseServerCombo;
import com.stratify.apps.vms.common.vos.VMSComponent;
import com.stratify.apps.vms.common.vos.VMSCaseServer;
import com.stratify.apps.vms.dao.VMSConnectionFactory;
import com.stratify.apps.vms.dao.VMSDAOFactory;
import com.stratify.apps.vms.dao.VMSMSDAO;
import com.stratify.apps.vms.dao.VMSMSDAOFactory;
import com.stratify.apps.vms.dao.VMSMyDAO;
import com.stratify.apps.vms.client.VMSFEComponentClient;
import com.stratify.apps.vms.dao.VMSStatXnManager;
import com.stratify.apps.vms.dao.VMSTransactionManager;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.stratify.common.logging.Logger;


/**
 *
 * @author ravikishores
 */
public class VMSGatherHelper{
    static Logger logger = Logger.getLogger(VMSGatherHelper.class.getName());
    String executiondomain = null;
    /**
     * Creates a new instance of VMSGatherHelper
     */
    public VMSGatherHelper() {
    }
    
    public VMSGatherHelper(String d) {
        this.executiondomain = d;
    }
    /*
     * Given the list of CaseServer components, adds the case, server, caseserver mapping info to
     * VMS tables depending on their existence
     */
    public void synchronizeCaseServers(ArrayList caseServs) throws Exception{
        logger.traceEntry("synchronizeCaseServers");
        Connection conDestDAO = null;
        VMSMyDAO destMyDAO = null;
        String caseName, serverName, domain;
        int resetCount = 0;
        
        try {
            //get connection to dest MySQL DB(VMS MYSQL DB) - param 1 for it
            conDestDAO = VMSConnectionFactory.getMyConnection(1);
            destMyDAO = VMSDAOFactory.getVerMyDAO(conDestDAO);
            
            //TODO: set IS_DELETED flag to true for all cases in this domain
            resetCount = destMyDAO.resetDeleteFlags(executiondomain);
            if(resetCount > 0){
                logger.info("synchronizeCaseServers: Flag reset to 1 for all the cases on domain: " + executiondomain);
            }
            
            
            for (int j =0; j < caseServs.size(); j++){
                VMSCaseServer cases = (VMSCaseServer)caseServs.get(j);
                caseName = null;serverName = null;domain = null;
                
                caseName = cases.getCaseName();
                serverName = cases.getServerName();
                domain = cases.getDomain();
                
                if(executiondomain.equalsIgnoreCase(VMSStaticParams.APAC)){
                    serverName.replaceAll(VMSStaticParams.BESUFFIX_APAC, "");
                    serverName.replaceAll(VMSStaticParams.FESUFFIX_APAC, "");
                }
                
                logger.info("synchronizeCaseServers: CASE, SERVER, DOMAIN: " + caseName + "," + serverName + "," + domain );
                
                try {
                    synchronizeCaseServerMappings(caseName, serverName, domain, destMyDAO);
                } catch (VMSDAOException ex) {
                    logger.error("synchronizeCaseServers:Error in synchronising case servers:" + ex);
                    continue;
                }
            }
        } catch (Exception ex) {
            logger.error("synchronizeCaseServers: Error : " + ex.getMessage());
            
        } finally {
            try{
                if (null != conDestDAO)
                    VMSConnectionFactory.closeCon(conDestDAO);
            }catch(Exception ex){
                logger.warn("synchronizeCaseServers:Error: " + ex);
            }
        }
        logger.traceExit("synchronizeCaseServers");
        
    }
    /*
     *With components list, caseName, serverName, caseserverName, this fn inserts/updates entries in
     *vms_compo_mapping table
     */
    public void updateInsert(ArrayList components,  VMSMyDAO insMyDAO, String caseName, String serverName, int caseServerGID) {
        logger.traceEntry("updateInsert");
        //take the snapshot of IS_DELETED flag for this case
        //Map to hold snapshot of Is_deleted flags, key - caseservergid_port, value - is_deleted
        HashMap snapshotFlagsInitial = new HashMap();
        int countResetAll = 0;
        
        //TODO - operations in this fn has to be in transactions
        try{
            if(executiondomain.equalsIgnoreCase("")){
                snapshotFlagsInitial = insMyDAO.getSnapShot(caseName, VMSStaticParams.BEALIAS);
                //reset IS_DELETED flags to 1(deleted) for entries of this case
                countResetAll = insMyDAO.resetFlags(caseName, VMSStaticParams.BEALIAS);
            } else if (executiondomain.equalsIgnoreCase(VMSStaticParams.APAC)){
                snapshotFlagsInitial = insMyDAO.getSnapShot(caseName, VMSStaticParams.APACBEALIAS);
                //reset IS_DELETED flags to 1(deleted) for entries of this case
                countResetAll = insMyDAO.resetFlags(caseName, VMSStaticParams.APACBEALIAS);
            }
            logger.info("updateInsert: flag reset for entries: " + countResetAll);
        } catch(VMSDAOException vde){
            logger.error("updateInsert: Error: " , vde);
        }
        
        int compoGID, serverGID, caseServGID, caseGID, compomapGID, countReset;
        int major, minor, build, patch, compoVerGID, actFlag;
        String servType, vers, servURL, trimServerName, port;
        String[]  serverPort;
        
        for(int i = 0 ; i < components.size(); i++){
            compoGID = 0; serverGID = 0; caseServGID = 0; caseGID = 0;
            compomapGID = 0;  countReset = 0;  major = 0;  minor = 0;
            build = 0;  patch = 0;  compoVerGID = 0; actFlag = 0;
            servType = null; vers = null; servURL = null; trimServerName = null;
            port = "0"; serverPort = null;
            int[] miscGID = {0, 0};
            int[] compoFlag = {0,0,0,0};
            
            VMSComponent temp = (VMSComponent)components.get(i);
            
            servType = temp.getServerType().trim();
            vers = temp.getVersion().trim();
            servURL = temp.getServerURL().trim();
            actFlag = temp.getIsActive();
            
            logger.info("updateInsert: " + servURL);
            
            //trim off the trailing slash from server url
            if(servURL.endsWith("/")){
                servURL = servURL.substring(0, servURL.lastIndexOf("/"));
            }
            
            if(executiondomain.equalsIgnoreCase("")){
                servURL = servURL.replaceAll(VMSStaticParams.BESUFFIX.toUpperCase(), "");
                servURL = servURL.replaceAll(VMSStaticParams.BESUFFIX, "");
            }else if (executiondomain.equalsIgnoreCase(VMSStaticParams.APAC)){
                servURL = servURL.replaceAll(VMSStaticParams.BESUFFIX_APAC.toUpperCase(), "");
                servURL = servURL.replaceAll(VMSStaticParams.BESUFFIX_APAC, "");
            }
            
            //function that returns an array with serverName and port
            serverPort = VMSCommonUtils.separateServerPort(servURL);
            trimServerName = serverPort[0];
            if(null != serverPort[1]){
                port = serverPort[1];
            }
            
            //TODO: externalise to method for getting caseserver GID
            
            //obtain the flags for this case, server
            try{
                if(executiondomain.equalsIgnoreCase("")){
                    compoFlag = insMyDAO.getServCompoFlags(caseName, trimServerName, servType, VMSStaticParams.BEALIAS);
                    //compoFlag[3] that holds caseServerGID will always be populated at this stage
                    logger.info("updateInsert: COMPOFLAG STATUS*******************");
                    logger.debug("updateInsert: COMPOFLAG: ServerGID:  " + compoFlag[0] + "> CompoGID: " + compoFlag[1] + "> CaseServerGID: " + compoFlag[2] + "> CASEGID: " + compoFlag[3] );
                    
                    miscGID = VMSCommonUtils.getMiscGIDs(compoFlag, VMSStaticParams.BEALIAS, insMyDAO, trimServerName);
                } else if(executiondomain.equalsIgnoreCase(VMSStaticParams.APAC)){
                    compoFlag = insMyDAO.getServCompoFlags(caseName, trimServerName, servType, VMSStaticParams.APACBEALIAS);
                    //compoFlag[3] that holds caseServerGID will always be populated at this stage
                    logger.info("updateInsert: COMPOFLAG STATUS*******************");
                    logger.debug("updateInsert: COMPOFLAG: ServerGID:  " + compoFlag[0] + "> CompoGID: " + compoFlag[1] + "> CaseServerGID: " + compoFlag[2] + "> CASEGID: " + compoFlag[3] );
                    
                    miscGID = VMSCommonUtils.getMiscGIDs(compoFlag, VMSStaticParams.APACBEALIAS, insMyDAO, trimServerName);
                }
                caseServGID = miscGID[0];
                if(0 != miscGID[1]){
                    serverGID = miscGID[1];
                }
            }catch(VMSDAOException vde){
                logger.info("updateInsert: Error: " , vde);
            } catch(Exception ex){
                logger.info("updateInsert: Error: " , ex);
            }
            
            logger.info("updateInsert: VERSION: " + vers);
            
            compoGID = compoFlag[1];
            
            //synchronize Component Mapping
            try{
                synchronizeComponentMappings(vers, compoGID, insMyDAO, caseServGID, port, snapshotFlagsInitial);
            }catch(VMSDAOException vde){
                logger.error("updateInsert: Error: " , vde);
            }
            
        }
        
        logger.debug("updateInsert: snapshotFlagsInitial SIZE: " + snapshotFlagsInitial.size());
        //for remaining stray entries in HashMap, try to update MOD_AT dates
        try{
            updateModifiedDates(snapshotFlagsInitial, insMyDAO);
        } catch(VMSSysException vde){
            logger.error("updateInsert: Error: " , vde);
        }
    }
    
    //function for updation/insertion of FE Components
    
    public void updateInsertFE(ArrayList componentsFE, VMSMyDAO insMyDAOFE, String caseNameFE, String serverNameFE, int caseServGIDFE) {
        logger.traceEntry("updateInsertFE");
        //take the snapshot of IS_DELETED flag for this case
        HashMap snapshotFlagsInitial = new HashMap();
        int countResetAll = 0;
        
        try{
            if(executiondomain.equalsIgnoreCase("")){
                snapshotFlagsInitial = insMyDAOFE.getSnapShot(caseNameFE, VMSStaticParams.FEALAIS);
                //reset IS_DELETED flags to 1(deleted) for entries of this case
                countResetAll = insMyDAOFE.resetFlags(caseNameFE, VMSStaticParams.FEALAIS);
            }else if (executiondomain.equalsIgnoreCase(VMSStaticParams.APAC)){
                snapshotFlagsInitial = insMyDAOFE.getSnapShot(caseNameFE, VMSStaticParams.APACFEALAIS);
                //reset IS_DELETED flags to 1(deleted) for entries of this case
                countResetAll = insMyDAOFE.resetFlags(caseNameFE, VMSStaticParams.APACFEALAIS);
            }
            logger.info("updateInsertFE: flag reset for entries: " + countResetAll);
        }catch(Exception ex){
            logger.error("updateInsertFE: Error: " , ex);
        }
        
        int compoGID, serverGID, caseServGID, caseGID, compomapGID, actFlag;
        int major, minor, build, patch, compoVerGID;
        String servType, vers, servURL, trimServerName, port;
        String[] verParts, serverPort;
        int[] miscGIDs;
        
        for(int i = 0 ; i < componentsFE.size(); i++){
            compoGID = 0; serverGID = 0; caseServGID = 0;
            caseGID = 0; compomapGID = 0; actFlag = 0; major = 0;
            minor = 0; build = 0; patch = 0; compoVerGID = 0;
            servType = null; vers = null; servURL = null; trimServerName = null;
            port = "0"; verParts = null; serverPort = null; miscGIDs = null;
            int[] compoFlag = {0,0,0,0};
            
            VMSComponent temp = (VMSComponent)componentsFE.get(i);
            
            servType = temp.getServerType().trim();
            vers = temp.getVersion().trim();
            servURL = temp.getServerURL().trim();
            actFlag = temp.getIsActive();
            
            logger.info("updateInsertFE: INSUPDATEFE: " + servURL);
            
            //trim off the trailing slash if any
            if(servURL.endsWith("/")){
                servURL = servURL.substring(0, servURL.lastIndexOf("/"));
            }
            
            //remove domain suffix
            if(executiondomain.equalsIgnoreCase("")){
                servURL = servURL.replaceAll(VMSStaticParams.FESUFFIX.toUpperCase(), "");
                servURL = servURL.replaceAll(VMSStaticParams.FESUFFIX, "");
            }else if(executiondomain.equalsIgnoreCase(VMSStaticParams.APAC)){
                servURL = servURL.replaceAll(VMSStaticParams.FESUFFIX_APAC.toUpperCase(), "");
                servURL = servURL.replaceAll(VMSStaticParams.FESUFFIX_APAC, "");
            }
            
            //get server, port from url
            serverPort = VMSCommonUtils.separateServerPort(servURL);
            trimServerName = serverPort[0];
            if(null != serverPort[1]){
                port = serverPort[1];
            }
            
            try{
                if(executiondomain.equalsIgnoreCase("")){
                    compoFlag = insMyDAOFE.getServCompoFlags(caseNameFE, trimServerName, servType, VMSStaticParams.FEALAIS);
                    //compoFlag[3] will always populated at this stage
                    
                    logger.debug("updateInsertFE: COMPOFLAG: ServerGID:  " + compoFlag[0] + "> CompoGID: " + compoFlag[1] + "> CaseServerGID: " + compoFlag[2] + "> CASEGID: " + compoFlag[3] );
                    
                    miscGIDs = VMSCommonUtils.getMiscGIDs(compoFlag, VMSStaticParams.FEALAIS, insMyDAOFE, trimServerName);
                }else if(executiondomain.equalsIgnoreCase(VMSStaticParams.APAC)){
                    compoFlag = insMyDAOFE.getServCompoFlags(caseNameFE, trimServerName, servType, VMSStaticParams.APACFEALAIS);
                    //compoFlag[3] will always populated at this stage
                    
                    logger.debug("updateInsertFE: COMPOFLAG: ServerGID:  " + compoFlag[0] + "> CompoGID: " + compoFlag[1] + "> CaseServerGID: " + compoFlag[2] + "> CASEGID: " + compoFlag[3] );
                    
                    miscGIDs = VMSCommonUtils.getMiscGIDs(compoFlag, VMSStaticParams.APACFEALAIS, insMyDAOFE, trimServerName);
                    
                    //I am here
                }
                caseServGID = miscGIDs[0];
                if(0 != miscGIDs[1]){
                    serverGID = miscGIDs[1];
                }
            }catch(Exception ex){
                logger.info("updateInsertFE: Error: " , ex);
            }
            
            logger.info("updateInsertFE: VERSFE: " + vers);
            
            compoGID = compoFlag[1];
            
            try{
                synchronizeComponentMappings(vers, compoGID, insMyDAOFE, caseServGID, port, snapshotFlagsInitial);
            }catch(VMSDAOException vde){
                logger.error("updateInsertFE: Error: " , vde);
            }
            
        }
        
        //for remaining stray entries in HashMap, try to update MOD_AT dates
        try{
            updateModifiedDates(snapshotFlagsInitial, insMyDAOFE);
        }catch(VMSSysException vde){
            logger.error("updateInsertFE: Error: " , vde);
        }
        logger.traceExit("updateInsertFE");
    }
    
    public int updateInsertHierarchy(int hierarchyGID, String caseName, VMSMyDAO insMyDAO) throws VMSDAOException {
        int ret = 0;
        ret = insMyDAO.updateHGID(hierarchyGID, caseName);
        return ret;
    }
    
    ArrayList callIncompatibleCompoProc() throws Exception {
        logger.traceEntry("callIncompatibleCompoProc");
        ArrayList ret = null;
        Connection coninsMyDAOFE= null;
        VMSMyDAO insMyDAOFE = null;
        try{
            coninsMyDAOFE = VMSConnectionFactory.getMyConnection(1);
            insMyDAOFE = VMSDAOFactory.getVerMyDAO(coninsMyDAOFE);
            ret = insMyDAOFE.callIncompatibleCompoProc();
        }catch(VMSDAOException dao){
            logger.error("callIncompatibleCompoProc: " + dao.getMessage());
        }finally{
            VMSConnectionFactory.closeCon(coninsMyDAOFE);
        }
        logger.traceExit("callIncompatibleCompoProc");
        return ret;
    }
    
    ArrayList callMutipleInstallProc() throws Exception {
        logger.traceEntry("callMutipleInstallProc");
        ArrayList ret = null;
        Connection coninsMyDAOFE= null;
        VMSMyDAO insMyDAOFE = null;
        try{
            coninsMyDAOFE = VMSConnectionFactory.getMyConnection(1);
            insMyDAOFE = VMSDAOFactory.getVerMyDAO(coninsMyDAOFE);
            ret = insMyDAOFE.callMutipleInstallProc();
        }catch(VMSDAOException dao){
            logger.error("callMutipleInstallProc: " + dao.getMessage());
        }finally{
            VMSConnectionFactory.closeCon(coninsMyDAOFE);
        }
        logger.traceExit("callMutipleInstallProc");
        return ret;
    }
    
    private void synchronizeCaseServerMappings(String caseName, String serverName, String domain, VMSMyDAO destMyDAO) throws VMSDAOException{
        logger.traceEntry("synchronizeCaseServerMappings");
        int caseGid = 0;
        int serverGid = 0 ;
        int caseServerGid = 0;
        int[] existenceFlags = {0,0,0};
        int flagUpdateCount = 0;
        
        //get the existance matrix - returns and Array of three flags
        //indicating case, server and case_server mapping existence
        //1st flag - case existence, 2nd - server existence, 3rd - case_server existence
        
        existenceFlags = destMyDAO.getFlags(caseName, serverName, domain);
        
        //check if case exist
        if((existenceFlags[0] == 0)&& (existenceFlags[1] == 0) && (existenceFlags[2] == 0)) {
            //none exists so insert all
            caseGid = destMyDAO.insCase(caseName);
            serverGid = destMyDAO.insServer(serverName, domain);
            caseServerGid = destMyDAO.insCaseServer(caseGid, serverGid);
            
            logger.info("synchronizeCaseServerMappings : CASE1: casegid, servergid, caseServergid all 3 absent in VMS MySQL DB.");
            logger.info("synchronizeCaseServerMappings : CASE1 executed  " + "CASE INFO: " + caseGid + ">" + caseName + " SERVER: " + serverGid + ">" + serverName);
        }else if((existenceFlags[0] ==0)&&(existenceFlags[1] > 0 )&&(existenceFlags[2] ==0)){
            //server exists so insert case, case-server mapping
            caseGid = destMyDAO.insCase(caseName);
            serverGid = existenceFlags[1];
            caseServerGid = destMyDAO.insCaseServer(caseGid, serverGid);
            
            logger.info("synchronizeCaseServerMappings: CASE2: Servergid exists, case and caseserver mapping absent in MySQL VMS DB.");
            logger.info("synchronizeCaseServerMappings: CASE2 executed: " + "CASE INFO: " + caseGid + ">" + caseName + " SERVER: " + serverGid + ">" + serverName);
        }else if((existenceFlags[0] > 0)&&(existenceFlags[1] == 0)&&(existenceFlags[2] ==0)){
            //case exists so insert server, case-server mapping
            serverGid = destMyDAO.insServer(serverName, domain);
            caseGid = existenceFlags[0];
            //set the IS_DELETED flag to 0 for this case
            flagUpdateCount = destMyDAO.updateFlag(caseGid);
            caseServerGid = destMyDAO.insCaseServer(caseGid, serverGid);
            
            logger.info("synchronizeCaseServerMappings: CASE3: Case exists, Server and CaseServer absent in VMS MySQL DB.");
            logger.info("synchronizeCaseServerMappings: CASE3 executed: " + "CASE INFO: " + caseGid + ">" + caseName + " SERVER: " + serverGid + ">" + serverName + " flagUpdateCount: "  + flagUpdateCount);
        }else if((existenceFlags[0] > 0)&&(existenceFlags[1] > 0)&&(existenceFlags[2] ==0)){
            //case, server entries exist, insert case server mapping entry
            caseGid = existenceFlags[0];
            //set the IS_DELETED flag to 0 for this case
            flagUpdateCount = destMyDAO.updateFlag(caseGid);
            serverGid = existenceFlags[1];
            caseServerGid = destMyDAO.insCaseServer(caseGid, serverGid);
            
            logger.info("synchronizeCaseServerMappings: CASE4: Case and Server exist. Case Server Mapping absent in VMS MySQL DB");
            logger.info("synchronizeCaseServerMappings: CASE4: " + "CASE INFO: " + caseGid + ">" + caseName + " SERVER: " + serverGid + ">" + serverName +  " flagUpdateCount: "  + flagUpdateCount);
        }else if((existenceFlags[0] > 0 )&&(existenceFlags[1] > 0)&&(existenceFlags[2] > 0)){
            //set the IS_DELETED flag to 0 for this case
            flagUpdateCount = destMyDAO.updateFlag(existenceFlags[0]);
            logger.info("synchronizeCaseServerMappings: CASE5: All 3 info present in VMS MySQL DB.");
            logger.info("synchronizeCaseServerMappings: CASE5: " + "CASE INFO: " + caseGid + ">" + caseName + " SERVER: " + serverGid + ">" + serverName +  " flagUpdateCount: "  + flagUpdateCount);
        }
        logger.traceExit("synchronizeCaseServerMappings");
    }
    
    //gathers all the BE/FE case servers and fetches LDS component for each case
    public void processComponents() throws VMSSysException, Exception {
        logger.traceEntry("processComponents");
        ArrayList caseServers = null;
        int caseServGID = 0;
        
        //get the list of BE/FE DB Servers and cases for the specified executiondomain
        caseServers = getVMSCaseServers();
        logger.info("processComponents : DB server list obtained");
        
        Connection conMS; //for connecting to MSSQL servers
        VMSMSDAO msdao = null;
        
        String caseNameBE, caseNameFE, serverNameBE, serverNameFE, domainBE, domainFE;
        int  hierarchyGIDFE, caseserverGIDBE, caseserverGIDFE;
        ArrayList componentsBE, componentsFE;
        VMSCaseServerCombo cs;
        String previousServerBE = null;
        Connection prevconms = null;
        VMSMSDAO prevmsdao = null;
        
        //get the compo list
        for(int i = 0; i < caseServers.size(); i++){
            caseNameBE = null;caseNameFE = null;
            serverNameBE = null;serverNameFE = null;
            domainBE = null;domainFE = null;
            caseserverGIDBE = 0;caseserverGIDFE = 0;
            hierarchyGIDFE = 0;
            conMS = null;
            msdao = null;
            
            cs = (VMSCaseServerCombo)caseServers.get(i);
            caseNameBE = cs.getCaseName();
            serverNameBE = cs.getServerName();
            domainBE = cs.getDomain();
            caseserverGIDBE = cs.getCaseServGID();
            caseNameFE = cs.getCaseNameFE();
            serverNameFE = cs.getServerNameFE();
            domainFE = cs.getDomainFE();
            caseserverGIDFE = cs.getCaseServGIDFE();
            
            logger.info("----------------------------------------------------");
            logger.info("processComponents: caseNameBE :" + caseNameBE);
            logger.info("processComponents: serverNameBE: " + serverNameBE);
            logger.info("processComponents: DomainBE: " + domainBE);
            logger.info("processComponents: CaseServGIDBE: " + caseserverGIDBE);
            
            logger.info("processComponents: caseNameFE :" + caseNameFE);
            logger.info("processComponents: serverNameFE: " + serverNameFE);
            logger.info("processComponents: DomainFE: " + domainFE);
            logger.info("processComponents: CaseServGIDFE: " + caseserverGIDFE);
            
            try{
                //try to get BE components info only if BE casename is present
                //to take care of clone cases etc
                if(null != serverNameBE){
                    //dont create new connections if servers are same
                    logger.info("processComponents: serverNameBE: " + serverNameBE + " previousServerBE: " + previousServerBE);
                    if( serverNameBE.equalsIgnoreCase(previousServerBE) ){
                        msdao = prevmsdao;
                    }else{
                        //take connection to the master db
                        conMS = VMSConnectionFactory.getMSConnection(serverNameBE, VMSStaticParams.MASTER, domainBE);
                        msdao = VMSMSDAOFactory.getVerMSDAO(conMS);
                    }
                    try{
                        processComponentsBE(caseNameBE, serverNameBE, caseserverGIDBE, domainBE, msdao);
                        
                    }catch(VMSSysException vse){
                        logger.error("processComponents: " , vse);
                    }
                    
                }
//RKS done upto here                
                //now process FE components
                if(null != serverNameFE){
                    processComponentsFE(caseNameFE, serverNameFE, caseserverGIDFE, domainFE);
                }
                
            }catch(Exception ex){
                logger.error("processComponents: " + ex);
                throw new VMSSysException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error getting component info for case", caseNameBE);
            }finally{
                try{
                    if( serverNameBE.equalsIgnoreCase(previousServerBE) ){
                        
                    }else{
                        if(null != prevconms){
                            VMSConnectionFactory.closeCon(prevconms);
                        }
                    }
                }  catch(Exception ex)  {
                    logger.error("processComponents: " + ex);
                }
                
            }
            
            previousServerBE = serverNameBE;
            prevmsdao = msdao;
        }
        
        if(null != msdao){
            msdao = null;
        }
        
        
        if(null != prevmsdao){
            prevmsdao = null;
        }
        
        logger.traceExit("processComponents");
        
    }
    
    private void processComponentsFE(String caseNameFE, String serverNameFE, int caseserverGIDFE, String domainFE) {
        logger.traceEntry("processComponentsFE");
        logger.info("-------------------------------------");
        logger.info("processComponentsFE: caseNameFE :" + caseNameFE);
        logger.info("processComponentsFE: serverameFE: " + serverNameFE);
        logger.info("processComponentsFE: caseserverGIDFE: " + caseserverGIDFE );
        logger.info("processComponentsFE: domainFE: " + domainFE );
        
        ArrayList componentsFE = new ArrayList();
        Connection coninsMyDAOFE= null;
        VMSMyDAO insMyDAOFE = null;
        Connection conMS = null;
        VMSMSDAO msdao = null;
        int hierarchyGID = 0;
        
        
        try{
            coninsMyDAOFE = VMSConnectionFactory.getMyConnection(1);
            insMyDAOFE = VMSDAOFactory.getVerMyDAO(coninsMyDAOFE);
            
            // use SOAP client to get from FE Web Service - pass params caseName and serverName
            VMSFEComponentClient client = new VMSFEComponentClient(caseNameFE, serverNameFE);
            componentsFE = client.getComponentObjects();
            
            logger.info("processComponentsFE: FE components obtained via Web Services for case: " + caseNameFE + " serverNameFE: " + serverNameFE );
            
            //check if componentsFE list is not null or empty before doing this
            if((null != componentsFE) && (componentsFE.size() != 0)){
                //update/insert serverNameFErver, component, caseserver mapping
                //updateInsertFE(componentsFE, insMyDAOFE, caseNameFE, serverNameFE, caseserverGIDFE);
                
                VMSTransactionManager xnmanager = new VMSTransactionManager(componentsFE, caseNameFE, serverNameFE, caseserverGIDFE);
                xnmanager.updateInsertFE(executiondomain);
                
                logger.info("processComponentsFE: FE components insertion/updation completed.");
            }else{
                logger.info("processComponentsFE: FE components list is null or empty.");
            }
            
            //fetch HGID from FE DB and do updation here
            conMS = VMSConnectionFactory.getMSConnection(serverNameFE, caseNameFE, domainFE);
            msdao = VMSMSDAOFactory.getVerMSDAO(conMS);
            logger.info("processComponentsFE: SERVER: "+ serverNameFE + "CASE: " + caseNameFE + " DOMAIN: " + domainFE + " CASESERVGID: " + caseserverGIDFE);
            
            //get the HIERARCHY GID
            logger.info("processComponentsFE: Getting Hierarchy GID.....");
            hierarchyGID = msdao.getHierarchy();
            
            if(updateInsertHierarchy(hierarchyGID, caseNameFE, insMyDAOFE) > 0){
                logger.info("processComponentsFE:Hierarchy GID updated " + hierarchyGID)  ;
            }
            
        }catch(Exception ex){
            logger.error("processComponentsFE: Unable to get FE components info via Web Services " +  ex.getMessage())  ;
        } finally{
            try{
                if(null != coninsMyDAOFE){
                    VMSConnectionFactory.closeCon(coninsMyDAOFE);
                }
                if(null != conMS){
                    VMSConnectionFactory.closeCon(conMS);
                }
            }  catch(Exception ex)  {
                logger.error("processComponentsFE: " + ex);
            }
        }
        
        logger.traceExit("processComponentsFE");
    }
    
    private void synchronizeComponentMappings(String vers, int compoGID, VMSMyDAO insMyDAO, int caseServGID, String port, HashMap snapshotFlagsInitial) throws VMSDAOException {
        logger.traceEntry("synchronizeComponentMappings");
        String[] verParts;
        int major, minor, build, patch, compoVerGID;
        major = 0; minor = 0; build = 0; patch = 0; compoVerGID = 0;
        
        //break up the version info into constituents
        verParts = vers.split("[.]");
        
        //TODO:Attempt removing compoGID usage and replace with componentName
        major = Integer.parseInt(verParts[0]);
        minor = Integer.parseInt(verParts[1]);
        build = Integer.parseInt(verParts[2]);
        patch = Integer.parseInt(verParts[3]);
        
        logger.debug("synchronizeComponentMappings: CompoGID: " + compoGID + "major: " + major + "minor: " + minor + "build: " + build + " patch : " + patch);
        //check the existence of compo info in vms_compo_version
        compoVerGID = insMyDAO.existsComponentVersion(compoGID, major, minor, build, patch);
        logger.debug("synchronizeComponentMappings: Existence COMPO VERSION Flag: " + compoVerGID);
        
        //logic revised 27 May 2008
        if(compoVerGID == 0){
            //insert into vms_compo_version table
            compoVerGID = insMyDAO.insVersion(compoGID, major, minor, build, patch);
            logger.debug("synchronizeComponentMappings: Compo Version inserted " + compoVerGID) ;
        }
        
        //check for the existence of caseServerGid, CompoVersionGid, Port combination
        //TODO: Check logic variable usage change
        int compoMapGid = 0;
        compoMapGid = insMyDAO.existsComponentMapping(caseServGID, compoVerGID, port);
        logger.info("synchronizeComponentMappings: compo mapping exists flag: " + compoMapGid);
        
        if(compoMapGid > 0){
            //update is_deleted = 0
            int udtFlag = 0;
            udtFlag = insMyDAO.updateMapping(compoMapGid);
            logger.info("synchronizeComponentMappings: IS_DELETED flag updated: " + udtFlag);
        }else{
            //insert compo_mapping
            compoMapGid = insMyDAO.insCompoMapping(caseServGID, compoVerGID, port);
            logger.info("synchronizeComponentMappings: CompoMapping inserted: " + compoMapGid);
        }
        
        //check if MOD_AT date needs to be updated
        //get the initial flag
        int initialFlag = 0;
        
        if(snapshotFlagsInitial.containsKey(caseServGID + ":" + port)){
            initialFlag = ( (Integer)snapshotFlagsInitial.get(caseServGID + ":" + port)).intValue();
            logger.info("synchronizeComponentMappings: initialFlag for caseServGID: " + caseServGID + " port: " + port + " initialFlag: " + initialFlag);
            
            //take the XOR of these flags, if 1, update MOD_AT and then remove entry from HashMap
            //if initially the case server + port was inactive
            //then update mod at since now its active
            if(initialFlag == 1){
                //update MOD_AT date
                int udtResult = insMyDAO.updateModDate(compoMapGid);
                if(udtResult > 0){
                    logger.info("synchronizeComponentMappings: MOD DATE UPDATED for compoMapGid: " + compoMapGid);
                }else{
                    logger.info("synchronizeComponentMappings: MOD DATE UPDATE FAILED for compoMapGid: " + compoMapGid);
                }
            }
            
            //remove from HashMap
            Object value = snapshotFlagsInitial.remove(caseServGID + ":" + port);
            
            if(null != value){
                logger.info("synchronizeComponentMappings: Removed entry from HashMap for : " + caseServGID + ":" + port );
            }else{
                logger.info("synchronizeComponentMappings: No entry in HashMap for : " + caseServGID + ":" + port );
            }
        }else{
            logger.info("synchronizeComponentMappings: initialFlag not exist for caseServGID: " + caseServGID + " port: " + port + " initialFlag: " + initialFlag);
        }
        
        logger.traceExit("synchronizeComponentMappings") ;
    }
    
    private void updateModifiedDates(HashMap snapshotFlagsInitial, VMSMyDAO insMyDAO) throws VMSSysException{
        String key;
        String[] constituents;
        int value;
        logger.debug("updateModifiedDates: HashMap Size: " + snapshotFlagsInitial.size());
        try {
            for(Iterator itr = snapshotFlagsInitial.keySet().iterator(); itr.hasNext();){
                key = null;
                constituents = null;
                
                key = (String)itr.next();
                
                logger.debug("updateModifiedDates: Current key: " + key);
                
                value = ((Integer)snapshotFlagsInitial.get(key)).intValue();
                
                logger.debug("updateModifiedDates: Stray entry: Key: " + key + " Value: " + value);
                
                constituents = key.split(":");
                
                //if initial value was 0 and now it's stray means, its removed
                if(value == 0){
                    //update its MOD_AT date
                    int udtStrayCount = insMyDAO.updateModDate(constituents[0], constituents[1]);
                    if(udtStrayCount > 0){
                        logger.info("updateModifiedDates: Stray entry updated: CaseServerGID : " + constituents[0] + " Port: " + constituents[1]);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("updateModifiedDates: Exception: " + ex);
            throw new VMSSysException(ex);
        }
    }
    
    public ArrayList getCaseServers(String dom) throws VMSSysException {
        Connection conMy = null;
        VMSMyDAO mydao = null;
        ArrayList caseServers = null;
        //get Connection to MySQL - specify param 0 to connect to DBLM MYSQL Server
        //param 1 to connect to VMS MYSQL
        try{
            conMy = VMSConnectionFactory.getMyConnection(0);
            mydao = VMSDAOFactory.getVerMyDAO(conMy);
            //get the caesservers from Source MySQL
            logger.info("getCaseServers: DOMAIN: " + dom);
            caseServers = mydao.getCaseServers(dom);
        }catch(VMSDAOException vde){
            logger.error("getCaseServers: Error: ", vde);
            throw new VMSSysException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to get caseserver info from DBLM DB", VMSStaticParams.NA);
        }catch(Exception ex){
            logger.error("getCaseServers: Error: ", ex);
            throw new VMSSysException(ex.getMessage());
        }finally{
            try {
                if(null != conMy){
                    VMSConnectionFactory.closeCon(conMy);
                }
            } catch (Exception ex) {}
        }
        return caseServers;
        
    }
    
    private ArrayList getVMSCaseServers() throws VMSSysException, Exception{
        Connection conMy= null;
        VMSMyDAO insMyDAO = null;
        ArrayList retList = null;
        try{
            conMy = VMSConnectionFactory.getMyConnection(1); //connecting to destMySQL
            insMyDAO = VMSDAOFactory.getVerMyDAO(conMy);
            retList = insMyDAO.getAllServers(executiondomain);
        }catch(Exception ex){
            logger.error("getVMSCaseServers: Unable to connect to VMS DB:" , ex);
            throw new VMSSysException(VMSStaticParams.SEVERITY_2, VMSStaticParams.CRITICAL, "Unable to get case server infro from VMS DB for this domain", executiondomain);
        }finally{
            if(null != conMy){
                VMSConnectionFactory.closeCon(conMy);
            }
        }
        return retList;
    }
    
    private void processComponentsBE(String caseNameBE, String serverNameBE, int caseserverGIDBE, String domainBE, VMSMSDAO msdao) throws VMSSysException, Exception {
        ArrayList componentsBE = null;
        VMSMyDAO insMyDAO = null;
        Connection conMy = null;
        int hierarchyGIDBE = 0;
        
        //get Connection to MySQL - specify param 0 to connect to DBLM MYSQL Server
        //param 1 to connect to VMS MYSQL
        try{
            conMy = VMSConnectionFactory.getMyConnection(1);
            insMyDAO = VMSDAOFactory.getVerMyDAO(conMy);
            //change to the relevant DB
            msdao.useDB(caseNameBE);
            
            logger.info("processComponentsBE: Getting BE components.....");
            //get VMSComponent objects for this case
            componentsBE = msdao.getComponents(serverNameBE, caseNameBE);
            logger.info("processComponentsBE: BE Components obtained.");
            
            //update/insert server, component, caseserver mapping
            logger.info("processComponentsBE: Inserting/Updating BE Components.....");
            //updateInsert(componentsBE, insMyDAO, caseNameBE, serverNameBE, caseserverGIDBE);
            VMSTransactionManager xnmanager = new VMSTransactionManager(componentsBE, caseNameBE, serverNameBE, caseserverGIDBE);
            xnmanager.updateInsert(executiondomain);
            
            logger.info("processComponentsBE: BE Components insertion/updation completed.");
            
            //get the HIERARCHY GID
            logger.info("processComponentsBE: Getting Hierarchy GID from BE DB.....");
            hierarchyGIDBE = msdao.getHierarchy();
            
            if(updateInsertHierarchy(hierarchyGIDBE, caseNameBE, insMyDAO) > 0){
                logger.info("processComponentsBE: Hierarchy GID updated " + hierarchyGIDBE)  ;
            }
        }catch(Exception ex){
            logger.error("processComponentsBE: Error: " , ex);
            throw new VMSSysException(ex);
        }finally{
            if(null != conMy){
                VMSConnectionFactory.closeCon(conMy);
            }
        }
        
    }
    
    public Logger getLogger() {
        return logger;
    }
  

    void logStats(long crawlDate, long date, int i) throws VMSSysException{
        try{
           VMSStatXnManager xnmanager = new VMSStatXnManager(crawlDate, date, i);
           xnmanager.record();                      
        }catch(Exception ex){
            logger.error("logStats:" , ex);
            throw new VMSSysException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Error logging stats", VMSStaticParams.NA);
        }  
    }
    
}
