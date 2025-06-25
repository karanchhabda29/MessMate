package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.LoginDto;
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

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserDto signup(SignupDto signupDto) {
        Optional<User> user = userRepository.findByEmail(signupDto.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User with email " +  signupDto.getEmail() + " already exists");
        }

        User toBeCreatedUser = modelMapper.map(signupDto, User.class);
        toBeCreatedUser.setRole(Role.STUDENT);
        toBeCreatedUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        return modelMapper.map(userRepository.save(toBeCreatedUser), UserDto.class);
    }

    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        User user = (User)authentication.getPrincipal();
        return jwtService.generateToken(user);
    }
}
