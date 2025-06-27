package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.LoginDto;
import com.springboot.MessApplication.MessMate.dto.LoginResponseDto;
import com.springboot.MessApplication.MessMate.dto.SignupDto;
import com.springboot.MessApplication.MessMate.dto.UserDto;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.Role;
import com.springboot.MessApplication.MessMate.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;


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
}
