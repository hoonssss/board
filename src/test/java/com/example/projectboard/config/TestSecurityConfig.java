package com.example.projectboard.config;

import static org.mockito.BDDMockito.*;

import com.example.projectboard.domain.UserAccount;
import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.repository.UserAccountRepository;
import com.example.projectboard.service.UserAccountService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserAccountRepository userAccountRepository;

    @MockBean
    private UserAccountService userAccountService;

    @BeforeTestMethod //인증 관련 테스트
    public void securitySetup() {
        given(userAccountService.searchUser(anyString()))
            .willReturn(Optional.of(createUserAccountDto()));
        given(userAccountService.saveUser(anyString(),anyString(),anyString(),anyString(),anyString()))
            .willReturn(createUserAccountDto());
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.of(
            "jhtest",
            "pw",
            "wogns8030@naver.com",
            "jh",
            "testmemo"
        );
    }
}
