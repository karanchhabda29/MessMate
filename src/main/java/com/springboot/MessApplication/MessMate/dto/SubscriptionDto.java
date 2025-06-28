package com.springboot.MessApplication.MessMate.dto;

import com.springboot.MessApplication.MessMate.entities.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDto {
    private Long id;
    private Status status;
    private Integer meals;
    private LocalDateTime date;
}
