package com.client;

public class LoggerFactory {

	public static Logger getLogger(Class<?> clazz) {
		return new Logger(clazz);
	}

}
