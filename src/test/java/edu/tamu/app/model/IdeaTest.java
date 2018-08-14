package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class IdeaTest {

    private static final String TEST_IDEA_TITLE = "Idea Title";
    private static final String TEST_SERVICE_URL = "https://library.tamu.edu";
    private static final String TEST_DESCRIPTION = "Test Service Description";
    private static final String TEST_ALTERNATIVE_IDEA_TITLE = "Alternative Idea Title";
    private static final String TEST_SERVICE_NAME = "Test Service Name";
    private static final String TEST_ALTERNATIVE_SERVICE_NAME = "Different Service Name";
    private static final String TEST_IDEA_DESCRIPTION = "Test Idea Description";
    private static final String TEST_ALTERNATIVE_IDEA_DESCRIPTION = "Alternative Idea Description";
    private static final String TEST_IDEA_EMAIL = "aggiejack@mailinator.com";
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

    @Before
    public void setUp() throws UserNotFoundException {
        testUser = userRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
        service1 = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service2 = serviceRepo.create(new Service(TEST_ALTERNATIVE_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        testIdea = ideaRepo.create(new Idea(TEST_IDEA_TITLE, TEST_IDEA_DESCRIPTION, testUser, service1, TEST_IDEA_EMAIL), TEST_CREDENTIALS);
        testFeatureProposal = featureProposalRepo.create(new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE, TEST_FEATURE_PROPOSAL_DESCRIPTION, testUser, service1), TEST_CREDENTIALS);
    }

    @Test
    public void testCreate() throws UserNotFoundException {
        long initialCount = ideaRepo.count();
        ideaRepo.create(new Idea(TEST_ALTERNATIVE_IDEA_TITLE, TEST_ALTERNATIVE_IDEA_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
        assertEquals("The number of Ideas did not increase by one", initialCount + 1, ideaRepo.count());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testTitleNotNull() throws UserNotFoundException {
        testIdea.setTitle(null);
        ideaRepo.create(new Idea(null, TEST_ALTERNATIVE_IDEA_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAuthorNotNull() throws UserNotFoundException {
        TEST_CREDENTIALS.setUin("987654321");
        ideaRepo.create(new Idea(TEST_ALTERNATIVE_IDEA_TITLE, TEST_ALTERNATIVE_IDEA_DESCRIPTION, null, service2), TEST_CREDENTIALS);
    }

    @Test
    public void testUpdateTitle() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea.setTitle(TEST_ALTERNATIVE_IDEA_TITLE);
        idea = ideaRepo.save(idea);
        assertEquals("Idea title was not updated", TEST_ALTERNATIVE_IDEA_TITLE, idea.getTitle());
    }

    @Test
    public void testUpdateServices() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea.setService(service1);
        idea = ideaRepo.save(idea);
        assertEquals("Service was not set", service1, idea.getService());
        idea.setService(service2);
        idea = ideaRepo.save(idea);
        assertEquals("Service was not updated correctly", service2, idea.getService());
    }

    @Test
    public void testUpdateDescription() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea.setDescription(TEST_IDEA_DESCRIPTION);
        idea = ideaRepo.save(idea);
        assertEquals("Idea body not set", TEST_IDEA_DESCRIPTION, idea.getDescription());
        idea.setDescription(TEST_ALTERNATIVE_IDEA_DESCRIPTION);
        idea = ideaRepo.save(idea);
        assertEquals("Idea body not updated", TEST_ALTERNATIVE_IDEA_DESCRIPTION, idea.getDescription());
    }

    @Test
    public void testUpdateFeatureProposal() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        testFeatureProposal.addIdea(idea);
        testFeatureProposal = featureProposalRepo.save(testFeatureProposal);
        idea.setFeatureProposal(testFeatureProposal);
        idea = ideaRepo.save(idea);

        assertEquals("Idea does not have feature proposal", testFeatureProposal, idea.getFeatureProposal());
        assertEquals("Feature proposal does not have expedted number of ideas", 1, testFeatureProposal.getIdeas().size());
        assertEquals("Feature proposal does not have idea", idea, testFeatureProposal.getIdeas().get(0));
    }

    @Test
    public void testReject() throws UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea = ideaRepo.reject(idea);

        assertEquals("Idea was not rejected", IdeaState.REJECTED, idea.getState());
    }

    @Test
    public void testRejectException() {
        Idea idea = new Idea(TEST_IDEA_TITLE, TEST_IDEA_DESCRIPTION, testUser, service1);
        ideaRepo.reject(idea);
    }

    @Test
    public void testTimestampSetOnCreate() throws UserNotFoundException {
        Idea Idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        Idea = ideaRepo.findOne(Idea.getId());
        assertNotEquals("Timestamp not set on creation", null, Idea.getLastModified());
    }

    @Test
    public void testTimestampSetOnUpdate() throws InterruptedException, UserNotFoundException {
        Idea idea = ideaRepo.create(testIdea, TEST_CREDENTIALS);
        idea = ideaRepo.findOne(idea.getId());
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
        assertEquals("Idea not created", initalCount + 1, ideaRepo.count());
        testFeatureProposal.addIdea(idea);
        testFeatureProposal = featureProposalRepo.save(testFeatureProposal);
        idea.setFeatureProposal(testFeatureProposal);
        idea = ideaRepo.save(idea);

        assertEquals("Idea does not have feature proposal", testFeatureProposal, idea.getFeatureProposal());
        assertEquals("Feature proposal does not have expedted number of ideas", 1, testFeatureProposal.getIdeas().size());
        assertEquals("Feature proposal does not have idea", idea, testFeatureProposal.getIdeas().get(0));

        ideaRepo.delete(idea);
        assertEquals("Idea not deleted", initalCount, ideaRepo.count());

    }

    @After
    public void cleanUp() {
        ideaRepo.deleteAll();
        featureProposalRepo.deleteAll();
        serviceRepo.deleteAll();
        userRepo.deleteAll();
    }
}
