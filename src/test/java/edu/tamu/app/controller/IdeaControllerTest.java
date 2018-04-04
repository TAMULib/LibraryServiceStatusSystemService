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
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.IdeaRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class IdeaControllerTest {

    private static User TEST_USER1 = new User("123456789");
    private static User TEST_USER2 = new User("987654321");

    private static final String TEST_IDEA_TITLE1 = "Test Idea Title 1";
    private static final String TEST_IDEA_TITLE2 = "Test Idea Title 2";
    private static final String TEST_IDEA_TITLE3 = "Test Idea Title 3";
    private static final String TEST_IDEA_DESCRIPTION1 = "Test Idea Description 1";
    private static final String TEST_IDEA_DESCRIPTION2 = "Test Idea Description 2";
    private static final String TEST_IDEA_DESCRIPTION3 = "Test Idea Description 3";
    private static final String TEST_MODIFIED_IDEA_TITLE = "Modified Idea Title";
    private static final String TEST_MODIFIED_IDEA_DESCRIPTION = "Modified Idea Description";
    private static final String TEST_SERVICE_NAME = "Test Service";

    private static Service TEST_SERVICE = new Service(TEST_SERVICE_NAME, Status.UP, false, true, true, "", "");
    private static Idea TEST_IDEA1 = new Idea(TEST_IDEA_TITLE1, TEST_IDEA_DESCRIPTION1, TEST_USER1);
    private static Idea TEST_IDEA2 = new Idea(TEST_IDEA_TITLE2, TEST_IDEA_DESCRIPTION2, TEST_USER1);
    private static Idea TEST_IDEA3 = new Idea(TEST_IDEA_TITLE3, TEST_IDEA_DESCRIPTION3, TEST_USER1);
    private static Idea TEST_MODIFIED_IDEA = new Idea(TEST_MODIFIED_IDEA_TITLE, TEST_MODIFIED_IDEA_DESCRIPTION, TEST_USER2, TEST_SERVICE);
    private static List<Idea> mockIdeaList = new ArrayList<Idea>(Arrays.asList(new Idea[] { TEST_IDEA1, TEST_IDEA2, TEST_IDEA3 }));

    private static User user = new User("123456789");

    private static ApiResponse response;

    @Mock
    private UserRepo userRepo;

    @Mock
    private IdeaRepo ideaRepo;

    @Mock
    private ServiceRepo serviceRepo;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private Credentials credentials;

    @InjectMocks
    private IdeaController ideaController;

    @Before
    public void setup() throws UserNotFoundException {
        MockitoAnnotations.initMocks(this);
        when(credentials.getUin()).thenReturn("123456789");
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(ideaRepo.findAll()).thenReturn(mockIdeaList);
        when(ideaRepo.findOne(any(Long.class))).thenReturn(TEST_IDEA1);
        when(ideaRepo.create(any(Idea.class), any(Credentials.class))).thenReturn(TEST_IDEA1);
        when(ideaRepo.update(any(Idea.class))).thenReturn(TEST_MODIFIED_IDEA);
        when(serviceRepo.findOne(any(Long.class))).thenReturn(TEST_SERVICE);
        doNothing().when(ideaRepo).delete(any(Idea.class));
        doNothing().when(ideaRepo).delete(any(Idea.class));
    }

    @Test
    public void testIdea() {
        response = ideaController.getById(TEST_IDEA1.getId());
        assertEquals("Not successful at getting requested Idea", SUCCESS, response.getMeta().getStatus());
        Idea idea = (Idea) response.getPayload().get("Idea");
        assertEquals("Did not get the expected service", TEST_IDEA1.getId(), idea.getId());
    }

    @Test
    public void testCreate() throws UserNotFoundException {
        response = ideaController.create(TEST_IDEA1, credentials);
        assertEquals("Not sucessful at creating Idea", SUCCESS, response.getMeta().getStatus());
    }

    @Test
    public void testUpdate() {
        response = ideaController.update(TEST_MODIFIED_IDEA);
        assertEquals("Not successful at updating idea", SUCCESS, response.getMeta().getStatus());
        Idea idea = (Idea) response.getPayload().get("Idea");
        assertEquals("Notification Title was not properly updated", TEST_MODIFIED_IDEA.getTitle(), idea.getTitle());
        assertEquals("Notification Author was not properly updated", TEST_MODIFIED_IDEA.getAuthor(), idea.getAuthor());
    }

    @Test
    public void testRemove() {
        response = ideaController.remove(TEST_MODIFIED_IDEA);
        assertEquals("Not successful at removing Idea", SUCCESS, response.getMeta().getStatus());
    }

}
