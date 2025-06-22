package com.springboot.MessApplication.MessMate.controllers;


import com.springboot.MessApplication.MessMate.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup() {
        return ResponseEntity.ok(new UserDto());
    }

}
