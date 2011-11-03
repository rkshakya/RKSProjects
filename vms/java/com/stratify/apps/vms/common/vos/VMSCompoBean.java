/*
 * VMSCompoBean.java
 *
 * Created on December 7, 2007, 3:16 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.vos;

/**
 *
 * @author ravikishores
 */
public class VMSCompoBean {
    private int compoVerGID = 0;
    private String compoName = "";
    private int maj = 0;   
    private int min = 0;
    private int bld = 0;
    private int patch = 0;     

    public String getCompleteCompoName(){
        return(compoName + " " + maj + "." + min + "." + bld + "." + patch);
    }
    
    
  
    public void setMaj(int maj) {
        this.maj = maj;
    }

    public void setCompoVerGID(int compoVerGID) {
        this.compoVerGID = compoVerGID;
    }

    public void setCompoName(String compoName) {
        this.compoName = compoName;
    }

    public void setBld(int bld) {
        this.bld = bld;
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

    public int getCompoVerGID() {
        return compoVerGID;
    }

    public String getCompoName() {
        return compoName;
    }

    public int getBld() {
        return bld;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setPatch(int patch) {
        this.patch = patch;        
    }   
    
    /**
     * Creates a new instance of VMSCompoBean
     */
    public VMSCompoBean() {
    }
    
}
