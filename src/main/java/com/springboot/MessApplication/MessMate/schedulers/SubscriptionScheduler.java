package com.springboot.MessApplication.MessMate.schedulers;

import com.springboot.MessApplication.MessMate.entities.MealOff;
import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.services.MealOffService;
import com.springboot.MessApplication.MessMate.services.NotificationService;
import com.springboot.MessApplication.MessMate.services.SubscriptionService;
import com.springboot.MessApplication.MessMate.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionScheduler {

    private final UserService userService;
    private final MealOffService mealOffService;
    private final SubscriptionService subscriptionService;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 15 * * *")
    void countLunch() {
        userService.getSubscribedUsers().forEach(user -> {
            MealOff mealOff = mealOffService.getMealOff(user);
            if(mealOff.getLunch()) {
                mealOff.setLunch(false);
                mealOffService.saveMealOff(mealOff);
            }else{
                countMeal(user);
                log.info("lunch counted successfully for {}", user.getName());
            }
        });
    }

    @Scheduled(cron = "0 0 23 * * *")
    void countDinner() {
        userService.getSubscribedUsers().forEach(user -> {
            MealOff mealOff = mealOffService.getMealOff(user);
            if(mealOff.getDinner()) {
                mealOff.setDinner(false);
                mealOffService.saveMealOff(mealOff);
            }else{
                countMeal(user);
                log.info("dinner counted successfully for {}", user.getName());
            }
        });
    }

    private void countMeal(User user) {
        Subscription subscription = subscriptionService.getSubscriptionByUser(user);
        int updatedMeals = subscription.getMeals()-1;
        subscription.setMeals(updatedMeals);
        if(subscriptionService.updateStatusIfMealsExhausted(subscription)){
            notificationService.createNotification(user.getId(), NotificationType.SUBSCRIPTION_EXPIRY, "Your Subscription has expired");
        }
        subscriptionService.saveSubscription(subscription);
    }

}
