/*
 * VMSMyDAO.java
 *
 * Created on November 22, 2007, 1:09 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.dao;

import com.stratify.apps.vms.common.VMSCommonUtils;
import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.common.vos.VMSCaseServer;
import com.stratify.apps.vms.common.vos.VMSCaseBean;
import com.stratify.apps.vms.common.vos.VMSCaseComponent;
import com.stratify.apps.vms.common.vos.VMSCaseServerCombo;
import com.stratify.apps.vms.common.vos.VMSCompoBean;
import com.stratify.apps.vms.common.vos.VMSComponentBean;
import com.stratify.apps.vms.common.vos.VMSServerBean;
import com.stratify.apps.vms.common.vos.VMSServerComponent;
import com.stratify.apps.vms.common.vos.VMSVersionBean;
import com.stratify.apps.vms.common.vos.VMSVersionComponent;
import com.stratify.apps.vms.common.vos.VMSRuleBean;
import com.stratify.apps.vms.dao.common.VMSDAOQueries;
import com.stratify.apps.vms.dao.common.VMSDAOUtils;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import com.stratify.common.logging.Logger;

/**
 *
 * @author ravikishores
 */
public class VMSMyDAO {
    static Logger logger = Logger.getLogger(VMSMyDAO.class.getName());
    
    Connection con = null;
    /**
     * Creates a new instance of VMSMyDAO
     */
    public VMSMyDAO(Connection con1) {
        this.con = con1;
    }
    
 public ArrayList getCaseServers(String dom) throws VMSDAOException{
        logger.traceEntry("getCaseServers");
        ArrayList temp = new ArrayList();
        PreparedStatement pstmtgetCaseServ = null;
        ResultSet rsgetCaseServ = null;
        
        String cName;
        String dbVersion;
        String serverName;
        String domain;
        
        try{
            pstmtgetCaseServ = con.prepareStatement(VMSDAOQueries.GET_CASE_SERVERS);
            logger.info("getCaseServers: GET_CASE_SERVERS" + VMSDAOQueries.GET_CASE_SERVERS);
            if(dom.equals("")){
                pstmtgetCaseServ.setString(1, VMSStaticParams.BEALIAS);
                pstmtgetCaseServ.setString(2, VMSStaticParams.FEALAIS);
                logger.info("Params: " + VMSStaticParams.BEALIAS + "," + VMSStaticParams.FEALAIS);
            }else{
                pstmtgetCaseServ.setString(1, dom + VMSStaticParams.BEALIAS);
                pstmtgetCaseServ.setString(2, dom + VMSStaticParams.FEALAIS);
                logger.info("Params: " + dom + VMSStaticParams.BEALIAS + "," + VMSStaticParams.FEALAIS);
            }
            rsgetCaseServ = pstmtgetCaseServ.executeQuery();
            
            while(rsgetCaseServ.next()){
                cName = null;
                dbVersion = null;
                serverName = null;
                domain = null;
                
                VMSCaseServer cs = new VMSCaseServer();
                
                cName = rsgetCaseServ.getString("CASENAME");
                dbVersion = rsgetCaseServ.getString("DB_VERSION");
                serverName = rsgetCaseServ.getString("SERVER_NAME");
                domain = rsgetCaseServ.getString("DOMAIN");
                
                if(serverName.toLowerCase().indexOf(VMSStaticParams.APACBEALIAS) > -1){
                    serverName = serverName.toLowerCase().replaceAll(VMSStaticParams.APACBEALIAS.toLowerCase(), "");
                }else if(serverName.toLowerCase().indexOf(VMSStaticParams.APACFEALAIS) > -1){
                    serverName = serverName.toLowerCase().replaceAll(VMSStaticParams.APACFEALAIS.toLowerCase(), "");
                }
                
                cs.setCaseName(cName);
                cs.setDbVersion(dbVersion);
                cs.setServerName(serverName);
                cs.setDomain(domain);
                
                temp.add(cs);
            }
            
        }catch(SQLException sqle){
            logger.error("getCaseServers: SQL Error: " + sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getCaseServers: Error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsgetCaseServ, pstmtgetCaseServ);
        }
        
        return temp;
    }
 
    public int[] getFlags(String caseName, String serverName, String domain) throws VMSDAOException {
        logger.traceEntry("getFlags");
        int[] temp = {0,0,0};
        PreparedStatement pstmtGetFlags = null;
        ResultSet rsGetFlags = null;
        int compo1, compo2, compo3;
        
        try{
            pstmtGetFlags = con.prepareStatement(VMSDAOQueries.GET_INS_INFO);
            
            pstmtGetFlags.setString(1, caseName);
            pstmtGetFlags.setString(2, serverName);
            pstmtGetFlags.setString(3, domain);
            pstmtGetFlags.setString(4, caseName);
            pstmtGetFlags.setString(5, serverName);
            pstmtGetFlags.setString(6, domain);
            
            logger.info("getFlags: " + VMSDAOQueries.GET_INS_INFO + " Params: " + caseName + "," + serverName + "," + domain + "," + caseName + "," + serverName + "," + domain);
            
            rsGetFlags = pstmtGetFlags.executeQuery();
            
            while(rsGetFlags.next()){
                compo1 = 0; compo2 = 0;  compo3 = 0;
                
                compo1 = rsGetFlags.getInt("CASE_GID");
                compo2 = rsGetFlags.getInt("SERVER_GID");
                compo3 = rsGetFlags.getInt("CASE_SERVER_GID");
                
                if((compo1 > 0) && (compo2 == 0) && (compo3 == 0)){
                    //case exists
                    temp[0] = compo1;
                }else if((compo1 == 0)&&(compo2 > 0)&&(compo3 ==0)){
                    //server entry exists
                    temp[1] = compo2;
                }else if((compo1 > 0 )&&(compo2 > 0)&&(compo3 >  0)){
                    //all 3 exists
                    temp[2] = compo3;
                }
                
            }
        }catch(SQLException sqle){
            logger.error("getFlags: DB related error: " + sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.CRITICAL, "Error getting flags for caseserver sync", 0);
        }catch(Exception ex){
            logger.error("getFlags: DB related error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetFlags, pstmtGetFlags);
        }
        
        logger.traceExit("getFlags");
        return temp;
    }
    
    public int[] getServCompoFlags(String caseName, String serverName, String component, String domain) throws VMSDAOException{
        int[] temp = {0,0,0,0}; //first flag - servGID, 2nd - compoGid, 3rd - caseServerGID, 4rth -caseGID
        PreparedStatement pstmtGetFlags = null;
        ResultSet rsGetFlags = null;
        
        try{
            pstmtGetFlags = con.prepareStatement(VMSDAOQueries.GET_COMPO_SERVER_FLAGS);
            logger.debug("getServCompoFlags:Getting flags using Query: " + VMSDAOQueries.GET_COMPO_SERVER_FLAGS);
            logger.debug("getServCompoFlags:PARAMS: serverName " + serverName + " domain " + domain + " component " + component + " caseName " + caseName );
            
            pstmtGetFlags.setString(1, serverName);
            pstmtGetFlags.setString(2, domain);
            pstmtGetFlags.setString(3, component);
            pstmtGetFlags.setString(4, caseName);
            pstmtGetFlags.setString(5, serverName);
            pstmtGetFlags.setString(6, domain);
            pstmtGetFlags.setString(7, caseName);
            
            rsGetFlags = pstmtGetFlags.executeQuery();
            int compo1, compo2, compo3, compo4;
            
            while(rsGetFlags.next()){
                compo1 = 0; compo2 = 0; compo3 = 0; compo4 = 0;
                
                compo1 = rsGetFlags.getInt("SERVER_GID");
                compo2 = rsGetFlags.getInt("COMPO_GID");
                compo3 = rsGetFlags.getInt("CASE_SERVER_GID");
                compo4 = rsGetFlags.getInt("CASE_GID");
                
                if((compo1 > 0) && (compo2 == 0) && (compo3 == 0) && (compo4 == 0)){
                    temp[0] = compo1;
                }else if((compo1 == 0)&&(compo2 > 0)&&(compo3 ==0) && (compo4 == 0)){
                    temp[1] = compo2;
                }else if((compo1 > 0 )&&(compo2 == 0)&&(compo3 >  0) && (compo4 > 0)){
                    temp[2] = compo3;
                }else if ((compo1 == 0 )&&(compo2 == 0)&&(compo3 ==  0) && (compo4 > 0)){
                    temp[3] = compo4;
                }
                
            }
        }catch(SQLException sqle){
            logger.error("getServCompoFlags:DB related error: ", sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error getting server compo flag", caseName);
        }catch(Exception ex){
            logger.error("getServCompoFlags:System error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetFlags, pstmtGetFlags);
        }
        
        return temp;
    }
    
    public int insCase(String caseName) throws VMSDAOException{
        int insGid = 0;
        int noIns = 0;
        PreparedStatement pstmtInsCase = null;
        PreparedStatement pstmtGetGid = null;
        ResultSet rsGetGid = null;
        
        try{
            pstmtInsCase = con.prepareStatement(VMSDAOQueries.INS_CASE);
            pstmtInsCase.setString(1, caseName);
            
            logger.info("insCase : " + VMSDAOQueries.INS_CASE + " Params: " + caseName);
            
            noIns = pstmtInsCase.executeUpdate();
            if(noIns > 0){
                pstmtGetGid = con.prepareStatement(VMSDAOQueries.GET_LAST_GID);
                rsGetGid = pstmtGetGid.executeQuery();
                if(rsGetGid.next()){
                    insGid = rsGetGid.getInt(1);
                }
            }
        }catch(SQLException sqle){
            logger.error("insCase: DB related error: " + sqle.getMessage());
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.CRITICAL, "Unable to insert case info", caseName);
        }catch(Exception ex){
            logger.error("insCase: DB related error: " + ex.getMessage());
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetGid, pstmtGetGid);
            VMSDAOUtils.freeUp(null, pstmtInsCase);
        }
        
        return insGid;
    }
    
    public int insServer(String serverName, String domain) throws VMSDAOException{
        int insGid = 0;
        int noIns = 0;
        PreparedStatement pstmtInsServer = null;
        PreparedStatement pstmtGetGid = null;
        ResultSet rsGetGid = null;
        try{
            pstmtInsServer = con.prepareStatement(VMSDAOQueries.INS_SERVER);
            
            pstmtInsServer.setString(1, serverName);
            pstmtInsServer.setString(2, domain);
            
            logger.info("insServer: Using query: " + VMSDAOQueries.INS_SERVER);
            logger.info("insServer: Params: " + serverName + "," + domain);
            
            noIns = pstmtInsServer.executeUpdate();
            if(noIns > 0){
                pstmtGetGid = con.prepareStatement(VMSDAOQueries.GET_LAST_GID);
                rsGetGid = pstmtGetGid.executeQuery();
                if(rsGetGid.next()){
                    insGid = rsGetGid.getInt(1);
                }
            }
        }catch(SQLException sqle){
            logger.error("insServer: DB related error: " + sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to insert Server info", serverName);
        }catch(Exception ex){
            logger.error("insServer: general error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetGid, pstmtGetGid);
            VMSDAOUtils.freeUp(null, pstmtInsServer);
        }
        return insGid;
    }
    
    public int insCaseServer(int caseGid, int serverGid) throws VMSDAOException{
        int insGid = 0;
        int noIns = 0;
        PreparedStatement pstmtInsCaseServer = null;
        PreparedStatement pstmtGetGid = null;
        ResultSet rsGetGid = null;
        try{
            pstmtInsCaseServer = con.prepareStatement(VMSDAOQueries.INS_CASE_SERVER);
            
            pstmtInsCaseServer.setInt(1, caseGid);
            pstmtInsCaseServer.setInt(2, serverGid);
            
            logger.info("insCaseServer: INS CASESERV" + VMSDAOQueries.INS_CASE_SERVER);
            logger.info("insCaseServer: Params" + caseGid + "," + serverGid);
            
            noIns = pstmtInsCaseServer.executeUpdate();
            if(noIns > 0){
                logger.info("CASESERV ROW INSERTED");
                pstmtGetGid = con.prepareStatement(VMSDAOQueries.GET_LAST_GID);
                rsGetGid = pstmtGetGid.executeQuery();
                if(rsGetGid.next()){
                    insGid = rsGetGid.getInt(1);
                }
            }
        }catch(SQLException sqle){
            logger.error("insCaseServer: DB related error: " + sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to insert caseserver info", caseGid + " " + serverGid);
        }catch(Exception ex){
            logger.error("insCaseServer: error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetGid, pstmtGetGid);
            VMSDAOUtils.freeUp(null, pstmtInsCaseServer);
        }
        return insGid;
    }
    
    //pass 0 to get BE caseServs, 1 to get FE caseServs
    public ArrayList getServers(int i) throws VMSDAOException{
        ArrayList temp = new ArrayList();
        PreparedStatement pstmtGetBEServs = null;
        ResultSet rsGetBEServs = null;
        int csGid ;
        String cName ;
        String servName ;
        String domain;
        
        try{
            if(i == 0){
                pstmtGetBEServs = con.prepareStatement(VMSDAOQueries.GET_BE_CASE_SERVS);
                logger.info("getServers: " + VMSDAOQueries.GET_BE_CASE_SERVS);
            }else if (i == 1){
                pstmtGetBEServs = con.prepareStatement(VMSDAOQueries.GET_FE_CASE_SERVS);
                logger.info("getServers: " + VMSDAOQueries.GET_FE_CASE_SERVS);
            }
            
            rsGetBEServs = pstmtGetBEServs.executeQuery();
            
            while(rsGetBEServs.next()){
                csGid = 0;
                cName = null;
                servName = null;
                domain = null;
                
                VMSCaseServer caseServBE = new VMSCaseServer();
                
                csGid = rsGetBEServs.getInt("GID");
                cName = rsGetBEServs.getString("CASENAME");
                servName = rsGetBEServs.getString("SERVER");
                domain = rsGetBEServs.getString("DOMAIN");
                
                caseServBE.setCaseName(cName);
                caseServBE.setServerName(servName);
                caseServBE.setDomain(domain);
                caseServBE.setCaseServGID(csGid);
                
                temp.add(caseServBE);
            }
            
        }catch(SQLException sqle){
            logger.error("getServers: DB related error: " + sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getServers: Genaral error: " + ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetBEServs, pstmtGetBEServs);
        }
        
        return temp;
    }
    
    public int existsComponentVersion(int compoGID, int major, int minor, int build, int patch) throws VMSDAOException {
        int temp = 0;
        PreparedStatement pstmtGetCompoVer = null;
        ResultSet rsGetCompoVer = null;
        
        try{
            pstmtGetCompoVer = con.prepareStatement(VMSDAOQueries.GET_COMPO_VERSION_GID);
            pstmtGetCompoVer.setInt(1,compoGID);
            pstmtGetCompoVer.setInt(2,major);
            pstmtGetCompoVer.setInt(3,minor);
            pstmtGetCompoVer.setInt(4,build);
            pstmtGetCompoVer.setInt(5,patch);
            
            rsGetCompoVer = pstmtGetCompoVer.executeQuery();
            
            if(rsGetCompoVer.next()){
                temp = rsGetCompoVer.getInt("GID");
            }
        }catch(SQLException sqle){
            logger.error("existsComponentVersion: DB related error: " , sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to get compo version info for compoGId", compoGID + "");
        }catch(Exception ex){
            logger.error("existsComponentVersion: Error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetCompoVer, pstmtGetCompoVer);
        }
        return temp;
    }
    
    public int insVersion(int compoGID, int major, int minor, int build, int patch) throws VMSDAOException {
        int temp = 0;
        int insCount = 0;
        PreparedStatement pstmtInsVersion = null;
        ResultSet rsLastGID = null;
        
        try{
            pstmtInsVersion = con.prepareStatement(VMSDAOQueries.INS_VERSION_INFO);
            pstmtInsVersion.setInt(1, compoGID);
            pstmtInsVersion.setInt(2, major);
            pstmtInsVersion.setInt(3, minor);
            pstmtInsVersion.setInt(4, build);
            pstmtInsVersion.setInt(5, patch);
            
            insCount = pstmtInsVersion.executeUpdate();
            
            logger.info("INSERTION QUERY: " + VMSDAOQueries.INS_VERSION_INFO);
            logger.info(compoGID + ":" + major + ":" + minor + ":" + build + ":" + patch);
            logger.info("INSCOUNT: " + insCount);
            
            if(insCount > 0){
                pstmtInsVersion = con.prepareStatement(VMSDAOQueries.GET_LAST_GID);
                rsLastGID = pstmtInsVersion.executeQuery();
                
                if (rsLastGID.next()){
                    temp = rsLastGID.getInt(1);
                }
            }
        }catch(SQLException sqle){
            logger.error("insVersion: DB related error: " , sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to insert version info", VMSStaticParams.NA);
        }catch(Exception ex){
            logger.error("insVersion: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsLastGID, pstmtInsVersion);
            VMSDAOUtils.freeUp(null, pstmtInsVersion);
        }
        return temp;
    }
    
    public int existsComponentMapping(int caseServGID, int compoVerGID, String port) throws VMSDAOException {
        int temp = 0;
        PreparedStatement pstmtCompoMap = null;
        ResultSet rsCompoMap = null;
        try{
            pstmtCompoMap = con.prepareStatement(VMSDAOQueries.GET_COMPO_MAPPING);
            
            pstmtCompoMap.setInt(1, caseServGID);
            pstmtCompoMap.setInt(2, compoVerGID);
            pstmtCompoMap.setString(3, port);
            
            rsCompoMap = pstmtCompoMap.executeQuery();
            
            while(rsCompoMap.next()){
                temp = rsCompoMap.getInt("GID");
            }
            
        }catch(SQLException sqle){
            logger.error("existsComponentMapping: DB related error: " , sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error in existsComponent Mapping check", caseServGID + " " + compoVerGID + " " + port);
        }catch(Exception ex){
            logger.error("existsComponentMapping:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsCompoMap, pstmtCompoMap);
        }
        return temp;
    }
    
    public int insCompoMapping(int caseServGID, int compoVerGID, String port) throws VMSDAOException{
        int temp = 0;
        int insCount = 0;
        PreparedStatement pstmtInsCompoMapping = null;
        ResultSet rsInsCompoGid = null;
        
        try{
            pstmtInsCompoMapping = con.prepareStatement(VMSDAOQueries.INS_COMPO_MAPPING);
            
            pstmtInsCompoMapping.setInt(1, caseServGID);
            pstmtInsCompoMapping.setInt(2, compoVerGID);
            pstmtInsCompoMapping.setString(3, port);
            
            insCount = pstmtInsCompoMapping.executeUpdate();
            
            if(insCount > 0){
                pstmtInsCompoMapping = con.prepareStatement(VMSDAOQueries.GET_LAST_GID);
                rsInsCompoGid = pstmtInsCompoMapping.executeQuery();
                while(rsInsCompoGid.next()){
                    temp = rsInsCompoGid.getInt(1);
                }
            }
            
        }catch(SQLException sqle){
            logger.error("insCompoMapping: DB related error: " , sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error inserting compo mapping info", caseServGID + " " + compoVerGID + " " + port);
        }catch(Exception ex){
            logger.error("insCompoMapping: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsInsCompoGid, pstmtInsCompoMapping);
            VMSDAOUtils.freeUp(null, pstmtInsCompoMapping);
        }
        return temp;
    }
    
    public ArrayList getCompoVerGID(int compoGID, int major, int minor, int build, int patch) throws VMSDAOException{
        ArrayList temp = new ArrayList();
        PreparedStatement pstmtGetCompoVer = null;
        ResultSet rsGetCompoVer = null;
        int compGID = 0;
        
        try{
            pstmtGetCompoVer = con.prepareStatement(VMSDAOQueries.GET_COMPO_VERSION_GID_EXC);
            
            pstmtGetCompoVer.setInt(1, compoGID);
            pstmtGetCompoVer.setInt(2, major);
            pstmtGetCompoVer.setInt(3, minor);
            pstmtGetCompoVer.setInt(4, build);
            pstmtGetCompoVer.setInt(5, patch);
            
            rsGetCompoVer = pstmtGetCompoVer.executeQuery();
            
            while(rsGetCompoVer.next()){
                compGID = rsGetCompoVer.getInt("GID");
                temp.add(new Integer(compGID));
            }
            
        }catch(SQLException sqle){
            logger.error("getCompoVerGID: DB related error: ", sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getCompoVerGID:  error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetCompoVer, pstmtGetCompoVer);
        }
        
        return temp;
    }
    
    public int updateMapping(int mapGID) throws VMSDAOException {
        int ret = 0;
        PreparedStatement pstmtUpdateMap = null;
        
        try{
            pstmtUpdateMap = con.prepareStatement(VMSDAOQueries.UPDATE_COMPO_MAPPING);
            pstmtUpdateMap.setInt(1, mapGID);
            ret = pstmtUpdateMap.executeUpdate();
            
        }catch(SQLException sqle){
            logger.error("updateMapping: DB related error: ",  sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error updating is_deleted flag ", mapGID + "");
        }catch(Exception ex){
            logger.error("updateMapping:  error: ",  ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtUpdateMap);
        }
        
        return ret;
    }
    
    public ArrayList getCompoBeans() throws VMSDAOException {
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetCompo = null ;
        ResultSet rsGetCompo = null;
        int comVerGID = 0;
        int Major = 0;
        int Minor = 0;
        int Build = 0;
        int Patch = 0;
        String CompoName = null;
        
        
        try{
            pstmtGetCompo = con.prepareStatement(VMSDAOQueries.GET_COMPO_VER);
            rsGetCompo = pstmtGetCompo.executeQuery();
            logger.info("getCompoBeans: " + VMSDAOQueries.GET_COMPO_VER);
            
            while(rsGetCompo.next()){
                comVerGID = 0;
                Major = 0;
                Minor = 0;
                Build = 0;
                Patch = 0;
                CompoName = null;
                
                VMSCompoBean cBean = new VMSCompoBean();
                
                comVerGID = rsGetCompo.getInt("GID");
                Major = rsGetCompo.getInt("MAJOR");
                Minor = rsGetCompo.getInt("MINOR");
                Build = rsGetCompo.getInt("BUILD");
                Patch = rsGetCompo.getInt("PATCH");
                CompoName = rsGetCompo.getString("COMPONENT");
                
                cBean.setCompoVerGID(comVerGID);
                cBean.setMaj(Major);
                cBean.setMin(Minor);
                cBean.setBld(Build);
                cBean.setPatch(Patch);
                cBean.setCompoName(CompoName);
                
                ret.add(cBean);
                
                logger.info("getCompoBeans: Component Bean added in DAO");
            }
            
        }catch(SQLException sqle){
            logger.error("getCompoBeans: DB related error: ", sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getCompoBeans: error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetCompo, pstmtGetCompo);
        }
        
        
        return ret;
    }
    
    public int getMaxRule() throws VMSDAOException{
        int ret = 0;
        PreparedStatement pstmtGetRule = null ;
        ResultSet rsGetRule = null;
        
        try{
            pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_MAX_RULE);
            rsGetRule = pstmtGetRule.executeQuery();
            logger.info("getMaxRule: " + VMSDAOQueries.GET_MAX_RULE);
            
            while(rsGetRule.next()){
                ret = rsGetRule.getInt(1);
            }
            
        }catch(SQLException sqle){
            logger.error("getMaxRule: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getMaxRule: error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        
        return ret;
    }
    
    public int insRule(int[] compoGIDs, String ruleName, String ruleDescription) throws VMSDAOException {
        int ret = 0;
        int maxRuleNum = 0;
        PreparedStatement pstmtInsRule = null;
        ResultSet rsInsRule = null;
        PreparedStatement pstmtInsRuleMetadata = null;
        ResultSet rsInsRuleMetadata = null;
        PreparedStatement pstmtMaxRule = null;
        ResultSet rsMaxRule = null;
        
        //execute these  queries as atomic
        try{
            con.setAutoCommit(false);
            //get the max rule num
            pstmtMaxRule = con.prepareStatement(VMSDAOQueries.GET_MAX_RULE);
            rsMaxRule = pstmtMaxRule.executeQuery();
            logger.info("insRule:  GET_MAX_RULE: " + VMSDAOQueries.GET_MAX_RULE);
            if(rsMaxRule.next()){
                maxRuleNum = rsMaxRule.getInt(1);
            }
            
            maxRuleNum++;
            
            // ins into vms_rules_info
            pstmtInsRuleMetadata = con.prepareStatement(VMSDAOQueries.INS_RULE_METADATA);
            pstmtInsRuleMetadata.setString(1, ruleName);
            pstmtInsRuleMetadata.setString(2, ruleDescription);
            pstmtInsRuleMetadata.setInt(3, maxRuleNum);
            //set it to system user - for future convenience
            pstmtInsRuleMetadata.setInt(4, 1);
            int cntIns2 = pstmtInsRuleMetadata.executeUpdate();
            logger.info("insRule: INS_RULE_METADATA Query: " + VMSDAOQueries.INS_RULE_METADATA);
            logger.info("insRule: params: " + ruleName + "," + ruleDescription + "," + maxRuleNum + "," + 1);
            
            pstmtInsRule = con.prepareStatement(VMSDAOQueries.INS_RULE);
            logger.info("insRule: INS_RULE Query: " + VMSDAOQueries.INS_RULE);
            
            for(int i = 0 ; i < compoGIDs.length; i++){
                pstmtInsRule.setInt(1, maxRuleNum);
                pstmtInsRule.setInt(2, compoGIDs[i]);
                pstmtInsRule.setInt(3, 1);
                pstmtInsRule.addBatch();
            }
            
            int[] cntIns = pstmtInsRule.executeBatch();
            
            con.commit();
            con.setAutoCommit(true);
            
            if(cntIns.length > 0 && cntIns2 > 0){
                ret = 1;
            }
            
            ret = cntIns.length;
        }catch(SQLException sqle){
            logger.error("insRule : DB related error ", sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("insRule : error " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsInsRule, pstmtInsRule);
            VMSDAOUtils.freeUp(rsInsRuleMetadata, pstmtInsRuleMetadata);
            VMSDAOUtils.freeUp(rsMaxRule, pstmtMaxRule);
        }
        return ret;
    }
    
    public int checkRuleExists(int[] compoGIDs) throws VMSDAOException {
        int ret = 0;
        Statement stmtcheckRule = null;
        ResultSet rscheckRule = null;
        String qry = null;
        String compVerGIDs = null;
        int count = 0;
        ArrayList temp = new ArrayList();
        
        //construct compVerGID string here
        for(int i = 0 ; i < compoGIDs.length; i++){
            temp.add(new Integer(compoGIDs[i]));
        }
        
        compVerGIDs = VMSCommonUtils.join(temp, ",");
        
        qry = "select RULE, count(GID) from vms_compatibility_rules where COMP_VER_GID "
                +   "IN ( " + compVerGIDs + " ) AND IS_DELETED = 0 GROUP BY RULE ORDER BY count(GID) DESC";
        /*
        qry = "SELECT RULES.RULE, COUNT(RULES.GID) FROM vms_compatibility_rules RULES, vms_rules_info INFO"
                + " WHERE (RULES.COMP_VER_GID IN ( " + compVerGIDs + " ) AND RULES.IS_DELETED = 0)"
                + " OR(RULES.RULE = INFO.RULE AND RULES.IS_DELETED = 0 AND INFO.NAME = '" + rulename + "' )"
                + " GROUP BY RULES.RULE ORDER BY count(RULES.GID) DESC";
         **/
        
        logger.info("checkRuleExists: " + qry);
        
        try{
            stmtcheckRule = con.createStatement();
            rscheckRule = stmtcheckRule.executeQuery(qry);
            
            if(rscheckRule.next()){
                count = rscheckRule.getInt(2);
            }
            
            logger.info("checkRuleExists : COUNT: " + count);
            
            if(count == compoGIDs.length){
                ret = 1;
            }
            
        }catch(SQLException sqle){
            logger.error("checkRuleExists: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("checkRuleExists:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            try{
                VMSDAOUtils.cleanUp(rscheckRule, stmtcheckRule);
            }catch(SQLException sqle){
                logger.error("checkRuleExists: ", sqle);
                throw new VMSDAOException(sqle.getMessage());
            }
        }
        
        return ret;
    }
    
    public ArrayList getCompoBeans(int[] compoVerGIDs) throws VMSDAOException {
        ArrayList ret = new ArrayList();
        Statement stmtGetCompo = null ;
        ResultSet rsGetCompo = null;
        String qryGetIns = null;
        String suffix = null;
        ArrayList gids = new ArrayList();
        int comVerGID = 0;
        int Major = 0;
        int Minor = 0;
        int Build = 0;
        int Patch = 0;
        String CompoName = null;
        
        for(int i = 0; i < compoVerGIDs.length; i++){
            gids.add(new Integer(compoVerGIDs[i]));
        }
        
        suffix = VMSCommonUtils.join(gids, ",");
        qryGetIns = "SELECT A.GID, A.MAJOR, A.MINOR, A.BUILD, A.PATCH, B.COMPONENT "
                + " FROM vms_compo_version A, vms_components B "
                + " WHERE A.COMP_GID = B.GID "
                + " AND A.GID IN (" + suffix + ")"
                + " ORDER BY B.BE, B.COMPONENT,A.MAJOR, A.MINOR, A.BUILD, A.PATCH";
        
        logger.info("getCompoBeans: " + qryGetIns);
        
        try{
            stmtGetCompo = con.createStatement();
            rsGetCompo = stmtGetCompo.executeQuery(qryGetIns);
            
            while(rsGetCompo.next()){
                comVerGID = 0;
                Major = 0;
                Minor = 0;
                Build = 0;
                Patch = 0;
                CompoName = null;
                
                VMSCompoBean cBean = new VMSCompoBean();
                
                comVerGID = rsGetCompo.getInt("GID");
                Major = rsGetCompo.getInt("MAJOR");
                Minor = rsGetCompo.getInt("MINOR");
                Build = rsGetCompo.getInt("BUILD");
                Patch = rsGetCompo.getInt("PATCH");
                CompoName = rsGetCompo.getString("COMPONENT");
                
                cBean.setCompoVerGID(comVerGID);
                cBean.setMaj(Major);
                cBean.setMin(Minor);
                cBean.setBld(Build);
                cBean.setPatch(Patch);
                cBean.setCompoName(CompoName);
                
                ret.add(cBean);
                logger.info("getCompoBeans: Compo Bean added in DAO");
            }
            
        }catch(SQLException sqle){
            logger.error("getCompoBeans: DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getCompoBeans:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            try{
                VMSDAOUtils.cleanUp(rsGetCompo, stmtGetCompo);
            }catch(SQLException sqle){
                logger.error("getCompoBeans: error: " , sqle);
                throw new VMSDAOException(sqle.getMessage());
            }
        }
        
        
        return ret;
    }
    
    public ArrayList getRules() throws VMSDAOException{
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetRule = null;
        ResultSet rsGetRule = null;
        int rule ;
        String name ;
        String desc ;
        
        try{
            pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_RULE);
            rsGetRule = pstmtGetRule.executeQuery();
            
            logger.info("getRules: " + VMSDAOQueries.GET_RULE);
            
            while(rsGetRule.next()){
                VMSRuleBean rb = new VMSRuleBean();
                rule = 0;
                name = null;
                desc = null;
                
                rule = rsGetRule.getInt("RULE");
                name = rsGetRule.getString("NAME");
                desc = rsGetRule.getString("DESCRIPTION");
                
                rb.setRule(rule);
                rb.setRuleName(name);
                rb.setRuleDescription(desc);
                
                ret.add(rb);
            }
            
            
        }catch(SQLException sqle){
            logger.error("getRules: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getRules: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        return ret;
    }
    
    public ArrayList getCompoBeans(int rulNum) throws VMSDAOException{
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetCompo = null ;
        ResultSet rsGetCompo = null;
        int comVerGID = 0;
        int Major = 0;
        int Minor = 0;
        int Build = 0;
        int Patch = 0;
        String CompoName = null;
        
        try{
            pstmtGetCompo = con.prepareStatement(VMSDAOQueries.GET_COMPO_VER_RULE);
            pstmtGetCompo.setInt(1, rulNum);
            
            rsGetCompo = pstmtGetCompo.executeQuery();
            logger.info("getCompoBeans: " + VMSDAOQueries.GET_COMPO_VER_RULE);
            
            while(rsGetCompo.next()){
                comVerGID = 0;
                Major = 0;
                Minor = 0;
                Build = 0;
                Patch = 0;
                CompoName = null;
                
                VMSCompoBean cBean = new VMSCompoBean();
                
                comVerGID = rsGetCompo.getInt("GID");
                Major = rsGetCompo.getInt("MAJOR");
                Minor = rsGetCompo.getInt("MINOR");
                Build = rsGetCompo.getInt("BUILD");
                Patch = rsGetCompo.getInt("PATCH");
                CompoName = rsGetCompo.getString("COMPONENT");
                
                cBean.setCompoVerGID(comVerGID);
                cBean.setMaj(Major);
                cBean.setMin(Minor);
                cBean.setBld(Build);
                cBean.setPatch(Patch);
                cBean.setCompoName(CompoName);
                
                ret.add(cBean);
            }
            
        }catch(SQLException sqle){
            logger.error("getCompoBeans: DB related error: ", sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getCompoBeans: error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetCompo, pstmtGetCompo);
        }
        return ret;
    }
    
    public int deleteRule(int rulNum) throws VMSDAOException{
        int ret = 0;
        int ret1 = 0;
        PreparedStatement pstmtDelRule = null ;
        PreparedStatement pstmtDelRuleInfo = null;
        
        try{
            //all this in Xn
            con.setAutoCommit(false);
            //1 delete from vms_compatibility_rules
            pstmtDelRule = con.prepareStatement(VMSDAOQueries.DEL_RULE);
            pstmtDelRule.setInt(1, rulNum);
            ret = pstmtDelRule.executeUpdate();
            logger.info("deleteRule: " + VMSDAOQueries.DEL_RULE + " param : " + rulNum);
            
            //2 delete from vms_rules_info
            pstmtDelRuleInfo = con.prepareStatement(VMSDAOQueries.DEL_RULE_INFO);
            pstmtDelRuleInfo.setInt(1, rulNum);
            ret1 = pstmtDelRuleInfo.executeUpdate();
            logger.info("deleteRule: " + VMSDAOQueries.DEL_RULE_INFO + " param: " + rulNum);
            
            if (ret > 0 && ret1 > 0){
                con.commit();
                ret = 1;
            }else{
                con.rollback();
            }
            
            con.setAutoCommit(true);
            
        }catch(SQLException sqle){
            logger.info("deleteRule: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.info("deleteRule:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtDelRule);
            VMSDAOUtils.freeUp(null, pstmtDelRuleInfo);
        }
        
        return ret;
    }
    
    public ArrayList getOtherCompoBeans(int rulNum) throws VMSDAOException {
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetCompo = null ;
        ResultSet rsGetCompo = null;
        int comVerGID = 0;
        int Major = 0;
        int Minor = 0;
        int Build = 0;
        int Patch = 0;
        String CompoName = null;
        
        try{
            pstmtGetCompo = con.prepareStatement(VMSDAOQueries.GET_OTHER_COMPO_VER_RULE);
            pstmtGetCompo.setInt(1, rulNum);
            
            rsGetCompo = pstmtGetCompo.executeQuery();
            logger.info("getOtherCompoBeans: " + VMSDAOQueries.GET_OTHER_COMPO_VER_RULE + " Params: " + rulNum );
            
            while(rsGetCompo.next()){
                comVerGID = 0;
                Major = 0;
                Minor = 0;
                Build = 0;
                Patch = 0;
                CompoName = null;
                
                VMSCompoBean cBean = new VMSCompoBean();
                
                comVerGID = rsGetCompo.getInt("GID");
                Major = rsGetCompo.getInt("MAJOR");
                Minor = rsGetCompo.getInt("MINOR");
                Build = rsGetCompo.getInt("BUILD");
                Patch = rsGetCompo.getInt("PATCH");
                CompoName = rsGetCompo.getString("COMPONENT");
                
                cBean.setCompoVerGID(comVerGID);
                cBean.setMaj(Major);
                cBean.setMin(Minor);
                cBean.setBld(Build);
                cBean.setPatch(Patch);
                cBean.setCompoName(CompoName);
                ret.add(cBean);
            }
            
        }catch(SQLException sqle){
            logger.error("getOtherCompoBeans: DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getOtherCompoBeans:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetCompo, pstmtGetCompo);
        }
        
        return ret;
    }
    
    public ArrayList getServers() throws VMSDAOException {
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetRule = null;
        ResultSet rsGetRule = null;
        
        try{
            pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_SERVERS);
            rsGetRule = pstmtGetRule.executeQuery();
            
            logger.info("getServers: " + VMSDAOQueries.GET_SERVERS);
            int gid = 0;
            String name = null;
            String domain = null;
            
            while(rsGetRule.next()){
                VMSServerBean sb = new VMSServerBean();
                
                gid = rsGetRule.getInt("GID");
                name = rsGetRule.getString("SERVER");
                domain = rsGetRule.getString("DOMAIN");
                
                sb.setGid(gid);
                sb.setName(name);
                sb.setDomain(domain);
                
                ret.add(sb);
            }
        }catch(SQLException sqle){
            logger.error("getServers: MySQL DB related error in getServers: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getServers: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        return ret;
    }
    
    public ArrayList getCaseComponents(int serverGid) throws VMSDAOException {
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetRule = null;
        ResultSet rsGetRule = null;
        int servGid ;
        int caseGid ;
        String casename;
        String component;
        int maj ;
        int min ;
        int build ;
        int patch ;
        String port;
        
        try{
            pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_CASE_COMPONENTS);
            pstmtGetRule.setInt(1,serverGid);
            rsGetRule = pstmtGetRule.executeQuery();
            
            while(rsGetRule.next()){
                VMSCaseComponent sb = new VMSCaseComponent();
                servGid = 0;
                caseGid = 0;
                casename = null;
                component = null;
                maj = 0;
                min = 0;
                build = 0;
                patch = 0;
                port = null;
                
                servGid = rsGetRule.getInt("SERVER_GID");
                caseGid = rsGetRule.getInt("GID");
                casename = rsGetRule.getString("CASENAME");
                component = rsGetRule.getString("COMPONENT");
                maj = rsGetRule.getInt("MAJOR");
                min = rsGetRule.getInt("MINOR");
                build = rsGetRule.getInt("BUILD");
                patch = rsGetRule.getInt("PATCH");
                port = rsGetRule.getString("PORT");
                
                sb.setServerGid(servGid);
                sb.setCaseGid(caseGid);
                sb.setCaseName(casename);
                sb.setComponent(component);
                sb.setMajor(maj);
                sb.setMinor(min);
                sb.setBuild(build);
                sb.setPatch(patch);
                sb.setPort(port);
                
                sb.setVersion();
                
                ret.add(sb);
            }
        }catch(SQLException sqle){
            logger.error("getCaseComponents: MySQL DB related error in getCaseCompo: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getCaseComponents: error : " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        return ret;
    }
    
    public ArrayList getCases() throws VMSDAOException {
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetRule = null;
        ResultSet rsGetRule = null;
        
        try{
            pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_CASES);
            rsGetRule = pstmtGetRule.executeQuery();
            
            while(rsGetRule.next()){
                VMSCaseBean cb = new VMSCaseBean();
                int gid = 0;
                String caseName = null;
                
                gid = rsGetRule.getInt("GID");
                caseName = rsGetRule.getString("CASENAME");
                
                cb.setCaseGid(gid);
                cb.setCaseName(caseName);
                
                ret.add(cb);
            }
        }catch(SQLException sqle){
            logger.error("getCases: MySQL DB related error in getCases: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getCases: Error:  " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        return ret;
    }
    
     public ArrayList getServerComponents(int[] caseGid) throws VMSDAOException, SQLException {
        ArrayList ret = new ArrayList();
        Statement stmtGetServerComponents = null;
        ResultSet rsGetServerComponents = null;
        String component;
        String serverName;
        int serverGid;
        String caseName;
        int caseGID;
        String port;
        int maj;
        int min;
        int build;
        int patch;
        int compoVerGid;
        String query = null;
        ArrayList caseGids = new ArrayList();
        
        for(int m = 0; m < caseGid.length; m++){
            caseGids.add(new Integer(caseGid[m]));
        }
        
        //form the appropriate query based on caseGid
        if( (caseGid.length == 1)&&(caseGid[0] == -1) ){
            //for all cases
            query = VMSDAOQueries.GET_SERVER_COMPONENTS_GENERIC
                    + " ORDER BY C.GID,S.DOMAIN, CO.COMPONENT, CV.MAJOR";
        }else{
            //for single/multiple cases
            query = VMSDAOQueries.GET_SERVER_COMPONENTS_GENERIC
                    + " AND CSM.CASE_GID IN (" + VMSCommonUtils.join(caseGids, ",") + ")"
                    + " ORDER BY C.GID,S.DOMAIN, CO.COMPONENT, CV.MAJOR";
        }
        
        logger.info("getServerComponents: Query used ServerComponents: " + query);
        
        try{
            stmtGetServerComponents = con.createStatement();
            rsGetServerComponents = stmtGetServerComponents.executeQuery(query);
            
            while(rsGetServerComponents.next()){
                VMSServerComponent sc = new VMSServerComponent();
                component = null;
                serverName = null;
                serverGid = 0;
                caseName = null;
                caseGID = 0;
                port = null;
                maj = 0;
                min = 0;
                build = 0;
                patch = 0;
                compoVerGid = 0;
                
                caseName = rsGetServerComponents.getString("CASENAME");
                caseGID = rsGetServerComponents.getInt("C.GID");
                component = rsGetServerComponents.getString("COMPONENT");
                serverGid = rsGetServerComponents.getInt("S.GID");
                serverName = rsGetServerComponents.getString("SERVER");
                port = rsGetServerComponents.getString("PORT");
                maj = rsGetServerComponents.getInt("MAJOR");
                min = rsGetServerComponents.getInt("MINOR");
                build = rsGetServerComponents.getInt("BUILD");
                patch = rsGetServerComponents.getInt("PATCH");
                compoVerGid = rsGetServerComponents.getInt("COMPO_VER_GID");
                
                sc.setCaseGid(caseGID);
                sc.setCaseName(caseName);
                sc.setComponent(component);
                sc.setServerGid(serverGid);
                sc.setServerName(serverName);
                sc.setPort(port);
                sc.setMaj(maj);
                sc.setMin(min);
                sc.setBuild(build);
                sc.setPatch(patch);
                sc.setCompoVerGid(compoVerGid);
                
                ret.add(sc);
            }
        }catch(SQLException sqle){
            logger.error("getServerComponents: error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getServerComponents: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.cleanUp(rsGetServerComponents, stmtGetServerComponents);
        }
        return ret;
    }
    
    public ArrayList getOutCastes(int caseGid) throws VMSDAOException {
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetRule = null;
        ResultSet rsGetRule = null;
        
        try{
            pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_OUTCASTES);
            pstmtGetRule.setInt(1,caseGid);
            pstmtGetRule.setInt(2, caseGid);
            rsGetRule = pstmtGetRule.executeQuery();
            
            while(rsGetRule.next()){
                int compoVerGid = 0;
                compoVerGid = rsGetRule.getInt("COMPO_VER_GID");
                ret.add(new Integer(compoVerGid));
            }
        }catch(SQLException sqle){
            logger.error("getOutCastes: MySQL DB related error in getOutCastes: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getOutCastes:Error : " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        return ret;
    }
    
    public ArrayList getVersionComponents(String major, String minor, String build, String pat, int compoGid) throws VMSDAOException, SQLException {
        ArrayList ret = new ArrayList();
        Statement stmtGetRule = null;
        ResultSet rsGetRule = null;
        String component;
        String serverName;
        String port;
        int maj;
        int min;
        int bld;
        int patch;
        String casename;
        String domain;
        String qry;
        
        qry = VMSDAOQueries.GET_VERSION_COMPONENTS;
        //form the query based on supplied 4 params
        if( (null != major) && (!major.equals("")) ){
            qry += " AND CV.MAJOR = '" + major + "' ";
        }
        if( (null != minor)  && (!minor.equals("")) ) {
            qry += " AND CV.MINOR = '" + minor + "' ";
        }
        if( (null != build)  && (!build.equals("")) ){
            qry += " AND CV.BUILD = '" + build + "' ";
        }
        if( (null != pat)  && (!pat.equals("")) ){
            qry += " AND CV.PATCH = '" + pat + "' ";
        }
        //if not ANY COMPONENT selected
        if(compoGid != 0){
            qry += " AND COMP.GID = " + compoGid ;
        }
        
        qry +=  " ORDER BY C.CASENAME, S.DOMAIN, COMP.COMPONENT";
        
        logger.info("getVersionComponents: QUERY VERSIONCOMPO: " + qry);
        
        try{
            stmtGetRule = con.createStatement();
            rsGetRule = stmtGetRule.executeQuery(qry);
            
            while(rsGetRule.next()){
                VMSVersionComponent vc = new VMSVersionComponent();
                component = null;
                serverName = null;
                port = null;
                maj = 0;
                min = 0;
                bld = 0;
                patch = 0;
                casename = null;
                domain = null;
                
                casename = rsGetRule.getString("CASENAME");
                serverName = rsGetRule.getString("SERVER");
                component = rsGetRule.getString("COMPONENT");
                maj = rsGetRule.getInt("MAJOR");
                min = rsGetRule.getInt("MINOR");
                bld = rsGetRule.getInt("BUILD");
                patch = rsGetRule.getInt("PATCH");
                domain = rsGetRule.getString("DOMAIN");
                port = rsGetRule.getString("PORT");
                
                vc.setCasename(casename);
                vc.setServername(serverName);
                vc.setComponentname(component);
                vc.setVersion(maj + "." + min + "." + bld + "." + patch);
                vc.setDomain(domain);
                vc.setPort(port);
                
                ret.add(vc);
            }
        }catch(SQLException sqle){
            logger.error("getVersionComponents: error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getVersionComponents: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.cleanUp(rsGetRule, stmtGetRule);
        }
        return ret;
    }
    
    
    public ArrayList getComponentList() throws VMSDAOException{
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetRule = null;
        ResultSet rsGetRule = null;
        
        try{
            pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_COMPONENTLIST);
            rsGetRule = pstmtGetRule.executeQuery();
            
            while(rsGetRule.next()){
                VMSComponentBean compobean = new VMSComponentBean();
                int gid = 0;
                String componentName = null;
                
                gid = rsGetRule.getInt("GID");
                componentName = rsGetRule.getString("COMPONENT");
                
                compobean.setComponentName(componentName);
                compobean.setGid(gid);
                
                ret.add(compobean);
            }
        }catch(SQLException sqle){
            logger.error("getComponentList: MySQL DB related error in getComponentList: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getComponentList:  error  " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        return ret;
    }
    
    public ArrayList getVersionComponents(int caseGid, int componentGid) throws VMSDAOException{
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetRule = null;
        ResultSet rsGetRule = null;
        String component;
        String serverName;
        String port ;
        int maj;
        int min;
        int build;
        int patch;
        String casename;
        String domain;
        
        try{
            pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_CASE_COMPONENTS_COMPONENTS);
            pstmtGetRule.setInt(1, componentGid);
            pstmtGetRule.setInt(2, caseGid);
            
            rsGetRule = pstmtGetRule.executeQuery();
            
            while(rsGetRule.next()){
                VMSVersionComponent vc = new VMSVersionComponent();
                component = null;
                serverName = null;
                port = null;
                maj = 0;
                min = 0;
                build = 0;
                patch = 0;
                casename = null;
                domain = null;
                
                casename = rsGetRule.getString("CASENAME");
                serverName = rsGetRule.getString("SERVER");
                component = rsGetRule.getString("COMPONENT");
                maj = rsGetRule.getInt("MAJOR");
                min = rsGetRule.getInt("MINOR");
                build = rsGetRule.getInt("BUILD");
                patch = rsGetRule.getInt("PATCH");
                domain = rsGetRule.getString("DOMAIN");
                port = rsGetRule.getString("PORT");
                
                vc.setCasename(casename);
                vc.setServername(serverName);
                vc.setComponentname(component);
                vc.setVersion(maj + "." + min + "." + build + "." + patch);
                vc.setDomain(domain);
                vc.setPort(port);
                
                ret.add(vc);
            }
        }catch(SQLException sqle){
            logger.error("getVersionComponents: MySQL DB related error in getVersionComponents: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getVersionComponents: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        return ret;
    }
    
    public ArrayList getVersionComponents(int componentGid, String string) throws VMSDAOException{
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetRule = null;
        ResultSet rsGetRule = null;
        String component ;
        String serverName;
        String port ;
        int maj;
        int min;
        int build;
        int patch;
        String casename;
        String domain;
        
        try{
            if(string.equalsIgnoreCase("case")){
                pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_CASE_COMPONENTS_COMPONENTS_CA);
            }else{
                pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_CASE_COMPONENTS_COMPONENTS_CO);
            }
            pstmtGetRule.setInt(1, componentGid);
            
            rsGetRule = pstmtGetRule.executeQuery();
            
            while(rsGetRule.next()){
                VMSVersionComponent vc = new VMSVersionComponent();
                component = null;
                serverName = null;
                port = null;
                maj = 0;
                min = 0;
                build = 0;
                patch = 0;
                casename = null;
                domain = null;
                
                casename = rsGetRule.getString("CASENAME");
                serverName = rsGetRule.getString("SERVER");
                component = rsGetRule.getString("COMPONENT");
                maj = rsGetRule.getInt("MAJOR");
                min = rsGetRule.getInt("MINOR");
                build = rsGetRule.getInt("BUILD");
                patch = rsGetRule.getInt("PATCH");
                domain = rsGetRule.getString("DOMAIN");
                port = rsGetRule.getString("PORT");
                
                vc.setCasename(casename);
                vc.setServername(serverName);
                vc.setComponentname(component);
                vc.setVersion(maj + "." + min + "." + build + "." + patch);
                vc.setDomain(domain);
                vc.setPort(port);
                
                ret.add(vc);
            }
        }catch(SQLException sqle){
            logger.error("getVersionComponents: MySQL DB related error in getVersionComponents: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getVersionComponents:  " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        return ret;
    }
    
    public ArrayList getServerComponents(int icasegid, String qry) throws VMSDAOException {
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetServerComponents = null;
        ResultSet rsGetServerComponents = null;
        
        try{
            pstmtGetServerComponents = con.prepareStatement(qry);
            pstmtGetServerComponents.setInt(1, icasegid);
            pstmtGetServerComponents.setInt(2, icasegid);
            pstmtGetServerComponents.setInt(3, icasegid);
            
            rsGetServerComponents = pstmtGetServerComponents.executeQuery();
            
            while(rsGetServerComponents.next()){
                VMSServerComponent sc = new VMSServerComponent();
                String component = null;
                String serverName = null;
                int serverGid = 0;
                String caseName = null;
                int caseGid = 0;
                String port = null;
                int maj = 0;
                int min = 0;
                int build = 0;
                int patch = 0;
                int compoVerGid = 0;
                int hierarchyGid = 0;
                String domain = null;
                
                caseName = rsGetServerComponents.getString("CASENAME");
                hierarchyGid = rsGetServerComponents.getInt("HIERARCHY_GID");
                caseGid = rsGetServerComponents.getInt("C.GID");
                component = rsGetServerComponents.getString("COMPONENT");
                serverGid = rsGetServerComponents.getInt("S.GID");
                serverName = rsGetServerComponents.getString("SERVER");
                port = rsGetServerComponents.getString("PORT");
                maj = rsGetServerComponents.getInt("MAJOR");
                min = rsGetServerComponents.getInt("MINOR");
                build = rsGetServerComponents.getInt("BUILD");
                patch = rsGetServerComponents.getInt("PATCH");
                compoVerGid = rsGetServerComponents.getInt("COMPO_VER_GID");
                domain = rsGetServerComponents.getString("DOMAIN");
                
                sc.setCaseGid(caseGid);
                sc.setHierarchyGid(hierarchyGid);
                sc.setCaseName(caseName);
                sc.setComponent(component);
                sc.setServerGid(serverGid);
                sc.setServerName(serverName);
                sc.setPort(port);
                sc.setMaj(maj);
                sc.setMin(min);
                sc.setBuild(build);
                sc.setPatch(patch);
                sc.setCompoVerGid(compoVerGid);
                sc.setDomain(domain);
                
//                sc.setVersion();
//                sc.setUrl();
                
                ret.add(sc);
            }
        }catch(SQLException sqle){
            logger.error("getServerComponents: MySQL DB related error in getServerComponents Valid: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getServerComponents: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetServerComponents, pstmtGetServerComponents);
        }
        return ret;
    }
    
    public int resolveCaseName(String caseName) throws VMSDAOException{
        int ret = 0;
        
        PreparedStatement pstmtResolveCase = null ;
        ResultSet rsResolveCase = null;
        
        try{
            pstmtResolveCase = con.prepareStatement(VMSDAOQueries.RESOLVE_CASENAME);
            pstmtResolveCase.setString(1, caseName);
            logger.info("resolveCaseName: " + VMSDAOQueries.RESOLVE_CASENAME);
            
            rsResolveCase = pstmtResolveCase.executeQuery();
            
            while(rsResolveCase.next()){
                ret = rsResolveCase.getInt(1);
            }
            
        }catch(SQLException sqle){
            logger.error("resolveCaseName: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("resolveCaseName: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsResolveCase, pstmtResolveCase);
        }
        
        return ret;
    }
    
    public ArrayList getVersion(int i) throws VMSDAOException{
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetVersions = null;
        ResultSet rsGetVersions = null;
        String queryVersions = null;
        
        switch(i){
            case 1 : queryVersions = VMSDAOQueries.GET_MAJORS;
            break;
            case 2 : queryVersions = VMSDAOQueries.GET_MINORS;
            break;
            case 3: queryVersions = VMSDAOQueries.GET_BUILDS;
            break;
            case 4 : queryVersions = VMSDAOQueries.GET_PATCHS;
        }
        
        try{
            pstmtGetVersions = con.prepareStatement(queryVersions);
            rsGetVersions = pstmtGetVersions.executeQuery();
            
            logger.info("getVersion: " + queryVersions);
            
            while(rsGetVersions.next()){
                int versionCompo = 0;
                VMSVersionBean vb = new VMSVersionBean();
                versionCompo = rsGetVersions.getInt(1);
                vb.setVersion(versionCompo);
                ret.add(vb);
            }
        }catch(SQLException sqle){
            logger.error("getVersion: MySQL DB related error in getVersions: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getVersion: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetVersions, pstmtGetVersions);
        }
        return ret;
    }
    
    public int updateHGID(int hierarchyGID, String caseName) throws VMSDAOException {
        PreparedStatement pstmtUpdateGid = null;
        ResultSet rsUpdateGid = null;
        int count = 0;
        
        try{
            pstmtUpdateGid = con.prepareStatement(VMSDAOQueries.UPDATE_HGID);
            pstmtUpdateGid.setInt(1, hierarchyGID);
            pstmtUpdateGid.setString(2, caseName);
            count = pstmtUpdateGid.executeUpdate();
            logger.info("updateHGID: " + VMSDAOQueries.UPDATE_HGID + " Params: " + hierarchyGID + ", " + caseName);
        }catch(SQLException sqle){
            logger.error("updateHGID: DB related error: ", sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error ins/updating HIerarchy GID", caseName);
        }catch(Exception ex){
            logger.error("updateHGID:  error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtUpdateGid);
        }
        return count;
    }
    
    public int getHierarchyGid(String caseNameFE) throws VMSDAOException {
        PreparedStatement pstmtGetHGid = null;
        ResultSet rsGetHGid = null;
        int hgid = 0;
        
        try{
            pstmtGetHGid = con.prepareStatement(VMSDAOQueries.GET_HGID);
            pstmtGetHGid.setString(1, caseNameFE);
            rsGetHGid = pstmtGetHGid.executeQuery();
            logger.info("getHierarchyGid: " + VMSDAOQueries.GET_HGID + " Params: " + caseNameFE );
            while(rsGetHGid.next()){
                hgid = rsGetHGid.getInt(1);
            }
            
        }catch(SQLException sqle){
            logger.error("getHierarchyGid: DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getHierarchyGid:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetHGid, pstmtGetHGid);
        }
        return hgid;
    }
    
    public HashMap getSnapShot(String caseName, String domain) throws VMSDAOException {
        HashMap retMap = new HashMap();
        PreparedStatement pstmtGetSnapShot = null;
        ResultSet rsGetSnapShot = null;
        int caseservgid = 0;
        String port = null;
        int isDel = 0;
        
        try{
            pstmtGetSnapShot = con.prepareStatement(VMSDAOQueries.GET_SNAPSHOT);
            pstmtGetSnapShot.setString(1, caseName);
            pstmtGetSnapShot.setString(2, domain );
            
            rsGetSnapShot = pstmtGetSnapShot.executeQuery();
            
            logger.info("getSnapShot: Getting snapshot for caseName: " + caseName + " , Domain: " + domain + " using query : " + VMSDAOQueries.GET_SNAPSHOT);
            
            while(rsGetSnapShot.next()){
                caseservgid = rsGetSnapShot.getInt(1);
                port = rsGetSnapShot.getString(2);
                isDel = rsGetSnapShot.getInt(3);
                
                retMap.put(caseservgid + ":" + port, new Integer(isDel));
                
                logger.info("getSnapShot: Putting into HashMap: " + caseservgid + ":" + port + " , " + isDel);
            }
            
        }catch(SQLException sqle){
            logger.error("getSnapShot: DB related error: " , sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to get snapshot for case", caseName);
        }catch(Exception ex){
            logger.error("getSnapShot: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetSnapShot, pstmtGetSnapShot);
        }
        
        return retMap;
    }
    
    public int resetFlags(String caseName, String domain) throws VMSDAOException{
        PreparedStatement pstmtResetFlags = null;
        int cntReset = 0;
        
        try{
            pstmtResetFlags = con.prepareStatement(VMSDAOQueries.RESET_DEL_FLAGS);
            pstmtResetFlags.setString(1, caseName);
            pstmtResetFlags.setString(2, domain);
            
            cntReset = pstmtResetFlags.executeUpdate();
            
            logger.info("resetFlags: Resetting flags for caseName: " + caseName + ", domain " + domain + " using query " + VMSDAOQueries.RESET_DEL_FLAGS);
            
        }catch(SQLException sqle){
            logger.error("resetFlags: DB related error: " , sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to reset flags for case", caseName);
        }catch(Exception ex){
            logger.error("resetFlags:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtResetFlags);
        }
        
        return cntReset;
    }
    
    
    public int updateModDate(String caseservgid, String port) throws VMSDAOException {
        int ret = 0;
        PreparedStatement pstmtUpdateDate = null;
        
        try{
            pstmtUpdateDate = con.prepareStatement(VMSDAOQueries.UPDATE_MOD_DATE);
            pstmtUpdateDate.setInt(1, Integer.parseInt(caseservgid));
            pstmtUpdateDate.setString(2, port);
            ret = pstmtUpdateDate.executeUpdate();
            logger.info("updateModDate: " + VMSDAOQueries.UPDATE_MOD_DATE + " Params: " + caseservgid + ", " + port);
            
        }catch(SQLException sqle){
            logger.error("updateModDate: DB related error: " , sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error updating modified date for caseservgid, port ", caseservgid + ": " + port);
        }catch(Exception ex){
            logger.error("updateModDate:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtUpdateDate);
        }
        
        return ret;
    }
    
    public int resetFlagsAll() throws VMSDAOException{
        PreparedStatement pstmtResetFlags = null;
        int cntReset = 0;
        
        try{
            pstmtResetFlags = con.prepareStatement(VMSDAOQueries.RESET_DEL_FLAGS_ALL);
            cntReset = pstmtResetFlags.executeUpdate();
            logger.info("resetFlagsAll: Resetting flags for all using query: " + VMSDAOQueries.RESET_DEL_FLAGS_ALL);
            
        }catch(SQLException sqle){
            logger.error("resetFlagsAll: DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("resetFlagsAll: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtResetFlags);
        }
        
        return cntReset;
    }
    
    public int updateModDate(int compoMapGid) throws VMSDAOException {
        int ret = 0;
        PreparedStatement pstmtUpdateDate = null;
        try{
            pstmtUpdateDate = con.prepareStatement(VMSDAOQueries.UPDATE_MOD_DATE_COMPOGID);
            pstmtUpdateDate.setInt(1, compoMapGid);
            ret = pstmtUpdateDate.executeUpdate();
            logger.info("updateModDate: " + VMSDAOQueries.UPDATE_MOD_DATE_COMPOGID + " Params : " + compoMapGid);
        }catch(SQLException sqle){
            logger.error("updateModDate: DB related error: " , sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Error updating modified date ", compoMapGid + "");
        }catch(Exception ex){
            logger.error("updateModDate:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtUpdateDate);
        }
        
        return ret;
    }
    
    public ArrayList callIncompatibleCompoProc() throws VMSDAOException{
        logger.traceEntry("callIncompatibleCompoProc");
        ArrayList temp = new ArrayList();
        CallableStatement csRunProc = null;
        ResultSet rsRunProc = null;
        int prevCaseGid = 0;
        ArrayList casecomponents = null;
        int casegid;
        
        try{
            csRunProc = con.prepareCall("{call VMS_POPULATE_INVALID_COMPOS()}");
            rsRunProc = csRunProc.executeQuery();
            
            logger.info("callIncompatibleCompoProc: Stored Proc called.");
            
            while(rsRunProc.next()){
                casegid = 0;
                casegid = rsRunProc.getInt("CASE_GID");
                
                VMSCaseComponent casecomponent = new VMSCaseComponent();
                
                casecomponent.setCaseGid(casegid);
                casecomponent.setCaseName(rsRunProc.getString("CASENAME"));
                casecomponent.setComponent( rsRunProc.getString("COMPONENT"));
                casecomponent.setVersion(rsRunProc.getString("VERSION"));
                casecomponent.setServerName(rsRunProc.getString("SERVER"));
                casecomponent.setPort(rsRunProc.getString("PORTDETAILS"));
                
                if((prevCaseGid == 0) || (prevCaseGid != casegid))  {
                    casecomponents = new ArrayList();
                    casecomponents.add(casecomponent);
                }else if(prevCaseGid == casegid) {
                    casecomponents.add(casecomponent);
                }
                
                if(prevCaseGid != casegid){
                    temp.add(casecomponents);
                }
                
                prevCaseGid = casegid;
            }
            
        }catch(SQLException sqle){
            logger.error("callProc: DB related error: " , sqle);
            throw new VMSDAOException(sqle);
        }catch(Exception ex){
            logger.error("callProc: error: " , ex);
            throw new VMSDAOException(ex);
        }finally{
            VMSDAOUtils.freeUp(null, csRunProc);
        }
        
        logger.traceExit("callIncompatibleCompoProc");
        return temp;
    }
    
    public int recordHistory(int cgid, String mesg) throws VMSDAOException{
        int ret = 0;
        PreparedStatement pstmtInsHistory = null;
        ResultSet rsInsHistory = null;
        
        try{
            pstmtInsHistory = con.prepareStatement(VMSDAOQueries.INS_HISTORY);
            pstmtInsHistory.setInt(1, cgid);
            pstmtInsHistory.setString(2, mesg);
            ret = pstmtInsHistory.executeUpdate();
            logger.info("recordHistory: " + VMSDAOQueries.INS_HISTORY + " Params: " + cgid + ", " + mesg);
        }catch(SQLException sqle){
            logger.error("recordHistory: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("recordHistory: error: " + ex.getMessage());
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsInsHistory, pstmtInsHistory);
        }
        return ret;
    }
    
    public ArrayList callMutipleInstallProc() throws VMSDAOException {
        ArrayList temp = new ArrayList();
        CallableStatement csRunProc = null;
        ResultSet rsRunProc = null;
        
        try{
            csRunProc = con.prepareCall("{call VMS_DETECT_MULTIPLE_INSTALLATIONS()}");
            rsRunProc = csRunProc.executeQuery();
            
            logger.info("callMutipleInstallProc: Stored Proc 2 called");
            
            while(rsRunProc.next()){
                VMSCaseComponent casecomponent = new VMSCaseComponent();
                
                casecomponent.setCaseGid(rsRunProc.getInt("CASEGID"));
                casecomponent.setCaseName(rsRunProc.getString("CASENAME"));
                casecomponent.setServerGid(rsRunProc.getInt("SERVERGID"));
                casecomponent.setServerName(rsRunProc.getString("SERVER"));
                casecomponent.setComponent( rsRunProc.getString("COMPONENT"));
                casecomponent.setVersion(rsRunProc.getString("VERSION"));
                casecomponent.setPort(rsRunProc.getString("PORT"));
                
                temp.add(casecomponent);
            }
            
        }catch(SQLException sqle){
            logger.error("callMutipleInstallProc: DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("callMutipleInstallProc:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, csRunProc);
        }
        
        return temp;
    }
    
    public int recordRedundantHistory(String MSG_REDUNDANCY) throws VMSDAOException {
        int ret = 0;
        PreparedStatement pstmtInsHistory = null;
        ResultSet rsInsHistory = null;
        
        try{
            pstmtInsHistory = con.prepareStatement(VMSDAOQueries.INS_REDUNDANT_HISTORY);
            pstmtInsHistory.setString(1, MSG_REDUNDANCY);
            ret = pstmtInsHistory.executeUpdate();
            logger.info("recordRedundantHistory: " + VMSDAOQueries.INS_REDUNDANT_HISTORY + " Params: " + MSG_REDUNDANCY );
        }catch(SQLException sqle){
            logger.error("recordRedundantHistory: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("recordRedundantHistory: MySQL DB related error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsInsHistory, pstmtInsHistory);
        }
        return ret;
    }
    
    public int insRule(int[] compoGIDs, int rule) throws VMSDAOException{
        int ret = 0;
        PreparedStatement pstmtInsRule = null;
        ResultSet rsInsRule = null;
        
        //execute these  queries in Xn
        try{
            //first ins into rule_info
            pstmtInsRule = con.prepareStatement(VMSDAOQueries.INS_RULE);
            
            for(int i = 0 ; i < compoGIDs.length; i++){
                pstmtInsRule.setInt(1, rule);
                pstmtInsRule.setInt(2, compoGIDs[i]);
                pstmtInsRule.setInt(3, VMSStaticParams.USER_ID);
                pstmtInsRule.addBatch();
            }
            
            int[] cntIns = pstmtInsRule.executeBatch();
            
            if(cntIns.length > 0){
                ret = 1;
            }
            
        }catch(SQLException sqle){
            logger.error("insRule: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("insRule:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsInsRule, pstmtInsRule);
        }
        return ret;
    }
    
    public VMSRuleBean getRuleObject(int rulNum) throws VMSDAOException{
        VMSRuleBean rb = new VMSRuleBean();
        PreparedStatement pstmtGetRule = null;
        ResultSet rsGetRule = null;
        
        try{
            pstmtGetRule = con.prepareStatement(VMSDAOQueries.GET_RULE_OBJECT);
            pstmtGetRule.setInt(1, rulNum);
            
            logger.info("getRuleObject: " + VMSDAOQueries.GET_RULE);
            logger.info("getRuleObject: params: " + rulNum);
            rsGetRule = pstmtGetRule.executeQuery();
            
            int rule;
            String name;
            String desc;
            
            if(rsGetRule.next()){
                rule = 0;
                name = null;
                desc = null;
                
                rule = rsGetRule.getInt("RULE");
                name = rsGetRule.getString("NAME");
                desc = rsGetRule.getString("DESCRIPTION");
                
                rb.setRule(rule);
                rb.setRuleName(name);
                rb.setRuleDescription(desc);
            }
            
            
        }catch(SQLException sqle){
            logger.error("getRuleObject: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getRuleObject: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetRule, pstmtGetRule);
        }
        return rb;
    }
    
    //given a ruleNum, returns ArrayList of Compo Version GIDs
    public ArrayList getComponentsGid(int rulNum) throws VMSDAOException{
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetCompo = null ;
        ResultSet rsGetCompo = null;
        
        try{
            pstmtGetCompo = con.prepareStatement(VMSDAOQueries.GET_COMPO_VER_GID_RULE);
            logger.info("getComponentsGid: " + VMSDAOQueries.GET_COMPO_VER_GID_RULE + " param: " + rulNum);
            pstmtGetCompo.setInt(1, rulNum);
            rsGetCompo = pstmtGetCompo.executeQuery();
            int compoVerGid;
            while(rsGetCompo.next()){
                compoVerGid = 0;
                compoVerGid = rsGetCompo.getInt("COMP_VER_GID");
                ret.add(new Integer(compoVerGid));
            }
            
        }catch(SQLException sqle){
            logger.error("getComponentsGID: DB related error: " ,  sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getComponentsGID: error: " ,  ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetCompo, pstmtGetCompo);
        }
        return ret;
    }
    
    public int updateRuleInfo(int rulNum, int userId, String ruleName, String ruleDescription) throws VMSDAOException{
        int ret = 0;
        PreparedStatement pstmtUpdateRuleInfo = null;
        
        try{
            pstmtUpdateRuleInfo = con.prepareStatement(VMSDAOQueries.UPDATE_RULE_INFO);
            pstmtUpdateRuleInfo.setString(1, ruleName);
            pstmtUpdateRuleInfo.setString(2, ruleDescription);
            pstmtUpdateRuleInfo.setInt(3, userId);
            pstmtUpdateRuleInfo.setInt(4, rulNum);
            
            logger.info("updateRuleInfo: " + VMSDAOQueries.UPDATE_RULE_INFO + " Params: " + ruleName + ", " + ruleDescription + ", " + userId + ", " + rulNum );
            ret = pstmtUpdateRuleInfo.executeUpdate();
            
        }catch(SQLException sqle){
            logger.error("updateRuleInfo: DB related error: ", sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("updateRuleInfo:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtUpdateRuleInfo);
        }
        return ret;
    }
    
    public int updateRule(int rulNum, int USER_ID, String ruleName, String ruleDescription, int[] components) throws VMSDAOException{
        int ret = 0;
        try{
            //execute all this in a Xn
            con.setAutoCommit(false);
            //update in vms_rules_info table
            int countInfoUpdate = updateRuleInfo(rulNum, USER_ID, ruleName, ruleDescription);
            //flag old entries in vms_compatiblity_rules table
            int countDel = flagRuleComponents(rulNum);
            //enter new ones
            int countIns = insRule(components, rulNum);
            
            if(countInfoUpdate > 0 && countDel > 0 && countIns > 0){
                con.commit();
                ret = 1;
            }else{
                con.rollback();
            }
            
            con.setAutoCommit(true);
        }catch(SQLException sqle){
            logger.error("updateRule: DB related error: " , sqle);
            throw new VMSDAOException(sqle);
        }catch(Exception ex){
            logger.error("updateRule: error: " , ex);
            throw new VMSDAOException(ex);
        }
        return ret;
    }
    
    private int flagRuleComponents(int rulNum) throws VMSDAOException{
        int ret = 0;
        PreparedStatement pstmtDelRule = null ;
        
        try{
            //1 delete from vms_compatibility_rules
            pstmtDelRule = con.prepareStatement(VMSDAOQueries.DEL_RULE);
            pstmtDelRule.setInt(1, rulNum);
            ret = pstmtDelRule.executeUpdate();
            logger.info("deleteRule: " + VMSDAOQueries.DEL_RULE + " param : " + rulNum);
        }catch(SQLException sqle){
            logger.info("flagRuleComponents: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.info("flagRuleComponents:  error: " , ex);
            ex.printStackTrace();
        }finally{
            VMSDAOUtils.freeUp(null, pstmtDelRule);
        }
        
        return ret;
    }
        
    //get server, case, caseservergid for both BE/FE
    public ArrayList getAllServers(String exedomain) throws VMSDAOException{
        ArrayList temp = new ArrayList();
        PreparedStatement pstmtGetAllServers = null;
        ResultSet rsGetAllServers = null;
        String caseNameBE, serverNameBE, domainBE;
        String caseNameFE, serverNameFE, domainFE;
        int caseServerGIDBE, caseServerGIDFE;
        
        try{
            pstmtGetAllServers = con.prepareStatement(VMSDAOQueries.GET_BEFE_CASE_SERVS);
            logger.info("getAllServers: " + VMSDAOQueries.GET_BEFE_CASE_SERVS);
            if(exedomain.equals("")){
                pstmtGetAllServers.setString(1, VMSStaticParams.BEALIAS);
                pstmtGetAllServers.setString(2, VMSStaticParams.FEALAIS);
//                pstmtGetAllServers.setString(3, VMSStaticParams.BEALIAS);
//                pstmtGetAllServers.setString(4, VMSStaticParams.FEALAIS);
                logger.info("getAllServers: Params: " + VMSStaticParams.BEALIAS + "," + VMSStaticParams.FEALAIS);
            }else{
                pstmtGetAllServers.setString(1, exedomain + VMSStaticParams.BEALIAS);
                pstmtGetAllServers.setString(2, exedomain + VMSStaticParams.FEALAIS);
//                pstmtGetAllServers.setString(3, exedomain + VMSStaticParams.BEALIAS);
//                pstmtGetAllServers.setString(4, exedomain + VMSStaticParams.FEALAIS);
                logger.info("getAllServers: Params: " + exedomain + VMSStaticParams.BEALIAS + "," + exedomain + VMSStaticParams.FEALAIS);
            }
            rsGetAllServers = pstmtGetAllServers.executeQuery();
            
            while(rsGetAllServers.next()){
                caseNameBE = null; serverNameBE = null; domainBE = null;
                caseNameFE = null; serverNameFE = null; domainFE = null;
                caseServerGIDBE = 0; caseServerGIDFE = 0;
                
                VMSCaseServerCombo caseserver = new VMSCaseServerCombo();
                
                caseNameBE = rsGetAllServers.getString("BECASENAME");
                caseserver.setCaseName(caseNameBE);
                serverNameBE = rsGetAllServers.getString("BESERVER");
                if(!rsGetAllServers.wasNull()){
                    caseserver.setServerName(serverNameBE);
                }
                domainBE = rsGetAllServers.getString("BEDOMAIN");
                caseserver.setDomain(domainBE);
                caseServerGIDBE = rsGetAllServers.getInt("BECASESERVERGID");
                caseserver.setCaseServGID(caseServerGIDBE);
                caseNameFE = rsGetAllServers.getString("FECASENAME");
                caseserver.setCaseNameFE(caseNameFE);
                serverNameFE = rsGetAllServers.getString("FESERVER");
                if(!rsGetAllServers.wasNull()){
                    caseserver.setServerNameFE(serverNameFE);
                }
                domainFE = rsGetAllServers.getString("FEDOMAIN");
                caseserver.setDomainFE(domainFE);
                caseServerGIDFE = rsGetAllServers.getInt("FECASESERVERGID");
                caseserver.setCaseServGIDFE(caseServerGIDFE);
                
                temp.add(caseserver);
            }
            
        }catch(SQLException sqle){
            logger.error("getAllServers: DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getAllServers: General error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetAllServers, pstmtGetAllServers);
        }
        
        return temp;
    }
    
    public int checkRuleNameExists(String ruleName) throws VMSDAOException{
        int ret = 0;
        Statement stmtcheckRuleName = null;
        ResultSet rscheckRuleName = null;
        String qry = null;
        
        qry = "SELECT GID FROM vms_rules_info WHERE NAME = '" + ruleName + "'";
        logger.info("checkRuleNameExists: " + qry);
        
        try{
            stmtcheckRuleName = con.createStatement();
            rscheckRuleName = stmtcheckRuleName.executeQuery(qry);
            
            if(rscheckRuleName.next()){
                ret = rscheckRuleName.getInt("GID");
            }
            logger.info("checkRuleNameExists : RET: " + ret);
        }catch(SQLException sqle){
            logger.error("checkRuleNameExists: MySQL DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("checkRuleNameExists:  error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            try{
                VMSDAOUtils.cleanUp(rscheckRuleName, stmtcheckRuleName);
            }catch(SQLException sqle){
                logger.error("checkRuleNameExists: ", sqle);
                throw new VMSDAOException(sqle.getMessage());
            }
        }
        
        return ret;
    }
    
   public int resetDeleteFlags(String exedomain) throws VMSDAOException{
        PreparedStatement pstmtResetFlags = null;
        int count = 0;
        
        try{
            pstmtResetFlags = con.prepareStatement(VMSDAOQueries.RESET_CASE_FLAGS);
            logger.info("resetDeleteFlags: " + VMSDAOQueries.RESET_CASE_FLAGS);
            if(exedomain.equals("")){
                pstmtResetFlags.setString(1, VMSStaticParams.BEALIAS);
                pstmtResetFlags.setString(2, VMSStaticParams.FEALAIS);
                logger.info("resetDeleteFlags: Params " + VMSStaticParams.BEALIAS + "," + VMSStaticParams.FEALAIS);
            }else{
                pstmtResetFlags.setString(1, exedomain + VMSStaticParams.BEALIAS);
                pstmtResetFlags.setString(2, exedomain + VMSStaticParams.FEALAIS);
                logger.info("resetDeleteFlags: Params " + exedomain + VMSStaticParams.BEALIAS + "," + exedomain + VMSStaticParams.FEALAIS);
            }
            count = pstmtResetFlags.executeUpdate();
        }catch(SQLException sqle){
            logger.error("resetDeleteFlags: DB related error: ", sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("resetDeleteFlags:  error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtResetFlags);
        }
        return count;
    }

    public int updateFlag(int caseGid) throws VMSDAOException{
       PreparedStatement pstmtResetFlag = null;        
        int count = 0;
        
        try{
            pstmtResetFlag = con.prepareStatement(VMSDAOQueries.RESET_CASE_FLAG); 
            pstmtResetFlag.setInt(1, caseGid);
            count = pstmtResetFlag.executeUpdate();           
            logger.info("updateFlag: " + VMSDAOQueries.RESET_CASE_FLAG + " Params : " + caseGid);
        }catch(SQLException sqle){
            logger.error("updateFlag: DB related error: ", sqle);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2, VMSStaticParams.ERROR, "Unable to update flag for this case", caseGid + "");
        }catch(Exception ex){
            logger.error("updateFlag:  error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtResetFlag);
        }
        return count;
    }
    
    public ArrayList getServerComponents(int icasegid, boolean valid) throws VMSDAOException {
        ArrayList ret = new ArrayList();
        PreparedStatement pstmtGetServerComponents = null;
        ResultSet rsGetServerComponents = null;
        String query = null;
        String component;
        String serverName;
        int serverGid;
        String caseName;
        int caseGid;
        String port;
        int maj;
        int min;
        int build;
        int patch;
        int compoVerGid;
        int hgid;
        String domain;
        
        try{
            if (valid) {
                query = VMSDAOQueries.GET_VALIDS;
            } else {
                query = VMSDAOQueries.GET_INVALIDS;
            }
            
            pstmtGetServerComponents = con.prepareStatement(query);
            pstmtGetServerComponents.setInt(1, icasegid);
            pstmtGetServerComponents.setInt(2, icasegid);
            pstmtGetServerComponents.setInt(3, icasegid);
            
            logger.debug("getServerComponents:Query:" + query);
            logger.debug("Parameters:" + icasegid + "," + icasegid + "," + icasegid);
            
            rsGetServerComponents = pstmtGetServerComponents.executeQuery();
            
            while(rsGetServerComponents.next()){
                VMSServerComponent sc = new VMSServerComponent();
                component = null;
                serverName = null;
                serverGid = 0;
                caseName = null;
                caseGid = 0;
                port = null;
                maj = 0;
                min = 0;
                build = 0;
                patch = 0;
                compoVerGid = 0;
                hgid = 0;
                domain = null;
                
                caseName = rsGetServerComponents.getString("CASENAME");
                hgid = rsGetServerComponents.getInt("HIERARCHY_GID");
                caseGid = rsGetServerComponents.getInt("C.GID");
                component = rsGetServerComponents.getString("COMPONENT");
                serverGid = rsGetServerComponents.getInt("S.GID");
                serverName = rsGetServerComponents.getString("SERVER");
                port = rsGetServerComponents.getString("PORT");
                maj = rsGetServerComponents.getInt("MAJOR");
                min = rsGetServerComponents.getInt("MINOR");
                build = rsGetServerComponents.getInt("BUILD");
                patch = rsGetServerComponents.getInt("PATCH");
                compoVerGid = rsGetServerComponents.getInt("COMPO_VER_GID");
                domain = rsGetServerComponents.getString("DOMAIN");
                
                sc.setCaseGid(caseGid);
                sc.setHierarchyGid(hgid);
                sc.setCaseName(caseName);
                sc.setComponent(component);
                sc.setServerGid(serverGid);
                sc.setServerName(serverName);
                sc.setPort(port);
                sc.setMaj(maj);
                sc.setMin(min);
                sc.setBuild(build);
                sc.setPatch(patch);
                sc.setCompoVerGid(compoVerGid);
                sc.setDomain(domain);
                
               // sc.setVersion();
                
                ret.add(sc);
            }
        }catch(SQLException sqle){
            logger.error("getServerComponents:SQLException in getServerComponents: ", sqle);
            throw new VMSDAOException(sqle);
        }catch(Exception ex){
            logger.error("getServerComponents:Exception in getServerComponents: ", ex);
            throw new VMSDAOException(ex);
        }finally{
            VMSDAOUtils.freeUp(rsGetServerComponents, pstmtGetServerComponents);
        }
        return ret;
    }

    public int logDeviation(String devType, String devMessage, String casename) throws VMSDAOException{        
        PreparedStatement pstmtInsDeviation = null;        
        int count = 0;
        java.util.Date crawldate = null;
      
        SimpleDateFormat sdf = new SimpleDateFormat(VMSStaticParams.DATE_FORMATTER); 
        crawldate = new java.util.Date();
        
        try{
            pstmtInsDeviation = con.prepareStatement(VMSDAOQueries.INS_CRAWL_DEVIATION);            
            pstmtInsDeviation.setTimestamp(1, new java.sql.Timestamp(crawldate.getTime()));
            pstmtInsDeviation.setString(2, devType);
            pstmtInsDeviation.setString(3, devMessage);
            pstmtInsDeviation.setString(4, casename);
            logger.info("logDeviation: " + VMSDAOQueries.INS_CRAWL_DEVIATION + " Params : " + crawldate + "," + devType + "," + devMessage + ", " + casename);
            count = pstmtInsDeviation.executeUpdate();                       
        }catch(SQLException sqle){
            logger.error("logDeviation: DB related error: ", sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("logDeviation:  error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtInsDeviation);
        }
        return count;
    }
     
    public int insCrawlEntry(long crawldate, long date, String string) throws VMSDAOException{
        int retCount = 0;
        PreparedStatement pstmtInsCrawl = null;        
        int count = 0;
      
        SimpleDateFormat sdf = new SimpleDateFormat(VMSStaticParams.DATE_FORMATTER);        
        
        try{
            pstmtInsCrawl = con.prepareStatement(VMSDAOQueries.INS_CRAWL_ENTRY);            
            pstmtInsCrawl.setTimestamp(1, new java.sql.Timestamp(crawldate));
            pstmtInsCrawl.setTimestamp(2, new java.sql.Timestamp(date));
            pstmtInsCrawl.setString(3, string);
            logger.info("insCrawlEntry: " + VMSDAOQueries.INS_CRAWL_ENTRY + " Params : " + crawldate + "," + date + "," + string);
            count = pstmtInsCrawl.executeUpdate();                       
        }catch(SQLException sqle){
            logger.error("insCrawlEntry: DB related error: ", sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("insCrawlEntry:  error: ", ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtInsCrawl);
        }
        return retCount;
    }

    public int updateCrawlEntry(long crawldate, long date, String string) throws VMSDAOException{
        int retCount = 0;         
        PreparedStatement pstmtCrawlEntry = null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(VMSStaticParams.DATE_FORMATTER);   
        
        try{
            pstmtCrawlEntry = con.prepareStatement(VMSDAOQueries.UPDATE_CRAWL_ENTRY);
            pstmtCrawlEntry.setTimestamp(1, new java.sql.Timestamp(date));
            pstmtCrawlEntry.setTimestamp(2, new java.sql.Timestamp(crawldate));
            pstmtCrawlEntry.setString(3, string);
            logger.info("updateCrawlEntry: " + VMSDAOQueries.UPDATE_CRAWL_ENTRY + " Params : " + date + "," + crawldate + "," + string);
            retCount = pstmtCrawlEntry.executeUpdate();            
        }catch(SQLException sqle){
            logger.error("updateCrawlEntry: DB related error: ",  sqle);
            throw new VMSDAOException(sqle.getMessage())           ;
        }catch(Exception ex){
            logger.error("updateCrawlEntry:  error: ",  ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(null, pstmtCrawlEntry);
        }
                
        return retCount;
    }
}
