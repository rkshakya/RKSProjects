/*
 * VMSScheduler.java
 *
 * Created on June 25, 2009, 3:53 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.client.helper;


import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import org.quartz.JobDetail;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.JobDataMap;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import com.stratify.common.logging.Logger;

/**
 *
 * @author RavikishoreS
 */

public class VMSScheduler {
	static Logger logger = Logger.getLogger(VMSScheduler.class.getName());

	private String exeDomain = null;	
	private String schedule = null;

	public VMSScheduler(String execution_domain, String schedule) {
		this.exeDomain = execution_domain;		
		this.schedule = schedule;
	}

	public void schedule() throws VMSSysException{
		try {
                        logger.info("schedule: Job scheduled.");
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler scheduler = sf.getScheduler();
			JobDetail jobdetail = new JobDetail("vmsjob", "vmsgroup",
					VMSGatherer.class);

			// populate jobdatamap
			JobDataMap jobdatamap = jobdetail.getJobDataMap();
			jobdatamap.put("execution_domain", exeDomain);
			
			CronTrigger cronTrigger = new CronTrigger("vmstrigger","vmsgroup", schedule);
			scheduler.scheduleJob(jobdetail, cronTrigger);
			scheduler.start();
		} catch (Exception se) {
                        logger.error("schedule: Error: " + se.getMessage());
                        throw new VMSSysException(VMSStaticParams.SEVERITY_1, VMSStaticParams.CRITICAL, "Scheduler could not be configured/initiated", VMSStaticParams.NA);
		}
	}

}
   
