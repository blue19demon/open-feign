package org.eurake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zk.EnableFeignZookeeper;

@SpringBootApplication
@EnableFeignZookeeper
public class FeignServerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FeignServerApplication.class);
		app.run(args);
	}
}
