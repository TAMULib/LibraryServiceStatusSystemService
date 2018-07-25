package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.Notification;
import edu.tamu.app.model.repo.NotificationRepo;
import edu.tamu.weaver.response.ApiResponse;

@RunWith(SpringRunner.class)
public class NotificationControllerTest {

    private static final String TEST_NOTIFICATION1_NAME = "Test Notification Name 1";
    private static final String TEST_NOTIFICATION2_NAME = "Test Notification Name 2";
    private static final String TEST_NOTIFICATION3_NAME = "Test Notification Name 3";
    private static final String TEST_MODIFIED_NOTIFICATION_NAME = "Test Modified Notification Name";
    private static final String TEST_NOTIFICATION1_BODY = "Test Notification Body 1";
    private static final String TEST_NOTIFICATION2_BODY = "Test Notification Body 2";
    private static final String TEST_NOTIFICATION3_BODY = "Test Notification Body 3";

    private static final String TEST_QUERY_PARAM = "CUSHING";
    private static final String TEST_NOTIFICATION_TEXT = "<p>Test Notification Body 1</p><p>Test Notification Body 2</p><p>Test Notification Body 3</p>";

    private static final List<NotificationLocation> TEST_LOCATIONS = Arrays.asList(new NotificationLocation[] { NotificationLocation.CUSHING });

    private static Notification TEST_NOTIFICATION1 = new Notification(TEST_NOTIFICATION1_NAME, TEST_NOTIFICATION1_BODY, TEST_LOCATIONS);
    private static Notification TEST_NOTIFICATION2 = new Notification(TEST_NOTIFICATION2_NAME, TEST_NOTIFICATION2_BODY, TEST_LOCATIONS);
    private static Notification TEST_NOTIFICATION3 = new Notification(TEST_NOTIFICATION3_NAME, TEST_NOTIFICATION3_BODY, TEST_LOCATIONS);
    private static Notification TEST_MODIFIED_NOTIFICATION = new Notification(TEST_MODIFIED_NOTIFICATION_NAME, TEST_NOTIFICATION2_BODY, TEST_LOCATIONS);

    private static List<Notification> mockNotificationList = new ArrayList<Notification>(Arrays.asList(new Notification[] { TEST_NOTIFICATION1, TEST_NOTIFICATION2, TEST_NOTIFICATION3 }));

    private static ApiResponse response;

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private NotificationController notificationController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(notificationRepo.findAllByOrderByIdDesc()).thenReturn(mockNotificationList);
        when(notificationRepo.findOne(any(Long.class))).thenReturn(TEST_NOTIFICATION1);
        when(notificationRepo.create(any(Notification.class))).thenReturn(TEST_NOTIFICATION1);
        when(notificationRepo.update(any(Notification.class))).thenReturn(TEST_MODIFIED_NOTIFICATION);
        when(notificationRepo.activeNotificationsByLocation(any(String.class))).thenReturn(mockNotificationList);
        doNothing().when(notificationRepo).delete(any(Notification.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAllNotifications() {
        response = notificationController.getAllNotifications();
        assertEquals("Not successful at getting all Notifications", SUCCESS, response.getMeta().getStatus());
        List<Notification> list = (List<Notification>) response.getPayload().get("ArrayList<Notification>");
        assertEquals("The list of services had the worng number of services", mockNotificationList.size(), list.size());
    }

    @Test
    public void testNotification() {
        response = notificationController.getById(TEST_NOTIFICATION1.getId());
        assertEquals("Not successful at getting requested Notification", SUCCESS, response.getMeta().getStatus());
        Notification notification = (Notification) response.getPayload().get("Notification");
        assertEquals("Did not get the expected Notification", TEST_NOTIFICATION1.getId(), notification.getId());
    }

    @Test
    public void testCreate() {
        response = notificationController.create(TEST_NOTIFICATION1);
        assertEquals("Not successful at creating notification", SUCCESS, response.getMeta().getStatus());
        Notification notification = (Notification) response.getPayload().get("Notification");
        assertEquals("Incorrect notification returned", TEST_NOTIFICATION1.getName(), notification.getName());
    }

    @Test
    public void testUpdate() {
        response = notificationController.update(TEST_MODIFIED_NOTIFICATION);
        assertEquals("Not successful at updating notification", SUCCESS, response.getMeta().getStatus());
        Notification notification = (Notification) response.getPayload().get("Notification");
        assertEquals("Notification Name was not properly updated", TEST_MODIFIED_NOTIFICATION.getName(), notification.getName());
        assertEquals("Notification Body was not properly updated", TEST_MODIFIED_NOTIFICATION.getBody(), notification.getBody());
    }

    @Test
    public void testRemove() {
        response = notificationController.remove(TEST_MODIFIED_NOTIFICATION);
        assertEquals("Not successful at removing Notification", SUCCESS, response.getMeta().getStatus());
    }

    @Test
    public void testActiveNotifications() {
        String notifications = notificationController.getActiveNotifications(TEST_QUERY_PARAM);
        assertEquals("Active Notifications not returned correctly", TEST_NOTIFICATION_TEXT, notifications);
    }

    @After
    public void cleanUp() {
        response = null;
    }

}
