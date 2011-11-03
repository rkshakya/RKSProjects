/*
 * VMSTransactionManager.java
 *
 * Created on June 26, 2009, 2:57 AM
 *
 * Confidential and Proprietary
 * (c) Copyright 1999 - 2008 Stratify, an Iron Mountain Company. ( f/k/a PurpleYogi f/k/a Calpurnia ). All rights reserved.
 * The foregoing shall not be deemed to indicate that this source has been published.
 * Instead, it remains a trade secret of Stratify, an Iron Mountain Company.
 */

package com.stratify.apps.vms.dao;

import com.stratify.apps.vms.common.VMSCommonUtils;
import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import com.stratify.apps.vms.common.vos.VMSComponent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.stratify.common.logging.Logger;

/**
 *
 * @author RavikishoreS
 */
public class VMSTransactionManager {
    static Logger logger = Logger.getLogger(VMSTransactionManager.class.getName());
    ArrayList components = null;
    String caseName = null;
    String serverName = null;
    int caseservergid = 0;
    Connection con = null;
    VMSMyDAO insMyDAO = null;
    VMSTransactionImpl transimpl = null;
    
    /** Creates a new instance of VMSTransactionManager */
    public VMSTransactionManager(ArrayList components, String caseName, String serverName, int caseservergid) {
        //create Connection to dest MySQL DB
        try{
        con = VMSConnectionFactory.getMyConnection(1);
        //create Transaction
        transimpl = new VMSTransactionImpl(con);
        //create DAO
        insMyDAO = VMSDAOFactory.getVerMyDAO(con);
        }catch(Exception ex){
            logger.error("const: Error: " + ex.getMessage());
        }
        
        this.components = components;
        this.caseName = caseName;
        this.serverName = serverName;
        this.caseservergid = caseservergid;
    }
    
    public void updateInsert(String executiondomain) throws VMSDAOException{
        logger.traceEntry("updateInsert");
        //take the snapshot of IS_DELETED flag for this case
        //Map to hold snapshot of Is_deleted flags, key - caseservergid_port, value - is_deleted
        HashMap snapshotFlagsInitial = new HashMap();
        int countResetAll = 0;
        
        //TODO - operations in this fn has to be in transactions
        transimpl.begin();
        
        try{
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
                //new VMSDAOException().logDeviation(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to reset flags for case", caseName);
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
            //commit xn
            transimpl.end();
            
        }catch(Exception e){
            transimpl.rollback();
        }
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
            throw new VMSSysException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error updating modified date", key);
        }
    }
    
    public void updateInsertFE(String executiondomain) throws VMSDAOException{
        logger.traceEntry("updateInsertFE");
        //take the snapshot of IS_DELETED flag for this case
        HashMap snapshotFlagsInitial = new HashMap();
        int countResetAll = 0;
        
        //this whole block in Xn
        transimpl.begin();
        try{
            try{
                if(executiondomain.equalsIgnoreCase("")){
                    snapshotFlagsInitial = insMyDAO.getSnapShot(caseName, VMSStaticParams.FEALAIS);
                    //reset IS_DELETED flags to 1(deleted) for entries of this case
                    countResetAll = insMyDAO.resetFlags(caseName, VMSStaticParams.FEALAIS);
                }else if (executiondomain.equalsIgnoreCase(VMSStaticParams.APAC)){
                    snapshotFlagsInitial = insMyDAO.getSnapShot(caseName, VMSStaticParams.APACFEALAIS);
                    //reset IS_DELETED flags to 1(deleted) for entries of this case
                    countResetAll = insMyDAO.resetFlags(caseName, VMSStaticParams.APACFEALAIS);
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
            
            for(int i = 0 ; i < components.size(); i++){
                compoGID = 0; serverGID = 0; caseServGID = 0;
                caseGID = 0; compomapGID = 0; actFlag = 0; major = 0;
                minor = 0; build = 0; patch = 0; compoVerGID = 0;
                servType = null; vers = null; servURL = null; trimServerName = null;
                port = "0"; verParts = null; serverPort = null; miscGIDs = null;
                int[] compoFlag = {0,0,0,0};
                
                VMSComponent temp = (VMSComponent)components.get(i);
                
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
                        compoFlag = insMyDAO.getServCompoFlags(caseName, trimServerName, servType, VMSStaticParams.FEALAIS);
                        //compoFlag[3] will always populated at this stage
                        
                        logger.debug("updateInsertFE: COMPOFLAG: ServerGID:  " + compoFlag[0] + "> CompoGID: " + compoFlag[1] + "> CaseServerGID: " + compoFlag[2] + "> CASEGID: " + compoFlag[3] );
                        
                        miscGIDs = VMSCommonUtils.getMiscGIDs(compoFlag, VMSStaticParams.FEALAIS, insMyDAO, trimServerName);
                    }else if(executiondomain.equalsIgnoreCase(VMSStaticParams.APAC)){
                        compoFlag = insMyDAO.getServCompoFlags(caseName, trimServerName, servType, VMSStaticParams.APACFEALAIS);
                        //compoFlag[3] will always populated at this stage
                        
                        logger.debug("updateInsertFE: COMPOFLAG: ServerGID:  " + compoFlag[0] + "> CompoGID: " + compoFlag[1] + "> CaseServerGID: " + compoFlag[2] + "> CASEGID: " + compoFlag[3] );
                        
                        miscGIDs = VMSCommonUtils.getMiscGIDs(compoFlag, VMSStaticParams.APACFEALAIS, insMyDAO, trimServerName);
                        
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
                    synchronizeComponentMappings(vers, compoGID, insMyDAO, caseServGID, port, snapshotFlagsInitial);
                }catch(VMSDAOException vde){
                    logger.error("updateInsertFE: Error: " , vde);
                }
                
            }
            
            //for remaining stray entries in HashMap, try to update MOD_AT dates
            try{
                updateModifiedDates(snapshotFlagsInitial, insMyDAO);
            }catch(VMSSysException vde){
                logger.error("updateInsertFE: Error: " , vde);
            }
            
            //commit the Xn
            transimpl.end();
            
        }catch(Exception e){
            transimpl.rollback();
        }
        logger.traceExit("updateInsertFE");
    }
}
