/*
 * VMSListRuleAction.java
 *
 * Created on December 12, 2007, 2:28 AM
 */

package com.stratify.apps.vms.web.handlers;

import com.stratify.apps.vms.web.helper.VMSCommonHelper;
import com.stratify.apps.vms.web.forms.VMSListRuleForm;
import com.stratify.apps.vms.common.vos.VMSRuleBean;
import com.stratify.apps.vms.web.helper.VMSRuleHelper;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import com.stratify.common.logging.Logger;
/**
 *
 * @author ravikishores
 * @version
 */

public class VMSListRuleAction extends Action {
    
    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";
    private final static String EDIT = "edit";
    private final static String ADD = "add";
    private Logger logger = Logger.getLogger(VMSListRuleAction.class.getName());
    
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        ActionForward af = null;
        VMSListRuleForm listForm = (VMSListRuleForm)form;
        
        int rulNum = 0;
        String actType = null;
        String deleteAction = null;
        String deleteMessage = null;
        
        rulNum = listForm.getRuleNum();
        actType = listForm.getActionType();
        deleteAction = listForm.getDeleteAction();
        
        VMSRuleHelper rulehelper = new VMSRuleHelper();
        
        if( ((!deleteAction.equalsIgnoreCase("delete")) && (!deleteAction.equalsIgnoreCase("edit")) && (!deleteAction.equalsIgnoreCase("add")) )
        || "".equals(actType) || null == actType){
            //if simple display, fetch compo versions for this rule
            ArrayList rcompoBeans = null;
            ArrayList rules = null;
            VMSRuleBean lastrule = null;
                        
            rcompoBeans = rulehelper.getCompoBeans(rulNum);
            rules = rulehelper.getRules();
            lastrule = rulehelper.getRuleObject(rulNum);
            
            request.setAttribute("RULES", rules);
            request.setAttribute("RCOMPOBEAN", rcompoBeans);
            request.setAttribute("LASTRULE", new Integer(rulNum));
            request.setAttribute("LASTRULEOBJ", lastrule);
            
            af = mapping.findForward(SUCCESS);
        }else if("delete".equalsIgnoreCase(deleteAction)){
            //if deleted button was pressed
            ArrayList rcompoBeans = null;
            ArrayList rules = null;
            int delFlag = 0;
            VMSRuleBean selectedRule = null;
            selectedRule = rulehelper.getRuleObject(rulNum);
            
            delFlag = rulehelper.deleteRule(rulNum);            
            rules = rulehelper.getRules();
            logger.debug("Rules obtained");                        
            
            if(delFlag > 0){
                deleteMessage = "Rule: " + selectedRule.getRuleName() + " is deleted from database.";
                logger.debug(deleteMessage);
            }else{
                deleteMessage = "Rule No " + selectedRule.getRuleName() + " could not be deleted from database.";
            }
            
            request.setAttribute("RULES", rules);
            request.setAttribute("MESSAGE", deleteMessage);
            af = mapping.findForward(SUCCESS);
        }else if("edit".equalsIgnoreCase(deleteAction)){
            //if edit button was pressed
            ArrayList rules = null;
            ArrayList thisComponents = null;
            ArrayList restComponents = null;
            VMSRuleBean lastrule = null;
            
            //get the list of rules
            rules = rulehelper.getRules();
            
            //get the list of components for this rule
            thisComponents = rulehelper.getCompoBeans(rulNum);
            
            //get list of components other than the above
            restComponents = rulehelper.getOtherComponents(rulNum);
            lastrule = rulehelper.getRuleObject(rulNum);
            
            request.setAttribute("RULES", rules);
            request.setAttribute("THISCOMPONENTS", thisComponents);
            request.setAttribute("RESTCOMPONENTS", restComponents);
            request.setAttribute("LASTRULE", new Integer(rulNum));
            request.setAttribute("LASTRULEOBJ", lastrule);
            
            af = mapping.findForward(EDIT);
        }else if("add".equalsIgnoreCase(deleteAction)){
            ArrayList compoBeans = null;
            compoBeans = rulehelper.getCompoBeans();
            request.setAttribute("COMPOBEANS", compoBeans);
            //send to addRule page
            af = mapping.findForward(ADD);
        }else{
            af = mapping.findForward(FAILURE);
        }
        
        return af;
        
    }
}
