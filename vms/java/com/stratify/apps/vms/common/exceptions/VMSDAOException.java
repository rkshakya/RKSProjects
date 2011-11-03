/*
 * VMSDAOException.java
 *
 * Created on December 7, 2007, 4:30 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.exceptions;

/**
 *
 * @author ravikishores
 */

public class VMSDAOException extends VMSBaseException {
    /**
     * This constructor takes the cause exception as an argument. It sets the
     * root cause as the cause of this exception.
     */
    public VMSDAOException(Throwable exception) {
        super(exception.getCause() == null ? exception : exception.getCause());
    }
    
    public VMSDAOException(int severity, String deviation_type, String deviation_message,String casename){
        super(severity, deviation_type, deviation_message, casename);
    }
    
    public VMSDAOException(){
        
    }
    
    /**
     * This constructor takes error message as argument.
     */
    public VMSDAOException(String message) {
        super(message);
    }
}

