/*
 * VMSOutCaste.java
 *
 * Created on December 18, 2007, 1:00 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSOutCaste {
    int caseGid = 0;
    int compoVerGid = 0;

    public void setCompoVerGid(int compoVerGid) {
        this.compoVerGid = compoVerGid;
    }

    public void setCaseGid(int caseGid) {
        this.caseGid = caseGid;
    }

    public int getCompoVerGid() {
        return compoVerGid;
    }

    
    public int getCaseGid() {
        return caseGid;
    }

    
    /**
     * Creates a new instance of VMSOutCaste
     */
    public VMSOutCaste() {
    }
    
}
