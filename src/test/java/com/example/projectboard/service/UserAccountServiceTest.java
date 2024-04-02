package com.example.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.projectboard.domain.UserAccount;
import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.repository.UserAccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks
    private UserAccountService userAccountService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Test
    @DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 Optional로 반환한다.")
    void givenExistentUserId_whenSearching_thenReturnsOptionalUserDate() {
        //Given
        String username = "jh";
        given(userAccountRepository.findById(username)).willReturn(Optional.of(createUserAccount(username)));

        //When
        Optional<UserAccountDto> result = userAccountService.searchUser(username);

        //Then
        assertThat(result).isPresent();
        then(userAccountRepository).should().findById(username);
    }

    @Test
    @DisplayName("비존재 ID 검색시")
    void givenNonexistentUserId_whenSearching_thenReturnsOptionalUserData() {
        //Given
        String username = "jh";
        given(userAccountRepository.findById(username)).willReturn(Optional.empty()); //빈값 반환

        //When
        Optional<UserAccountDto> result = userAccountService.searchUser(username);

        //Then
        assertThat(result).isEmpty();
        then(userAccountRepository).should().findById(username);
    }

    @Test
    @DisplayName("회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴")
    void givenUserParams_whenSaving_thenSavesUserAccount() {
        //Given
        UserAccount userAccount = createUserAccount("jh");
        UserAccount saveUserAccount = createSigningUpUserAccount("jh");
        given(userAccountRepository.save(userAccount)).willReturn(saveUserAccount);

        //When
        UserAccountDto userAccountDto = userAccountService.saveUser(
            userAccount.getUserId(),
            userAccount.getUserPassword(),
            userAccount.getEmail(),
            userAccount.getNickname(),
            userAccount.getMemo()
        );

        //Then
        assertThat(userAccountDto)
            .hasFieldOrPropertyWithValue("userId",userAccount.getUserId())
            .hasFieldOrPropertyWithValue("userPassword",userAccount.getUserPassword())
            .hasFieldOrPropertyWithValue("email",userAccount.getEmail())
            .hasFieldOrPropertyWithValue("nickname",userAccount.getNickname())
            .hasFieldOrPropertyWithValue("memo",userAccount.getMemo())
            .hasFieldOrPropertyWithValue("createdBy",userAccount.getUserId())
            .hasFieldOrPropertyWithValue("modifiedBy",userAccount.getUserId());
        then(userAccountRepository).should().save(userAccount);
    }

    private UserAccount createUserAccount(String username){
        return createUserAccount(username, null);
    }

    private UserAccount createSigningUpUserAccount(String username){
        return createUserAccount(username, username);
    }

    private UserAccount createUserAccount(String username, String createdBy){
        return UserAccount.of(
            username,
            "password",
            "email",
            "nickname",
            "memo",
            createdBy
        );
    }
}