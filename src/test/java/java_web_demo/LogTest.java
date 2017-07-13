package java_web_demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {
	private static Logger log = LoggerFactory.getLogger(LogTest.class);
	public static void main(String[] args) throws Exception{
		log.info("test");
	}
}
