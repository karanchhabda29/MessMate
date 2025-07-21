package com.springboot.MessApplication.MessMate.services;

import com.springboot.MessApplication.MessMate.dto.NotificationDto;
import com.springboot.MessApplication.MessMate.entities.User;
import com.springboot.MessApplication.MessMate.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ModelMapper modelMapper;
    private final NotificationRepository notificationRepository;

    public List<NotificationDto> getAllNotifications() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return notificationRepository.findByUserOrderByIsReadAscTimestampDesc(user)
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .toList();
    }
}
