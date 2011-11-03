/*
 * VMSConnectionFactory.java
 *
 * Created on September 18, 2007, 1:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.stratify.apps.vms.dao;

import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.dao.common.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.stratify.common.logging.Logger;

/**
 *
 * @author ravikishores
 */
public class VMSConnectionFactory {
    static Logger logger = Logger.getLogger(VMSConnectionFactory.class.getName());
    
    /**
     * Creates a new instance of VMSConnectionFactory
     */
    public VMSConnectionFactory() {
    }
    
    public static Connection getMyConnection(int i) throws Exception{
        String myurl = "";
        Connection conMy = null;
        
        //myurl = VMSDAOUtils.getMyUrl(VMSStaticParams.MYSQL_DATABASE, VMSStaticParams.MYSQL_DB_SERVER, i);
        //get Connection to MySQL, i = 1 for connecting to VMS MYSQL server
        if(i == 0){
            //conMy = getMySQLDBConnection(VMSStaticParams.MYSQL_CONNECTION_STRING,myurl, VMSStaticParams.MYSQL_USERNAME, VMSStaticParams.MYSQL_PASSWORD);
            DBLMDBConnection ddc = new DBLMDBConnection();
            ddc.open();
            conMy = ddc.getConnection();
        } else{
            //conMy = getMySQLDBConnection(VMSStaticParams.MYSQL_CONNECTION_STRING,myurl, VMSStaticParams.DEST_MYSQL_USERNAME, VMSStaticParams.DEST_MYSQL_PASSWORD);
            VMSDBConnection vdc = new VMSDBConnection();
            vdc.open();
            conMy = vdc.getConnection();
        }
        return conMy;
        
    }
    
    
    
    public static Connection getMSConnection(String serverName, String dbName, String domain) throws Exception{
        String msurl = null;
        Connection conMS = null;
        
        logger.info("getMSConnection: DOMAIN passed " + domain);
        if(domain.equalsIgnoreCase(VMSStaticParams.BEALIAS)){
            msurl = VMSDAOUtils.getUrl(dbName, serverName, VMSStaticParams.BEALIAS);
        }else if (domain.equalsIgnoreCase(VMSStaticParams.FEALAIS)){
            msurl = VMSDAOUtils.getUrl(dbName, serverName, VMSStaticParams.FEALAIS);
            logger.info("getMSConnection: 2nd part : " + domain);
        }else if (domain.equalsIgnoreCase(VMSStaticParams.APACBEALIAS)){
            msurl = VMSDAOUtils.getUrl(dbName, serverName, VMSStaticParams.APACBEALIAS);
        }else if (domain.equalsIgnoreCase(VMSStaticParams.APACFEALAIS)){
            msurl = VMSDAOUtils.getUrl(dbName, serverName, VMSStaticParams.APACFEALAIS);
            logger.info("getMSConnection: 4rth part : " + domain);
        }
        
        logger.info("getMSConnection: URL: " + msurl);
        logger.info("getMSConnection : DB AUTH : " + VMSStaticParams.MSSQL_BE_USERNAME + ":" + VMSStaticParams.MSSQL_BE_PASSWORD);
        
        conMS = getDBConnection(VMSStaticParams.CONNECTION_STRING, msurl, VMSStaticParams.MSSQL_BE_USERNAME, VMSStaticParams.MSSQL_BE_PASSWORD);
        
        return conMS;
    }
    
    public static Connection getMySQLDBConnection(String conString, String dbURL,
            String uName, String passWd) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        // TODO Auto-generated method stub
        Connection con = null;
        Class.forName(conString).newInstance();
        con = DriverManager.getConnection(dbURL, uName, passWd);
        
        return con;
    }
    
    public static Connection getDBConnection(String conString, String dbURL,
            String uName, String passWd) throws ClassNotFoundException,
            SQLException {
        // TODO Auto-generated method stub
        Connection con = null;
        Class.forName(conString);
                
        logger.info("URL: " + dbURL + "UNAME: " + uName + "PASSWD: " + passWd + " TIMEOUT: " + VMSStaticParams.DB_LOGIN_TIMEOUT);        
        DriverManager.setLoginTimeout(VMSStaticParams.DB_LOGIN_TIMEOUT);
        con = DriverManager.getConnection(dbURL, uName, passWd);
        
        return con;
    }
    
    
    public static void closeCon(Connection con) throws SQLException {
        if(con != null){
            con.close();
            con = null;
        }
        
    }
    
}
