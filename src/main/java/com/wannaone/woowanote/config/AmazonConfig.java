package com.wannaone.woowanote.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AmazonBucketProperties.class)
public class AmazonConfig {
}
