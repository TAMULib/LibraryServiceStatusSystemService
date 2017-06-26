package edu.tamu.app.model.repo.custom;

import edu.tamu.app.model.Notification;

public interface NotificationRepoCustom {

    public Notification create(String name, String body);
    
}
