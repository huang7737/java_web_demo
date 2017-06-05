package com.sinosafe.app.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimpleJob {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	@Scheduled(fixedRate=30000)//30秒执行一次
    public void execute(){  
    	logger.info("job test"); 
    }
}
