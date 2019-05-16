package com.crawl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class GoodRecord  implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String queryAllowed;
	private Object rows;
	private String total;
}
