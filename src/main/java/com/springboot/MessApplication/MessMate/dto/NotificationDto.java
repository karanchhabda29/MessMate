package com.springboot.MessApplication.MessMate.dto;

import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {

    private NotificationType type;
    private String message;
    private Boolean isRead;
    private LocalDateTime timestamp;

}
