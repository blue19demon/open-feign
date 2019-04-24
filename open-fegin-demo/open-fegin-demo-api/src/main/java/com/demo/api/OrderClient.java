package com.demo.api;

import com.demo.entity.Order;
import com.feign.anno.FeignClient;
import com.feign.anno.RequestMapping;
import com.feign.anno.RequestMethod;

@FeignClient("server-order")
public interface OrderClient {

	@RequestMapping(value="/saveOrder",method=RequestMethod.POST)
	public Order saveOrder(Order order);
}
