package com.springboot.MessApplication.MessMate.dto;

import lombok.Data;

@Data
public class VerifyOtpRequestDto {
    private String resetToken;
    private String otp;
}
