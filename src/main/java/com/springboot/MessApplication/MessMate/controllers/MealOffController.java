package com.springboot.MessApplication.MessMate.controllers;

import com.springboot.MessApplication.MessMate.dto.MealOffDto;
import com.springboot.MessApplication.MessMate.dto.MealOffResponseDto;
import com.springboot.MessApplication.MessMate.services.MealOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/mealoff")
@RequiredArgsConstructor
public class MealOffController {

    private final MealOffService mealOffService;

    @PostMapping("/lunch")
    public ResponseEntity<MealOffResponseDto> setLunchOff() {
        return ResponseEntity.ok(mealOffService.setLunchOff());
    }

    @PostMapping("/dinner")
    public ResponseEntity<MealOffResponseDto> setDinnerOff() {
        return ResponseEntity.ok(mealOffService.setDinnerOff());
    }

    @PostMapping
    public ResponseEntity<MealOffDto> setMealOff(@RequestBody MealOffDto mealOffDto) {
        return ResponseEntity.ok(mealOffService.setMealOff(mealOffDto));
    }

}
