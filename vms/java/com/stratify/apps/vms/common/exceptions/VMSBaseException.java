/*
 * VMSBaseException.java
 *
 * Created on June 29, 2009, 6:14 AM
 *
 * Confidential and Proprietary
 * (c) Copyright 1999 - 2008 Stratify, an Iron Mountain Company. ( f/k/a PurpleYogi f/k/a Calpurnia ). All rights reserved.
 * The foregoing shall not be deemed to indicate that this source has been published.
 * Instead, it remains a trade secret of Stratify, an Iron Mountain Company.
 */

package com.stratify.apps.vms.common.exceptions;

import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.dao.VMSConnectionFactory;
import com.stratify.apps.vms.dao.VMSDAOFactory;
import com.stratify.apps.vms.dao.VMSMyDAO;
import com.stratify.apps.vms.dao.common.VMSDAOUtils;
import java.sql.Connection;
import com.stratify.common.logging.Logger;

/**
 *
 * @author RavikishoreS
 */
public class VMSBaseException extends java.lang.Exception {
    Connection coninsMyDAOFE = null;
    VMSMyDAO insmydao = null;
    
    static Logger logger = Logger.getLogger(VMSBaseException.class.getName());
    
    /**
     * Creates a new instance of <code>VMSBaseException</code> without detail message.
     */
    public VMSBaseException() {
    }
    
    public VMSBaseException(Throwable exception) {
        super(exception.getCause() == null ? exception : exception.getCause());
    }
    
    /**
     * Constructs an instance of <code>VMSBaseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public VMSBaseException(String msg) {
        super(msg);
    }
    
    public VMSBaseException(int severity, String deviation_type, String deviation_message, String casename){
        if(severity == VMSStaticParams.SEVERITY_1 || severity == VMSStaticParams.SEVERITY_2){
            try{
                this.coninsMyDAOFE = VMSConnectionFactory.getMyConnection(1);
                this.insmydao = VMSDAOFactory.getVerMyDAO(coninsMyDAOFE);
                
                int retVal = 0;
                retVal = insmydao.logDeviation(deviation_type, deviation_message, casename);
                VMSDAOUtils.freeCon(coninsMyDAOFE);
            }catch(Exception ex){
                logger.error("VMSBaseException: Error: " + ex.getMessage());
            }
        }
    }
    
    public void logDeviation(int severity, String deviation_type, String deviation_message, String casename){
        if(severity == VMSStaticParams.SEVERITY_1 || severity == VMSStaticParams.SEVERITY_2){
            try{
                this.coninsMyDAOFE = VMSConnectionFactory.getMyConnection(1);
                this.insmydao = VMSDAOFactory.getVerMyDAO(coninsMyDAOFE);
                
                int retVal = 0;
                retVal = insmydao.logDeviation(deviation_type, deviation_message, casename);
                VMSDAOUtils.freeCon(coninsMyDAOFE);
            }catch(Exception ex){
                logger.error("logDeviation: Error: " + ex.getMessage());
            }
        }
    }
    
    
}
