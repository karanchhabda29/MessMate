package com.springboot.MessApplication.MessMate.controllers;

import com.springboot.MessApplication.MessMate.advice.ApiResponse;
import com.springboot.MessApplication.MessMate.dto.AnnouncementDto;
import com.springboot.MessApplication.MessMate.dto.NotificationDto;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.services.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllNotifications(@RequestParam(value="type", required = false) NotificationType type){
        return ResponseEntity.ok(notificationService.getAllNotifications(type));
    }

    @PostMapping("/announcement")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> createAnnouncement(@Valid @RequestBody AnnouncementDto dto) {
        notificationService.createAnnouncement(dto.getMessage(), dto.isNotifyAllUsers());
        return ResponseEntity.ok(new ApiResponse<>("Announcement sent successfully"));
    }
}
