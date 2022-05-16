package edu.tamu.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tamu.app.StatusApplication;
import edu.tamu.app.enums.IdeaState;
import edu.tamu.app.enums.Role;
import edu.tamu.app.enums.Status;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.repo.FeatureProposalRepo;
import edu.tamu.app.model.repo.IdeaRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class IdeaTest {

    private static final String TEST_IDEA_TITLE = "Idea Title";
    private static final String TEST_IDEA_DESCRIPTION = "Test Idea Description";
    private static final String TEST_IDEA_FEEDBACK = "Idea Feedback";
    private static final String TEST_IDEA_EMAIL = "aggiejack@mailinator.com";

    private static final String TEST_SERVICE_NAME = "Test Service Name";
    private static final String TEST_SERVICE_URL = "https://library.tamu.edu";
    private static final String TEST_SERVICE_DESCRIPTION = "Test Service Description";

    private static final String TEST_ALTERNATIVE_IDEA_TITLE = "Alternative Idea Title";
    private static final String TEST_ALTERNATIVE_IDEA_FEEDBACK = "Alternative Idea Feedback";
    private static final String TEST_ALTERNATIVE_IDEA_DESCRIPTION = "Alternative Idea Description";

    private static final String TEST_ALTERNATIVE_SERVICE_NAME = "Different Service Name";

    private static final Boolean TEST_IS_AUTO = false;
    private static final Boolean TEST_IS_PUBLIC = true;
    private static final Boolean TEST_ON_SHORT_LIST = true;
    private static final Status TEST_SERVICE_STATUS = Status.UP;

    private static final String TEST_FEATURE_PROPOSAL_TITLE = "Feature Proposal Title";
    private static final String TEST_FEATURE_PROPOSAL_DESCRIPTION = "Test Feature Proposal Description";

    private Service service1;
    private Service service2;

    private static final Credentials TEST_CREDENTIALS = new Credentials();
    {
        TEST_CREDENTIALS.setUin("123456789");
        TEST_CREDENTIALS.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS.setFirstName("Aggie");
        TEST_CREDENTIALS.setLastName("Jack");
        TEST_CREDENTIALS.setRole("ROLE_USER");
    }

    private User testUser;

    private Idea testIdea;

    private FeatureProposal testFeatureProposal;

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FeatureProposalRepo featureProposalRepo;

    @BeforeEach
    public void setUp() throws UserNotFoundException {
        testUser = userRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
        service1 = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_SERVICE_DESCRIPTION));
        service2 = serviceRepo.create(new Service(TEST_ALTERNATIVE_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_SERVICE_DESCRIPTION));
        testIdea = ideaRepo.create(new Idea(TEST_IDEA_TITLE, TEST_IDEA_DESCRIPTION, testUser, service1, TEST_IDEA_EMAIL), TEST_CREDENTIALS);
        testFeatureProposal = featureProposalRepo.create(new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE, TEST_FEATURE_PROPOSAL_DESCRIPTION, testUser, service1), TEST_CREDENTIALS);
    }

    @Test
    public void testCreate() throws UserNotFoundException {
        long initialCount = ideaRepo.count();
        ideaRepo.create(new Idea(TEST_ALTERNATIVE_IDEA_TITLE, TEST_ALTERNATIVE_IDEA_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
        assertEquals(initialCount + 1, ideaRepo.count(), "The number of Ideas did not increase by one");
    }

    @Test
    public void testTitleNotNull() throws UserNotFoundException {
        assertThrows(DataIntegrityViolationException.class, () -> {
            testIdea.setTitle(null);
            ideaRepo.create(new Idea(null, TEST_ALTERNATIVE_IDEA_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
        });
    }

    @Test
    public void testAuthorNotNull() throws UserNotFoundException {
        assertThrows(UserNotFoundException.class, () -> {
            TEST_CREDENTIALS.setUin("987654321");
            ideaRepo.create(new Idea(TEST_ALTERNATIVE_IDEA_TITLE, TEST_ALTERNATIVE_IDEA_DESCRIPTION, null, service2), TEST_CREDENTIALS);
        });
    }

    @Test
    public void testUpdateTitle() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea.setTitle(TEST_ALTERNATIVE_IDEA_TITLE);
        idea = ideaRepo.save(idea);
        assertEquals(TEST_ALTERNATIVE_IDEA_TITLE, idea.getTitle(), "Idea title was not updated");
    }

    @Test
    public void testUpdateServices() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea.setService(service1);
        idea = ideaRepo.save(idea);
        assertEquals(service1, idea.getService(), "Service was not set");
        idea.setService(service2);
        idea = ideaRepo.save(idea);
        assertEquals(service2, idea.getService(), "Service was not updated correctly");
    }

    @Test
    public void testUpdateDescription() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea.setDescription(TEST_IDEA_DESCRIPTION);
        idea = ideaRepo.save(idea);
        assertEquals(TEST_IDEA_DESCRIPTION, idea.getDescription(), "Idea body not set");
        idea.setDescription(TEST_ALTERNATIVE_IDEA_DESCRIPTION);
        idea = ideaRepo.save(idea);
        assertEquals(TEST_ALTERNATIVE_IDEA_DESCRIPTION, idea.getDescription(), "Idea body not updated");
    }

    @Test
    public void testUpdateFeedback() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea.setFeedback(TEST_IDEA_FEEDBACK);
        idea = ideaRepo.save(idea);
        assertEquals(TEST_IDEA_FEEDBACK, idea.getFeedback(), "Idea feedback not set");
        idea.setFeedback(TEST_ALTERNATIVE_IDEA_FEEDBACK);
        idea = ideaRepo.save(idea);
        assertEquals(TEST_ALTERNATIVE_IDEA_FEEDBACK, idea.getFeedback(), "Idea feedback not updated");
    }

    @Test
    public void testUpdateFeatureProposal() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        testFeatureProposal.addIdea(idea);
        testFeatureProposal = featureProposalRepo.save(testFeatureProposal);
        idea.setFeatureProposal(testFeatureProposal);
        idea = ideaRepo.save(idea);

        assertEquals(testFeatureProposal, idea.getFeatureProposal(), "Idea does not have feature proposal");
        assertEquals(1, testFeatureProposal.getIdeas().size(), "Feature proposal does not have expedted number of ideas");
        assertEquals(idea, testFeatureProposal.getIdeas().get(0), "Feature proposal does not have idea");
    }

    @Test
    public void testReject() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea = ideaRepo.reject(idea);

        assertEquals(IdeaState.REJECTED, idea.getState(), "Idea was not rejected");
    }

    @Test
    public void testRejectException() {
        Idea idea = new Idea(TEST_IDEA_TITLE, TEST_IDEA_DESCRIPTION, testUser, service1);
        ideaRepo.reject(idea);
    }

    @Test
    public void testTimestampSetOnCreate() throws UserNotFoundException {
        Idea Idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        Idea = ideaRepo.getById(Idea.getId());
        assertNotEquals(null, Idea.getLastModified(), "Timestamp not set on creation");
    }

    @Test
    public void testTimestampSetOnUpdate() throws InterruptedException, UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea = ideaRepo.getById(idea.getId());
        // Calendar createTime = Idea.getLastModified();
        idea.setDescription(TEST_IDEA_DESCRIPTION);

        Thread.sleep(1000);

        ideaRepo.save(idea);

        // TODO: fix false positive, time is not updated

        // assertNotEquals("The timestamp was not updated from creation", createTime.getTime().getTime(), updatedIdea.getLastModified().getTime().getTime());
    }

    @Test
    public void testDelete() throws UserNotFoundException {
        long initalCount = ideaRepo.count();
        Idea idea = ideaRepo.create(new Idea(TEST_ALTERNATIVE_IDEA_TITLE, TEST_ALTERNATIVE_IDEA_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
        assertEquals(initalCount + 1, ideaRepo.count(), "Idea not created");
        testFeatureProposal.addIdea(idea);
        testFeatureProposal = featureProposalRepo.save(testFeatureProposal);
        idea.setFeatureProposal(testFeatureProposal);
        idea = ideaRepo.save(idea);

        assertEquals(testFeatureProposal, idea.getFeatureProposal(), "Idea does not have feature proposal");
        assertEquals(1, testFeatureProposal.getIdeas().size(), "Feature proposal does not have expedted number of ideas");
        assertEquals(idea, testFeatureProposal.getIdeas().get(0), "Feature proposal does not have idea");

        ideaRepo.delete(idea);
        assertEquals(initalCount, ideaRepo.count(), "Idea not deleted");

    }

    @AfterEach
    public void cleanUp() {
        ideaRepo.deleteAll();
        featureProposalRepo.deleteAll();
        serviceRepo.deleteAll();
        userRepo.deleteAll();
    }
}
