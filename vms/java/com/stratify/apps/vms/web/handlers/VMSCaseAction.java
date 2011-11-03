/*
 * VMSCaseAction.java
 *
 * Created on December 17, 2007, 8:35 PM
 */

package com.stratify.apps.vms.web.handlers;

import com.stratify.apps.vms.web.helper.VMSCaseHelper;
import com.stratify.apps.vms.web.helper.VMSCommonHelper;
import com.stratify.apps.vms.web.forms.VMSCaseActionForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;
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

public class VMSCaseAction extends Action {
    static Logger logger = Logger.getLogger(VMSCaseAction.class.getName());

    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";


    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward af = null;
        int[] caseGids;
        String actType = null;
        String caseName = null;

        VMSCaseActionForm caf = (VMSCaseActionForm)form;
         caseGids = caf.getCaseGids(); 
        actType = caf.getActionType();
        
        //if caseName is  provided as parameter
       if (null != request.getParameter("caseName")) {
           caseName = request.getParameter("caseName");
        }
       

        VMSCaseHelper casehelper = new VMSCaseHelper();

        if("case".equalsIgnoreCase(actType)){
            //request from self
            ArrayList caseBeans = null;
            ArrayList components = null;
            //this treemap will hold : caseName(key) - List of serverComponents(values) staisfying rules
            TreeMap validServerComponents = null;
            //this treemap will hold : caseName(key) - List of serverComponents(values) not satisfying rules
            TreeMap invalidServerComponents = null;
            ArrayList lastGids = new ArrayList();            

            //get caselist
            caseBeans = casehelper.getCases();           
            
            if(null != caseName){
                //get the corresp caseGid                
                caseGids = casehelper.getCaseGid(caseName);                
                
            }

            components = casehelper.getServerComponentsValidInvalid(caseGids);
           
            //get map of caseGid and list of ServerComponents
            //serverComponents = casehelper.getServerComponents(caseGids);
            validServerComponents = (TreeMap)components.get(0);

            //get a map of caseGid and list of outcaste component gids i.e the components that do not belong to any rule
            //outcastes = casehelper.getOutCastes(caseGids);
           invalidServerComponents = (TreeMap)components.get(1);

            for(int i = 0;i < caseGids.length ; i++){
                lastGids.add(caseGids[i] + "");
            }           

            //make it available to the jsp
            request.setAttribute("OUTCASTES", invalidServerComponents);
            request.setAttribute("CASEBEANS", caseBeans);
            request.setAttribute("SERVERCOMPONENTS", validServerComponents);
            request.setAttribute("LASTCASES", lastGids);            

            af =  mapping.findForward(SUCCESS);
        }else if("display".equalsIgnoreCase(actType) || "".equals(actType) || actType == null){
            //request from entry page
            ArrayList caseBeans = null;
            
            //fetch case beans for all the cases
            caseBeans = casehelper.getCases();

            //make it available to showCase.jsp
            request.setAttribute("CASEBEANS", caseBeans);

            af =  mapping.findForward(SUCCESS);
        }else{            
            af =  mapping.findForward(FAILURE);
        }

        return af;

    }
}
