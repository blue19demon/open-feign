package com.crawl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BaseInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String assessmentcano;
	/**
	 * 
	 */
	private String brokercano;
	/**
	 * 业务范围
	 */
	private String businesscope;
	/**
	 * 身份证后四位
	 */
	private String cardno;
	/**
	 *有效期
	 */
	private String end_date;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 所属公司
	 */
	private String insurnnce_code;
	/**
	 * 执业证状态
	 */
	private String is_status;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 
	 */
	private String personid;
	/**
	 * 执业区域
	 */
	private String practicearea;
	/**
	 *执业证编号
	 */
	private String practicecode;
	/**
	 * 资格证书号码
	 */
	private String ualificano;
	/**
	 * 资格证书状态
	 */
	private String validStatus;
}
