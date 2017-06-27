package controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    protected static final Boolean TEST_ON_SHORT_LIST = true;
    
    protected static Service TEST_SERVICE1 = new Service(TEST_SERVICE1_NAME, TEST_SERVICE_STATUS, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST);
    protected static Service TEST_SERVICE2 = new Service(TEST_SERVICE2_NAME, TEST_SERVICE_STATUS, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST);
    protected static Service TEST_SERVICE3 = new Service(TEST_SERVICE3_NAME, TEST_SERVICE_STATUS, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST);
    protected static List<Service> mockServiceList = new ArrayList<Service>(Arrays.asList(new Service[] { TEST_SERVICE1, TEST_SERVICE2,TEST_SERVICE3 }));
    
    static {
        TEST_SERVICE1.setId(11L);
        TEST_SERVICE2.setId(21L);
        TEST_SERVICE3.setId(31L);
    }
    
    protected static ApiResponse response;
    
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
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testAllServices() {
        response = serviceController.getAllServices();
        assertEquals("Not successful at getting all services", SUCCESS, response.getMeta().getType());
        List<Service> list = (List<Service>) response.getPayload().get("ArrayList<Service>");
        assertEquals("The list of Services had none in it", mockServiceList.size(), list.size());
        
    }
}
