/*
 * VMSVersionActionForm.java
 *
 * Created on January 10, 2008, 1:20 AM
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

public class VMSVersionActionForm extends org.apache.struts.action.ActionForm {
       String actionType = null;
       String major = null;
       String minor = null;
       String build = null;
       String patch = null;
       int componentGid = 0;

    public void setComponentGid(int componentGid) {
        this.componentGid = componentGid;
    }

    public int getComponentGid() {
        return componentGid;
    }


    public String getActionType() {
        return actionType;
    }

    public String getBuild() {
        return build;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public String getPatch() {
        return patch;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

       
}
