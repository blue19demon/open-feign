package org.open.fegin.demo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zk.EnableFeignZookeeper;

@SpringBootApplication
@EnableFeignZookeeper
public class DemoProviderApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DemoProviderApplication.class);
		app.run(args);
	}
}
