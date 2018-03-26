package edu.tamu.app.model.repo.impl;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.Notification;
import edu.tamu.app.model.Schedule;
import edu.tamu.app.model.repo.NotificationRepo;
import edu.tamu.app.model.repo.custom.NotificationRepoCustom;
import edu.tamu.weaver.response.ApiResponse;

public class NotificationRepoImpl implements NotificationRepoCustom {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Notification create(Notification notification) {
        notification = notificationRepo.save(notification);
        simpMessagingTemplate.convertAndSend("/channel/notifications/create", new ApiResponse(SUCCESS, notification));
        return notification;
    }

    @Override
    public Notification update(Notification notification) {
        for (Schedule schedule : notification.getSchedules()) {
            schedule.setScheduler(notification);
        }
        notification = notificationRepo.save(notification);
        simpMessagingTemplate.convertAndSend("/channel/notifications/update", new ApiResponse(SUCCESS, notification));
        return notification;
    }

    @Override
    public void delete(Notification notification) {
        notificationRepo.delete(notification.getId());
        simpMessagingTemplate.convertAndSend("/channel/notifications/delete", new ApiResponse(SUCCESS, notification.getId()));
    }

    @Override
    public List<Notification> activeNotificationsByLocation(String location) {
        List<Notification> notifications = new ArrayList<Notification>();
        if (location.equals("ALL")) {
            notifications = notificationRepo.findByActiveTrueOrderByIdDesc();
        } else {
            try {
                notifications = notificationRepo.findByActiveTrueAndLocationsOrderByIdDesc(NotificationLocation.valueOf(location));
            } catch (IllegalArgumentException e) {

            }
        }
        return notifications;
    }

}
