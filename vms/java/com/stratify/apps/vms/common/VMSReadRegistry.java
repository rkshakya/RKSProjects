/*
 * VMSReadRegistry.java
 *
 * Created on November 30, 2007, 5:01 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common;

import ca.beq.util.win32.registry.*;
import java.util.Iterator;
import com.stratify.common.logging.Logger;
/**
 *
 * @author ravikishores
 */
public class VMSReadRegistry {
    static Logger logger = Logger.getLogger(VMSReadRegistry.class.getName());
    
    private String remoteMachine = null;
    private RegistryKey regkey = null;
    private final String PATH = "SOFTWARE\\Stratify";
    
    /**
     * Creates a new instance of VMSReadRegistry
     */
    public VMSReadRegistry(String machine) {
        this.remoteMachine = machine;
        this.regkey = new RegistryKey(machine, RootKey.HKEY_LOCAL_MACHINE, PATH);
    }
    
    //search for value
    public String searchValue(String searchString){
        String ret = "";
       // RegistryKey r = new RegistryKey(remoteMachine, RootKey.HKEY_LOCAL_MACHINE, PATH);
        System.out.println("Searching for " + searchString + "..... under " + regkey.getPath() + " which exists:" + Boolean.toString(regkey.exists()));
        logger.info("Searching for " + searchString + "..... under " + regkey.getPath() + " which exists:" + Boolean.toString(regkey.exists()));
        
        if(regkey.hasValues()) {
            Iterator i = regkey.values();
            while(i.hasNext()) {
                RegistryValue x = (RegistryValue)i.next();
                System.out.println("Sub values: " + x.getName());
                if(x.getName().indexOf(searchString) > -1){
                    System.out.println("Found match " + x.getName() + " for " + searchString );
                    logger.info("Found match " + x.getName() + " for " + searchString );
                    
                    ret = (String)x.getData();
                }
            } 
        } 
        
        return ret;
    }
    
    public String getRegistryVal(String keyVal){
        String ret = null;
        System.out.println("REMOTEMACHINE: " + remoteMachine + " PATH: " + PATH + " KEYVAL: " + keyVal);
        logger.info("REMOTEMACHINE: " + remoteMachine + " PATH: " + PATH + " KEYVAL: " + keyVal);
        //RegistryKey r = new RegistryKey(remoteMachine, RootKey.HKEY_LOCAL_MACHINE, PATH);
        if(regkey.hasValue(keyVal)) {
            RegistryValue regVal = regkey.getValue(keyVal);
            ret = (String)regVal.getData();
        }
        return ret;
        
    }
    
}
