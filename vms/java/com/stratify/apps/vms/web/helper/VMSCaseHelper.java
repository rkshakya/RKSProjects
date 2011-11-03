/*
 * VMSCaseHelper.java
 *
 * Created on January 17, 2008, 9:46 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.web.helper;

import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import com.stratify.apps.vms.common.vos.VMSCaseBean;
import com.stratify.apps.vms.common.vos.VMSServerComponent;
import com.stratify.apps.vms.dao.VMSConnectionFactory;
import com.stratify.apps.vms.dao.VMSDAOFactory;
import com.stratify.apps.vms.dao.common.VMSDAOUtils;
import com.stratify.apps.vms.dao.VMSMyDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import com.stratify.common.logging.Logger;

/**
 *
 * @author ravikishores
 */
public class VMSCaseHelper extends VMSCommonHelper{
    static Logger logger = Logger.getLogger(VMSCaseHelper.class.getName());
    
    /** Creates a new instance of VMSCaseHelper */
    public VMSCaseHelper() {
    }
    
    public TreeMap getServerComponents(int[] caseGid) throws SQLException {
        TreeMap ret = new TreeMap();               
        ArrayList components = new ArrayList();
                       
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        //get servers from vms_servers
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            //get the caseBeans constructed from  MySQL
            components = addDAO.getServerComponents(caseGid);
        }catch(SQLException sqle){                        
            logger.error("getServerComponents: Prob in servercomponents : " , sqle);
        }catch(Exception ex){
            logger.error("getServerComponents: Prob in servercomponents : " , ex);
        }finally{
            if(null != conAdd){
                conAdd.close();
            }
        }
        
        //add to Map
        for(int j = 0 ; j < components.size(); j++){
            String caseName = null;
            ArrayList caseSpecificComponents = null;
            VMSServerComponent sco = (VMSServerComponent)components.get(j);
            caseName = sco.getCaseName();
            
            if(ret.containsKey(caseName)){
                caseSpecificComponents = (ArrayList)ret.get(caseName);
                caseSpecificComponents.add(sco);
                ret.put(caseName, caseSpecificComponents);
                
            }else{
                caseSpecificComponents = new ArrayList();
                caseSpecificComponents.add(sco);
                ret.put(caseName, caseSpecificComponents);
            }
        }
        
        //return map of caseGid - list of ServerBeans
        return ret;
    }
    
    public TreeMap getOutCastes(int[] caseGids) throws SQLException {
        TreeMap ret = new TreeMap();
        HashMap caseMap = new HashMap();
        ArrayList gids = new ArrayList();
        ArrayList caseList = new ArrayList();
        
        caseList = getCases();
        
        logger.info("getOutCastes: Cases length: " + caseList.size());
        
        //put into caseMap
        for(int m = 0; m < caseList.size(); m++){
            VMSCaseBean cBean = (VMSCaseBean)caseList.get(m);
            caseMap.put( new Integer(cBean.getCaseGid()), cBean.getCaseName() );
        }
        
        
        if( (caseGids.length == 1)&& (caseGids[0] == -1) ){
            //fetch all distinct caseGids
            Set set = caseMap.keySet();
            gids = new ArrayList(set);
                        
            logger.info("getOutCastes: Inside All Cases Block in getOutcastes");
        }else{
            for(int m =0; m < caseGids.length; m++){
                gids.add(new Integer(caseGids[m]));
            }            
            logger.info("getOutCastes: Inside Selected Cases Block in getOutcastes");
        }
        
        //fetch outcaste list for each case
        for(int i = 0 ; i < gids.size(); i++ )        {
            Connection conAdd = null;
            VMSMyDAO addDAO = null;
            ArrayList retList = new ArrayList();
            
            
            try {
                //param 1 to connect to Destination MYSQL
                conAdd = VMSConnectionFactory.getMyConnection(1);
                addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
                retList = addDAO.getOutCastes( ((Integer)gids.get(i)).intValue()  );
            }catch(SQLException sqle){                                
                logger.info("getOutCastes:Prob in getOutCastes : " + sqle.getMessage() + " for caseGid : " + ((Integer)gids.get(i)).intValue() );
            }catch(Exception ex){
                logger.info("getOutCastes: Error: ", ex);
            }finally{
                if(null != conAdd){
                    conAdd.close();
                }
            }
            
            ret.put( caseMap.get( (Integer)gids.get(i) ), retList);
        }
        
        return ret;
    }
    
    public ArrayList getServerComponentsValidInvalid(int[] caseGids) throws SQLException {
        //returns ArrayList of 2 TreeMaps - one with valid components, another with invalid ones
        ArrayList ret = new ArrayList();
        TreeMap retValids = new TreeMap();
        TreeMap retInvalids = new TreeMap();
        
        ArrayList caseList = null;
        TreeMap caseLookUp = new TreeMap();
        ArrayList alCaseGids = new ArrayList();
        
        ArrayList componentsValid = null;
        ArrayList componentsInvalid = null;
        
        if( (caseGids.length == 1) && (caseGids[0] == -1) ){
            //            fetch all the distinct caseGids
            caseList = getCases();
            for(int m = 0; m < caseList.size(); m++){
                VMSCaseBean cBean = (VMSCaseBean) caseList.get(m);
                caseLookUp.put(new Integer(cBean.getCaseGid()), cBean.getCaseName());
                alCaseGids.add(new Integer(cBean.getCaseGid()));
            }            
            
        }else{                       
            caseList = getCases();
            for(int m = 0; m < caseList.size(); m++){
                VMSCaseBean cBean = (VMSCaseBean) caseList.get(m);
                caseLookUp.put(new Integer(cBean.getCaseGid()), cBean.getCaseName());
            }
            //put the selected casegids in List
            for(int k = 0; k < caseGids.length; k++){
                alCaseGids.add(new Integer(caseGids[k]) );
            }
        }
        
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        //get servers from vms_servers
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            
        }catch(SQLException sqle){            
            logger.error("getServerComponentsValidInvalid: Prob in servercomponents : " , sqle);
        }catch(Exception ex){
            logger.error("getServerComponentsValidInvalid: Prob in servercomponents : " , ex);
        }
        
        int icaseGid;
        //iterate over all caseGids to find the valid components
        for(int m = 0 ; m < alCaseGids.size(); m++){
            try{
                icaseGid = ((Integer)alCaseGids.get(m)).intValue();
                //get valid serverComponents for each case
                componentsValid = addDAO.getServerComponents(icaseGid, true);
                //put casename and components list in map
                retValids.put( caseLookUp.get( (Integer)alCaseGids.get(m) ), componentsValid );
                
                //get invalid serverComponents for each case
                componentsInvalid = addDAO.getServerComponents(icaseGid, false);
                //put casename and components list in map
                retInvalids.put( caseLookUp.get( (Integer)alCaseGids.get(m) ), componentsInvalid );
            }catch(VMSDAOException vde){
                logger.error("getServerComponentsValidInvalid: Error: " , vde);
                continue;
            }
        }
        
        if(null != conAdd){
            conAdd.close();
        }
        
        ret.add(0, retValids);
        ret.add(1, retInvalids);
        
        return ret;
    }
    
    public int[] getCaseGid(String caseName) throws VMSSysException{
        int ret[] = {0};
        int iret = 0;
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        try {
            //param 1 to connect to VMS MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            
            iret = addDAO.resolveCaseName(caseName);
            logger.info("getCaseGid: CASENAME :" +  caseName + "Case GID " + iret );
            if(null != conAdd){
                conAdd.close();
            }
        }catch(VMSDAOException vde){
            logger.error("getCaseGid: Prob in getCaseGid : " , vde);
            throw new VMSSysException(vde.getMessage());
        }catch(Exception ex){
            logger.error("getCaseGid: Prob in getCaseGid : " , ex);
            throw new VMSSysException(ex.getMessage());
        }finally{
            if(null != conAdd){
                try{
                    conAdd.close();
                }catch(Exception ex){
                    logger.error("getCaseGid: Prob in getCaseGid : " , ex);
                    throw new VMSSysException(ex.getMessage());
                }
            }
        }
        
        ret[0] = iret;
        return ret;
    }
    
}
