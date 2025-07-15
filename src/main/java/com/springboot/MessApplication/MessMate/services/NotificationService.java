package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.NotificationDto;
import com.springboot.MessApplication.MessMate.entities.Notification;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ModelMapper modelMapper;
    private final NotificationRepository notificationRepository;

    public List<NotificationDto> getNewNotifications() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Notification> notifications = notificationRepository.getNotificationByUser(user);

        List<NotificationDto> notificationDtoList = new ArrayList<>();
        notifications.forEach(notification -> {
            if(notification.getIsRead().equals(false)){
                notificationDtoList.add(modelMapper.map(notification, NotificationDto.class));
            }
        });
        return notificationDtoList;
    }
}
