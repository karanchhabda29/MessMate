package com.springboot.MessApplication.MessMate.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    private String resetToken;
    private String newPassword;
}
