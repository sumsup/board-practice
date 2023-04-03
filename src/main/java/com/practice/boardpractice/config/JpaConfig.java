package com.practice.boardpractice.config;

import com.practice.boardpractice.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing // 작성자 || 수정자 이름을 자동으로 맵핑 시켜주는 기능. @CreatedBy 류의 설정을 지원하는 것.
@Configuration
public class JpaConfig {

    /**
     * 작성자나 수정자 자동 설정을 위한 기능. auditor (심사자).
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        // SecurityContextHolder : 인증정보를 가지고 있는 클래스.
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(BoardPrincipal.class::cast) // BoardPrincipal 클래스로 타입 캐스팅함.
                .map(BoardPrincipal::getUsername);
    }

}
