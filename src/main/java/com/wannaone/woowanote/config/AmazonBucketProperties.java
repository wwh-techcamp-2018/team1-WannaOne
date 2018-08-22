package com.wannaone.woowanote.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * application.properties의 aws.s3 프리픽스의 bucket, directory값을
 * 해당 객체에 바인딩해주고 bean 형태로 만들어준다.
 */
@ConfigurationProperties(prefix = "aws.s3")
@Validated
public class AmazonBucketProperties {
    @NotBlank
    private String bucket;
    @NotBlank
    private String directory;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
