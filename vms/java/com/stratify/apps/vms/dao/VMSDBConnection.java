/*
 * VMSDBConnection.java
 *
 * Created on April 17, 2008, 11:25 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.dao;

import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.common.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author RavikishoreS
 */

public class VMSDBConnection {
    private static Logger logger = Logger.getLogger(VMSDBConnection.class.getName());
    private static transient DataSource vmsDS = null;
    private transient Connection vmsConn = null;
    
    /**
     * Creates a new instance of DbConnection
     */
    public VMSDBConnection() throws Exception {
        if (null == vmsDS) {
            try {
                Context context = new InitialContext();
                Context envContext = (Context) context.lookup("java:/comp/env");
                
                vmsDS = (DataSource) envContext.lookup("jdbc/vms");
                logger.info("VMSDBConnection - VMS Context obtained.");
                
            } catch (NamingException ne) {
                logger.error("VMSDBConnection - ", ne);
                throw new VMSDAOException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Failed to get VMS DB Naming Context", VMSStaticParams.NA);
            }
        }
        
    }
    
    public void open() throws Exception {
        try {
            if ((vmsConn != null) && (! vmsConn.isClosed())) {
                return;
            }
            vmsDS.setLoginTimeout(VMSStaticParams.DB_LOGIN_TIMEOUT);
            vmsConn = vmsDS.getConnection();
        } catch (SQLException se) {
            logger.error("open - ", se);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_1, VMSStaticParams.CRITICAL, "Failed opening connection to VMS MySQL DB", VMSStaticParams.NA);
        }
    }
    
    public void close() {
        try {
            if (vmsConn != null) {
                if (! vmsConn.isClosed()) {
                    vmsConn.close();
                } else {
                    logger.warning("close - Connection Already Closed.");
                }
            } else {
                logger.warning("close - Connection is null.");
            }
        } catch (Exception se) {
            logger.error("close - ", se);
        }
    }
    
    public Connection getConnection() throws Exception {
        try {
            if (vmsConn == null || vmsConn.isClosed()) {
                logger.error("getConnection - Either connection not created or is closed.");
            }
        } catch(SQLException se) {
            logger.error("getConnection - ", se);
        }
        
        return vmsConn;
    }
    
}




