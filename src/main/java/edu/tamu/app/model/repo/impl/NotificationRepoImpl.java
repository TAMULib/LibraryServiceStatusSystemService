package edu.tamu.app.model.repo.impl;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.Notification;
import edu.tamu.app.model.repo.NotificationRepo;
import edu.tamu.app.model.repo.custom.NotificationRepoCustom;
import edu.tamu.framework.model.ApiResponse;

public class NotificationRepoImpl implements NotificationRepoCustom {

    @Autowired
    NotificationRepo notificationRepo;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Notification create(String name, String body, boolean isActive, List<NotificationLocation> locations) {
        Notification notification = notificationRepo.save(new Notification(name, body, isActive, locations));
        simpMessagingTemplate.convertAndSend("/channel/notification/create", new ApiResponse(SUCCESS, notification));
        return notification;
    }
}
