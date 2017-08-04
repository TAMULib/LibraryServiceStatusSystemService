package edu.tamu.app.model.repo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.Notification;
import edu.tamu.app.model.repo.NotificationRepo;
import edu.tamu.app.model.repo.custom.NotificationRepoCustom;

public class NotificationRepoImpl implements NotificationRepoCustom {

    @Autowired
    NotificationRepo notificationRepo;

    @Override
    public Notification create(String name, String body, boolean isActive, List<NotificationLocation> locations) {
        return notificationRepo.save(new Notification(name, body, isActive, locations));
    }
}
