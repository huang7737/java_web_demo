package com.sinosafe.app.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 
 * @author huangping5
 * 适应单个服务器情况，如果多服务器考虑使用quartz，或者用redis加锁
 */
@Component
public class SimpleJob {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	@Scheduled(fixedRate=30000)//30秒执行一次
    public void execute(){  
    	logger.info("run job test"); 
    }
}
