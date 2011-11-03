/*
 * VMSServerComponent.java
 *
 * Created on December 17, 2007, 10:45 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

import com.stratify.apps.vms.common.VMSStaticParams;

/**
 *
 * @author ravikishores
 */
public class VMSServerComponent {
    String component = "";
    String serverName = "";
    String caseName = "";
    int caseGid = 0;
    int serverGid = 0;
    String port = "";
    int maj = 0;
    int min = 0;
    int build = 0;
    int patch = 0;
    String version = "";
    int compoVerGid = 0;
    int hierarchyGid = 0;
    String domain = "";
    String url = "";
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
        setUrl();
    }
    
    public String getDomain() {
        return domain;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl() {
        if(this.domain.equalsIgnoreCase(VMSStaticParams.BEALIAS)){
            if(this.serverName.startsWith("\\")){
                this.url = "file:///" + this.serverName + "\\" + this.port;
            }else if(this.component.equalsIgnoreCase(VMSStaticParams.BEDB)){
                this.url = VMSStaticParams.NOT_APPLICABLE;
            }else{
                this.url = VMSStaticParams.HTTP + this.serverName.toLowerCase() + VMSStaticParams.BESUFFIX + ":" + this.port;
            }
        }else if(this.domain.equalsIgnoreCase(VMSStaticParams.FEALAIS)){
            if(this.serverName.startsWith("\\")){
                this.url = "file:///" + this.serverName + "\\" + this.port;
            }else if(this.component.equalsIgnoreCase(VMSStaticParams.FEDB)|| this.component.equalsIgnoreCase(VMSStaticParams.LDSUI)){
                this.url = VMSStaticParams.NOT_APPLICABLE;
            }else{
                this.url = VMSStaticParams.HTTP + this.serverName.toLowerCase() + VMSStaticParams.FESUFFIX + ":" + this.port;
            }
        }else if(this.domain.equalsIgnoreCase(VMSStaticParams.APACBEALIAS)){
            if(this.serverName.startsWith("\\")){
                this.url = "file:///" + this.serverName + "\\" + this.port;
            }else if(this.component.equalsIgnoreCase(VMSStaticParams.BEDB)){
                this.url = VMSStaticParams.NOT_APPLICABLE;
            }else{
                this.url = VMSStaticParams.HTTP + this.serverName.toLowerCase() + VMSStaticParams.BESUFFIX_APAC + ":" + this.port;
            }
        }else if(this.domain.equalsIgnoreCase(VMSStaticParams.APACFEALAIS)){
            if(this.serverName.startsWith("\\")){
                this.url = "file:///" + this.serverName + "\\" + this.port;
            }else if(this.component.equalsIgnoreCase(VMSStaticParams.FEDB)|| this.component.equalsIgnoreCase(VMSStaticParams.LDSUI)){
                this.url = VMSStaticParams.NOT_APPLICABLE;
            }else{
                this.url = VMSStaticParams.HTTP + this.serverName.toLowerCase() + VMSStaticParams.FESUFFIX_APAC + ":" + this.port;
            }
        }
    }
        
    public int getHierarchyGid() {
        return hierarchyGid;
    }
    
    public void setHierarchyGid(int hierarchyGid) {
        this.hierarchyGid = hierarchyGid;
    }
    
    public void setCaseGid(int caseGid) {
        this.caseGid = caseGid;
    }
    
    public int getCaseGid() {
        return caseGid;
    }
    
    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
    
    public String getCaseName() {
        return caseName;
    }
    
    public void setServerGid(int serverGid) {
        this.serverGid = serverGid;
    }
    
    public int getServerGid() {
        return serverGid;
    }
    
    public void setVersion() {
        this.version = maj + "." + min + "." + build + "." + patch;
        if(version.equals("0.0.0.0")){
            this.version = VMSStaticParams.NOT_APPLICABLE;
        }
    }
    
    public void setCompoVerGid(int compoVerGid) {
        this.compoVerGid = compoVerGid;
    }
    
    public int getCompoVerGid() {
        return compoVerGid;
    }
    
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public void setPatch(int patch) {
        this.patch = patch;
        setVersion();
    }
    
    public void setMin(int min) {
        this.min = min;
        setVersion();
    }
    
    public void setMaj(int maj) {
        this.maj = maj;
        setVersion();
    }
    
    public void setComponent(String component) {
        this.component = component;
    }
    
    public void setBuild(int build) {
        this.build = build;
        setVersion();
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getServerName() {
        return serverName;
    }
    
    public String getPort() {
        return port;
    }
    
    public int getPatch() {
        return patch;
    }
    
    public int getMin() {
        return min;
    }
    
    public int getMaj() {
        return maj;
    }
    
    public String getComponent() {
        return component;
    }
    
    public int getBuild() {
        return build;
    }
    
    /**
     * Creates a new instance of VMSServerComponent
     */
    public VMSServerComponent() {
    }
    
}
