package com.springboot.MessApplication.MessMate.repositories;

import com.springboot.MessApplication.MessMate.entities.PasswordResetToken;
import com.springboot.MessApplication.MessMate.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    void deleteByUser(User user);

    Optional<PasswordResetToken> findByResetToken(String resetToken);
}
