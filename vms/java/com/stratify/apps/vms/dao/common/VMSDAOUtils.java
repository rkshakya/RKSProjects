/*
 * VMSDAOUtils.java
 *
 * Created on September 17, 2007, 10:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.stratify.apps.vms.dao.common;

import com.stratify.apps.vms.common.VMSStaticParams;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import com.stratify.common.logging.Logger;

/**
 *
 * @author ravikishores
 */
public  class  VMSDAOUtils {
    static Logger logger = Logger.getLogger(VMSDAOUtils.class.getName());                      
    
    public static String getUrl(String dbname, String dbserver, String domain) throws Exception {
        // jdbc:jtds:<server_type>://<server>[:<port>][/<database>]
        String url = null;
        //String url = "jdbc:jtds:sqlserver://"+ dbserver +":1433/"+dbname+ ";domain=LDS";
        if(domain.equalsIgnoreCase(VMSStaticParams.BEALIAS)){
            url = VMSStaticParams.DB_URL_PREFIX + dbserver + ":" + VMSStaticParams.MSSQL_PORT + "/" + dbname;
        }else if (domain.equalsIgnoreCase(VMSStaticParams.FEALAIS)){
            url = VMSStaticParams.DB_URL_PREFIX + dbserver + VMSStaticParams.FESUFFIX + ":" + VMSStaticParams.MSSQL_PORT + "/" + dbname;
        }else if (domain.equalsIgnoreCase(VMSStaticParams.APACBEALIAS)){
            url = VMSStaticParams.DB_URL_PREFIX + dbserver + VMSStaticParams.BESUFFIX_APAC + ":" + VMSStaticParams.MSSQL_PORT + "/" + dbname;
        }else if (domain.equalsIgnoreCase(VMSStaticParams.APACFEALAIS)){
             url = VMSStaticParams.DB_URL_PREFIX + dbserver + VMSStaticParams.FESUFFIX_APAC + ":" + VMSStaticParams.MSSQL_PORT + "/" + dbname;
        }
        return url;
    }
    
//    public static String getMyUrl(String dbname, String dbserver, int i) throws Exception {
//        String url = null;
//        if(i == 0)    {
//        url = VMSStaticParams.MYSQL_DB_URL_PREFIX + VMSStaticParams.MYSQL_DB_SERVER + ":" + VMSStaticParams.MYSQL_PORT + "/"
//                + VMSStaticParams.MYSQL_DATABASE;
//        } else if(i == 1){
//          url = VMSStaticParams.DEST_MYSQL_DB_URL_PREFIX + VMSStaticParams.DEST_MYSQL_DB_SERVER + ":" + VMSStaticParams.DEST_MYSQL_PORT + "/"
//                + VMSStaticParams.DEST_MYSQL_DATABASE;
//        }
//        System.out.println("URL returned: " + url);
//        logger.info("URL returned: " + url);
//
//        return url;
//    }
    
    /**
     * Creates a new instance of VMSDAOUtils
     */
    public VMSDAOUtils() {
    }
    
    
    
    public static void freeCon(Connection con) {
        try{
            if(con != null){
                con.close();
                con = null;
            }
        }catch(SQLException sqle){
            logger.error("freeCon:" , sqle);
        }
    }
    
    public static void freeUp(ResultSet rsCntSize, PreparedStatement ps0) {
        
        try{
            if (rsCntSize != null ) {
                rsCntSize.close();
                rsCntSize = null;
            }
            
            if (ps0 != null) {
                ps0.close();
                ps0 = null;
            }
        }catch(SQLException sqle){
            logger.warn("freeUp: Error: " ,sqle);
        }
        
    }
    
    
    public static void checkUpdateCounts(int[] updateCounts) {
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] >= 0) {
                logger.info("checkUpdateCounts: Successfully executed; updateCount=" + updateCounts[i]);
            } else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                logger.info("checkUpdateCounts: Successfully executed; updateCount=Statement.SUCCESS_NO_INFO");
            } else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                logger.info("checkUpdateCounts: Failed to execute; updateCount=Statement.EXECUTE_FAILED");
            }
        }
    }
    
    /**
     * @param rs1
     * @param st1
     * @throws SQLException
     */
    public static void cleanUp(ResultSet rs1, Statement st1) throws SQLException {
        
        if(rs1 != null){
            rs1.close();
            rs1 = null;
        }
        if(st1 != null){
            st1.close();
            st1 = null;
        }
        
    }
    
    /**
     * @param con
     * @param useDB
     * @return
     * @throws SQLException
     */
    public static int runQueryCom(Connection con, String useDB) throws SQLException {
        
        PreparedStatement st0 = con.prepareStatement(useDB);
        int num = st0.executeUpdate();
        
        st0.close();
        st0 = null;
        
        return num;
    }
    
    /**
     * @param con
     * @param qry
     * @return
     * @throws SQLException
     */
    public static Vector runQuery(Connection con, String qry) throws SQLException {
        
        Vector res = new Vector();
        PreparedStatement st1 = con.prepareStatement(qry);
        ResultSet rs1 = st1.executeQuery();
        
        while (rs1.next()) {
            
            res.add(new Integer(rs1.getInt(1)));
        }
        
        //free resources
        freeUp(rs1, st1);
        
        return res;
    }
    
    
}
