package com.springboot.MessApplication.MessMate.schedulers;

import com.springboot.MessApplication.MessMate.services.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SubscriptionScheduler {

    private final UserService userService;

    public SubscriptionScheduler(UserService userService) {
        this.userService = userService;
    }

//    @Scheduled(cron = "0 * * * * *")
//    public void countMeal() {
//        Arrays.stream(userService.getSubscribedUsers())
//                .forEach
//    }
}
