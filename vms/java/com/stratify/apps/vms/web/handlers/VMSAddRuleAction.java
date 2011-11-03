/*
 * VMSAddRuleAction.java
 *
 * Created on December 7, 2007, 2:44 AM
 */

package com.stratify.apps.vms.web.handlers;

import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.web.helper.VMSCommonHelper;
import com.stratify.apps.vms.web.forms.VMSAddRuleForm;
import com.stratify.apps.vms.web.helper.VMSRuleHelper;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.MessageResources;
import com.stratify.common.logging.Logger;
/**
 *
 * @author ravikishores
 * @version
 */

public class VMSAddRuleAction extends Action {
    
    private final static String SUCCESS = "success";
    private final static String VIEW = "view";
    private final static String FAILURE = "failure";
    private Logger logger = Logger.getLogger(VMSAddRuleAction.class.getName());
    
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response) {
        ActionForward af = null;
        
        try{
            VMSAddRuleForm addForm = (VMSAddRuleForm) form;
            
            //if the AddRule form is submitted with values
            //insert the values into DB
            
            int[] compoGIDs = null;
            String actType = null;
            String ruleName = null;
            String ruleDescription = null;
            
            compoGIDs = addForm.getSelectList();
            actType = addForm.getActionType();
            ruleName = addForm.getRuleName();
            ruleDescription = addForm.getRuleDescription();
            
            logger.debug("execute: ACTTYPE: " + actType);
            VMSRuleHelper rulehelper = new VMSRuleHelper();
            
            if ("add".equalsIgnoreCase(actType)) {               
                //if not existing, insert
                //logic to check if the rule already exists in DB in helper
                int resIns = rulehelper.insRule(compoGIDs, ruleName, ruleDescription);
                ArrayList insBeans = null;
                insBeans = rulehelper.getCompoBeans(compoGIDs);
                ArrayList rules = null;
                rules = rulehelper.getRules();
                
                if(resIns > 0){
                    request.setAttribute("INSBEANS", insBeans);
                    request.setAttribute("INSCOUNT", new Integer(resIns));
                    request.setAttribute("RULES", rules);
                    request.setAttribute("MESSAGE", "New rule added for " + resIns + " components.");
                    af =  mapping.findForward(SUCCESS);
                }else{
                    ArrayList compoBeans = null;
                    compoBeans = rulehelper.getCompoBeans();                   
                    request.setAttribute("COMPOBEANS", compoBeans);
                    request.setAttribute("MESSAGE", VMSStaticParams.RULE_NOT_ADDED);
                    af =  mapping.findForward(VIEW);
                }
            } else if("view".equalsIgnoreCase(actType) || actType == null || "".equals(actType)) {
                ArrayList compoBeans = null;
                compoBeans = rulehelper.getCompoBeans();
                request.setAttribute("COMPOBEANS", compoBeans);
                af =  mapping.findForward(VIEW);
            } else {
                ArrayList compBeans = null;
                compBeans = rulehelper.getCompoBeans();
                request.setAttribute("COMPOBEANS", compBeans);
                af = mapping.findForward(FAILURE);
            }
        }catch(Exception ex){
            logger.debug("Problem in AddAction:" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (af == null){
                request.setAttribute("MESSAGE", "Select min 2 components.");
                af = mapping.findForward(FAILURE);
            }
        }
        return af;
        
    }
}
