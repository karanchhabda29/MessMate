package com.springboot.MessApplication.MessMate.repositories;


import com.springboot.MessApplication.MessMate.entities.MealOff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealOffRepository extends JpaRepository<MealOff,Long> {
}
