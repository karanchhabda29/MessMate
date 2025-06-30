package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.MealOffDto;
import com.springboot.MessApplication.MessMate.dto.MealOffResponseDto;
import com.springboot.MessApplication.MessMate.entities.MealOff;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.Meal;
import com.springboot.MessApplication.MessMate.exceptions.MealOffDeadlineException;
import com.springboot.MessApplication.MessMate.repositories.MealOffRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class MealOffService {

    private final LocalTime LUNCH_DEADLINE = LocalTime.of(8, 0,0);
    private final LocalTime DINNER_DEADLINE = LocalTime.of(16, 0,0);
    private final MealOffRepository mealOffRepository;
    private final ModelMapper modelMapper;

    public MealOffResponseDto setLunchOff() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(LocalTime.now().isBefore(LUNCH_DEADLINE)) {
            MealOff mealOff = user.getMealOff();
            mealOff.setLunch(true);
            mealOffRepository.save(mealOff);
            return new MealOffResponseDto("lunch set off successfully");
        }else {
            throw new MealOffDeadlineException("Cannot set lunch off after " + LUNCH_DEADLINE);
        }
    }

    public MealOffResponseDto setDinnerOff() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(LocalTime.now().isBefore(DINNER_DEADLINE)) {
            MealOff mealOff = user.getMealOff();
            mealOff.setDinner(true);
            mealOffRepository.save(mealOff);
            return new MealOffResponseDto("dinner set off successfully");
        }else {
            throw new MealOffDeadlineException("Cannot set dinner off after " + DINNER_DEADLINE);
        }
    }

    public MealOffResponseDto setMealOff(MealOffDto mealOffDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(mealOffDto.getStartDate().isAfter(LocalDate.now()) ||
                (mealOffDto.getStartMeal() == Meal.LUNCH && LocalTime.now().isBefore(LUNCH_DEADLINE)) ||
                (mealOffDto.getStartMeal() == Meal.DINNER && LocalTime.now().isBefore(DINNER_DEADLINE) )
        ){
            MealOff mealOff = user.getMealOff();
            modelMapper.map(mealOffDto, mealOff);
            mealOffRepository.save(mealOff);
            return new MealOffResponseDto("meal off set successfully");

        }else {
            throw new MealOffDeadlineException("Today's Deadline for "+ mealOffDto.getStartMeal() + " is missed");
        }
    }

    public MealOff saveMealOff(MealOff mealOff) {
        return mealOffRepository.save(mealOff);
    }
}
