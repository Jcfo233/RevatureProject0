package com.revature.util;

import org.apache.log4j.Logger;

import com.revature.instances.Driver;

public class LoggerUtil {

	public static Logger log = Logger.getLogger(LoggerUtil.class);
	
	private LoggerUtil() {
		super();
	}
	public static Logger getLogger() {
		return log;
	}
	
}

