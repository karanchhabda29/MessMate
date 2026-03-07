package com.springboot.MessApplication.MessMate.controllers;

import com.springboot.MessApplication.MessMate.dto.*;
import com.springboot.MessApplication.MessMate.services.MealOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/mealoff")
@RequiredArgsConstructor
public class MealOffController {

    private final MealOffService mealOffService;

    @Secured("ROLE_STUDENT")
    @PostMapping("/lunch")
    public ResponseEntity<TodayMealOffDto> setLunchOff() {
        return ResponseEntity.ok(mealOffService.setLunchOff());
    }

    @Secured("ROLE_STUDENT")
    @DeleteMapping("/lunch")
    public  ResponseEntity<TodayMealOffDto> cancelLunchOff() {
        return ResponseEntity.ok(mealOffService.cancelLunchOff());
    }

    @Secured("ROLE_STUDENT")
    @PostMapping("/dinner")
    public ResponseEntity<TodayMealOffDto> setDinnerOff() {
        return ResponseEntity.ok(mealOffService.setDinnerOff());
    }

    @Secured("ROLE_STUDENT")
    @DeleteMapping("/dinner")
    public  ResponseEntity<TodayMealOffDto> cancelDinnerOff() {
        return ResponseEntity.ok(mealOffService.cancelDinnerOff());
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/today")
    public ResponseEntity<TodayMealOffDto> getTodayMealOffDetails(){
        return ResponseEntity.ok(mealOffService.getTodayMealOffDetails());
    }

    @Secured("ROLE_STUDENT")
    @PostMapping
    public ResponseEntity<CustomMealOffDto> setCustomMealOff(@RequestBody CustomMealOffDto mealOffDto) {
        return ResponseEntity.ok(mealOffService.setCustomMealOff(mealOffDto));
    }

    //getCustomMealOffDetails
    @Secured("ROLE_STUDENT")
    @GetMapping("/custom")
    public ResponseEntity<CustomMealOffDto> getCustomMealOffDetails(){
        return ResponseEntity.ok(mealOffService.getCustomMealOffDetails());
    }

    @Secured("ROLE_STUDENT")
    @DeleteMapping
    public ResponseEntity<CustomMealOffDto> cancelCustomMealOff(){
        return ResponseEntity.ok(mealOffService.cancelCustomMealOff());
    }

    //get all lunch off (for admin)
    @Secured("ROLE_ADMIN")
    @GetMapping("/lunch_offs")
    public  ResponseEntity<UserListDto> getAllLunchOffs(){
        return ResponseEntity.ok(mealOffService.getAllLunchOffs());
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("lunch/{userId}")
    public ResponseEntity<TodayMealOffDto> cancelLunchOffByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(mealOffService.cancelLunchOffByUserId(userId));
    }

    //get all dinner off (for admin)
    @Secured("ROLE_ADMIN")
    @GetMapping("/dinner_offs")
    public ResponseEntity<UserListDto> getAllDinnerOffs(){
        return ResponseEntity.ok(mealOffService.getAllDinnerOffs());
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("dinner/{userId}")
    public ResponseEntity<TodayMealOffDto> cancelDinnerOffByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(mealOffService.cancelDinnerOffByUserId(userId));
    }

    //get all custom meal off (for admin)
    @Secured("ROLE_ADMIN")
    @GetMapping("/custom_offs")
    public ResponseEntity<List<CustomOffDetailDto>> getAllCustomOffDetails(){
        return ResponseEntity.ok(mealOffService.getAllCustomOffs());
    }

    //get custom meal off details by userId (for admin)
    @Secured("ROLE_ADMIN")
    @GetMapping("/custom/{userId}")
    public ResponseEntity<CustomMealOffDto> getCustomOffDetailsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(mealOffService.getCustomOffDetailsByUserId(userId));
    }

    //cancel custom off by userId (for admin)
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/custom/{userId}")
    public ResponseEntity<CustomMealOffDto> cancelCustomOffByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(mealOffService.cancelCustomOffByUserId(userId));
    }

}
