/*
 * VMSVersionComponent.java
 *
 * Created on January 10, 2008, 2:42 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSVersionComponent {
    String casename = null;
    String servername = null;
    String componentname = null;
    String version = null;
    String domain = null;
    String port = null;
    
    public String getCasename() {
        return casename;
    }
    
    public String getComponentname() {
        return componentname;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public String getPort() {
        return port;
    }
    
    public String getServername() {
        return servername;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setCasename(String casename) {
        this.casename = casename;
    }
    
    public void setComponentname(String componentname) {
        this.componentname = componentname;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public void setServername(String servername) {
        this.servername = servername;
    }
    
    public void setVersion(String version) {
        if(version.equals("0.0.0.0")){
            this.version = "NA";
        }else{
            this.version = version;
        }
    }
    
    
    
    /**
     * Creates a new instance of VMSVersionComponent
     */
    public VMSVersionComponent() {
    }
    
}
