package controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.controller.ServiceController;
import edu.tamu.app.enums.Status;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.framework.model.ApiResponse;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ServiceControllerTest {
    
    protected static final String TEST_SERVICE1_NAME = "Test Service 1 Name";
    protected static final String TEST_SERVICE2_NAME = "Test Service 2 Name";
    protected static final String TEST_SERVICE3_NAME = "Test Service 3 Name";
    protected static final Status TEST_SERVICE_STATUS = Status.UP;
    protected static final Boolean TEST_IS_PUBLIC = true;
    protected static final Boolean TEST_IS_NOT_PUBLIC = false;
    protected static final Boolean TEST_ON_SHORT_LIST = true;
    protected static final Boolean TEST_NOT_ON_SHORT_LIST = false;
    
    protected static Service TEST_SERVICE1 = new Service(TEST_SERVICE1_NAME, TEST_SERVICE_STATUS, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST);
    protected static Service TEST_SERVICE2 = new Service(TEST_SERVICE2_NAME, TEST_SERVICE_STATUS, TEST_IS_NOT_PUBLIC, TEST_ON_SHORT_LIST);
    protected static Service TEST_SERVICE3 = new Service(TEST_SERVICE3_NAME, TEST_SERVICE_STATUS, TEST_IS_PUBLIC, TEST_NOT_ON_SHORT_LIST);
    protected static Service TEST_SERVICE_FOR_MODIFICATION = new Service(TEST_SERVICE1_NAME, TEST_SERVICE_STATUS, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST);
    protected static Service TEST_MODIFIED_SERVICE1 = new Service(TEST_SERVICE1_NAME, TEST_SERVICE_STATUS, TEST_IS_NOT_PUBLIC, TEST_NOT_ON_SHORT_LIST);
    protected static List<Service> mockServiceList = new ArrayList<Service>(Arrays.asList(new Service[] { TEST_SERVICE1, TEST_SERVICE2,TEST_SERVICE3 }));
    protected static List<Service> mockPublicServiceList = new ArrayList<Service>(Arrays.asList(new Service[] { TEST_SERVICE1, TEST_SERVICE3 }));
    
    protected static ApiResponse response;
    
    static {
        TEST_SERVICE1.setId(11L);
        TEST_SERVICE2.setId(21L);
        TEST_SERVICE3.setId(31L);
        TEST_SERVICE_FOR_MODIFICATION.setId(100L);
        TEST_MODIFIED_SERVICE1.setId(101L);
    }
    
    @Mock
    protected ServiceRepo serviceRepo;
    
    @Mock
    protected SimpMessagingTemplate simpMessageTemplate;
    
    @InjectMocks
    protected ServiceController serviceController;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(serviceRepo.findAll()).thenReturn(mockServiceList);
        when(serviceRepo.findByIsPublic(true)).thenReturn(mockPublicServiceList);
        when(serviceRepo.findOne(any(Long.class))).then(new Answer<Service>() {
            @Override
            public Service answer(InvocationOnMock invocation) throws Throwable {
                return findServiceById((Long) invocation.getArguments()[0]);
            }
        });
        when(serviceRepo.create(any(String.class), any(Status.class), any(Boolean.class), any(Boolean.class)))
            .thenReturn(TEST_SERVICE1);
        when(serviceRepo.save(any(Service.class))).then(new Answer<Service>() {
            @Override
            public Service answer(InvocationOnMock invocation) throws Throwable {
                return updateService((Service) invocation.getArguments()[0]);
            }
        });
        doNothing().when(serviceRepo).delete(any(Service.class));
    }
    
    private Service findServiceById(Long id) {
        Service result = null;
        if (id.equals(TEST_SERVICE_FOR_MODIFICATION.getId())) {
            result = TEST_SERVICE_FOR_MODIFICATION;
        }
        for (Service service : mockServiceList) {
            if (service.getId().equals(id)) {
                result = service;
            }
        }
        return result;
    }
    
    private Service updateService(Service modifiedService) {
        Service result = null;
        if (TEST_SERVICE_FOR_MODIFICATION.getName().equals(modifiedService.getName())) {
            TEST_SERVICE_FOR_MODIFICATION.setName(modifiedService.getName());
            TEST_SERVICE_FOR_MODIFICATION.setAliases(modifiedService.getAliases());
            TEST_SERVICE_FOR_MODIFICATION.setStatus(modifiedService.getStatus());
            TEST_SERVICE_FOR_MODIFICATION.setServiceUrl(modifiedService.getServiceUrl());
            TEST_SERVICE_FOR_MODIFICATION.setIsPublic(modifiedService.getIsPublic());
            TEST_SERVICE_FOR_MODIFICATION.setOnShortList(modifiedService.getOnShortList());
            TEST_SERVICE_FOR_MODIFICATION.setNotes(modifiedService.getNotes());
            result = TEST_SERVICE_FOR_MODIFICATION;
        }
        return result;
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testAllServices() {
        response = serviceController.getAllServices();
        assertEquals("Not successful at getting all services", SUCCESS, response.getMeta().getType());
        List<Service> list = (List<Service>) response.getPayload().get("ArrayList<Service>");
        assertEquals("The list of Services had none in it", mockServiceList.size(), list.size());
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testPublicServices() {
        response = serviceController.getPublicServices();
        assertEquals("Not successful at getting public services", SUCCESS, response.getMeta().getType());
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
        assertEquals("Not successful at getting requested Service", SUCCESS, response.getMeta().getType());
        Service service = (Service) response.getPayload().get("Service");
        assertEquals("Did not get the expected service", TEST_SERVICE1.getId(), service.getId());
    }
    
    @Test
    public void testCreate() {
        response = serviceController.createService(TEST_SERVICE1);
        assertEquals("Not sucessful at creating Service", SUCCESS, response.getMeta().getType());
        Service service = (Service) response.getPayload().get("Service");
        assertEquals("Incorrect service returned", TEST_SERVICE1.getName(), service.getName());
    }
    
    @Test
    public void testUpdate() {
        response = serviceController.updateService(TEST_MODIFIED_SERVICE1);
        assertEquals("Not successful at updating service", SUCCESS, response.getMeta().getType());
        Service service = (Service) response.getPayload().get("Service");
        assertEquals("Service name was not properly updated", TEST_MODIFIED_SERVICE1.getName(), service.getName());
        assertEquals("Service status was not properly updated", TEST_MODIFIED_SERVICE1.getStatus(), service.getStatus());
        assertEquals("Service isPublic was not properly updated", TEST_MODIFIED_SERVICE1.getIsPublic(), service.getIsPublic());
        assertEquals("Service onShortList was not properly updated", TEST_MODIFIED_SERVICE1.getOnShortList(), service.getOnShortList());
        
    }
    
    @Test
    public void testRemove() {
        response = serviceController.removeService(TEST_SERVICE1);
        assertEquals("Not successful at removing Service",SUCCESS, response.getMeta().getType());
    }
    
    @After
    public void cleanUp() {
        response = null;
    }
}
