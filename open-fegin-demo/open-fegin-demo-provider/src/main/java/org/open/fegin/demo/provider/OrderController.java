package org.open.fegin.demo.provider;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.demo.api.OrderClient;
import com.demo.entity.Order;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class OrderController implements OrderClient{
	
	@PostMapping("/saveOrder")
	@ResponseBody
	public Order saveOrder(Order order) {
		log.info(JSONObject.toJSONString(order));
		order.setSerialNo(UUID.randomUUID().toString());
		return order;
	}
}
