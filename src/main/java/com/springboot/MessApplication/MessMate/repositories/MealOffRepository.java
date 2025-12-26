package com.springboot.MessApplication.MessMate.repositories;


import com.springboot.MessApplication.MessMate.entities.MealOff;
import com.springboot.MessApplication.MessMate.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealOffRepository extends JpaRepository<MealOff,Long> {
    MealOff findByUser_Id(Long userId);

    List<MealOff> findAllByCustomOff(Boolean customOff);
}
