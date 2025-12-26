package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.SubscriptionDto;
import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SubscriptionMealOffCoordinatorService {

    private final SubscriptionService subscriptionService;
    private final MealOffService mealOffService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    @Transactional
    public SubscriptionDto updateSubscriptionByUserId(long userId, SubscriptionDto subscriptionDto) {
        Subscription subscription = subscriptionService.getSubscriptionByUserId(userId);
        subscription.setMeals(subscriptionDto.getMeals());
        subscriptionService.updateStatusIfMealsExhausted(subscription);
        Subscription savedSubscription = subscriptionService.saveSubscription(subscription);
        notificationService.createNotification(userId, NotificationType.MEAL_UPDATE,"Your meal count has been updated to "+ savedSubscription.getMeals()+" by admin");
        if(savedSubscription.getStatus().equals(SubscriptionStatus.INACTIVE)){
            notificationService.createNotification(userId,NotificationType.SUBSCRIPTION_EXPIRY,"Your subscription has been expired (via admin) ");
            mealOffService.cancelCustomMealOffIfExists(mealOffService.getMealOff(userId));
        }
        return modelMapper.map(savedSubscription,SubscriptionDto.class);
    }
}
