package com.springboot.MessApplication.MessMate.repositories;


import com.springboot.MessApplication.MessMate.entities.MealOff;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MealOffRepository extends JpaRepository<MealOff,Long> {
    Optional<MealOff> findByUser_Id(Long userId);

    List<MealOff> findAllByCustomOffAndUser_Subscription_Status(Boolean customOff, SubscriptionStatus userSubscriptionStatus);

    List<MealOff> findAllByLunchAndUser_Subscription_Status(Boolean lunch, SubscriptionStatus subscriptionStatus);

    List<MealOff> findAllByDinnerAndUser_Subscription_Status(Boolean dinner, SubscriptionStatus subscriptionStatus);
}
