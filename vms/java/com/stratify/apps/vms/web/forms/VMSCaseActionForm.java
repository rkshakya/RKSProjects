/*
 * VMSCaseActionForm.java
 *
 * Created on December 17, 2007, 8:34 PM
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

public class VMSCaseActionForm extends org.apache.struts.action.ActionForm {
    int[] caseGids = {0};
    String actionType = "";
    String caseName = null;

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public int[] getCaseGids() {
        return caseGids;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setCaseGid(int[] caseGids) {
        this.caseGids = caseGids;
    }

   
}
