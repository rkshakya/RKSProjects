/*
 * VMSComponent.java
 *
 * Created on November 27, 2007, 10:07 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSComponent {
    private String serverType = null;
    private String version = "0.0.0.0";
    private String serverURL = null;
    private int isActive = 0;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getVersion() {
        return version;
    }

    public String getServerURL() {
        return serverURL;
    }

    public String getServerType() {
        return serverType;
    }

    public int getIsActive() {
        return isActive;
    }

    /**
     * Creates a new instance of VMSComponent
     */
    public VMSComponent() {
    }
    
}
