/*
 * VMSServerActionForm.java
 *
 * Created on December 16, 2007, 9:31 PM
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

public class VMSServerActionForm extends org.apache.struts.action.ActionForm {
    int serverGid = 0;
    String actionType = "";

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }

    public int getServerGid() {
        return serverGid;
    }

    public void setServerGid(int serverGid) {
        this.serverGid = serverGid;
    }
  

  
}
