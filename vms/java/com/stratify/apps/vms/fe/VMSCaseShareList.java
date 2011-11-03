/*
 * CaseShareList.java
 *
 * Created on June 5, 2008, 7:04 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.fe;

import com.stratify.apps.vms.common.vos.VMSComponent;
import java.util.ArrayList;

/**
 *
 * @author RavikishoreS
 */
public class VMSCaseShareList extends ArrayList {
    ArrayList serverList = new ArrayList();
    
    /** Creates a new instance of CaseShareList */
    public VMSCaseShareList() {
        super();
    }
    
    public boolean add(VMSComponent component){
        serverList.add(component.getServerURL().toLowerCase().trim());
        super.add(component);
        return true;
    }
    
    public boolean containsServer(VMSComponent component){
        return serverList.contains(component.getServerURL().toLowerCase().trim());
    }
    
}
