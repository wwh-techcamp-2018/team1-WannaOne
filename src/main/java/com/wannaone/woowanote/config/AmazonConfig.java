package com.wannaone.woowanote.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * AmazonAws S3 설정을 위한 Config
 * aws.properties 값이 있는 경우 로드하고, 없을 경우 무시한다.
 */
@Configuration
@EnableConfigurationProperties(AmazonBucketProperties.class)
@PropertySource(value="classpath:aws.properties", ignoreResourceNotFound = true)
public class AmazonConfig {
}
