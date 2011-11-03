/*
 * VMSRuleBean.java
 *
 * Created on December 12, 2007, 1:00 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSRuleBean {
    private int rule = 0;
    private String ruleName = null;
    private String ruleDescription = null;

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
    


    public void setRule(int rule) {
        this.rule = rule;
    }

    public int getRule() {
        return rule;
    }

    /**
     * Creates a new instance of VMSRuleBean
     */
    public VMSRuleBean() {
    }
    
}
