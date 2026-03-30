package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.entities.PasswordResetToken;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.exceptions.PasswordResetException;
import com.springboot.MessApplication.MessMate.repositories.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        emailService.sendHtmlMail(
                user.getEmail(),
                "Password Reset OTP - MessMate",
                buildPasswordResetHtmlEmail(user.getName(), otp)
        );

        //return token
        return token.getResetToken();
    }

    private String generateOtp() {
        return String.valueOf((int)(Math.random()*900000)+100000 );
    }

    private String buildPasswordResetHtmlEmail(String userName, String otp) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; background-color: #f5f5f5;">
                <table width="100%%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td align="center" style="padding: 40px 0;">
                            <table width="560" border="0" cellspacing="0" cellpadding="0" style="background-color: #ffffff; border-radius: 4px;">
                                <!-- Header -->
                                <tr>
                                    <td style="padding: 32px 40px; border-bottom: 1px solid #e5e5e5;">
                                        <h1 style="color: #1a1a1a; margin: 0; font-size: 24px; font-weight: 600; letter-spacing: -0.5px;">MessMate</h1>
                                    </td>
                                </tr>
                                <!-- Content -->
                                <tr>
                                    <td style="padding: 40px;">
                                        <h2 style="color: #1a1a1a; margin: 0 0 24px 0; font-size: 20px; font-weight: 600;">Reset your password</h2>
                                        <p style="color: #4a4a4a; margin: 0 0 24px 0; font-size: 15px; line-height: 1.6;">
                                            Hi %s,
                                        </p>
                                        <p style="color: #4a4a4a; margin: 0 0 32px 0; font-size: 15px; line-height: 1.6;">
                                            We received a request to reset your password. Use the OTP below to proceed:
                                        </p>
                                        <!-- OTP Box -->
                                        <table width="100%%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td align="center">
                                                    <div style="background-color: #fafafa; border: 1px solid #e0e0e0; border-radius: 6px; padding: 20px 32px; display: inline-block;">
                                                        <p style="color: #888888; margin: 0 0 8px 0; font-size: 11px; text-transform: uppercase; letter-spacing: 1px;">One-Time Password</p>
                                                        <p style="color: #1a1a1a; margin: 0; font-size: 28px; font-weight: 600; letter-spacing: 6px; font-family: 'SF Mono', 'Monaco', 'Inconsolata', 'Fira Mono', monospace;">%s</p>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- Warning -->
                                        <div style="background-color: #fffbe6; border: 1px solid #ffe58f; padding: 14px 16px; margin: 32px 0 0 0; border-radius: 4px;">
                                            <p style="color: #ad6800; margin: 0; font-size: 13px; line-height: 1.5;">
                                                <strong>Valid for 5 minutes.</strong> Don't share this OTP with anyone.
                                            </p>
                                        </div>
                                    </td>
                                </tr>
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #fafafa; padding: 24px 40px; border-top: 1px solid #e5e5e5;">
                                        <p style="color: #888888; margin: 0 0 8px 0; font-size: 13px;">
                                            If you didn't request this, you can safely ignore this email.
                                        </p>
                                        <p style="color: #bbbbbb; margin: 0; font-size: 12px;">
                                            © 2026 MessMate
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(userName, otp);
    }

    public void verifyOtp(String resetToken, String otp) {
        PasswordResetToken token = passwordResetTokenRepository.findByResetToken(resetToken)
                .orElseThrow(()->new PasswordResetException("Invalid session"));

        if(token.isOtpVerified()){
            throw new PasswordResetException("OTP already verified");
        }

        if(token.getTokenExpiry().isBefore(LocalDateTime.now())){
            throw new PasswordResetException("Reset session expired");
        }

        if(token.getOtpExpiry().isBefore(LocalDateTime.now())){
            throw new PasswordResetException("OTP expired");
        }

        if(token.getAttemptCount()>=MAX_ATTEMPTS){
            throw new PasswordResetException("Too many attempts");
        }

        if(!passwordEncoder.matches(otp,token.getHashedOtp())){
            token.setAttemptCount(token.getAttemptCount() + 1);
            passwordResetTokenRepository.save(token);
            throw new PasswordResetException("Invalid OTP");
        }

        token.setOtpVerified(true);
        passwordResetTokenRepository.save(token);
    }

    public void resetPassword(String resetToken, String newPassword) {
        PasswordResetToken token = passwordResetTokenRepository
                .findByResetToken(resetToken)
                .orElseThrow(()->new PasswordResetException("Invalid Session"));

        if(!token.isOtpVerified()){
            throw new PasswordResetException("OTP not verified");
        }

        if(token.getTokenExpiry().isBefore(LocalDateTime.now())){
            throw new PasswordResetException("Reset session expired");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);

        passwordResetTokenRepository.delete(token);
    }
}
