package com.client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private Class<?> clazz;
	private static SimpleDateFormat smf=new SimpleDateFormat("HH:mm:ss.SSS");
	public Logger(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	public void info(String str) {
		Date now=new Date();
		System.out.println(smf.format(now)+" [main] INFO "+clazz.getName()+" - "+str);
	}

}

