/*
 * VMSStatXnManager.java
 *
 * Created on June 26, 2009, 8:03 AM
 *
 * Confidential and Proprietary
 * (c) Copyright 1999 - 2008 Stratify, an Iron Mountain Company. ( f/k/a PurpleYogi f/k/a Calpurnia ). All rights reserved.
 * The foregoing shall not be deemed to indicate that this source has been published.
 * Instead, it remains a trade secret of Stratify, an Iron Mountain Company.
 */

package com.stratify.apps.vms.dao;

import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import java.sql.Connection;
import com.stratify.common.logging.Logger;

/**
 *
 * @author RavikishoreS
 */
public class VMSStatXnManager {
    static Logger logger = Logger.getLogger(VMSStatXnManager.class.getName());
    private long crawldate = 0L;
    private long date = 0L;
    int indicator = 0;
    private Connection con = null;
    private VMSTransactionImpl transimpl = null;
    private VMSMyDAO insMyDAO = null;
    
    /** Creates a new instance of VMSStatXnManager */
    public VMSStatXnManager(long datecrawl, long datetemp, int indicator ) {
        this.crawldate = datecrawl;
        this.date = datetemp;
        this.indicator = indicator;
        try{
        //create Connection to dest MySQL DB
        con = VMSConnectionFactory.getMyConnection(1);
        //create Transaction
        transimpl = new VMSTransactionImpl(con);
        //create DAO
        insMyDAO = VMSDAOFactory.getVerMyDAO(con);
        }catch(Exception ex){
            logger.error("VMSStatXnManager: Error : " + ex.getMessage());
        }        
        
    }
    
    public void record() throws VMSDAOException{
        int counterIns = 0, counterUdt = 0;
        if(indicator == 1){
            //insert entry for casesynch
            counterIns = insMyDAO.insCrawlEntry(crawldate, date, "casesynch");           
        }else if(indicator == 2){
            transimpl.begin();           
            try{
                //insert entry for fetchComponents
                counterIns = insMyDAO.insCrawlEntry(crawldate, date, "fetchComponents");
                //update entry for casesynch
                counterUdt = insMyDAO.updateCrawlEntry(crawldate, date, "casesynch");
            }catch(Exception e){
                transimpl.rollback();
                logger.error("record: ", e);
            }            
            transimpl.end();
        }else if(indicator == 3){                      
            transimpl.begin();            
            try{
                //insert entry for mailer
                counterIns = insMyDAO.insCrawlEntry(crawldate, date, "mailer");
                 //update for fetchComponents
                counterUdt = insMyDAO.updateCrawlEntry(crawldate, date, "fetchComponents");
            }catch(Exception e){
                transimpl.rollback();
                logger.error("record: ", e);
            }
            
            transimpl.end();
        }else if(indicator == 4){
            //update for mailer
            try{
            counterUdt = insMyDAO.updateCrawlEntry(crawldate, date, "mailer");
            }catch(Exception ex){
                logger.error("record: ", ex);
            }
            
        }
    }
    
}
