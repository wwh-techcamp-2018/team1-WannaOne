package com.wannaone.woowanote;

import com.wannaone.woowanote.config.AmazonProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AmazonProperties.class)
public class WoowanoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoowanoteApplication.class, args);
	}
}
