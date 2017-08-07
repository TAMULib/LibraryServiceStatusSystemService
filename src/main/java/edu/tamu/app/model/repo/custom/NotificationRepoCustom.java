package edu.tamu.app.model.repo.custom;

import java.util.List;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.Notification;

public interface NotificationRepoCustom {

    public Notification create(String name, String body, boolean isActive, List<NotificationLocation> locations);
    
    public Notification update(Notification notification);
    
    public void delete(Notification notification);
    
}
