/*
 * VMSVersionHelper.java
 *
 * Created on January 17, 2008, 8:29 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.web.helper;

import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.dao.VMSConnectionFactory;
import com.stratify.apps.vms.dao.VMSDAOFactory;
import com.stratify.apps.vms.dao.common.VMSDAOQueries;
import com.stratify.apps.vms.dao.VMSMyDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.stratify.common.logging.Logger;

/**
 *
 * @author ravikishores
 */
public class VMSVersionHelper extends VMSCommonHelper{
    static Logger logger = Logger.getLogger(VMSVersionHelper.class.getName());
    
    /** Creates a new instance of VMSVersionHelper */
    public VMSVersionHelper() {
    }
    
    public ArrayList getVersionComponents(String maj, String min, String bld, String patch, int compoGid)  {
        ArrayList ret = new ArrayList();
        
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
                        
            //get the versionBeans constructed from  MySQL
            ret = addDAO.getVersionComponents(maj, min, bld, patch, compoGid);
        }catch(VMSDAOException vde){
            logger.error("getVersionComponents: Prob in versioncomponents : " , vde);            
        }catch(Exception ex){
            logger.error("getVersionComponents: Prob in versioncomponents : " , ex);            
        }finally{
            if(null != conAdd){
                try{
                conAdd.close();
                }catch(Exception ex){
                    
                }
            }
        }
        //return list of beans
        return ret;
    }
    
    
    public ArrayList getVersions(int i) {
        ArrayList ret = new ArrayList();
        
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        //get servers from vms_servers
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            //get the serverBeans constructed from  MySQL
            ret = addDAO.getVersion(i);
            logger.info("getVersions: Number of version components got:" + ret.size());
        }catch(VMSDAOException vde){
            logger.error("getVersions: Prob in versions : ", vde);            
        }catch(Exception ex){
            logger.error("getVersions: Prob in versions : ", ex);            
        }finally{
            if(null != conAdd){
                try{
                conAdd.close();
                }catch(Exception ex){
                    
                }
            }
        }                
        //return list of beans
        return ret;
    }
    
}
