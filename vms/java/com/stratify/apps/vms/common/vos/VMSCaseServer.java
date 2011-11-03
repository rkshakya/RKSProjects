/*
 * VMSCaseServer.java
 *
 * Created on November 22, 2007, 1:26 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSCaseServer {
    private String caseName = null;
    private String dbVersion = null;
    private String serverName = null;
    private String domain = null;
    private int caseServGID = 0; //useful only for Dest MySQL DB

    public void setCaseServGID(int caseServGID) {
        this.caseServGID = caseServGID;
    }

    public int getCaseServGID() {
        return caseServGID;
    }

   

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    
    public String getDomain() {
        return domain;
    }


    
    public String getDbVersion() {
        return dbVersion;
    }
    


    public String getCaseName() {
        return caseName;
    }
    
     public String getServerName() {
        return serverName;
    }

    
    /**
     * Creates a new instance of VMSCaseServer
     */
    public VMSCaseServer() {
    }
    
}
