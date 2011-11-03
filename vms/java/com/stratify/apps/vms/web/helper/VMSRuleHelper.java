/*
 * VMSRuleHelper.java
 *
 * Created on January 17, 2008, 8:47 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.web.helper;

import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.vos.VMSRuleBean;
import com.stratify.apps.vms.dao.VMSConnectionFactory;
import com.stratify.apps.vms.dao.VMSDAOFactory;
import com.stratify.apps.vms.dao.VMSMyDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.stratify.common.logging.Logger;
import java.util.Arrays;

/**
 *
 * @author ravikishores
 */
public class VMSRuleHelper extends VMSCommonHelper{
    static Logger logger = Logger.getLogger(VMSRuleHelper.class.getName());
    
    /** Creates a new instance of VMSRuleHelper */
    public VMSRuleHelper() {
    }
    
    public ArrayList getCompoBeans(int rulNum) throws SQLException {
        ArrayList ret = new ArrayList();
        
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
                
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            //get the compoBeans constructed from  MySQL                                    
            ret = addDAO.getCompoBeans(rulNum);                        
            
        }catch(SQLException sqle){            
            logger.error("getCompoBeans: Prob in GETCompoBeans : " , sqle);            
        }catch(Exception ex){
            logger.error("getCompoBeans: Prob in GETCompoBeans : " , ex);            
        }finally{
            if(null != conAdd){
                conAdd.close();
            }
        }
        
        
        //return list of beans
        return ret;
    }
    
    public ArrayList getRules() throws SQLException {
        ArrayList ret = new ArrayList();
        
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
                
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);            
            ret = addDAO.getRules();            
        }catch(SQLException sqle){            
            logger.error("getRules: Prob in rules : " , sqle);
        }catch(Exception ex){            
            logger.error("getRules: Prob in rules : " , ex);
        }finally{
            if(null != conAdd){
                conAdd.close();
            }
        }               
        //return list of beans
        return ret;
    }
    
    public int deleteRule(int rulNum) throws SQLException {
        int ret = 0;
        
        //create connection obj
        Connection conMax = null;
        VMSMyDAO maxDAO = null;
                
        try {
            //param 1 to connect to Destination MYSQL
            conMax = VMSConnectionFactory.getMyConnection(1);
            maxDAO = VMSDAOFactory.getVerMyDAO(conMax);        
            ret = maxDAO.deleteRule(rulNum);
            
        }catch(SQLException sqle){
            logger.error("deleteRule: Prob in DeleteRule : ", sqle);            
        }catch(Exception ex){
            logger.error("deleteRule: Prob in DeleteRule : " , ex);            
        }finally{
            if(null != conMax){
                conMax.close();
            }
        }
        
        return ret;
    }
    
    public ArrayList getOtherComponents(int rulNum) throws SQLException {
        ArrayList ret = new ArrayList();
        
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            //get the compoBeans constructed from  MySQL                        
            ret = addDAO.getOtherCompoBeans(rulNum);            
            
        }catch(SQLException sqle){                        
            logger.error("getOtherComponents: Error : " , sqle);
        }catch(Exception ex){
            logger.error("getOtherComponents: Error : " , ex);
        }finally{
            if(null != conAdd){
                conAdd.close();
            }
        }
        //return list of beans
        return ret;
    }
    
    public int insRuleComponents(int[] compoGIDs, int rule) throws SQLException {
        int ret = 0;
        //create connection obj
        Connection conMax = null;
        VMSMyDAO maxDAO = null;
        int flagRuleExistence = 0;
        
        try {
            //param 1 to connect to Destination MYSQL
            conMax = VMSConnectionFactory.getMyConnection(1);
            maxDAO = VMSDAOFactory.getVerMyDAO(conMax);
            
            //check if rule already exists
            flagRuleExistence = maxDAO.checkRuleExists(compoGIDs);
                        
            logger.info("insRuleComponents: RULEFLAG :" + flagRuleExistence);            
            ret = maxDAO.insRule(compoGIDs, rule);
            
        }catch(SQLException sqle){                        
            logger.error("insRuleComponents: Error : " , sqle);
        }catch(Exception ex){
            logger.error("insRuleComponents: Error : " , ex);
        }finally{
            if(null != conMax){
                conMax.close();
            }
        }
        
        return ret;
    }
    
    public int insRule(int[] compoGIDs, String ruleName, String ruleDescription) throws SQLException {
        int ret = 0;
        //create connection obj
        Connection conMax = null;
        VMSMyDAO maxDAO = null;
        int rule = 0;
        int flagRuleExistence = 0;
        int flagRuleNameExistence = 0;
        
        try {
            //param 1 to connect to Destination MYSQL
            conMax = VMSConnectionFactory.getMyConnection(1);
            maxDAO = VMSDAOFactory.getVerMyDAO(conMax);
            //fetch the max rule no from DB - do this inside transaction
            //rule = maxDAO.getMaxRule();
            
            //check if rule already exists - components exist            
            flagRuleExistence = maxDAO.checkRuleExists(compoGIDs);                                                
            logger.info("insRule: RULEFLAG :" + flagRuleExistence);
            
            //check if rulename already exists
            flagRuleNameExistence = maxDAO.checkRuleNameExists(ruleName);
            logger.info("insRule: RULENAMEFLAG: " + flagRuleNameExistence);
            
            if(flagRuleExistence == 0 && flagRuleNameExistence == 0){
                ret = maxDAO.insRule(compoGIDs, ruleName, ruleDescription);
            }else{
                ret = -1;
            }
            
            logger.info("inRule: RETVAL: " + ret);
            
        }catch(SQLException sqle){
            logger.error("insRule: Prob in GetMAXRULE : " + sqle.getMessage());            
        }catch(Exception ex){
            logger.error("insRule: Prob in GetMAXRULE : " + ex.getMessage());            
        }finally{
            if(null != conMax){
                conMax.close();
            }
        }
        
        return ret;
    }
    
    public int getMaxRule() throws SQLException {
        int ret = 0;        
        //create connection obj
        Connection conMax = null;
        VMSMyDAO maxDAO = null;
                
        try {
            //param 1 to connect to Destination MYSQL
            conMax = VMSConnectionFactory.getMyConnection(1);
            maxDAO = VMSDAOFactory.getVerMyDAO(conMax);                        
            ret = maxDAO.getMaxRule();            
        }catch(SQLException sqle){                        
            logger.error("getMaxRule: Prob in GetMAXRULE : " , sqle);
        }catch(Exception ex){
            logger.error("getMaxRule: Prob in GetMAXRULE : " , ex);
        }finally{
            if(null != conMax){
                conMax.close();
            }
        }
        
        return ret;
    }
    
    public VMSRuleBean getRuleObject(int rulNum) throws SQLException{
        VMSRuleBean ret = new VMSRuleBean();
        
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);            
            ret = addDAO.getRuleObject(rulNum);            
        }catch(SQLException sqle){            
            logger.error("getRuleObject: Prob in rules : " , sqle);
        }catch(Exception ex){            
            logger.error("getRulesObject: Prob in rules : " , ex);
        }finally{
            if(null != conAdd){
                conAdd.close();
            }
        }
        
        return ret;
    }
                
    public int editRule(int rulNum, int[] components, String ruleName, String ruleDescription) throws SQLException {
        int ret = 0;
        ArrayList oldcomponents = null;
        Integer[] oldComponents;
        int[] ioldComponents;
        
        //create connection obj
        Connection conAdd = null;
        VMSMyDAO addDAO = null;
        
        try {
            //param 1 to connect to Destination MYSQL
            conAdd = VMSConnectionFactory.getMyConnection(1);
            addDAO = VMSDAOFactory.getVerMyDAO(conAdd);
            
            //fetch old components list(sorted) from DB
            oldcomponents = addDAO.getComponentsGid(rulNum);
            oldComponents = (Integer[])oldcomponents.toArray(new Integer[oldcomponents.size()]);
            ioldComponents = new int[oldcomponents.size()];
            for(int count = 0; count < oldComponents.length; count++){
                ioldComponents[count] = oldComponents[count].intValue();
            }
            
            //sort components for comparision
            Arrays.sort(components);
            
            //if new components and old are same do nothing in vms_compatibility_rules table
            if(Arrays.equals(ioldComponents, components)){
                //just update in vms_rules_info
                ret = addDAO.updateRuleInfo(rulNum, VMSStaticParams.USER_ID, ruleName, ruleDescription);
            }else{                
                ret = addDAO.updateRule(rulNum, VMSStaticParams.USER_ID, ruleName, ruleDescription, components);
            }
            
            
        }catch(SQLException sqle){            
            logger.error("editRule: error : " , sqle);
        }catch(Exception ex){            
            logger.error("editRule: error : " , ex);
        }finally{
            if(null != conAdd){
                conAdd.close();
            }
        }
        
        return ret;
    }
}
