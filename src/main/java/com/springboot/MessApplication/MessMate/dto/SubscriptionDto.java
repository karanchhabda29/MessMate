package com.springboot.MessApplication.MessMate.dto;

import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDto {
    private Long id;
    private SubscriptionStatus status;
    private SubscriptionType type;
    private Integer meals;
    private LocalDateTime date;
}
