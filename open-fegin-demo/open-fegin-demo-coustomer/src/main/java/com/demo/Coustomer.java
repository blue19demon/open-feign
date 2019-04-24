package com.demo;


import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.demo.api.OrderClient;
import com.demo.entity.Order;
import com.feign.core.Feign;
import com.feign.logger.Logger;
import com.feign.logger.LoggerFactory;

public class Coustomer {
	private static Logger log = LoggerFactory.getLogger(Coustomer.class);
	public static void main(String[] args) {
		Order o = Order.builder().name("淘宝订单").price(new BigDecimal(100)).time(new Date()).build();
		OrderClient orderClient = Feign.builder().target(OrderClient.class);
		Order order = orderClient.saveOrder(o);
		log.info(JSONObject.toJSONString(order));
	}

}
