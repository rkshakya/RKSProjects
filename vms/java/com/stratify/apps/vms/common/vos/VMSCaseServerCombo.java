/*
 * VMSCaseServerCombo.java
 *
 * Created on June 19, 2008, 10:26 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author RavikishoreS
 */
public class VMSCaseServerCombo extends VMSCaseServer{
    private String caseNameFE = null;
    private String dbVersionFE = null;
    private String serverNameFE = null;
    private String domainFE = null;
    private int caseServGIDFE = 0; //useful only for Dest MySQL DB
    
    /** Creates a new instance of VMSCaseServerCombo */
    public VMSCaseServerCombo() {
        super();
    }
    
    public String getCaseNameFE() {
        return caseNameFE;
    }
    
    public int getCaseServGIDFE() {
        return caseServGIDFE;
    }
    
    public String getDbVersionFE() {
        return dbVersionFE;
    }
    
    public String getDomainFE() {
        return domainFE;
    }
    
    public String getServerNameFE() {
        return serverNameFE;
    }
    
    public void setCaseNameFE(String caseNameFE) {      
            this.caseNameFE = caseNameFE;        
    }
    
    public void setCaseServGIDFE(int caseServGIDFE) {
        this.caseServGIDFE = caseServGIDFE;
    }
    
    public void setDbVersionFE(String dbVersionFE) {
        this.dbVersionFE = dbVersionFE;
    }
    
    public void setDomainFE(String domainFE) {
        this.domainFE = domainFE;
    }
    
    public void setServerNameFE(String serverNameFE) {
        this.serverNameFE = serverNameFE;
    }
    
}
