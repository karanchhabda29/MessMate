package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.LoginDto;
import com.springboot.MessApplication.MessMate.dto.LoginResponseDto;
import com.springboot.MessApplication.MessMate.entities.PasswordResetToken;
import com.springboot.MessApplication.MessMate.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;

    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        User user = (User)authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new  LoginResponseDto(user.getId(),accessToken, refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userId);

        String accessToken = jwtService.generateAccessToken(user);
        return new   LoginResponseDto(user.getId(),accessToken,refreshToken);
    }

    public String forgotPassword(String email) {
        User user = userService.findByEmail(email);

        //create reset token and send otp to user
        String resetToken = passwordResetTokenService.createResetTokenAndSendOtp(user);

        //return reset token
        return resetToken;
    }

}
