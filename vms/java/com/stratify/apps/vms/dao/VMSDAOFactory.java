/*
 * VMSDAOFactory.java
 *
 * Created on November 22, 2007, 1:22 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.dao;

import java.sql.Connection;

/**
 *
 * @author ravikishores
 */
public class VMSDAOFactory {
    
    /**
     * Creates a new instance of VMSDAOFactory
     */
    public VMSDAOFactory() {
    }
    
    public static VMSMyDAO getVerMyDAO(Connection con){
        return (new VMSMyDAO(con));
        
    }
    
}
