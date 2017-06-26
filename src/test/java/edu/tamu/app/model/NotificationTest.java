package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.model.repo.NotificationRepo;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WebServerInit.class })
public class NotificationTest {

    protected static final String TEST_NOTIFICATION_NAME = "Test Notification Name";
    protected static final String TEST_NOTIFICATION_BODY = "Test Notification Body";
    protected static final String TEST_ALTERNATE_NOTIFICATION_NAME = "Different Notification Name";
    protected static final String TEST_ALTERNATE_NOTIFICATION_BODY = "Different Notification Body";

    @Autowired
    NotificationRepo notificationRepo;

    @Test
    public void testCreate() {
        long initialCount = notificationRepo.count();
        notificationRepo.create(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY);
        assertEquals("The number of Notifications did not increase by one", initialCount + 1, notificationRepo.count());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testNameNotNull() {
        notificationRepo.create(null, TEST_NOTIFICATION_BODY);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testBodyNotNull() {
        notificationRepo.create(TEST_NOTIFICATION_NAME, null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNameNotEmpty() {
        notificationRepo.create("", TEST_NOTIFICATION_BODY);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testBodyNotEmpty() {
        notificationRepo.create(TEST_NOTIFICATION_NAME, "");
    }

    @Test
    public void testUpdateName() {
       Notification notification = notificationRepo.create(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY);
       notification.setName(TEST_ALTERNATE_NOTIFICATION_NAME);
       notificationRepo.save(notification);
       notification = notificationRepo.findOne(notification.getId());
       assertEquals("Notification name was not changed", TEST_ALTERNATE_NOTIFICATION_NAME, notification.getName());
    }
    
    @Test
    public void testUpdateBody() {
       Notification notification = notificationRepo.create(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY);
       notification.setBody(TEST_ALTERNATE_NOTIFICATION_BODY);
       notificationRepo.save(notification);
       notification = notificationRepo.findOne(notification.getId());
       assertEquals("Notification body was not changed", TEST_ALTERNATE_NOTIFICATION_BODY, notification.getBody());
    }

    @Test
    public void testDelete() {
        long initialCount = notificationRepo.count();
        Notification notification = notificationRepo.create(TEST_NOTIFICATION_NAME, TEST_NOTIFICATION_BODY);
        assertEquals("Notification not created", initialCount + 1, notificationRepo.count());
        notificationRepo.delete(notification);
        assertEquals("Notification was not deleted", initialCount, notificationRepo.count());
    }

    @After
    public void cleanUp() {
        notificationRepo.deleteAll();
    }

}
