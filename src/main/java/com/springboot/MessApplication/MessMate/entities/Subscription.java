package com.springboot.MessApplication.MessMate.entities;

import com.springboot.MessApplication.MessMate.entities.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy ="subscription", cascade = CascadeType.ALL)
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime date;

    private Integer meals;

}
