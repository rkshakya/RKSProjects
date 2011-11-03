/*
 * VMSInvoker.java
 *
 * Created on November 19, 2007, 4:24 AM
 *
 * Stratify Inc P Ltd.
 *
 */

package com.stratify.apps.vms.client.helper;

import com.stratify.apps.vms.common.IStartup;
import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import java.util.Properties;
//import java.util.Timer;
//import java.util.TimerTask;
import com.stratify.common.logging.Logger;



/**
 *
 * @author ravikishores
 */
public class VMSInvoker implements IStartup {
    static Logger logger = Logger.getLogger(VMSInvoker.class.getName());
    private final long CONVER_MIN2MILLI = 60 * 1000;
    
    /** Creates a new instance of Invoker */
    public VMSInvoker() {
    }
    
    public String startup(String name, String rootpath, Properties args) throws Exception {
        //get the prop values
//        int iafter_mins = 0;
//        int iperiod = 0;
//        long lperiod = 0L;
//        long lafter_mins = 0L;
        try{
            String domain = null, schedule = null;
            //defaults to immediately and period 24 hrs, domain : normal(SDP) domain
//        iafter_mins = Integer.parseInt(args.getProperty(VMSStaticParams.START_AFTER_MINS, VMSStaticParams.DEFAULT_START_AFTER_MINS));
//        iperiod = Integer.parseInt(args.getProperty(VMSStaticParams.PERIOD_MINS, VMSStaticParams.DEFAULT_PERIOD_MINS));
            domain = args.getProperty(VMSStaticParams.EXECUTION_DOMAIN, "");
            schedule = args.getProperty(VMSStaticParams.SCHEDULE, VMSStaticParams.DEFAULT_SCHEDULE);
            
//        lafter_mins = (long)iafter_mins * CONVER_MIN2MILLI;
//        lperiod = (long)iperiod * CONVER_MIN2MILLI;
            
            logger.info("VMSInvoker: Starting VMSGatherHelper with PARAMS: execution domain: " + domain + " schedule: " + schedule);
            
//        TimerTask tt = new VMSGatherer(domain);
//
//        Timer t = new Timer();
//        //call the timertask
//        t.scheduleAtFixedRate(tt, lafter_mins, lperiod);
            
            //call Scheduler here
            VMSScheduler vmsscheduler = new VMSScheduler(domain, schedule);
            vmsscheduler.schedule();
        }catch(Exception ex){
            logger.info("startup: Error: " + ex.getMessage());
        }
        
        return IStartup.STATUS_OK;
    }
    
    public String shutdown() throws Exception {
        logger.info("VMSInvoker: Shutting down the VMSInvoker class..");
        return IStartup.STATUS_OK;
    }
    
}



