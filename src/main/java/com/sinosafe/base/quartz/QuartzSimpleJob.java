package com.sinosafe.base.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 支付回调出单任务
 * @author huangping5
 *
 */
@Service("quartzSimpleJob")
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzSimpleJob implements Job{
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		logger.info("quartz job test");
	}
}
