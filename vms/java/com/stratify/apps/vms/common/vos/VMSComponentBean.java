/*
 * VMSComponentBean.java
 *
 * Created on January 10, 2008, 5:13 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSComponentBean {
    private int gid = 0;
    private String componentName = null;

    public String getComponentName() {
        return componentName;
    }

    public int getGid() {
        return gid;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }
    
    
    /**
     * Creates a new instance of VMSComponentBean
     */
    public VMSComponentBean() {
    }
    
}
