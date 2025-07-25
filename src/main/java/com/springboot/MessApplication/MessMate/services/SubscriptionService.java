package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.SubscriptionDto;
import com.springboot.MessApplication.MessMate.dto.UserDto;
import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import com.springboot.MessApplication.MessMate.exceptions.ResourceNotFoundException;
import com.springboot.MessApplication.MessMate.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public SubscriptionDto requestNewSubscription() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Subscription subscription = getSubscriptionByUser(user);
        subscription.setStatus(SubscriptionStatus.REQUESTED);
        subscription.setDate(LocalDateTime.now());
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return modelMapper.map(savedSubscription, SubscriptionDto.class);
    }

    public List<UserDto> getAllRequests() {
        List<Subscription> subscriptions = subscriptionRepository.findByStatus(SubscriptionStatus.REQUESTED);
        return subscriptions.stream()
                .map(subscription -> modelMapper.map(subscription.getUser(), UserDto.class)).
                collect(Collectors.toList());
    }

    public SubscriptionDto acceptSubscriptionRequestByUserId(Long id) {
        User user = userService.getUserById(id);
        Subscription subscription = getSubscriptionByUser(user);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setDate(LocalDateTime.now());
        subscription.setMeals(56);
        return modelMapper.map(subscriptionRepository.save(subscription), SubscriptionDto.class);
    }

    public SubscriptionDto getSubscriptionDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Subscription subscription = getSubscriptionByUser(user);
        return modelMapper.map(subscription, SubscriptionDto.class);
    }

    public Subscription getSubscriptionByUser(User user) {
        return subscriptionRepository.findByUser(user);
    }

    public void saveSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    public SubscriptionDto getSubscriptionDetailsByUserId(long userId) {
        Subscription subscription = subscriptionRepository.findByUser_Id(userId)
                .orElseThrow(() ->new ResourceNotFoundException("User with Id " + userId + " doesnot exist"));
        return modelMapper.map(subscription, SubscriptionDto.class);
    }
}
