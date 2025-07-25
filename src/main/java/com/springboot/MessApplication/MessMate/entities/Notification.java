package com.springboot.MessApplication.MessMate.entities;


import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // e.g., SUBSCRIPTION_EXPIRY, MEAL_UPDATE

    private String message;

    private Boolean isRead = false;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
}
