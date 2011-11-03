/*
 * VMSEditRuleAction.java
 *
 * Created on December 14, 2007, 3:54 AM
 */

package com.stratify.apps.vms.web.handlers;

import com.stratify.apps.vms.common.vos.VMSRuleBean;
import com.stratify.apps.vms.web.forms.VMSEditRuleForm;
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

public class VMSEditRuleAction extends Action {
    
    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";
    private Logger logger = Logger.getLogger(VMSEditRuleAction.class.getName());
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward af = null;
        VMSEditRuleForm ef = (VMSEditRuleForm)form;
        
        int rulNum = 0;
        String actType = null;
        int[] components = null;
        String ruleName = null;
        String ruleDescription = null;       
        
        rulNum = ef.getRuleNum();
        actType = ef.getEditType();
        components = ef.getRcomponents();
        ruleName = ef.getRuleName();
        ruleDescription = ef.getRuleDescription();       
        
        VMSRuleHelper rulehelper = new VMSRuleHelper();
        
        //if request came from editRule.jsp
        if("save".equalsIgnoreCase(actType)){
            int delFlag = 0;
            int insFlag = 0;
            String message = null;
            
            delFlag = rulehelper.editRule(rulNum, components, ruleName, ruleDescription);
            
            if(delFlag > 0){
                message = "Rule " + ruleName + " has been updated";
                logger.debug("Modified rule inserted");
            }else{
                message = "Rule " + ruleName + " updation failed";
                logger.debug("Rule  " + rulNum + " could not be deleted.");
            }
            
            request.setAttribute("MESSAGE", message) ;
            //af = mapping.findForward(SUCCESS);
            
        }
        //if request came from other page
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
        request.setAttribute("LASTRULE", rulNum);
        request.setAttribute("LASTRULEOBJ", lastrule);
        
        af = mapping.findForward(SUCCESS);
        
        return af;
    }
}
