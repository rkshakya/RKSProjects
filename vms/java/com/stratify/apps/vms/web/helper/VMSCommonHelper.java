/*
 * VMSCommonHelper.java
 *
 * Created on December 7, 2007, 3:12 AM
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
public class VMSCommonHelper {
    
    static Logger logger = Logger.getLogger(VMSCommonHelper.class.getName());
    /**
     * Creates a new instance of VMSCommonHelper
     */
    public VMSCommonHelper() {}           
    
    public ArrayList getCompoBeans(){
        ArrayList ret = new ArrayList();
        
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
                
        //get cases from DBH DB
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            //get the compoBeans constructed from  MySQL
            ret = addDAO.getCompoBeans();                        
        }catch(SQLException sqle){
            logger.error("getCompoBeans: Error : ", sqle);            
        }catch(Exception ex){
            logger.error("getCompoBeans: Error: ", ex);
        }finally{
            if(null != conAdd){
                try{
                conAdd.close();
                } catch(Exception ex){
                    
                }
            }
        }
        
        //return list of beans
        return ret;
    }
    
    public ArrayList getCompoBeans(int[] compoVerGIDs) {
        ArrayList ret = new ArrayList();
                
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
                       
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            //get the compoBeans constructed from  MySQL
            ret = addDAO.getCompoBeans(compoVerGIDs);                                               
        }catch(SQLException sqle){
            logger.error("getCompoBeans: Error : " , sqle);            
        }catch(Exception ex){
            logger.error("getCompoBeans: Error : " , ex);
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
    
    public ArrayList getVersionComponents(int caseGid, int componentGid) {
        ArrayList ret = new ArrayList();
        
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        //get cases from vms_cases
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            //get the VersionComponentBean constructed from  MySQL
            if( (caseGid > 0) && (componentGid > 0) ){
                ret = addDAO.getVersionComponents(caseGid, componentGid);
            }else if((caseGid == 0) && (componentGid > 0)){
                ret = addDAO.getVersionComponents(componentGid, "component");
            }else if ((caseGid > 0) && (componentGid == 0)){
                ret = addDAO.getVersionComponents(caseGid, "case");
            }
            
        }catch(VMSDAOException vde){
            logger.error("getVersionComponents: Error : ", vde);            
        }catch(Exception ex){
            logger.error("getVersionComponents: Error : ", ex); 
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
       
    public ArrayList getCases(){
        ArrayList ret = new ArrayList();
                
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        //get cases from vms_cases
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            //get the casBeans constructed from  MySQL
            ret = addDAO.getCases();
            
        }catch(SQLException sqle){
            logger.error("getCases: Error : " , sqle);            
        }catch(Exception ex){
            logger.error("getCases: Error : " , ex);  
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
     
    public ArrayList getComponentList() {
        ArrayList ret = new ArrayList();
                
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        //get cases from vms_cases
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            //get the casBeans constructed from  MySQL
            ret = addDAO.getComponentList();
            
        }catch(SQLException sqle){
            logger.error("getComponentList: Error : " , sqle);            
        }catch(Exception ex){
            logger.error("getComponentList: Error : " , ex);
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
