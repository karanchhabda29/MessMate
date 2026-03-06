package com.springboot.MessApplication.MessMate.dto;

import lombok.Data;

@Data
public class UpdateMealCountRequestDto {
    private Integer updatedMeals;
    private String reason;
}
