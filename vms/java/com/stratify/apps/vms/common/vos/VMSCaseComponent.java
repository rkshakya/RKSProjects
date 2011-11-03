/*
 * VMSCaseComponent.java
 *
 * Created on December 17, 2007, 1:14 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSCaseComponent {
    int serverGid = 0;
    int caseGid = 0;
    String caseName = null;
    String component = null;
    int major = 0;
    int minor = 0;
    int build = 0;
    int patch = 0;
    String port = null;
    String version = null;
    String serverName = null;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    


    public void setCaseGid(int caseGid) {
        this.caseGid = caseGid;
    }

    public int getCaseGid() {
        return caseGid;
    }


    public void setVersion() {
        this.version = major + "." + minor + "." + build + "." + patch;
        if(version.equals("0.0.0.0")){
            version = "NA";            
        }
    }
    
    public void setVersion(String vers){
        this.version = vers;
    }

    public void setServerGid(int serverGid) {
        this.serverGid = serverGid;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPatch(int patch) {
        this.patch = patch;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public String getVersion() {
        return version;
    }

    public int getServerGid() {
        return serverGid;
    }

    public String getPort() {
        return port;
    }

    public int getPatch() {
        return patch;
    }

    public int getMinor() {
        return minor;
    }

    public int getMajor() {
        return major;
    }

    public String getComponent() {
        return component;
    }

    public String getCaseName() {
        return caseName;
    }

    public int getBuild() {
        return build;
    }

    /**
     * Creates a new instance of VMSCaseComponent
     */
    public VMSCaseComponent() {
    }
    
}
