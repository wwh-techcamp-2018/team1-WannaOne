package com.wannaone.woowanote.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * AuditingDateEntity에 적용된 JpaAuditing 기능을 사용하기 위한 설정
 * EnableJpaAuditing만 선언해주면 된다.
 */
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {

}