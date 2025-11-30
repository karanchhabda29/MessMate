package com.springboot.MessApplication.MessMate.repositories;

import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(java.lang.String email);

    List<User> findBySubscription_Status(SubscriptionStatus status);

    List<User> findBySubscription_StatusAndMealOff_Lunch(SubscriptionStatus subscriptionStatus, Boolean mealOffLunch);

    List<User> findBySubscription_StatusAndMealOff_Dinner(SubscriptionStatus subscriptionStatus, Boolean mealOffDinner);
}
