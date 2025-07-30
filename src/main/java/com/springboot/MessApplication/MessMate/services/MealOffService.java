package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.CustomMealOffDto;
import com.springboot.MessApplication.MessMate.dto.TodayMealOffDto;
import com.springboot.MessApplication.MessMate.entities.MealOff;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.Meal;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.exceptions.MealOffDeadlineException;
import com.springboot.MessApplication.MessMate.repositories.MealOffRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class MealOffService {

    private final LocalTime LUNCH_DEADLINE = LocalTime.of(8, 0,0);
    private final LocalTime DINNER_DEADLINE = LocalTime.of(16, 0,0);
    private final MealOffRepository mealOffRepository;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    public TodayMealOffDto setLunchOff() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MealOff mealOff = getMealOff(user);
        if(mealOff.getLunch()){
            TodayMealOffDto todayMealOffDto = modelMapper.map(mealOff, TodayMealOffDto.class);
            todayMealOffDto.setMessage("Lunch Already set off for today");
            return todayMealOffDto;
        }else{
            if(LocalTime.now().isBefore(LUNCH_DEADLINE)) {
                mealOff.setLunch(true);
                MealOff savedMealOff = mealOffRepository.save(mealOff);
                TodayMealOffDto todayMealOffDto = modelMapper.map(savedMealOff, TodayMealOffDto.class);
                todayMealOffDto.setMessage("Lunch set off for today successfully");
                //TODO createNewNotification
                notificationService.createNotification(user.getId(), NotificationType.MEAL_UPDATE,"Lunch set off for today successfully");
                return todayMealOffDto;
            }else {
                throw new MealOffDeadlineException("Cannot set lunch off after " + LUNCH_DEADLINE);
            }
        }
    }

    public TodayMealOffDto setDinnerOff() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MealOff mealOff = getMealOff(user);
        if(mealOff.getDinner()) {
            TodayMealOffDto todayMealOffDto = modelMapper.map(mealOff, TodayMealOffDto.class);
            todayMealOffDto.setMessage("Dinner Already set off for today");
            return todayMealOffDto;
        }else{
            if(LocalTime.now().isBefore(DINNER_DEADLINE)) {
                mealOff.setDinner(true);
                MealOff savedMealoff = mealOffRepository.save(mealOff);
                TodayMealOffDto todayMealOffDto = modelMapper.map(savedMealoff, TodayMealOffDto.class);
                todayMealOffDto.setMessage("Dinner set off for today successfully");
                //TODO createNewNotification
                notificationService.createNotification(user.getId(), NotificationType.MEAL_UPDATE,"Dinner set off for today successfully");
                return todayMealOffDto;
            }else {
                throw new MealOffDeadlineException("Cannot set dinner off after " + DINNER_DEADLINE);
            }
        }
    }

    public CustomMealOffDto setCustomMealOff(CustomMealOffDto mealOffDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(mealOffDto.getStartDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Start date cannot be before current date");
        }

        if(mealOffDto.getEndDate().isBefore(mealOffDto.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"End date cannot be before Start date");
        }

        if(mealOffDto.getStartDate().isEqual(mealOffDto.getEndDate()) && mealOffDto.getStartMeal()==Meal.DINNER && mealOffDto.getEndMeal()==Meal.LUNCH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Start meal cannot be after end meal for one day off");
        }

        boolean isFutureDate = mealOffDto.getStartDate().isAfter(LocalDate.now());
        boolean lunchAllowed = mealOffDto.getStartMeal() == Meal.LUNCH && LocalTime.now().isBefore(LUNCH_DEADLINE);
        boolean dinnerAllowed = mealOffDto.getStartMeal() == Meal.DINNER && LocalTime.now().isBefore(DINNER_DEADLINE);

        if( isFutureDate || lunchAllowed || dinnerAllowed){
            MealOff mealOff = getMealOff(user);
            modelMapper.map(mealOffDto, mealOff);
            MealOff savedMealOff = mealOffRepository.save(mealOff);
            //TODO createNewNotification
             notificationService.createNotification(user.getId(), NotificationType.MEAL_UPDATE,"Meals set off successfully");
            return modelMapper.map(savedMealOff, CustomMealOffDto.class);
        }else {
            throw new MealOffDeadlineException("Today's Deadline for "+ mealOffDto.getStartMeal() + " is missed. Please try again");
        }
    }

    public TodayMealOffDto getTodayMealOffDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MealOff mealOff = getMealOff(user);
        return modelMapper.map(mealOff, TodayMealOffDto.class);
    }

    public CustomMealOffDto getCustomMealOffDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MealOff mealOff = getMealOff(user);
        return modelMapper.map(mealOff, CustomMealOffDto.class);
    }

    // non-controller methods
    public MealOff getMealOff(User user) {
        return mealOffRepository.findByUser(user);
    }

    public void saveMealOff(MealOff mealOff) {
        mealOffRepository.save(mealOff);
    }
}
