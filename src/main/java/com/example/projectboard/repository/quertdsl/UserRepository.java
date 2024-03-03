package com.example.projectboard.repository.quertdsl;

import com.example.projectboard.domain.UserAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends
    JpaRepository<UserAccount, String> {

    Optional<UserAccount> findByNickname(String nickname);

    Optional<UserAccount> findByUserId(String userId);

    Optional<UserAccount> findByEmail(String email);
}
