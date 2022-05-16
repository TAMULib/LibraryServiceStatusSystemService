package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.Notification;
import edu.tamu.app.model.repo.NotificationRepo;
import edu.tamu.weaver.response.ApiResponse;

@ExtendWith(SpringExtension.class)
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(notificationRepo.findAllByOrderByIdDesc()).thenReturn(mockNotificationList);
        when(notificationRepo.getById(any(Long.class))).thenReturn(TEST_NOTIFICATION1);
        when(notificationRepo.create(any(Notification.class))).thenReturn(TEST_NOTIFICATION1);
        when(notificationRepo.update(any(Notification.class))).thenReturn(TEST_MODIFIED_NOTIFICATION);
        when(notificationRepo.activeNotificationsByLocation(any(String.class))).thenReturn(mockNotificationList);
        doNothing().when(notificationRepo).delete(any(Notification.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAllNotifications() {
        response = notificationController.getAllNotifications();
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting all Notifications");
        List<Notification> list = (List<Notification>) response.getPayload().get("ArrayList<Notification>");
        assertEquals(mockNotificationList.size(), list.size(), "The list of services had the worng number of services");
    }

    @Test
    public void testNotification() {
        response = notificationController.getById(TEST_NOTIFICATION1.getId());
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting requested Notification");
        Notification notification = (Notification) response.getPayload().get("Notification");
        assertEquals(TEST_NOTIFICATION1.getId(), notification.getId(), "Did not get the expected Notification");
    }

    @Test
    public void testCreate() {
        response = notificationController.create(TEST_NOTIFICATION1);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at creating notification");
        Notification notification = (Notification) response.getPayload().get("Notification");
        assertEquals(TEST_NOTIFICATION1.getName(), notification.getName(), "Incorrect notification returned");
    }

    @Test
    public void testUpdate() {
        response = notificationController.update(TEST_MODIFIED_NOTIFICATION);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at updating notification");
        Notification notification = (Notification) response.getPayload().get("Notification");
        assertEquals(TEST_MODIFIED_NOTIFICATION.getName(), notification.getName(), "Notification Name was not properly updated");
        assertEquals(TEST_MODIFIED_NOTIFICATION.getBody(), notification.getBody(), "Notification Body was not properly updated");
    }

    @Test
    public void testRemove() {
        response = notificationController.remove(TEST_MODIFIED_NOTIFICATION);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at removing Notification");
    }

    @Test
    public void testActiveNotifications() {
        String notifications = notificationController.getActiveNotifications(TEST_QUERY_PARAM);
        assertEquals(TEST_NOTIFICATION_TEXT, notifications, "Active Notifications not returned correctly");
    }

    @AfterEach
    public void cleanUp() {
        response = null;
    }

}
