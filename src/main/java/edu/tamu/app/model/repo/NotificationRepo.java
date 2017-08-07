package edu.tamu.app.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tamu.app.model.Notification;
import edu.tamu.app.model.repo.custom.NotificationRepoCustom;

public interface NotificationRepo extends JpaRepository<Notification, Long>, NotificationRepoCustom {
    
    public void delete(Notification notification);

}
