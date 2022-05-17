package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.INVALID;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import edu.tamu.app.enums.IdeaState;
import edu.tamu.app.enums.Status;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.IdeaRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.specification.IdeaSpecification;
import edu.tamu.app.model.request.FilteredPageRequest;
import edu.tamu.app.model.request.IssueRequest;
import edu.tamu.app.service.ProductService;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

@ExtendWith(SpringExtension.class)
public class IdeaControllerTest {

    private static User TEST_USER1 = new User("123456789");
    private static User TEST_USER2 = new User("987654321");

    private static final Credentials TEST_CREDENTIALS_1 = new Credentials();
    static {
        TEST_CREDENTIALS_1.setUin("123456789");
        TEST_CREDENTIALS_1.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS_1.setFirstName("Aggie");
        TEST_CREDENTIALS_1.setLastName("Jack");
        TEST_CREDENTIALS_1.setRole("ROLE_USER");
    }

    private static final String TEST_IDEA_TITLE1 = "Test Idea Title 1";
    private static final String TEST_IDEA_TITLE2 = "Test Idea Title 2";
    private static final String TEST_IDEA_TITLE3 = "Test Idea Title 3";
    private static final String TEST_IDEA_DESCRIPTION1 = "Test Idea Description 1";
    private static final String TEST_IDEA_DESCRIPTION2 = "Test Idea Description 2";
    private static final String TEST_IDEA_DESCRIPTION3 = "Test Idea Description 3";
    private static final String TEST_MODIFIED_IDEA_TITLE = "Modified Idea Title";
    private static final String TEST_MODIFIED_IDEA_DESCRIPTION = "Modified Idea Description";
    private static final String TEST_SERVICE_NAME = "Test Service";
    private static final String TEST_FEEDBACK = "Test Rejection Feedback";

    private static Service TEST_SERVICE = new Service(TEST_SERVICE_NAME, Status.UP, false, true, true, "", "");
    private static Idea TEST_IDEA1 = new Idea(TEST_IDEA_TITLE1, TEST_IDEA_DESCRIPTION1, TEST_USER1, TEST_SERVICE);
    private static Idea TEST_IDEA2 = new Idea(TEST_IDEA_TITLE2, TEST_IDEA_DESCRIPTION2, TEST_USER1);
    private static Idea TEST_IDEA3 = new Idea(TEST_IDEA_TITLE3, TEST_IDEA_DESCRIPTION3, TEST_USER1);
    private static Idea TEST_MODIFIED_IDEA = new Idea(TEST_MODIFIED_IDEA_TITLE, TEST_MODIFIED_IDEA_DESCRIPTION, TEST_USER2, TEST_SERVICE);
    private static Idea ideaWithFeedback = new Idea(TEST_IDEA_TITLE1, TEST_IDEA_DESCRIPTION1, TEST_USER1);
    private Idea rejectedIdea = new Idea(TEST_IDEA_TITLE1, TEST_IDEA_DESCRIPTION1, TEST_USER1);
    private static List<Idea> mockIdeaList = new ArrayList<Idea>(Arrays.asList(new Idea[] { TEST_IDEA1, TEST_IDEA2, TEST_IDEA3 }));
    private static Page<Idea> mockPageableIdeaList = new PageImpl<Idea>(Arrays.asList(new Idea[] { TEST_IDEA1, TEST_IDEA2, TEST_IDEA3 }));
    private static IssueRequest TEST_ISSUE_REQUEST = new IssueRequest(TEST_IDEA1, TEST_CREDENTIALS_1);

    private static User user = new User("123456789");

    private static ApiResponse response;

    @Mock
    private UserRepo userRepo;

    @Mock
    private IdeaRepo ideaRepo;

    @Mock
    private ServiceRepo serviceRepo;

    @Mock
    private ProductService productService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private Credentials credentials;

    @InjectMocks
    private IdeaController ideaController;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() throws UserNotFoundException {
        rejectedIdea.setState(IdeaState.REJECTED);
        ideaWithFeedback.setFeedback(TEST_FEEDBACK);
        MockitoAnnotations.openMocks(this);
        when(credentials.getUin()).thenReturn("123456789");
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(ideaRepo.findAll()).thenReturn(mockIdeaList);
        when(ideaRepo.findAll(any(IdeaSpecification.class), any(Pageable.class))).thenReturn(mockPageableIdeaList);
        when(ideaRepo.getById(any(Long.class))).thenReturn(TEST_IDEA1);
        when(ideaRepo.create(any(Idea.class), any(Credentials.class))).thenReturn(TEST_IDEA1);
        when(ideaRepo.update(any(Idea.class))).thenReturn(TEST_MODIFIED_IDEA);
        when(ideaRepo.reject(ideaWithFeedback)).thenReturn(rejectedIdea);
        when(serviceRepo.getById(any(Long.class))).thenReturn(TEST_SERVICE);
        when(productService.submitIssueRequest(any(IssueRequest.class))).thenReturn(new ApiResponse(SUCCESS, TEST_ISSUE_REQUEST));
        doNothing().when(ideaRepo).delete(any(Idea.class));
        doNothing().when(ideaRepo).delete(any(Idea.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPage() {
        FilteredPageRequest mockFilter = new FilteredPageRequest();
        response = ideaController.page(mockFilter);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting paged Ideas");

        Page<Idea> page = (Page<Idea>) response.getPayload().get("PageImpl");
        assertEquals(mockPageableIdeaList.getSize(), page.getSize(), "The paged list of Ideas is the wrong length");
    }

    @Test
    public void testIdea() {
        TEST_IDEA1.setId(1L);
        response = ideaController.getById(TEST_IDEA1.getId());
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting requested Idea");
        Idea idea = (Idea) response.getPayload().get("Idea");
        assertEquals(TEST_IDEA1.getId(), idea.getId(), "Did not get the expected service");
    }

    @Test
    public void testCreate() throws UserNotFoundException {
        response = ideaController.create(TEST_IDEA1, credentials);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not sucessful at creating Idea");
    }

    @Test
    public void testUpdate() {
        response = ideaController.update(TEST_MODIFIED_IDEA);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at updating idea");
        Idea idea = (Idea) response.getPayload().get("Idea");
        assertEquals(TEST_MODIFIED_IDEA.getTitle(), idea.getTitle(), "Notification Title was not properly updated");
        assertEquals(TEST_MODIFIED_IDEA.getAuthor(), idea.getAuthor(), "Notification Author was not properly updated");
    }

    @Test
    public void testReject() {
        response = ideaController.reject(ideaWithFeedback);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at rejecting idea");
        Idea idea = (Idea) response.getPayload().get("Idea");
        assertEquals(rejectedIdea.getState(), idea.getState(), "State was not set to Rejected");
    }

    @Test
    public void testInvalidReject() {
        response = ideaController.reject(TEST_IDEA1);
        assertEquals(INVALID, response.getMeta().getStatus(), "Idea without feedback was successfull");
    }

    @Test
    public void testHelpdesk() {
        when(ideaRepo.update(any(Idea.class))).thenReturn(TEST_IDEA1);
        response = ideaController.helpdesk(TEST_IDEA1, credentials);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Request was not successfull");
    }

    @Test
    public void testRemove() {
        response = ideaController.remove(TEST_MODIFIED_IDEA);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at removing Idea");
    }

}
