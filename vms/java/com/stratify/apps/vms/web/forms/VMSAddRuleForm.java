/*
 * VMSAddRuleForm.java
 *
 * Created on December 7, 2007, 2:06 AM
 */

package com.stratify.apps.vms.web.forms;

import java.util.ArrayList;

/**
 *
 * @author ravikishores
 * @version
 */

public class VMSAddRuleForm extends org.apache.struts.action.ActionForm {        
    private int[] selectList = null;
    private String actionType = "";
    private String ruleName = "";
    private String ruleDescription = "";

    public String getRuleDescription() {
        return ruleDescription;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getActionType() {
        return actionType;
    }        
    
    public void setSelectList(int[] selectList) {
        this.selectList = selectList;
    }
    
    public int[] getSelectList() {
        return selectList;
    }
    
    
    
    
}
