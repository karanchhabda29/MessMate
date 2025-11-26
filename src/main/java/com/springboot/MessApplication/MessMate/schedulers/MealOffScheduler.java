package com.springboot.MessApplication.MessMate.schedulers;

import com.springboot.MessApplication.MessMate.entities.MealOff;
import com.springboot.MessApplication.MessMate.entities.enums.Meal;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.services.MealOffService;
import com.springboot.MessApplication.MessMate.services.NotificationService;
import com.springboot.MessApplication.MessMate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MealOffScheduler {

    private final UserService userService;
    private final MealOffService mealOffService;

    @Scheduled(cron = "0 1 8 * * *",zone = "Asia/Kolkata")
    void setLunchOff(){
        userService.getSubscribedUsers().forEach(user -> {
            LocalDate today = LocalDate.now();
            MealOff mealOff = mealOffService.getMealOff(user);

            // Skip if any field is null
            if (mealOff.getStartDate() == null || mealOff.getEndDate() == null ||
                    mealOff.getStartMeal() == null || mealOff.getEndMeal() == null) {
                return; // skip this user, continue with next
            }

            if((today.equals(mealOff.getStartDate()) && mealOff.getStartMeal().equals(Meal.LUNCH)) ||
                    (today.isAfter(mealOff.getStartDate()) && !today.isAfter(mealOff.getEndDate()))){
                mealOff.setLunch(true);
                mealOffService.saveMealOff(mealOff);
            }
        });
    }

    @Scheduled(cron = "0 1 16 * * *",zone = "Asia/Kolkata")
    void setDinnerOff(){
        userService.getSubscribedUsers().forEach(user -> {
            LocalDate today = LocalDate.now();
            MealOff mealOff = mealOffService.getMealOff(user);

            // Skip if any field is null
            if (mealOff.getStartDate() == null || mealOff.getEndDate() == null ||
                    mealOff.getStartMeal() == null || mealOff.getEndMeal() == null) {
                return; // skip this user, continue with next
            }

            if((today.equals(mealOff.getEndDate()) && mealOff.getEndMeal().equals(Meal.DINNER)) ||
                    (!today.isBefore(mealOff.getStartDate()) && today.isBefore(mealOff.getEndDate()))){
                mealOff.setDinner(true);
                mealOffService.saveMealOff(mealOff);
            }
        });
    }
}
