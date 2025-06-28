package com.springboot.MessApplication.MessMate.repositories;

import com.springboot.MessApplication.MessMate.entities.Subscription;
import com.springboot.MessApplication.MessMate.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    List<Subscription> findByStatus(Status status);

    Subscription findByUser_Id(Long userId);
}
