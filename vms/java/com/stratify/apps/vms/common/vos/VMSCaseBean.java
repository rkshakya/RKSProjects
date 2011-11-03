/*
 * VMSCaseBean.java
 *
 * Created on December 17, 2007, 8:57 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSCaseBean {
    int caseGid = 0;
    String caseName = "";

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void setCaseGid(int caseGid) {
        this.caseGid = caseGid;
    }

    public String getCaseName() {
        return caseName;
    }

    public int getCaseGid() {
        return caseGid;
    }

    
    /**
     * Creates a new instance of VMSCaseBean
     */
    public VMSCaseBean() {
    }
    
}
