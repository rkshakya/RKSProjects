/*
 * VMSEditRuleForm.java
 *
 * Created on December 14, 2007, 3:55 AM
 */

package com.stratify.apps.vms.web.forms;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author ravikishores
 * @version
 */

public class VMSEditRuleForm extends org.apache.struts.action.ActionForm {
    
    private String editType = null;    
    private int ruleNum = 0;
    private String ruleName = null;
    private String ruleDescription = null;
    private int[] rcomponents = null;              
        
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    
    public void setEditType(String editType) {
        this.editType = editType;
    }

    public String getEditType() {
        return editType;
    }


    public void setRuleNum(int ruleNum) {
        this.ruleNum = ruleNum;
    }

    public void setRcomponents(int[] rcomponents) {
        this.rcomponents = rcomponents;
    }

   

    public int getRuleNum() {
        return ruleNum;
    }

    public int[] getRcomponents() {
        return rcomponents;
    }

   

}
