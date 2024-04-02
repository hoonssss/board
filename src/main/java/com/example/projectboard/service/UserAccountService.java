package com.example.projectboard.service;

import com.example.projectboard.domain.UserAccount;
import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.repository.UserAccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Optional<UserAccountDto> searchUser(String username){
        return userAccountRepository.findById(username)
            .map(UserAccountDto::from);
//            .orElseThrow(() -> new EntityNotFoundException()) 개별적으로 예외처리
    }

    public UserAccountDto saveUser(String username, String password, String email, String nickname, String memo){
        return UserAccountDto.from(
            userAccountRepository.save(UserAccount.of(username,password,email,nickname,memo,username))
        );
    }

}
