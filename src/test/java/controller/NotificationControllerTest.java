package controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.controller.NotificationController;
import edu.tamu.app.model.Notification;
import edu.tamu.app.model.repo.NotificationRepo;
import edu.tamu.framework.model.ApiResponse;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class NotificationControllerTest {

    protected static final String TEST_NOTIFICATION1_NAME = "Test Notification Name 1";
    protected static final String TEST_NOTIFICATION2_NAME = "Test Notification Name 2";
    protected static final String TEST_NOTIFICATION3_NAME = "Test Notification Name 3";
    protected static final String TEST_MODIFIED_NOTIFICATION_NAME ="Test Modified Notification Name";
    protected static final String TEST_NOTIFICATION1_BODY = "Test Notification Body 1";
    protected static final String TEST_NOTIFICATION2_BODY = "Test Notification Body 2";
    protected static final String TEST_NOTIFICATION3_BODY = "Test Notification Body 3";
    protected static final String TEST_MODIFIED_NOTIFICATION_BODY = "Test Modified Notification Body";
    
    protected static Notification TEST_NOTIFICATION1 = new Notification(TEST_NOTIFICATION1_NAME, TEST_NOTIFICATION1_BODY);
    protected static Notification TEST_NOTIFICATION2= new Notification(TEST_NOTIFICATION2_NAME,TEST_NOTIFICATION2_BODY); 
    protected static Notification TEST_NOTIFICATION3 = new Notification(TEST_NOTIFICATION3_NAME, TEST_NOTIFICATION3_BODY);
    protected static Notification TEST_MODIFIED_NOTIFICATION = new Notification(TEST_MODIFIED_NOTIFICATION_NAME, TEST_NOTIFICATION2_BODY);
    
    protected static List<Notification> mockNotificationList = new ArrayList<Notification>(Arrays.asList(new Notification[] { TEST_NOTIFICATION1, TEST_NOTIFICATION2, TEST_NOTIFICATION3 }));

    protected static ApiResponse response;
    
    @Mock
    protected NotificationRepo notificationRepo;
    
    @Mock
    protected SimpMessagingTemplate simpMessagingTemplate;
    
    @InjectMocks
    protected NotificationController notificationController;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        when(notificationRepo.findAll()).thenReturn(mockNotificationList);
        when(notificationRepo.findOne(any(Long.class))).thenReturn(TEST_NOTIFICATION1);
        when(notificationRepo.create(any(String.class), any(String.class))).thenReturn(TEST_NOTIFICATION1);
        when(notificationRepo.save(any(Notification.class))).thenReturn(TEST_MODIFIED_NOTIFICATION);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testAllNotifications() {
        response = notificationController.getAllNotifications();
        assertEquals("Not successful at getting all Notifications", SUCCESS, response.getMeta().getType());
        List<Notification> list = (List<Notification>) response.getPayload().get("ArrayList<Notification>");
        assertEquals("The list of services had the worng number of services", mockNotificationList.size(), list.size());
    }
    
    @Test
    public void testNotification() {
        response = notificationController.getNotification(TEST_NOTIFICATION1.getId());
        assertEquals("Not successful at getting requested Notification", SUCCESS, response.getMeta().getType());
        Notification notification = (Notification) response.getPayload().get("Notification");
        assertEquals("Did not get the expected Notification", TEST_NOTIFICATION1.getId(), notification.getId());
    }
    
    @Test
    public void testCreate() {
        response = notificationController.create(TEST_NOTIFICATION1);
        assertEquals("Not successful at creating notification", SUCCESS, response.getMeta().getType());
        Notification notification = (Notification) response.getPayload().get("Notification");
        assertEquals("Incorrect notification returned", TEST_NOTIFICATION1.getName(), notification.getName());
    }
    
    @Test
    public void testUpdate() {
        response = notificationController.update(TEST_MODIFIED_NOTIFICATION);
        assertEquals("Not successful at updating notification", SUCCESS, response.getMeta().getType());
        Notification notification = (Notification) response.getPayload().get("Notification");
        assertEquals("Notification Name was not properly updated", TEST_MODIFIED_NOTIFICATION.getName(), notification.getName());
        assertEquals("Notification Body was not properly updated", TEST_MODIFIED_NOTIFICATION.getBody(), notification.getBody());
    }
    
    @Test
    public void testRemove() {
        response = notificationController.remove(TEST_MODIFIED_NOTIFICATION);
        assertEquals("Not successful at removing Notification", SUCCESS, response.getMeta().getType());
    }
    
    @After
    public void cleanUp() {
        response = null;
    }
}
