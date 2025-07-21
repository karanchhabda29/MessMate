package com.springboot.MessApplication.MessMate.schedulers;

import com.springboot.MessApplication.MessMate.entities.MealOff;
import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.services.MealOffService;
import com.springboot.MessApplication.MessMate.services.SubscriptionService;
import com.springboot.MessApplication.MessMate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final UserService userService;
    private final MealOffService mealOffService;
    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 15 * * *")
    void countLunch() {
        userService.getSubscribedUsers().forEach(user -> {
            MealOff mealOff = mealOffService.getMealOff(user);
            if(mealOff.getLunch()) {
                mealOff.setLunch(false);
                mealOffService.saveMealOff(mealOff);
            }else{
                Subscription subscription = subscriptionService.getSubscriptionByUser(user);
                int updatedMeals = subscription.getMeals()-1;
                subscription.setMeals(updatedMeals);
                //TODO checkSubscriptionStatus
                subscriptionService.saveSubscription(subscription);
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
                Subscription subscription = subscriptionService.getSubscriptionByUser(user);
                int updatedMeals = subscription.getMeals()-1;
                subscription.setMeals(updatedMeals);
                //TODO checkSubscriptionStatus
                subscriptionService.saveSubscription(subscription);
            }
        });
    }

}
