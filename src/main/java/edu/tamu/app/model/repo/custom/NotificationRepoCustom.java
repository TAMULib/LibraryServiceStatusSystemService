package edu.tamu.app.model.repo.custom;

import java.util.List;

import edu.tamu.app.model.Notification;

public interface NotificationRepoCustom {

    public Notification create(Notification notification);

    public Notification update(Notification notification);

    public void delete(Notification notification);

    public List<Notification> activeNotificationsByLocation(String location);

}
