package com.crawl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BaseList implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private BaseInfo[] list;

}
