package com.practice.boardpractice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing // 작성자 || 수정자 이름을 자동으로 맵핑 시켜주는 기능. @CreatedBy 류의 설정을 지원하는 것.
@Configuration
public class JpaConfig {

    /**
     * 작성자나 수정자 자동 설정을 위한 기능. auditor (심사자).
     * @return
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("kim"); // TODO : 스프링 시큐리티 인증기능 붙이게 될 때 수정.
    }

}
