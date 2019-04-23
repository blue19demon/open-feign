package com.feign.service;

import com.feign.anno.FeignClient;
import com.feign.anno.RequestMapping;
import com.feign.anno.RequestMethod;
import com.feign.api.Product;

@FeignClient("feign-server02")
public interface ProductClient {

	@RequestMapping(value="/saveProduct",method=RequestMethod.POST)
	public Product saveProduct(Product product);
	
}
