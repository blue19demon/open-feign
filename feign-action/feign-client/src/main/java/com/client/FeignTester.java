package com.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.feign.api.Product;
import com.feign.api.User;
import com.feign.core.EurakeServer;
import com.feign.core.Feign;
import com.feign.service.PersonClient;
import com.feign.service.ProductClient;

public class FeignTester {
	private static Logger log = LoggerFactory.getLogger(FeignTester.class);
	private static Map<String, EurakeServer> eurakeServerMaps = null;
	static {
		eurakeServerMaps = new HashMap<String, EurakeServer>() {
			private static final long serialVersionUID = 1L;
			{
				put("feign-server",
						EurakeServer.builder().serverName("feign-server").serverURL("127.0.0.1:80").build());
				put("feign-server02",
						EurakeServer.builder().serverName("feign-server02").serverURL("127.0.0.1:81").build());
			}
		};
	}

	public static void main(String[] args) {
		// 1.简单字符串返回值
		Product para = Product.builder().name("ipad").price(new BigDecimal(10000)).build();
		ProductClient client = Feign.builderByZookeeper().target(ProductClient.class);
		Product p = client.saveProduct(para);

		User upara = User.builder().name("李斯").desc("宰相").salary(new BigDecimal(10000)).build();
		PersonClient PersonClient = Feign.builderByZookeeper().target(PersonClient.class);
		User u = PersonClient.toHello3(upara);
		log.info(JSONObject.toJSONString(p) + ">>>>>" + JSONObject.toJSONString(u));
	}

	public static void testCase2() {
		// 1.简单字符串返回值
		Product para = Product.builder().name("ipad").price(new BigDecimal(10000)).build();
		ProductClient client = Feign.builder(eurakeServerMaps).target(ProductClient.class);
		Product p = client.saveProduct(para);

		User upara = User.builder().name("李斯").desc("宰相").salary(new BigDecimal(10000)).build();
		PersonClient PersonClient = Feign.builder(eurakeServerMaps).target(PersonClient.class);
		User u = PersonClient.toHello3(upara);
		log.info(JSONObject.toJSONString(p) + ">>>>>" + JSONObject.toJSONString(u));
	}

	public static void testCase1() {
		// 1.简单字符串返回值
		User para = User.builder().name("李斯").desc("宰相").salary(new BigDecimal(10000)).build();
		PersonClient client = Feign.builder().target(PersonClient.class);
		// String result = client.toHello("张三");
		// String result = client.toHello1(para);
		// User result = client.toHello2("张三", "好人");
		User result = client.toHello3(para);
		log.info(JSONObject.toJSONString(result));

		// User postUser = client.postUser("张三", "好人");
		// log.info(JSONObject.toJSONString(postUser));

		// User u = client.postUser("李斯","宰相");
		// String u = client.postUser1(para);
		// User u = client.postUser2("李斯","宰相");
		// User u = client.postUser3(para);
		// String u = client.postUser4("李斯","宰相");
		// log.info(u);
	}
}
