/*
 * VMSHomeAction.java
 *
 * Created on December 19, 2007, 12:07 AM
 */

package com.stratify.apps.vms.web.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
/**
 *
 * @author ravikishores
 * @version
 */

public class VMSHomeAction extends Action {
        
    private final static String HOME = "home";
       
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        return mapping.findForward(HOME);
        
    }
}
