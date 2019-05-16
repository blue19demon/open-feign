package com.crawl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.crawl.api.IicCrawlAPI;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IicCrawlApplicationTests {

	@Autowired
	private IicCrawlAPI IicCrawlAPI;
	@Test
	public void contextLoads() {
		String name="周菁";
		String cardno="7263";
		String personid=IicCrawlAPI.findPersonid(name, cardno);
		log.info(personid);
		log.info(JSONObject.toJSONString(IicCrawlAPI.queryBasicInfo(personid)));
		log.info(JSONObject.toJSONString(IicCrawlAPI.queryGoodRecord(personid)));
		log.info(JSONObject.toJSONString(IicCrawlAPI.queryFindBad(personid)));
	}

}
