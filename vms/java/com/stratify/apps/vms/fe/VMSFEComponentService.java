/*
 * VMSFEComponentService.java
 *
 * Created on January 7, 2008, 8:23 PM
 *
 * Stratify Inc P Ltd.
 *
 */

package com.stratify.apps.vms.fe;

import com.stratify.apps.vms.common.vos.VMSComponent;
import java.util.ArrayList;

/**
 *
 * @author ravikishores
 */
public class VMSFEComponentService {            
    
    public VMSComponent[] fetchComponents(String caseName, String serverName){
        ArrayList results = null;
        VMSFEComponentsStore store = null;
        try{
            store = new VMSFEComponentsStore(caseName, serverName);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        
        results = store.getComponents();
       
        return ((VMSComponent[])results.toArray(new VMSComponent[0]));
        
    }        
    
}
