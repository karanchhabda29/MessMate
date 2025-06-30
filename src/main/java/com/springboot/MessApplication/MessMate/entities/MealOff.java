package com.springboot.MessApplication.MessMate.entities;


import com.springboot.MessApplication.MessMate.entities.enums.Meal;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class MealOff {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean lunch = false;
    private Boolean dinner = false;

    private Meal startMeal;
    private Meal endMeal;

    private LocalDate startDate;
    private LocalDate endDate;

    @OneToOne(mappedBy = "mealOff", cascade = CascadeType.ALL)
    private User user;

}
