package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.OverallStatus;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.service.SystemMonitorService;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ServiceControllerTest {

    private static final String TEST_SERVICE1_NAME = "Test Service 1 Name";
    private static final String TEST_SERVICE2_NAME = "Test Service 2 Name";
    private static final String TEST_SERVICE3_NAME = "Test Service 3 Name";
    private static final Status TEST_SERVICE_STATUS = Status.UP;
    private static final Boolean TEST_IS_AUTO = false;
    private static final Boolean TEST_IS_PUBLIC = true;
    private static final Boolean TEST_IS_NOT_PUBLIC = false;
    private static final Boolean TEST_ON_SHORT_LIST = true;
    private static final Boolean TEST_NOT_ON_SHORT_LIST = false;

    private static Service TEST_SERVICE1 = new Service(TEST_SERVICE1_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, "", "");
    private static Service TEST_SERVICE2 = new Service(TEST_SERVICE2_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_NOT_PUBLIC, TEST_ON_SHORT_LIST, "", "");
    private static Service TEST_SERVICE3 = new Service(TEST_SERVICE3_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_NOT_ON_SHORT_LIST, "", "");
    private static Service TEST_MODIFIED_SERVICE1 = new Service(TEST_SERVICE1_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_NOT_PUBLIC, TEST_NOT_ON_SHORT_LIST, "", "");
    private static List<Service> mockServiceList = new ArrayList<Service>(Arrays.asList(new Service[] { TEST_SERVICE1, TEST_SERVICE2, TEST_SERVICE3 }));
    private static List<Service> mockPublicServiceList = new ArrayList<Service>(Arrays.asList(new Service[] { TEST_SERVICE1, TEST_SERVICE3 }));

    private static ApiResponse response;

    private static User user = new User("123456789");

    @Mock
    private UserRepo userRepo;

    @Mock
    private ServiceRepo serviceRepo;

    @Mock
    private SystemMonitorService systemMonitorService;

    @Mock
    private SimpMessagingTemplate simpMessageTemplate;

    @Mock
    private Credentials credentials;

    @InjectMocks
    private ServiceController serviceController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(credentials.getUin()).thenReturn("123456789");
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(systemMonitorService.getOverallStatus()).thenReturn(new OverallStatus(edu.tamu.app.enums.OverallMessageType.SUCCESS, "Success"));
        when(serviceRepo.findAllByOrderByStatusDescNameAsc()).thenReturn(mockServiceList);
        when(serviceRepo.findByIsPublicOrderByStatusDescNameAsc(true)).thenReturn(mockPublicServiceList);
        when(serviceRepo.findOne(any(Long.class))).thenReturn(TEST_SERVICE1);
        when(serviceRepo.create(any(Service.class))).thenReturn(TEST_SERVICE1);
        when(serviceRepo.update(any(Service.class))).thenReturn(TEST_MODIFIED_SERVICE1);
        doNothing().when(serviceRepo).delete(any(Service.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAllServices() {
        response = serviceController.getAllServices();
        assertEquals("Not successful at getting all services", SUCCESS, response.getMeta().getStatus());
        List<Service> list = (List<Service>) response.getPayload().get("ArrayList<Service>");
        assertEquals("The list of Services had none in it", mockServiceList.size(), list.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPublicServices() {
        response = serviceController.getPublicServices();
        assertEquals("Not successful at getting public services", SUCCESS, response.getMeta().getStatus());
        List<Service> list = (List<Service>) response.getPayload().get("ArrayList<Service>");
        assertEquals("The list of Services is the wrong length", mockPublicServiceList.size(), list.size());
        assertEquals("The list of Services contains services that are not public", list.size(), countPublicServices(list));
    }

    private int countPublicServices(List<Service> list) {
        int count = 0;
        for (Service service : list) {
            if (service.getIsPublic()) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void testService() {
        response = serviceController.getService(TEST_SERVICE1.getId());
        assertEquals("Not successful at getting requested Service", SUCCESS, response.getMeta().getStatus());
        Service service = (Service) response.getPayload().get("Service");
        assertEquals("Did not get the expected service", TEST_SERVICE1.getId(), service.getId());
    }

    @Test
    public void testCreate() {
        response = serviceController.createService(TEST_SERVICE1, credentials);
        assertEquals("Not sucessful at creating Service", SUCCESS, response.getMeta().getStatus());
        Service service = (Service) response.getPayload().get("Service");
        assertEquals("Incorrect service returned", TEST_SERVICE1.getName(), service.getName());
    }

    @Test
    public void testUpdate() {
        response = serviceController.updateService(TEST_MODIFIED_SERVICE1, credentials);
        assertEquals("Not successful at updating service", SUCCESS, response.getMeta().getStatus());
        Service service = (Service) response.getPayload().get("Service");
        assertEquals("Service name was not properly updated", TEST_MODIFIED_SERVICE1.getName(), service.getName());
        assertEquals("Service status was not properly updated", TEST_MODIFIED_SERVICE1.getStatus(), service.getStatus());
        assertEquals("Service isPublic was not properly updated", TEST_MODIFIED_SERVICE1.getIsPublic(), service.getIsPublic());
        assertEquals("Service onShortList was not properly updated", TEST_MODIFIED_SERVICE1.getOnShortList(), service.getOnShortList());
    }

    @Test
    public void testRemove() {
        response = serviceController.removeService(TEST_SERVICE1);
        assertEquals("Not successful at removing Service", SUCCESS, response.getMeta().getStatus());
    }

    @After
    public void cleanUp() {
        response = null;
    }

}