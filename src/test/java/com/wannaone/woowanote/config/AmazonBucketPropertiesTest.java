package com.wannaone.woowanote.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AmazonBucketPropertiesTest {
    private static final Logger log = LoggerFactory.getLogger(AmazonBucketPropertiesTest.class);
    @Autowired
    private AmazonBucketProperties amazonBucketProperties;

    @Test
    public void propertiesNotBlankTest() {
        log.debug("bucket : {}", amazonBucketProperties.getBucket());
        log.debug("directory : {}", amazonBucketProperties.getDirectory());
        assertThat(amazonBucketProperties.getBucket()).isNotBlank();
        assertThat(amazonBucketProperties.getDirectory()).isNotBlank();
    }
}