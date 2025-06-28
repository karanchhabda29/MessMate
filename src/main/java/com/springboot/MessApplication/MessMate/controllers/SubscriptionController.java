package com.springboot.MessApplication.MessMate.controllers;


import com.springboot.MessApplication.MessMate.dto.SubscriptionDto;
import com.springboot.MessApplication.MessMate.dto.UserDto;
import com.springboot.MessApplication.MessMate.services.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/request-new-subscription")
    public ResponseEntity<SubscriptionDto> requestNewSubscription(){
        return ResponseEntity.ok(subscriptionService.requestNewSubscription());
    }

    @GetMapping("/requests")
    public ResponseEntity<List<UserDto>> requests(){
        return ResponseEntity.ok(subscriptionService.getAllRequests());
    }

    @PostMapping("/requests/{id}")
    public ResponseEntity<SubscriptionDto> acceptSubscriptionRequestById(@PathVariable Long id){
        return ResponseEntity.ok(subscriptionService.acceptSubscriptionRequestById(id));
    }
}
