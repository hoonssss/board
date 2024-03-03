package com.example.projectboard.dto.request;

import com.example.projectboard.domain.UserAccount;
import lombok.Builder;

@Builder
public record SignupRequestDto(
    String userId,
    String userPassword,
    String email,
    String nickname,
    String memo

) {

    public static SignupRequestDto of(String userId, String userPassword, String email, String nickname, String memo){
        return new SignupRequestDto(userId,userPassword,email,nickname,memo);
    }

    public UserAccount toEntity(String userId, String userPassword, String email, String nickname, String memo){
        return UserAccount.of(userId,userPassword,email,nickname,memo);
    }

}
