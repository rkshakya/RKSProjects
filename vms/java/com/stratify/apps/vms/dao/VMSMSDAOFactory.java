/*
 * VMSMSDAOFactory.java
 *
 * Created on November 27, 2007, 4:47 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.dao;

import java.sql.Connection;

/**
 *
 * @author ravikishores
 */
public class VMSMSDAOFactory {
    
    /**
     * Creates a new instance of VMSMSDAOFactory
     */
    public VMSMSDAOFactory() {
    }
    
     public static VMSMSDAO getVerMSDAO(Connection con){
        return (new VMSMSDAO(con));
        
    }
    
    
}
