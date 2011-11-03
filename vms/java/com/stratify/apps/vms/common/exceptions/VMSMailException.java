/*
 * VMSMailException.java
 *
 * Created on June 18, 2008, 2:50 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.exceptions;

/**
 *
 * @author RavikishoreS
 */
public class VMSMailException extends VMSBaseException {
    
    /** Creates a new instance of VMSMailException */
    public VMSMailException(String message) {
        super(message);
    }
    
    public VMSMailException(Throwable exception) {
        super(exception.getCause() == null ? exception : exception.getCause());
    }
    
     public VMSMailException(int severity, String deviation_type, String deviation_message, String casename){
        super(severity, deviation_type, deviation_message, casename);
    }
}