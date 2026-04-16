package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.NotificationDto;
import com.springboot.MessApplication.MessMate.entities.Notification;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.entities.enums.NotificationType;
import com.springboot.MessApplication.MessMate.entities.enums.SubscriptionStatus;
import com.springboot.MessApplication.MessMate.repositories.NotificationRepository;
import com.springboot.MessApplication.MessMate.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final UserService userService;

    public List<NotificationDto> getAllNotifications(NotificationType type) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Notification> notifications;
        if(type==null){
            notifications = notificationRepository.findByUserOrderByIsReadAscTimestampDesc(user);
        }else{
            notifications = notificationRepository.findByUserAndTypeOrderByIsReadAscTimestampDesc(user,type);
        }


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

    public void createAnnouncement(String message, boolean notifyAllUsers) {
        List<User> users = notifyAllUsers
                ? userRepository.findAll()
                : userRepository.findBySubscription_Status(SubscriptionStatus.ACTIVE);

        List<Notification> notifications = users.stream()
                .map(user -> Notification.builder()
                        .user(user)
                        .type(NotificationType.ANNOUNCEMENT)
                        .message(message)
                        .isRead(false)
                        .build())
                .collect(Collectors.toList());

        notificationRepository.saveAll(notifications);
    }
}
