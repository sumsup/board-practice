package com.practice.boardpractice.config;

import com.practice.boardpractice.domain.UserAccount;
import com.practice.boardpractice.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Spring Security를 로딩하는 테스트에서 userAccount를 불러올 수 없어서 모든 테스트의 로딩이 실패함.
 * 그래서 MockBean을 만들어서 제공 하는 역할.
 * Security Config가 할당되는 테스트 클래스에서 @Import 해서 사용할 것.
 */
@Import(SecurityConfig.class)
public class TestSecurityConfig {
    @MockBean
    private UserAccountRepository userAccountRepository; // Security Config를 로드하는 테스트에 MockBean을 제공.

    /**
     * Mocking한 userAccountRepository에서 반환하는 유저 정보.
     */
    @BeforeTestMethod
    public void securitySetUp() {
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
                "userKim",
                "pw",
                "userKim-test@email.com",
                "userKim-test",
                "test memo"
        )));
    }
}
