package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.SubscriptionDto;
import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionType;
import com.springboot.MessApplication.MessMate.exceptions.ResourceNotFoundException;
import com.springboot.MessApplication.MessMate.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final NotificationService notificationService;

    public SubscriptionDto getSubscriptionDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Subscription subscription = getSubscriptionByUserId(user.getId());
        return modelMapper.map(subscription, SubscriptionDto.class);
    }

    public SubscriptionDto requestNewSubscription(SubscriptionType type) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Subscription subscription = getSubscriptionByUserId(user.getId());
        if(subscription.getStatus()==SubscriptionStatus.ACTIVE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subscription is already active");
        }
        if(subscription.getStatus()==SubscriptionStatus.REQUESTED){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subscription already requested");
        }
        subscription.setStatus(SubscriptionStatus.REQUESTED);
        subscription.setType(type);
        subscription.setDate(LocalDateTime.now());
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return modelMapper.map(savedSubscription, SubscriptionDto.class);
    }


    public SubscriptionDto acceptSubscriptionRequestByUserId(Long id) {
        User user = userService.getUserById(id);
        Subscription subscription = getSubscriptionByUserId(user.getId());
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setDate(LocalDateTime.now());
        subscription.setMeals(56);
        return modelMapper.map(subscriptionRepository.save(subscription), SubscriptionDto.class);
    }

    public SubscriptionDto getSubscriptionDetailsByUserId(long userId) {
        Subscription subscription = subscriptionRepository.findByUser_Id(userId)
                .orElseThrow(() ->new ResourceNotFoundException("User with Id " + userId + " doesnot exist"));
        return modelMapper.map(subscription, SubscriptionDto.class);
    }

    public SubscriptionDto updateSubscriptionByUserId(long userId, SubscriptionDto subscriptionDto) {
        Subscription subscription = getSubscriptionByUserId(userId);
        subscription.setMeals(subscriptionDto.getMeals());
        notificationService.createNotification(userId,NotificationType.MEAL_UPDATE,"Your meal count has been updated to "+ subscriptionDto.getMeals()+" by admin");
        updateStatusIfMealsExhausted(userId,subscription);
        return modelMapper.map(subscriptionRepository.save(subscription),SubscriptionDto.class);
    }

    //non-controller methods

    public Subscription getSubscriptionByUserId(Long userId) {
        return subscriptionRepository.findByUser_Id(userId)
                .orElseThrow(() ->new ResourceNotFoundException("User with Id " + userId + " doesnot exist"));
    }

    public void saveSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }


    public void updateStatusIfMealsExhausted(long userId,Subscription subscription) {
        if(subscription.getMeals() == 0){
            subscription.setStatus(SubscriptionStatus.INACTIVE);
            subscription.setDate(LocalDateTime.now());
            notificationService.createNotification(userId, NotificationType.SUBSCRIPTION_EXPIRY, "Your Subscription has expired");
        }
    }


}
