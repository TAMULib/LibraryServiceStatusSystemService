package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.repo.NotificationRepo;

@WebAppConfiguration
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WebServerInit.class })
public class NotificationTest {

    protected static final String TEST_NOTIFICATION_NAME = "Test Notification Name";
    protected static final String TEST_NOTIFICATION_BODY = "Test Notification Body";
    protected static final String TEST_ALTERNATE_NOTIFICATION_NAME = "Different Notification Name";
    protected static final String TEST_ALTERNATE_NOTIFICATION_BODY = "Different Notification Body";
    protected static final List<NotificationLocation> TEST_LOCATIONS = Arrays.asList(new NotificationLocation[] { NotificationLocation.CUSHING });

    @Autowired
    NotificationRepo notificationRepo;

    @Test
    public void testCreate() {
        long initialCount = notificationRepo.count();
        notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
        assertEquals("The number of Notifications did not increase by one", initialCount + 1, notificationRepo.count());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testNameNotNull() {
        notificationRepo.create(new Notification(null, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testBodyNotNull() {
        notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, null, TEST_LOCATIONS));
    }

    @Test
    public void testUpdateName() {
        Notification notification = notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
        notification.setName(TEST_ALTERNATE_NOTIFICATION_NAME);
        notificationRepo.save(notification);
        notification = notificationRepo.findOne(notification.getId());
        assertEquals("Notification name was not changed", TEST_ALTERNATE_NOTIFICATION_NAME, notification.getName());
    }

    @Test
    public void testUpdateBody() {
        Notification notification = notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
        notification.setBody(TEST_ALTERNATE_NOTIFICATION_BODY);
        notificationRepo.update(notification);
        notification = notificationRepo.findOne(notification.getId());
        assertEquals("Notification body was not changed", TEST_ALTERNATE_NOTIFICATION_BODY, notification.getBody());
    }

    @Test
    public void testDelete() {
        long initialCount = notificationRepo.count();
        Notification notification = notificationRepo.create(new Notification(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY, TEST_LOCATIONS));
        assertEquals("Notification not created", initialCount + 1, notificationRepo.count());
        notificationRepo.delete(notification);
        assertEquals("Notification was not deleted", initialCount, notificationRepo.count());
    }

    @After
    public void cleanUp() {
        notificationRepo.deleteAll();
    }

}
