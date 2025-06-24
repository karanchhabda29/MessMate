package com.springboot.MessApplication.MessMate.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SignupDto {
    private String email;
    private String password;
    private String name;
}
