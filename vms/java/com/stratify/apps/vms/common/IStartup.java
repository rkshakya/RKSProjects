/*
 * IStartup.java
 *
 * Created on February 4, 2008, 2:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.stratify.apps.vms.common;

import java.util.Properties;
import javax.servlet.*;


/**
 *
 * @author ravikishores
 */
public interface IStartup {           
        public static final String STATUS_OK = "OK";
        public static final String STATUS_FAILED = "FAILED";
        
        public String startup(String name, String rootpath, Properties args)throws Exception;
        public String shutdown()throws Exception;
                    
}
