/*
 * VMSGatherer.java
 *
 * Created on June 17, 2008, 11:25 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.client.helper;

import com.stratify.apps.vms.common.VMSCommonUtils;
import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.common.exceptions.VMSMailException;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import com.stratify.apps.vms.common.vos.VMSComponent;
import com.stratify.apps.vms.common.vos.VMSCaseServer;
import com.stratify.apps.vms.dao.VMSConnectionFactory;
import com.stratify.apps.vms.dao.VMSDAOFactory;
import com.stratify.apps.vms.dao.VMSMSDAO;
import com.stratify.apps.vms.dao.VMSMSDAOFactory;
import com.stratify.apps.vms.dao.VMSMyDAO;
import com.stratify.apps.vms.client.VMSFEComponentClient;
import com.stratify.apps.vms.mail.VMSMailer;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;
import com.stratify.common.logging.Logger;
import com.stratify.datahub.common.util.PyEnv;
import com.stratify.datahub.common.util.PyEnvLocal;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobDataMap;


/**
 *
 * @author RavikishoreS
 */
public class VMSGatherer implements  Job{
    static Logger logger = Logger.getLogger(VMSGatherer.class.getName());
    String executiondomain = null;
    
    /** Creates a new instance of VMSGatherer */
    public VMSGatherer() {
    }
    
    public VMSGatherer(String d){
        this.executiondomain = d;
    }
    
 public void execute(JobExecutionContext jcontext) throws JobExecutionException {       
        ArrayList caseServers = null;
        Properties mailerProps = null;
        int mailerFlag = 0;
        ArrayList mailerData = null;
        ArrayList multipleInstallationData = null;
        long crawlDate = 0L, compoDate = 0L, mailDate = 0L, mailCompleteDate = 0L;
        
        JobDataMap jobdatamap = jcontext.getJobDetail().getJobDataMap();		
	this.executiondomain = jobdatamap.getString("execution_domain");			
        
        VMSGatherHelper gatherhelper = new VMSGatherHelper(executiondomain);
        
//        SimpleDateFormat sdf = new SimpleDateFormat(VMSStaticParams.DATE_FORMATTER);
//        crawlDate = sdf.format(new Date());
        
        crawlDate = System.currentTimeMillis();
        
        //get cases from DBLM MySQL DB        
        try {
             gatherhelper.logStats(crawlDate, crawlDate, 1);
            caseServers = gatherhelper.getCaseServers(executiondomain);           
        }catch(VMSSysException ex){
            logger.error("run : Error connecting to DBLM MYSQL DB: " , ex)  ;
            logger.info("run : Terminating execution of the VMS crawler...")  ;
            System.exit(0);
        }catch(Exception e){
            logger.error("run : Error : " , e)  ;
            logger.info("run : Terminating execution of the VMS crawler...")  ;
            System.exit(0);
        }
                       
        try{                       
            //try to synch case, servers and their mapping in destination MYSQL
            gatherhelper.synchronizeCaseServers(caseServers);
            logger.info("run : Case, Servers and their mappings synchronized.");
            
            compoDate = System.currentTimeMillis();
            gatherhelper.logStats(crawlDate, compoDate, 2);
            
            //process BE/FE components
            gatherhelper.processComponents();
            
            mailDate = System.currentTimeMillis();
            gatherhelper.logStats(crawlDate, mailDate, 3);
                     
            //start activity for mailer if mailer option is enabled
            //read mailer on/off flag from config file - by default ON
            mailerProps = VMSCommonUtils.getPropertiesObject(System.getProperty(PyEnv.DATAHUB_ROOT_KEY) + VMSStaticParams.MAILERCONFIGFILE);
            mailerFlag = Integer.parseInt(mailerProps.getProperty(VMSStaticParams.MAILERFLAG));
            logger.info("run :Mailer Config properties read. mailerFlag: " + mailerFlag);
            
            if(mailerFlag == 1){
                //call the stored proc for generating incompatible components
                logger.info("run: Calling sp for detecting incompatible components...");
                mailerData = gatherhelper.callIncompatibleCompoProc();
                
                //run mailer
                VMSMailer vmsmailer = new VMSMailer(mailerProps);
                vmsmailer.runMailer(mailerData);
                logger.info("run: Mails dispatched for incompatible components.");
                
                //call the stored proc to detect multiple installations
                logger.info("run: Calling sp to detect multiple installations.");
                multipleInstallationData = gatherhelper.callMutipleInstallProc();
                
                vmsmailer.sendMail4MultipleInstallations(multipleInstallationData);
            }
            
            mailCompleteDate = System.currentTimeMillis();
            gatherhelper.logStats(crawlDate, mailCompleteDate,  4);
            
        }catch(VMSSysException ve){
            logger.error("run: Error : " , ve);
        }catch(VMSMailException me){
            logger.error("run: Error in Mailer : " , me);
        }catch(Exception ex){
            logger.error("run: Error : " , ex);
        }   
    }
    
}
