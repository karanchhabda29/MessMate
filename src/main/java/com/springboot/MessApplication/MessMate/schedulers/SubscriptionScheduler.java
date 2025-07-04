package com.springboot.MessApplication.MessMate.schedulers;

import com.springboot.MessApplication.MessMate.services.SubscriptionService;
import com.springboot.MessApplication.MessMate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final UserService userService;

    @Scheduled(cron = "0 0 15 * * *")
    void countLunch() {
        userService.getSubscribedUsers().forEach(user -> {
            if(user.getMealOff().getLunch()) {
                user.getMealOff().setLunch(false);
            }else{
                Integer updatedMeals = user.getSubscription().getMeals()-1;
                user.getSubscription().setMeals(updatedMeals);
            }
            userService.saveUser(user);
        });
    }
    @Scheduled(cron = "0 0 23 * * *")
    void countDinner() {
        userService.getSubscribedUsers().forEach(user -> {
            if(user.getMealOff().getDinner()) {
                user.getMealOff().setDinner(false);
            }else{
                Integer updatedMeals = user.getSubscription().getMeals()-1;
                user.getSubscription().setMeals(updatedMeals);
            }
            userService.saveUser(user);
        });
    }

}
