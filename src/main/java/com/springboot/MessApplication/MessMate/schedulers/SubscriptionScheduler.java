package com.springboot.MessApplication.MessMate.schedulers;

import com.springboot.MessApplication.MessMate.entities.MealOff;
import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.Meal;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import com.springboot.MessApplication.MessMate.services.MealOffService;
import com.springboot.MessApplication.MessMate.services.NotificationService;
import com.springboot.MessApplication.MessMate.services.SubscriptionService;
import com.springboot.MessApplication.MessMate.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionScheduler {

    private final UserService userService;
    private final MealOffService mealOffService;
    private final SubscriptionService subscriptionService;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 16 * * *",zone = "Asia/Kolkata")
    void countLunch() {
        userService.getSubscribedUsers().forEach(user -> {
            MealOff mealOff = mealOffService.getMealOff(user.getId());
            if(mealOff.getLunch()) {
                //creating notification
                notificationService.createNotification(
                        user.getId(), NotificationType.MEAL_UPDATE, "Lunch set off successfully for " + LocalDate.now()
                );
                mealOff.setLunch(false);
                mealOffService.saveMealOff(mealOff);
            }else{
                Subscription updatedSubscription = countMeal(user,Meal.LUNCH);
                log.info("lunch counted successfully for {}", user.getName());
                if(updatedSubscription.getStatus().equals(SubscriptionStatus.INACTIVE)) {
                    notificationService.createNotification(user.getId(), NotificationType.SUBSCRIPTION_EXPIRY, "Your subscription expired on " + LocalDate.now());
                    mealOffService.cancelCustomMealOffIfExists(mealOff);
                }
            }
        });
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Kolkata")
    void countDinner() {
        userService.getSubscribedUsers().forEach(user -> {
            MealOff mealOff = mealOffService.getMealOff(user.getId());
            if(mealOff.getDinner()) {
                //creating notification
                notificationService.createNotification(
                        user.getId(), NotificationType.MEAL_UPDATE, "Dinner set off successfully for " + LocalDate.now()
                );
                mealOff.setDinner(false);
                mealOffService.saveMealOff(mealOff);
            }else{
                Subscription updatedSubscription = countMeal(user,Meal.DINNER);
                log.info("dinner counted successfully for {}", user.getName());
                if(updatedSubscription.getStatus().equals(SubscriptionStatus.INACTIVE)) {
                    notificationService.createNotification(user.getId(), NotificationType.SUBSCRIPTION_EXPIRY, "Your subscription expired on " + LocalDate.now());
                    mealOffService.cancelCustomMealOffIfExists(mealOff);
                }
            }
        });
    }

    private Subscription countMeal(User user, Meal meal) {
        Subscription subscription = subscriptionService.getSubscriptionByUserId(user.getId());
        int updatedMeals = subscription.getMeals()-1;
        subscription.setMeals(updatedMeals);
        subscriptionService.updateStatusIfMealsExhausted(subscription);
        Subscription savedSubscription = subscriptionService.saveSubscription(subscription);
        notificationService.createNotification(
                user.getId(), NotificationType.MEAL_UPDATE, "Your " + meal + " counted successfully for " + LocalDate.now()
        );
        return savedSubscription;
    }

}
