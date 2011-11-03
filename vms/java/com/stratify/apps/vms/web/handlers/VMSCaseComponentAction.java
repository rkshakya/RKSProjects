/*
 * VMSCaseComponentAction.java
 *
 * Created on January 10, 2008, 5:28 AM
 */

package com.stratify.apps.vms.web.handlers;

import com.stratify.apps.vms.common.vos.VMSVersionComponent;
import com.stratify.apps.vms.web.helper.VMSCommonHelper;
import com.stratify.apps.vms.web.forms.VMSCaseComponentActionForm;
import com.stratify.apps.vms.web.helper.VMSRuleHelper;
import java.util.ArrayList;
import java.util.HashSet;
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

public class VMSCaseComponentAction extends Action {
    
    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        ActionForward af = null;
        VMSCaseComponentActionForm ccaf = (VMSCaseComponentActionForm)form;
        
        int caseGid = 0;
        int componentGid = 0;
        String actType = null;
        
        caseGid = ccaf.getCaseGid();
        componentGid = ccaf.getComponentGid();
        actType = ccaf.getActionType();
        
        VMSRuleHelper rulehelper = new VMSRuleHelper();
        
        if("display".equalsIgnoreCase(actType) || null == actType || "".equals(actType)){
            //entering from other jsp
            ArrayList caseList = null;
            ArrayList componentList = null;
            
            caseList = rulehelper.getCases();
            componentList = rulehelper.getComponentList();
            
            request.setAttribute("CASES", caseList);
            request.setAttribute("COMPONENTS", componentList);
            
            af = mapping.findForward(SUCCESS);
        } else if ("casecomponent".equalsIgnoreCase(actType)){
            //entering from caseComponents.jsp
            ArrayList caseList = null;
            ArrayList componentList = null;
            ArrayList components = null;
            HashSet caseCount = new HashSet();
            
            caseList = rulehelper.getCases();
            componentList = rulehelper.getComponentList();
            
            components = rulehelper.getVersionComponents(caseGid, componentGid);
            
            //get the unique vase count
            for(int cnt = 0; cnt < components.size(); cnt++){
                VMSVersionComponent compo = (VMSVersionComponent)components.get(cnt);
                caseCount.add(compo.getCasename());
            }
            
            request.setAttribute("CASECOUNT", caseCount.size());
            request.setAttribute("CASES", caseList);
            request.setAttribute("COMPONENTS", componentList);
            request.setAttribute("CASECOMPONENTS", components);
            
            request.setAttribute("LASTCOMPONENT", new Integer(componentGid));
            request.setAttribute("LASTCASE", new Integer(caseGid));
            
            af = mapping.findForward(SUCCESS);
        } else{
            af=mapping.findForward(FAILURE);
        }
        return af;
        
    }
}
