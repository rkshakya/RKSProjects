  
 /*
 * DBLMDBConnection.java
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

public class DBLMDBConnection {
    private static Logger logger = Logger.getLogger(DBLMDBConnection.class.getName());
    private static transient DataSource dblmDB = null;
    private transient Connection dblmConn = null;
    
    /**
     * Creates a new instance of DbConnection
     */
    public DBLMDBConnection() throws VMSDAOException {
        if (null == dblmDB) {
            try {
                Context context = new InitialContext();
                Context envContext = (Context) context.lookup("java:/comp/env");
                
                dblmDB = (DataSource) envContext.lookup("jdbc/dblm");
                logger.info("DBLMDBConnection - DBLM Context obtd.");
                
            } catch (NamingException ne) {
                logger.error("DBLMDBConnection - ", ne);
                throw new VMSDAOException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Failed to get DBLM Naming Context", VMSStaticParams.NA);
            }
        }
        
    }
    
    public void open() throws Exception {
        try {
            if ((dblmConn != null) && (! dblmConn.isClosed())) {
                return;
            }
            dblmDB.setLoginTimeout(VMSStaticParams.DB_LOGIN_TIMEOUT);
            dblmConn = dblmDB.getConnection();
        } catch (SQLException se) {
            logger.error("open - ", se);
            throw new VMSDAOException(VMSStaticParams.SEVERITY_1, VMSStaticParams.CRITICAL, "Failed opening connection to DBLM DB", VMSStaticParams.NA);
            
        }
    }
    
    public void close() {
        try {
            if (dblmConn != null) {
                if (! dblmConn.isClosed()) {
                    dblmConn.close();
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
            if (dblmConn == null || dblmConn.isClosed()) {
                logger.error("getConnection - Either connection not created or is closed.");
            }
        } catch(SQLException se) {
            logger.error("getConnection - ", se);
        }
        
        return dblmConn;
    }
    
}





    
