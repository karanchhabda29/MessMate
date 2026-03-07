package com.springboot.MessApplication.MessMate.controllers;


import com.springboot.MessApplication.MessMate.dto.SubscriptionDto;
import com.springboot.MessApplication.MessMate.dto.UpdateMealCountRequestDto;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionType;
import com.springboot.MessApplication.MessMate.services.SubscriptionMealOffCoordinatorService;
import com.springboot.MessApplication.MessMate.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMealOffCoordinatorService subscriptionMealOffCoordinatorService;

    @Secured("ROLE_STUDENT")
    @GetMapping
    public ResponseEntity<SubscriptionDto> getSubscriptionDetails() {
        return ResponseEntity.ok(subscriptionService.getSubscriptionDetails());
    }

    @Secured("ROLE_STUDENT")
    @PostMapping("/request-new-subscription/{type}")
    public ResponseEntity<SubscriptionDto> requestNewSubscription(@PathVariable SubscriptionType type){
        return ResponseEntity.ok(subscriptionService.requestNewSubscription(type));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/requests/{userId}")
    public ResponseEntity<SubscriptionDto> acceptSubscriptionRequestByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(subscriptionService.acceptSubscriptionRequestByUserId(userId));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{userId}")
    public ResponseEntity<SubscriptionDto> getSubscriptionDetailsByUserId(@PathVariable long userId){
        return ResponseEntity.ok(subscriptionService.getSubscriptionDetailsByUserId(userId));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{userId}")
    public ResponseEntity<SubscriptionDto> updateSubscriptionByUserId(@PathVariable long userId,@RequestBody UpdateMealCountRequestDto request){
        return ResponseEntity.ok(subscriptionMealOffCoordinatorService.updateSubscriptionByUserId(userId,request));
    }

}
