package com.springboot.MessApplication.MessMate.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayMealOffDto {
    private Boolean Lunch;
    private Boolean Dinner;
    private String message;
}
