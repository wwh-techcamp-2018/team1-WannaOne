package com.wannaone.woowanote.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="file:/home/ec2-user/app/application-real.properties")
@Profile("prod")
public class ProductionDataBaseConfig {
}
