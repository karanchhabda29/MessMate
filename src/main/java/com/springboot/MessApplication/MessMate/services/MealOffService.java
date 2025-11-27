package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.CustomMealOffDto;
import com.springboot.MessApplication.MessMate.dto.TodayMealOffDto;
import com.springboot.MessApplication.MessMate.entities.MealOff;
import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.Meal;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import com.springboot.MessApplication.MessMate.exceptions.MealOffDeadlineException;
import com.springboot.MessApplication.MessMate.exceptions.ResourceNotFoundException;
import com.springboot.MessApplication.MessMate.exceptions.UserNotSubscribedException;
import com.springboot.MessApplication.MessMate.repositories.MealOffRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MealOffService {

    private final LocalTime LUNCH_DEADLINE = LocalTime.of(8, 0,0);
    private final LocalTime DINNER_DEADLINE = LocalTime.of(16, 0,0);
    private final MealOffRepository mealOffRepository;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;

    public TodayMealOffDto setLunchOff() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkSubscriptionStatus(user);
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
                return todayMealOffDto;
            }else {
                throw new MealOffDeadlineException("Cannot set lunch off after " + LUNCH_DEADLINE);
            }
        }
    }
    public TodayMealOffDto cancelLunchOff() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkSubscriptionStatus(user);
        MealOff mealOff = getMealOff(user);
        if(!mealOff.getLunch()){
            TodayMealOffDto todayMealOffDto = modelMapper.map(mealOff, TodayMealOffDto.class);
            todayMealOffDto.setMessage("Lunch not set off for today");
            return todayMealOffDto;
        }else if(LocalTime.now().isBefore(LUNCH_DEADLINE)) {
            mealOff.setLunch(false);
            MealOff savedMealOff = mealOffRepository.save(mealOff);
            TodayMealOffDto todayMealOffDto = modelMapper.map(savedMealOff, TodayMealOffDto.class);
            todayMealOffDto.setMessage("Lunch off cancelled successfully");
            return todayMealOffDto;
        }else{
            throw new MealOffDeadlineException("Cannot cancel Lunch off after " + LUNCH_DEADLINE);
        }
    }

    public TodayMealOffDto setDinnerOff() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkSubscriptionStatus(user);
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
                return todayMealOffDto;
            }else {
                throw new MealOffDeadlineException("Cannot set dinner off after " + DINNER_DEADLINE);
            }
        }
    }

    public TodayMealOffDto cancelDinnerOff() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkSubscriptionStatus(user);
        MealOff mealOff = getMealOff(user);
        if(!mealOff.getDinner()){
            TodayMealOffDto todayMealOffDto = modelMapper.map(mealOff, TodayMealOffDto.class);
            todayMealOffDto.setMessage("Dinner not set off for today");
            return todayMealOffDto;
        }else if(LocalTime.now().isBefore(DINNER_DEADLINE)) {
            mealOff.setDinner(false);
            MealOff savedMealOff = mealOffRepository.save(mealOff);
            TodayMealOffDto todayMealOffDto = modelMapper.map(savedMealOff, TodayMealOffDto.class);
            todayMealOffDto.setMessage("Dinner off cancelled successfully");
            return todayMealOffDto;
        }else{
            throw new MealOffDeadlineException("Cannot cancel Dinner off after " + DINNER_DEADLINE);
        }
    }

    public CustomMealOffDto setCustomMealOff(CustomMealOffDto mealOffDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkSubscriptionStatus(user);
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
            //creating notification
            String message = "Meals set off successfully from " +savedMealOff.getStartDate() + " " + savedMealOff.getStartMeal() + " to " +savedMealOff.getEndDate() + " " +  savedMealOff.getEndMeal();
            notificationService.createNotification(user.getId(), NotificationType.MEAL_UPDATE,message);
            return modelMapper.map(savedMealOff, CustomMealOffDto.class);
        }else {
            throw new MealOffDeadlineException("Today's Deadline for "+ mealOffDto.getStartMeal() + " is missed. Please try again");
        }
    }

    public CustomMealOffDto cancelCustomMealOff() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkSubscriptionStatus(user);
        MealOff mealOff = getMealOff(user);
        //check if meal off exists or not
        if (mealOff.getStartDate() == null || mealOff.getEndDate() == null ||
                mealOff.getStartMeal() == null || mealOff.getEndMeal() == null){
            throw new ResourceNotFoundException("No existing meal off found");
        }else{
            mealOff.setStartMeal(null);
            mealOff.setEndMeal(null);
            mealOff.setStartDate(null);
            mealOff.setEndDate(null);
            //creating notification
            notificationService.createNotification(user.getId(), NotificationType.MEAL_UPDATE, "your custom meal off has been cancelled");
            return modelMapper.map(mealOffRepository.save(mealOff), CustomMealOffDto.class);
        }
    }


    public TodayMealOffDto getTodayMealOffDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkSubscriptionStatus(user);
        MealOff mealOff = getMealOff(user);
        return modelMapper.map(mealOff, TodayMealOffDto.class);
    }

    public CustomMealOffDto getCustomMealOffDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkSubscriptionStatus(user);
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

    public void checkSubscriptionStatus(User user) {
        Subscription subscription = subscriptionService.getSubscriptionByUser(user);
        if(Set.of(SubscriptionStatus.INACTIVE,SubscriptionStatus.REQUESTED).contains(subscription.getStatus()) ){
            throw new UserNotSubscribedException("User not subscribed");
        }
    }

}
