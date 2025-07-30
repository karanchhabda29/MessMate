package com.springboot.MessApplication.MessMate.controllers;


import com.springboot.MessApplication.MessMate.dto.SubscriptionDto;
import com.springboot.MessApplication.MessMate.dto.UserDto;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionType;
import com.springboot.MessApplication.MessMate.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<SubscriptionDto> getSubscriptionDetails() {
        return ResponseEntity.ok(subscriptionService.getSubscriptionDetails());
    }

    @PostMapping("/request-new-subscription/{type}")
    public ResponseEntity<SubscriptionDto> requestNewSubscription(@PathVariable SubscriptionType type){
        return ResponseEntity.ok(subscriptionService.requestNewSubscription(type));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<UserDto>> requests(){
        return ResponseEntity.ok(subscriptionService.getAllRequests());
    }

    @PostMapping("/requests/{userId}")
    public ResponseEntity<SubscriptionDto> acceptSubscriptionRequestByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(subscriptionService.acceptSubscriptionRequestByUserId(userId));
    }

    //TODO getSubscriptionDetailsByUserId
    @GetMapping("/{userId}")
    public ResponseEntity<SubscriptionDto> getSubscriptionDetailsByUserId(@PathVariable long userId){
        return ResponseEntity.ok(subscriptionService.getSubscriptionDetailsByUserId(userId));
    }

}
