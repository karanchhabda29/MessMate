package com.springboot.MessApplication.MessMate.dto;

import com.springboot.MessApplication.MessMate.entities.enums.Meal;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealOffResponseDto {
    private Boolean Lunch;
    private Boolean Dinner;
}
