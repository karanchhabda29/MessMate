package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.NotificationDto;
import com.springboot.MessApplication.MessMate.entities.Notification;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ModelMapper modelMapper;
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public List<NotificationDto> getAllNotifications() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Notification> notifications = notificationRepository.findByUserOrderByIsReadAscTimestampDesc(user);

        List<NotificationDto> notificationDtoList = notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .toList();
        //mark unread notifications as read
        List<Notification> unreadNotifications = notifications
                .stream()
                .filter(notification -> !notification.getIsRead())
                .peek(notification -> notification.setIsRead(true))
                .toList();

        //save them
        notificationRepository.saveAll(unreadNotifications);

        return notificationDtoList;
    }

    public void createNotification(Long userId, NotificationType type, String message) {
        User user =  userService.getUserById(userId);
        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .message(message)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }
}
