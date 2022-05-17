package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tamu.app.enums.Status;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.OverallStatus;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.IdeaRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.specification.ServiceSpecification;
import edu.tamu.app.model.request.AbstractRequest;
import edu.tamu.app.model.request.FilteredPageRequest;
import edu.tamu.app.model.request.IssueRequest;
import edu.tamu.app.model.request.ServiceRequest;
import edu.tamu.app.service.ProductService;
import edu.tamu.app.service.SystemMonitorService;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@ExtendWith(SpringExtension.class)
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

    private static final Service TEST_SERVICE1 = new Service(TEST_SERVICE1_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, "", "");
    private static final Service TEST_SERVICE2 = new Service(TEST_SERVICE2_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_NOT_PUBLIC, TEST_ON_SHORT_LIST, "", "");
    private static final Service TEST_SERVICE3 = new Service(TEST_SERVICE3_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_NOT_ON_SHORT_LIST, "", "");
    private static final Service TEST_MODIFIED_SERVICE1 = new Service(TEST_SERVICE1_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_NOT_PUBLIC, TEST_NOT_ON_SHORT_LIST, "", "");
    private static final List<Service> mockServiceList = new ArrayList<Service>(Arrays.asList(new Service[] { TEST_SERVICE1, TEST_SERVICE2, TEST_SERVICE3 }));
    private static final Page<Service> mockPageableServiceList = new PageImpl<Service>(Arrays.asList(new Service[] { TEST_SERVICE1, TEST_SERVICE2, TEST_SERVICE3 }));
    private static final List<Service> mockPublicServiceList = new ArrayList<Service>(Arrays.asList(new Service[] { TEST_SERVICE1, TEST_SERVICE3 }));

    private static final User TEST_SERVICE = new User("123456789");

    private static final Idea TEST_IDEA = new Idea("Test Idea Title", "Test Idea Description", TEST_SERVICE, TEST_SERVICE1);

    private static ApiResponse response;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ServiceRepo serviceRepo;

    @Mock
    private IdeaRepo ideaRepo;

    @Mock
    private ProductService productService;

    @Mock
    private SystemMonitorService systemMonitorService;

    @Mock
    private SimpMessagingTemplate simpMessageTemplate;

    @Mock
    private Credentials credentials;

    @InjectMocks
    private ServiceController serviceController;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() throws UserNotFoundException {
        MockitoAnnotations.openMocks(this);
        when(credentials.getUin()).thenReturn("123456789");
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(TEST_SERVICE));
        when(systemMonitorService.getOverallStatus()).thenReturn(new OverallStatus(edu.tamu.app.enums.OverallMessageType.SUCCESS, "Success"));
        when(serviceRepo.findAll()).thenReturn(mockServiceList);
        when(serviceRepo.findAll(any(ServiceSpecification.class), any(Pageable.class))).thenReturn(mockPageableServiceList);
        when(serviceRepo.findAllByOrderByStatusDescNameAsc()).thenReturn(mockServiceList);
        when(serviceRepo.findByIsPublicOrderByStatusDescNameAsc(true)).thenReturn(mockPublicServiceList);
        when(serviceRepo.getById(any(Long.class))).thenReturn(TEST_SERVICE1);
        when(serviceRepo.create(any(Service.class))).thenReturn(TEST_SERVICE1);
        when(serviceRepo.update(any(Service.class))).thenReturn(TEST_MODIFIED_SERVICE1);
        when(ideaRepo.create(any(Idea.class), any(Credentials.class))).thenReturn(TEST_IDEA);
        doNothing().when(serviceRepo).delete(any(Service.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAllServices() {
        response = serviceController.getAllServices();
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting all services");
        List<Service> list = (List<Service>) response.getPayload().get("ArrayList<Service>");
        assertEquals(mockServiceList.size(), list.size(), "The list of Services had none in it");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPublicServices() {
        response = serviceController.getPublicServices();
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting public services");
        List<Service> list = (List<Service>) response.getPayload().get("ArrayList<Service>");
        assertEquals(mockPublicServiceList.size(), list.size(), "The list of Services is the wrong length");
        assertEquals(list.size(), countPublicServices(list), "The list of Services contains services that are not public");
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
    @SuppressWarnings("unchecked")
    public void testPage() {
        FilteredPageRequest mockFilter = new FilteredPageRequest();
        response = serviceController.page(mockFilter);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting paged Services");

        Page<Service> page = (Page<Service>) response.getPayload().get("PageImpl");
        assertEquals(mockPageableServiceList.getSize(), page.getSize(), "The paged list of Services is the wrong length");
    }

    @Test
    public void testService() {
        TEST_SERVICE1.setId(1L);
        response = serviceController.getService(TEST_SERVICE1.getId());
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting requested Service");
        Service service = (Service) response.getPayload().get("Service");
        assertEquals(TEST_SERVICE1.getId(), service.getId(), "Did not get the expected service");
    }

    @Test
    public void testCreate() {
        response = serviceController.createService(TEST_SERVICE1, credentials);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not sucessful at creating Service");
        Service service = (Service) response.getPayload().get("Service");
        assertEquals(TEST_SERVICE1.getName(), service.getName(), "Incorrect service returned");
    }

    @Test
    public void testUpdate() {
        response = serviceController.updateService(TEST_MODIFIED_SERVICE1, credentials);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at updating service");
        Service service = (Service) response.getPayload().get("Service");
        assertEquals(TEST_MODIFIED_SERVICE1.getName(), service.getName(), "Service name was not properly updated");
        assertEquals(TEST_MODIFIED_SERVICE1.getStatus(), service.getStatus(), "Service status was not properly updated");
        assertEquals(TEST_MODIFIED_SERVICE1.getIsPublic(), service.getIsPublic(), "Service isPublic was not properly updated");
        assertEquals(TEST_MODIFIED_SERVICE1.getOnShortList(), service.getOnShortList(), "Service onShortList was not properly updated");
    }

    @Test
    public void testRemove() {
        response = serviceController.removeService(TEST_SERVICE1);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at removing Service");
    }

    @Test
    public void submitIssueRequest() {
        when(productService.submitIssueRequest(any(IssueRequest.class))).thenReturn(new ApiResponse(SUCCESS, "Successfully submitted issue request!"));
        ServiceRequest request = new ServiceRequest(AbstractRequest.RequestType.ISSUE, "Test feature request", "This is a test issue request on product 1", 1L);
        ApiResponse response = serviceController.submitIssueRequest(request, credentials);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus(), "Response was not a success!");
        assertEquals(String.format("Your issue for %s has been submitted!", TEST_SERVICE1_NAME), response.getMeta().getMessage(), "Response message was not correct!");
    }

    @Test
    public void submitFeatureRequest() throws UserNotFoundException {
        ServiceRequest request = new ServiceRequest(AbstractRequest.RequestType.FEATURE, "Test issue request", "This is a test issue request on product 1", 1L);
        ApiResponse response = serviceController.submitFeatureRequest(request, credentials);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus(), "Response was not a success!");
        assertEquals(String.format("Your feature request for %s has been submitted as an idea!", TEST_SERVICE1_NAME), response.getMeta().getMessage(), "Response message was not correct!");
    }

    @AfterEach
    public void cleanUp() {
        response = null;
    }

}
