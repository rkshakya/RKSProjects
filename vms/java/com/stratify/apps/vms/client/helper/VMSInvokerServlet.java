/*
 * VMSInvokerServlet.java
 *
 * Created on February 4, 2008, 2:38 AM
 */

package com.stratify.apps.vms.client.helper;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.*;
import javax.servlet.http.*;

import com.stratify.apps.vms.common.IStartup;
import com.stratify.common.logging.Logger;
import com.stratify.datahub.common.util.PyEnv;
import com.stratify.datahub.common.util.PyEnvLocal;

/**
 *
 * @author ravikishores
 * @version
 */
public class VMSInvokerServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        // get root context
        ServletContext context = config.getServletContext();       

        // init context
        setupContext(context);

        // find root application path
        String root = context.getRealPath("/");        

        // get context path from root
        String contextName = root.substring(0, root.length()-1);
        contextName = contextName.substring(contextName.lastIndexOf('\\') + 1);

        /**************************WARNING****************************/
        /******DO NOT USE LOGGER BEFORE THIS LINE OF CODE*************/
        /*************************************************************/
        String prevRoot = System.setProperty(PyEnv.DATAHUB_ROOT_KEY, root);            

        // setup the current app property with it's context name
        PyEnvLocal.setAppName(contextName);
        System.setProperty(PyEnvLocal.getCurrentAppKey(), root);        

        // initiate the logger setup
        Logger.getLogger(getServletName()).info("Starting application from root..");

        // initial args
        Properties args = new Properties();

        // build start-up args table
        Enumeration enumProp = config.getInitParameterNames();
        while(enumProp.hasMoreElements()) {
            String key = (String)enumProp.nextElement();
            if(key != null) {
                String value = config.getInitParameter(key);
                args.setProperty(key, value);

            }
        }

        List startupList = new ArrayList();
        String classname = null;
        try {
            Enumeration props = args.propertyNames();
            while(props.hasMoreElements()){
                String key = null;
                key = (String)props.nextElement();
                //all startup element in web.xml should start with 'startup-class'
                if( key.indexOf("startup-class") > -1 ){
                    classname = args.getProperty(key);

                    if(classname == null){
                        continue;
                    }

                    IStartup startup = null;

                    Logger.getLogger(getServletName()).debug("Loading startup class...:" + classname);

                    // load the class
                    Class startUp = Class.forName(classname);
                    startup = (IStartup)startUp.newInstance();

                    startupList.add(startup);

                    // run the start method
                    startup.startup(getServletName(), root, args);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(getServletName()).fatal("Failed to start one of the startup classes:" + classname, ex);
            performShutdownActivities(startupList);
        }
    }

    private void setupContext(ServletContext context) {
        // setup the work directory
        String warpath = context.getRealPath("/");
        if(warpath != null) {
            try {
                new File(warpath, "work").mkdirs();
            } catch(Throwable e) {};
        }
    }

    private void performShutdownActivities(List startupList){
        for(int j = 0; j < startupList.size(); j++){
            IStartup startedClass = (IStartup)startupList.get(j);
            Logger.getLogger(getServletName()).debug("shutting down startup class: " + startedClass.getClass().getName());
            try {
                if(startedClass != null)
                    startedClass.shutdown();
            } catch(Exception e) {
                Logger.getLogger(getServletName()).debug("Failed to shutdown startup class...:" + startedClass.getClass().getName(), e);
            }
        }
        System.exit(1);
    }


    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Startup servlet for VMS";
    }
    // </editor-fold>
}
