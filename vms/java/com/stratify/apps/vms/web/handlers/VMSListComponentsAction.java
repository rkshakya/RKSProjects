/*
 * VMSListComponentsAction.java
 *
 * Created on December 20, 2007, 4:25 AM
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

public class VMSListComponentsAction extends Action {
       
    private final static String SUCCESS = "success";
    private final static String LISTCOMPONENTS = "listComponents";
    private final static String ADDCOMPONENT = "addComponent";
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        
        return mapping.findForward(ADDCOMPONENT);
        
    }
}
