/*
 * VMSVersionAction.java
 *
 * Created on January 10, 2008, 1:25 AM
 */

package com.stratify.apps.vms.web.handlers;

import com.stratify.apps.vms.common.vos.VMSVersionComponent;
import com.stratify.apps.vms.web.helper.VMSCommonHelper;
import com.stratify.apps.vms.web.forms.VMSVersionActionForm;
import com.stratify.apps.vms.web.helper.VMSVersionHelper;
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

public class VMSVersionAction extends Action {
    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";
    
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward af = null;
        String actType = null;
        String maj = null;
        String min = null;
        String bld = null;
        String patch = null;
        int compoGid = 0;
        
        VMSVersionActionForm vaf = (VMSVersionActionForm)form;
        maj = vaf.getMajor();
        min = vaf.getMinor();
        bld = vaf.getBuild();
        patch = vaf.getPatch();
        actType = vaf.getActionType();
        compoGid = vaf.getComponentGid();
        
        ArrayList majors = null;
        ArrayList minors = null;
        ArrayList builds = null;
        ArrayList patchs = null;
        ArrayList components = null;
        
        VMSVersionHelper versionhelper = new VMSVersionHelper();
        
        majors = versionhelper.getVersions(1); // 1 major
        minors = versionhelper.getVersions(2); //2 minor
        builds = versionhelper.getVersions(3); //3 build
        patchs = versionhelper.getVersions(4); //4 patch
        components = versionhelper.getComponentList(); //get Component List
        
        if("version".equalsIgnoreCase(actType)){
            //request from self
            ArrayList versionComponents = null;
            HashSet countCases = new HashSet();
                       
            //get component info
            versionComponents = versionhelper.getVersionComponents(maj, min, bld, patch, compoGid);
            
            //hold the unique cases
            for(int cnt = 0; cnt < versionComponents.size(); cnt++ ){
                VMSVersionComponent compo = (VMSVersionComponent)versionComponents.get(cnt);
                countCases.add(compo.getCasename());
            }
            
            //make it available to the jsp
            request.setAttribute("CASECOUNTS", countCases.size());
            request.setAttribute("VERSIONCOMPONENTS", versionComponents); 
            request.setAttribute("LASTCOMPONENT", compoGid);            
        }else{
            
        }
        request.setAttribute("MAJORS", majors);
        request.setAttribute("MINORS", minors);
        request.setAttribute("BUILDS", builds);
        request.setAttribute("PATCHS", patchs);
        request.setAttribute("COMPONENTS", components);                 
        
        af =  mapping.findForward(SUCCESS);
        
        return af;
        
        
    }
}
