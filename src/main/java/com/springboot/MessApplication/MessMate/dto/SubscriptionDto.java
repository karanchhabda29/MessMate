package com.springboot.MessApplication.MessMate.dto;

import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDto {
    private Long id;
    private SubscriptionStatus status;
    private Integer meals;
    private LocalDateTime date;
}
