/*
 * VMSCaseComponentActionForm.java
 *
 * Created on January 10, 2008, 5:23 AM
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

public class VMSCaseComponentActionForm extends org.apache.struts.action.ActionForm {
    String actionType = null;
    int caseGid = 0;
    int componentGid = 0;

    public String getActionType() {
        return actionType;
    }

    public int getCaseGid() {
        return caseGid;
    }

    public int getComponentGid() {
        return componentGid;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setCaseGid(int caseGid) {
        this.caseGid = caseGid;
    }

    public void setComponentGid(int componentGid) {
        this.componentGid = componentGid;
    }
    

   
}
