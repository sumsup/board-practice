package com.practice.boardpractice.config;

import com.practice.boardpractice.dto.UserAccountDto;
import com.practice.boardpractice.dto.security.BoardPrincipal;
import com.practice.boardpractice.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean // TODO: Bean 메서드로 등록하면 어떤 일이 벌어지는가? 메서드를 빈으로 등록할 수 있다고?
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // static 리소스는 모두 허용. css나 js. atCommonLocations는 스프링 기본 설정 경로를 적용함.
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll() // 위 요청은 모두에게 접근 허용.
                        .anyRequest().authenticated() // 그밖의 다른 것은 인증 받아야 함.
                )
                .formLogin().and()
                .logout()
                .logoutSuccessUrl("/") // 로그 아웃시 해당 URL로 이동.
                .and()
                .build();
    }

    /**
     * TODO: 왜 필요?
     * @param userAccountRepository
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
