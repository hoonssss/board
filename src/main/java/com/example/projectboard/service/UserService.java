package com.example.projectboard.service;

import com.example.projectboard.domain.UserAccount;
import com.example.projectboard.dto.UserAccountDto;
import com.example.projectboard.dto.request.SignupRequestDto;
import com.example.projectboard.repository.quertdsl.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAccount signUp(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.userId();
        String email = signupRequestDto.email();
        String nickname = signupRequestDto.nickname();

        checkIfExists(userRepository::findByNickname, nickname, "중복된 닉네임 존재");
        checkIfExists(userRepository::findByEmail, email, "중복된 이메일 존재");
        checkIfExists(userRepository::findByUserId, username, "중복된 UserId 존재");

        String password = signupRequestDto.userPassword();
        String memo = signupRequestDto.memo();
        UserAccount entity = signupRequestDto.toEntity(username, password, email, nickname, memo);
        userRepository.save(entity);

        return entity;
    }

    private void checkIfExists(CheckFunction checkFunction, String value, String errorMessage) {
        Optional<UserAccount> userAccountOptional = checkFunction.check(value);
        if (userAccountOptional.isPresent()) {
            throw new RuntimeException(errorMessage);
        }
    }

    interface CheckFunction {
        Optional<UserAccount> check(String value);
    }
}