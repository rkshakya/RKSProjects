/*
 * VMSServerAction.java
 *
 * Created on December 16, 2007, 9:33 PM
 */

package com.stratify.apps.vms.web.handlers;

import com.stratify.apps.vms.common.vos.VMSCaseComponent;
import com.stratify.apps.vms.web.helper.VMSCommonHelper;
import com.stratify.apps.vms.web.forms.VMSServerActionForm;
import com.stratify.apps.vms.web.helper.VMSServerHelper;
import java.util.ArrayList;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import com.stratify.common.logging.Logger;
/**
 *
 * @author ravikishores
 * @version
 */

public class VMSServerAction extends Action {
    static Logger logger = Logger.getLogger(VMSServerAction.class.getName());
    
    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        ActionForward af = null;
        VMSServerActionForm saf = (VMSServerActionForm)form;
        
        int serverGid = 0;
        String actType = null;
        serverGid = saf.getServerGid();
        actType = saf.getActionType();
        
        VMSServerHelper serverhelper = new VMSServerHelper();
        logger.info("execute: ActionType:" + actType + " Server GID: " + serverGid);
        
        if("display".equalsIgnoreCase(actType) || null == actType || "".equals(actType)){            
            //serverList will hold ServerBean
            ArrayList serverList = null;
            //get server info
            serverList = serverhelper.getServers();             
            logger.info("execute: Number of servers got:" + serverList.size());
            
            request.setAttribute("SERVERS", serverList);            
            af = mapping.findForward(SUCCESS);
        } else if ("server".equalsIgnoreCase(actType)){
            //entering from showServer.jsp
             //serverList will hold ServerBean
            ArrayList serverList = null;
            //componentList will hold CaseComponent beans
            ArrayList componentList = null; 
            HashSet caseCount = new HashSet();
            
             //get server info
            serverList = serverhelper.getServers();  
            //get component info for selected server
            componentList = serverhelper.getCaseComponents(serverGid);
            
            //get the counts of cases
            for(int cnt = 0; cnt < componentList.size(); cnt++){
                VMSCaseComponent com = (VMSCaseComponent)componentList.get(cnt);
                caseCount.add(com.getCaseName());
            }
            
            request.setAttribute("CASECOUNT", caseCount.size());
            request.setAttribute("SERVERS", serverList);            
            request.setAttribute("CASECOMPONENTS", componentList);
            request.setAttribute("LASTSERVER", new Integer(serverGid));
            
            af = mapping.findForward(SUCCESS);
        } else{
            af = mapping.findForward(FAILURE);
        }
        return af;
        
    }
}
