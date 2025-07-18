package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.SubscriptionDto;
import com.springboot.MessApplication.MessMate.dto.UserDto;
import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.Status;
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
        Subscription subscription = user.getSubscription();
        subscription.setStatus(Status.REQUESTED);
        subscription.setDate(LocalDateTime.now());
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return modelMapper.map(savedSubscription, SubscriptionDto.class);
    }

    public List<UserDto> getAllRequests() {
        List<Subscription> subscriptions = subscriptionRepository.findByStatus(Status.REQUESTED);
        return subscriptions.stream()
                .map(subscription -> modelMapper.map(subscription.getUser(), UserDto.class)).
                collect(Collectors.toList());
    }

    public SubscriptionDto acceptSubscriptionRequestByUserId(Long id) {
        User user = userService.getUserById(id);
        Subscription subscription = user.getSubscription();
        subscription.setStatus(Status.ACTIVE);
        subscription.setDate(LocalDateTime.now());
        subscription.setMeals(56);
        return modelMapper.map(subscriptionRepository.save(subscription), SubscriptionDto.class);
    }

    public SubscriptionDto getSubscriptionDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Subscription subscription = subscriptionRepository.findByUser_Id(user.getId());
        return modelMapper.map(subscription, SubscriptionDto.class);
    }

}
