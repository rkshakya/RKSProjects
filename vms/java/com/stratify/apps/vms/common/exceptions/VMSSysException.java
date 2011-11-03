/*
 * VMSSysException.java
 *
 * Created on June 18, 2008, 2:14 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.common.exceptions;

/**
 *
 * @author RavikishoreS
 */
public class VMSSysException extends VMSBaseException{
    
    /** Creates a new instance of VMSSysException */
    public VMSSysException(String message) {
        super(message);
    }
    
    public VMSSysException(){
        
    }
    
    public VMSSysException(int severity, String deviation_type, String deviation_message, String casename){
        super(severity, deviation_type, deviation_message, casename);
    }
    
    public VMSSysException(Throwable exception) {
        super(exception.getCause() == null ? exception : exception.getCause());
    }
}
