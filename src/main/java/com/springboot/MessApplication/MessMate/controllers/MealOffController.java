package com.springboot.MessApplication.MessMate.controllers;

import com.springboot.MessApplication.MessMate.dto.CustomMealOffDto;
import com.springboot.MessApplication.MessMate.dto.TodayMealOffDto;
import com.springboot.MessApplication.MessMate.services.MealOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/mealoff")
@RequiredArgsConstructor
public class MealOffController {

    private final MealOffService mealOffService;

    @PostMapping("/lunch")
    public ResponseEntity<TodayMealOffDto> setLunchOff() {
        return ResponseEntity.ok(mealOffService.setLunchOff());
    }

    @PostMapping("/dinner")
    public ResponseEntity<TodayMealOffDto> setDinnerOff() {
        return ResponseEntity.ok(mealOffService.setDinnerOff());
    }

    @PostMapping
    public ResponseEntity<CustomMealOffDto> setMealOff(@RequestBody CustomMealOffDto mealOffDto) {
        return ResponseEntity.ok(mealOffService.setCustomMealOff(mealOffDto));
    }

    //getMealOffDetails
    @GetMapping("/today")
    public ResponseEntity<TodayMealOffDto> getTodayMealOffDetails(){
        return ResponseEntity.ok(mealOffService.getTodayMealOffDetails());
    }

    //getCustomMealOffDetails
    @GetMapping("/custom")
    public ResponseEntity<CustomMealOffDto> getCustomMealOffDetails(){
        return ResponseEntity.ok(mealOffService.getCustomMealOffDetails());
    }

    //cancel today lunch off
    //cancel today dinner off

    //cancel custom off
}
