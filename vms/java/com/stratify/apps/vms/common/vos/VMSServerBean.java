/*
 * VMSServerBean.java
 *
 * Created on December 16, 2007, 10:08 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSServerBean {
    private int gid = 0;
    private String name = "";
    private String domain = "";

    public void setName(String name) {
        this.name = name;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public int getGid() {
        return gid;
    }

    public String getDomain() {
        return domain;
    }

    
    /**
     * Creates a new instance of VMSServerBean
     */
    public VMSServerBean() {
    }
    
}
