package org.eurake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zk.EnableFeignZookeeper;

@SpringBootApplication
@EnableFeignZookeeper
public class FeignServer02Application {
	
	public static void main(String[] args) {
		SpringApplication.run(FeignServer02Application.class, args);
	}

}
