package org.eurake;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.feign.api.Product;
import com.feign.service.ProductClient;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProductController implements ProductClient{
	
	@Override
	@PostMapping("/saveProduct")
	@ResponseBody
	public Product saveProduct(Product product) {
		log.info(JSONObject.toJSONString(product));
		product.setId(2);
		return product;
	}
}
