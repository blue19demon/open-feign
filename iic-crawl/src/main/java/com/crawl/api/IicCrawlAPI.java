package com.crawl.api;

import com.crawl.dto.BadRecord;
import com.crawl.dto.BaseInfo;
import com.crawl.dto.GoodRecord;

public interface IicCrawlAPI {

	/**
	 * 模拟登录系统
	 * 
	 * @param personid
	 */
	public String findPersonid(String name, String cardno);

	/**
	 * 基本信息记录
	 * 
	 * @param personid
	 */
	public BaseInfo queryBasicInfo(String personid);

	/**
	 * 表彰奖励记录
	 * 
	 * @param personid
	 */
	public GoodRecord queryGoodRecord(String personid);

	/**
	 * 违规违章记录
	 * 
	 * @param personid
	 */
	public BadRecord queryFindBad(String personid);
}
