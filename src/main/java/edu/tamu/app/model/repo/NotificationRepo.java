package edu.tamu.app.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.Notification;
import edu.tamu.app.model.repo.custom.NotificationRepoCustom;

public interface NotificationRepo extends JpaRepository<Notification, Long>, NotificationRepoCustom {

    public List<Notification> findByActiveTrue();

    public List<Notification> findByActiveTrueAndLocations(NotificationLocation location);

    public void delete(Notification notification);

}
