/*
 * VMSTransactionImpl.java
 *
 * Created on June 26, 2009, 3:25 AM
 *
 * Confidential and Proprietary
 * (c) Copyright 1999 - 2008 Stratify, an Iron Mountain Company. ( f/k/a PurpleYogi f/k/a Calpurnia ). All rights reserved.
 * The foregoing shall not be deemed to indicate that this source has been published.
 * Instead, it remains a trade secret of Stratify, an Iron Mountain Company.
 */

package com.stratify.apps.vms.dao;

import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import java.sql.Connection;
import com.stratify.common.logging.Logger;
import java.sql.SQLException;

/**
 *
 * @author RavikishoreS
 */
public class VMSTransactionImpl implements VMSTransaction{
    Connection con = null;
    static Logger logger = Logger.getLogger(VMSTransactionImpl.class.getName());
    
    /** Creates a new instance of VMSTransactionImpl */
    public VMSTransactionImpl(Connection con) {
        this.con = con;
    }
    
    public void begin() throws VMSDAOException {
        logger.info("begin: Starting Transaction..");
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            logger.info("begin: Error Starting Transaction: " + e.getMessage());
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Error Starting transaction VMSTransactionimpl", VMSStaticParams.NA);
        }
    }
    
    public void rollback() throws VMSDAOException {
        try {
            logger.info("rollback - Rollback transaction");
            con.rollback();
        } catch (SQLException e) {
            try {
                close();
            } catch (VMSDAOException de) {
                logger.error("Close failed after rollback failed", de);
            }
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Close failed after rollback", VMSStaticParams.NA);
        }finally{
            try {
                close();
            } catch (VMSDAOException e) {
                throw new VMSDAOException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Close failed after rollback", VMSStaticParams.NA);
            }
        }
    }
    
    public void end() throws VMSDAOException {
        try {
            logger.debug("end-End transaction");
            con.commit();
        } catch (SQLException e) {
            try {
                close();
            } catch (VMSDAOException de) {
                logger.error("Close failed after commit", de);
            }
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Close failed after commit", VMSStaticParams.NA);
        }finally{
            try {
                close();
            } catch (VMSDAOException e) {
                throw new VMSDAOException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Close failed after commit", VMSStaticParams.NA);
            }
        }
    }
    
    private void close() throws VMSDAOException {
        try {
            if(null != con){
                logger.info("Close connection");
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            throw new VMSDAOException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Error closing connection VMSTransactionimpl", VMSStaticParams.NA);
        }
    }
    
}
