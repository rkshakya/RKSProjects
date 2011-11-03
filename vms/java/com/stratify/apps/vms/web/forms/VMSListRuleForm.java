/*
 * VMSListRuleForm.java
 *
 * Created on December 12, 2007, 2:29 AM
 */

package com.stratify.apps.vms.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author ravikishores
 * @version
 */

public class VMSListRuleForm extends org.apache.struts.action.ActionForm {
      
    private String actionType = "";    
    private int ruleNum = 0;
    private int[] rcomponents = null;
    private String deleteAction = "";

    public String getDeleteAction() {
        return deleteAction;
    }

    public void setDeleteAction(String deleteAction) {
        this.deleteAction = deleteAction;
    }
       
    public void setRcomponents(int[] rcomponents) {
        this.rcomponents = rcomponents;
    }

    public int[] getRcomponents() {
        return rcomponents;
    }

    public void setRuleNum(int ruleNum) {
        this.ruleNum = ruleNum;
    }

    public int getRuleNum() {
        return ruleNum;
    }

    

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }


   

    
    /**
     *
     */
    public VMSListRuleForm() {
        super();
        // TODO Auto-generated constructor stub
    }
    
   
}
