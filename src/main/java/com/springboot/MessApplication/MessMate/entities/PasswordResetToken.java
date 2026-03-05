package com.springboot.MessApplication.MessMate.entities;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String resetToken;

    @Column(nullable = false)
    private String hashedOtp;

    private int attemptCount;

    private boolean otpVerified;

    @Column(nullable = false)
    private LocalDateTime otpExpiry;

    @Column(nullable = false)
    private LocalDateTime tokenExpiry;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    private User user;
}

