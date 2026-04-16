package com.springboot.MessApplication.MessMate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementDto {

    @NotBlank(message = "Message is required")
    private String message;

    private boolean notifyAllUsers = false;
}
