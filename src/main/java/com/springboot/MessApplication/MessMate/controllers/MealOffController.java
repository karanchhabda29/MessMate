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

    @DeleteMapping("/lunch")
    public  ResponseEntity<TodayMealOffDto> cancelLunchOff() {
        return ResponseEntity.ok(mealOffService.cancelLunchOff());
    }

    @PostMapping("/dinner")
    public ResponseEntity<TodayMealOffDto> setDinnerOff() {
        return ResponseEntity.ok(mealOffService.setDinnerOff());
    }

    @DeleteMapping("/dinner")
    public  ResponseEntity<TodayMealOffDto> cancelDinnerOff() {
        return ResponseEntity.ok(mealOffService.cancelDinnerOff());
    }

    @PostMapping
    public ResponseEntity<CustomMealOffDto> setMealOff(@RequestBody CustomMealOffDto mealOffDto) {
        return ResponseEntity.ok(mealOffService.setCustomMealOff(mealOffDto));
    }

    @DeleteMapping
    public ResponseEntity<CustomMealOffDto> cancelMealOff(){
        return ResponseEntity.ok(mealOffService.cancelCustomMealOff());
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


}
