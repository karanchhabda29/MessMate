package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.SubscriptionDto;
import com.springboot.MessApplication.MessMate.dto.UpdateMealCountRequestDto;
import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.entities.enums.Meal;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import com.springboot.MessApplication.MessMate.exceptions.BadRequestException;
import com.springboot.MessApplication.MessMate.exceptions.UserNotSubscribedException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SubscriptionMealOffCoordinatorService {

    private final SubscriptionService subscriptionService;
    private final MealOffService mealOffService;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    @Transactional
    public SubscriptionDto updateSubscriptionByUserId(long userId, UpdateMealCountRequestDto request) {
        if(request.getUpdatedMeals()<0){
            throw new BadRequestException("Meal count cannot be less than 0");
        }
        Subscription subscription = subscriptionService.getSubscriptionByUserId(userId);
        if(EnumSet.of(SubscriptionStatus.INACTIVE, SubscriptionStatus.REQUESTED)
                .contains(subscription.getStatus())){
            throw new UserNotSubscribedException("User not subscribed");
        }
        subscription.setMeals(request.getUpdatedMeals());
        subscriptionService.updateStatusIfMealsExhausted(subscription);

        Subscription savedSubscription = subscriptionService.saveSubscription(subscription);
        notificationService.createNotification(
                userId,
                NotificationType.MEAL_COUNT,
                "Your meal count has been updated to "+ savedSubscription.getMeals()+" by admin\n"+
                        "Reason: "+ request.getReason()
        );
        if(savedSubscription.getStatus().equals(SubscriptionStatus.INACTIVE)){
            notificationService.createNotification(
                    userId,
                    NotificationType.SUBSCRIPTION_EXPIRY,
                    "Your subscription has been expired (via admin) "
            );
            mealOffService.resetMealOff(userId);
        }
        return modelMapper.map(savedSubscription,SubscriptionDto.class);
    }

    @Transactional
    public  void countMeal(Meal meal){
        List<Subscription> subscriptions =
                meal == Meal.LUNCH
                        ? subscriptionService.getLunchCountableActiveSubscriptions()
                        : subscriptionService.getDinnerCountableActiveSubscriptions();
        for(Subscription subscription : subscriptions){

            subscription.setMeals(subscription.getMeals()-1);
            subscriptionService.updateStatusIfMealsExhausted(subscription);

            Subscription savedSubscription = subscriptionService.saveSubscription(subscription);

            //since fetch type is eager for User in subscription, we can directly get user from subscription object
            //we do not need to make the database call
            Long userId = subscription.getUser().getId();

            notificationService.createNotification(
                    userId,NotificationType.MEAL_COUNT,"Your " + meal + " counted successfully for " + LocalDate.now()
            );
            //subscription expiry check
            if(savedSubscription.getStatus().equals(SubscriptionStatus.INACTIVE)){
                notificationService.createNotification(
                        userId,
                        NotificationType.SUBSCRIPTION_EXPIRY,
                        "Your subscription has been expired "
                );
                mealOffService.resetMealOff(userId);
            }
        }
    }


}
