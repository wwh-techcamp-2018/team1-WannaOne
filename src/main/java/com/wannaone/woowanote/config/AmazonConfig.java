package com.wannaone.woowanote.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties(AmazonBucketProperties.class)
@PropertySource(("classpath:aws.properties"))
public class AmazonConfig {
}
