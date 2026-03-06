package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.entities.PasswordResetToken;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.repositories.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int TOKEN_EXPIRY_MINUTES = 15;

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserService userService;

    @Transactional
    public String createResetTokenAndSendOtp(User user) {
        //delete an existing token
        passwordResetTokenRepository.deleteByUser(user);

        String resetToken = UUID.randomUUID().toString();
        String otp = generateOtp();

        //create token
        PasswordResetToken token = PasswordResetToken.builder()
                .resetToken(resetToken)
                .hashedOtp(passwordEncoder.encode(otp))
                .attemptCount(0)
                .otpVerified(false)
                .otpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES))
                .tokenExpiry(LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_MINUTES))
                .user(user)
                .build();

        //save token
        passwordResetTokenRepository.save(token);

        //send email to user
        emailService.sendMail(
                user.getEmail(),
                "Password reset OTP",
                "your OTP is: " + otp
        );

        //return token
        return token.getResetToken();
    }

    private String generateOtp() {
        return String.valueOf((int)(Math.random()*900000)+100000 );
    }

    public void verifyOtp(String resetToken, String otp) {
        PasswordResetToken token = passwordResetTokenRepository.findByResetToken(resetToken)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Session"));

        if(token.isOtpVerified()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"OTP already verified"
            );
        }

        if(token.getTokenExpiry().isBefore(LocalDateTime.now())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"Reset Session Expired"
            );
        }

        if(token.getOtpExpiry().isBefore(LocalDateTime.now())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"OTP expired"
            );
        }

        if(token.getAttemptCount()>=MAX_ATTEMPTS){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"Too many attempts"
            );
        }

        if(!passwordEncoder.matches(otp,token.getHashedOtp())){
            token.setAttemptCount(token.getAttemptCount() + 1);
            passwordResetTokenRepository.save(token);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"invalid Otp"
            );
        }

        token.setOtpVerified(true);
        passwordResetTokenRepository.save(token);
    }

    public void resetPassword(String resetToken, String newPassword) {
        PasswordResetToken token = passwordResetTokenRepository
                .findByResetToken(resetToken)
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,"Invalid Session"
                ));

        if(!token.isOtpVerified()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"otp not verified"
            );
        }

        if(token.getTokenExpiry().isBefore(LocalDateTime.now())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"Reset session Expired"
            );
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);

        passwordResetTokenRepository.delete(token);
    }
}
