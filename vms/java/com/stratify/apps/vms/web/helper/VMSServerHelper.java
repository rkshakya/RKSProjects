/*
 * VMSServerHelper.java
 *
 * Created on January 17, 2008, 8:37 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.web.helper;

import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.dao.VMSConnectionFactory;
import com.stratify.apps.vms.dao.VMSDAOFactory;
import com.stratify.apps.vms.dao.VMSMyDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.stratify.common.logging.Logger;

/**
 *
 * @author ravikishores
 */
public class VMSServerHelper extends VMSCommonHelper{    
    static Logger logger = Logger.getLogger(VMSServerHelper.class.getName());
    /** Creates a new instance of VMSServerHelper */
    public VMSServerHelper() {
    }
    
    public ArrayList getServers() throws SQLException {
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
            ret = addDAO.getServers();            
            logger.info("getServers: Number of servers got:" + ret.size());
        }catch(SQLException sqle){            
            logger.error("getServers:Error : " , sqle);            
        }catch(Exception ex){
            logger.error("getServers:Error : " , ex);
        }finally{
            if(null != conAdd){
                conAdd.close();
            }
        }
        
        
        //return list of beans
        return ret;
    }
    
    public ArrayList getCaseComponents(int serverGid) {
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
            ret = addDAO.getCaseComponents(serverGid);
        }catch(VMSDAOException vde){                        
            logger.error("getCaseComponents: Error : " , vde);
        }catch(Exception ex){
            logger.error("getCaseComponents: Error : " , ex);
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
