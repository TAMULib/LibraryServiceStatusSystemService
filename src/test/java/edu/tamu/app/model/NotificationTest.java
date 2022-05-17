package edu.tamu.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import edu.tamu.app.StatusApplication;
import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.repo.NotificationRepo;

@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class NotificationTest {

    private static final String TEST_NOTIFICATION_NAME = "Test Notification Name";
    private static final String TEST_NOTIFICATION_BODY = "Test Notification Body";
    private static final String TEST_ALTERNATE_NOTIFICATION_NAME = "Different Notification Name";
    private static final String TEST_ALTERNATE_NOTIFICATION_BODY = "Different Notification Body";
    private static final List<NotificationLocation> TEST_LOCATIONS = Arrays.asList(new NotificationLocation[] { NotificationLocation.CUSHING });

    @Autowired
    private NotificationRepo notificationRepo;

    @Test
    public void testCreate() {
        long initialCount = notificationRepo.count();
        notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
        assertEquals(initialCount + 1, notificationRepo.count(), "The number of Notifications did not increase by one");
    }

    @Test
    public void testNameNotNull() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            notificationRepo.create(new Notification(null, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
        });
    }

    @Test
    public void testBodyNotNull() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, null, TEST_LOCATIONS));
        });
    }

    @Test
    public void testUpdateName() {
        Notification notification = notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
        notification.setName(TEST_ALTERNATE_NOTIFICATION_NAME);
        notificationRepo.save(notification);
        notification = notificationRepo.findById(notification.getId()).get();
        assertEquals(TEST_ALTERNATE_NOTIFICATION_NAME, notification.getName(), "Notification name was not changed");
    }

    @Test
    public void testUpdateBody() {
        Notification notification = notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
        notification.setBody(TEST_ALTERNATE_NOTIFICATION_BODY);
        notificationRepo.update(notification);
        notification = notificationRepo.findById(notification.getId()).get();
        assertEquals(TEST_ALTERNATE_NOTIFICATION_BODY, notification.getBody(), "Notification body was not changed");
    }

    @Test
    public void testDelete() {
        long initialCount = notificationRepo.count();
        Notification notification = notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
        assertEquals(initialCount + 1, notificationRepo.count(), "Notification not created");
        notificationRepo.delete(notification);
        assertEquals(initialCount, notificationRepo.count(), "Notification was not deleted");
    }

    @AfterEach
    public void cleanUp() {
        notificationRepo.deleteAll();
    }

}
