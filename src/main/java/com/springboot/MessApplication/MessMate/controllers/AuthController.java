package com.springboot.MessApplication.MessMate.controllers;


import com.springboot.MessApplication.MessMate.dto.LoginDto;
import com.springboot.MessApplication.MessMate.dto.SignupDto;
import com.springboot.MessApplication.MessMate.dto.UserDto;
import com.springboot.MessApplication.MessMate.services.AuthService;
import com.springboot.MessApplication.MessMate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto) {
        UserDto userDto = authService.signup(signupDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String accessToken = authService.login(loginDto);

        return ResponseEntity.ok(accessToken);
    }


}
