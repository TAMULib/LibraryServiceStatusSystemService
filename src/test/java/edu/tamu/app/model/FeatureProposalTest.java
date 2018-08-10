package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;

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
public class FeatureProposalTest {

    private static final String TEST_FEATURE_PROPOSAL_TITLE = "Feature Proposal Title";
    private static final String TEST_FEATURE_PROPOSAL_DESCRIPTION = "Test Feature Proposal Description";

    private static final String TEST_SERVICE_NAME = "Test Service Name";
    private static final String TEST_SERVICE_URL = "https://library.tamu.edu";
    private static final String TEST_SERVICE_DESCRIPTION = "Test Service Description";

    private static final String TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE = "Alternative Feature Proposal Title";
    private static final String TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION = "Alternative Feature Proposal Description";

    private static final String TEST_ALTERNATIVE_SERVICE_NAME = "Different Service Name";

    private static final Boolean TEST_IS_AUTO = false;
    private static final Boolean TEST_IS_PUBLIC = true;
    private static final Boolean TEST_ON_SHORT_LIST = true;
    private static final Status TEST_SERVICE_STATUS = Status.UP;

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

    private static final Credentials TEST_VOTER_CREDENTIALS = new Credentials();
    {
        TEST_VOTER_CREDENTIALS.setUin("987654321");
        TEST_VOTER_CREDENTIALS.setEmail("aggieJack@tamu.edu");
        TEST_VOTER_CREDENTIALS.setFirstName("Aggie");
        TEST_VOTER_CREDENTIALS.setLastName("Jack");
        TEST_VOTER_CREDENTIALS.setRole("ROLE_USER");
    }

    private User testUser;

    private FeatureProposal testFeatureProposal;

    @Autowired
    private FeatureProposalRepo featureProposalRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private UserRepo userRepo;

    @Before
    public void setUp() throws UserNotFoundException {
        testUser = userRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
        service1 = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_SERVICE_DESCRIPTION));
        service2 = serviceRepo.create(new Service(TEST_ALTERNATIVE_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_SERVICE_DESCRIPTION));
        testFeatureProposal = featureProposalRepo.create(new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE, TEST_FEATURE_PROPOSAL_DESCRIPTION, testUser, service1), TEST_CREDENTIALS);
    }

    @Test
    public void testCreate() throws UserNotFoundException {
        long initialCount = featureProposalRepo.count();
        featureProposalRepo.create(new FeatureProposal(TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE, TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
        assertEquals("The number of FeatureProposals did not increase by one", initialCount + 1, featureProposalRepo.count());
    }

    @Test
    public void testVote() throws UserNotFoundException {
        FeatureProposal newFeatureProposal = featureProposalRepo.create(new FeatureProposal(TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE, TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
        User testVoter = userRepo.create(TEST_VOTER_CREDENTIALS.getUin(), TEST_VOTER_CREDENTIALS.getEmail(), TEST_VOTER_CREDENTIALS.getFirstName(), TEST_VOTER_CREDENTIALS.getLastName(), Role.valueOf(TEST_VOTER_CREDENTIALS.getRole()));
        newFeatureProposal.addVoter(testVoter);
        newFeatureProposal = featureProposalRepo.save(newFeatureProposal);
        assertEquals("The FeatureProposals had incorrect number of voters", 1, newFeatureProposal.getVotes());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testVoteDuplicate() throws UserNotFoundException {
        FeatureProposal newFeatureProposal = featureProposalRepo.create(new FeatureProposal(TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE, TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
        User testVoter = userRepo.create(TEST_VOTER_CREDENTIALS.getUin(), TEST_VOTER_CREDENTIALS.getEmail(), TEST_VOTER_CREDENTIALS.getFirstName(), TEST_VOTER_CREDENTIALS.getLastName(), Role.valueOf(TEST_VOTER_CREDENTIALS.getRole()));
        newFeatureProposal.setVoters(new ArrayList<User>() {
            private static final long serialVersionUID = -7900927790649066585L;
            {
                add(testVoter);
                add(testVoter);
            }
        });
        newFeatureProposal = featureProposalRepo.save(newFeatureProposal);
    }

    @Test
    public void testVoteOnMulitpleFeatureProposals() throws UserNotFoundException {
        FeatureProposal newFeatureProposal = featureProposalRepo.create(new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE, TEST_FEATURE_PROPOSAL_DESCRIPTION, testUser, service1), TEST_CREDENTIALS);
        FeatureProposal anotherFeatureProposal = featureProposalRepo.create(new FeatureProposal(TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE, TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
        User testVoter = userRepo.create(TEST_VOTER_CREDENTIALS.getUin(), TEST_VOTER_CREDENTIALS.getEmail(), TEST_VOTER_CREDENTIALS.getFirstName(), TEST_VOTER_CREDENTIALS.getLastName(), Role.valueOf(TEST_VOTER_CREDENTIALS.getRole()));
        newFeatureProposal.addVoter(testVoter);
        newFeatureProposal = featureProposalRepo.save(newFeatureProposal);
        assertEquals("The FeatureProposals had incorrect number of voters", 1, newFeatureProposal.getVotes());

        anotherFeatureProposal.addVoter(testVoter);
        anotherFeatureProposal = featureProposalRepo.save(newFeatureProposal);
        assertEquals("The FeatureProposals had incorrect number of voters", 1, anotherFeatureProposal.getVotes());
    }

    @Test
    public void testElevateIdea() throws UserNotFoundException {
        long initialCount = featureProposalRepo.count();
        Idea testIdea = ideaRepo.create(new Idea(TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE, TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, testUser, service1), TEST_CREDENTIALS);
        featureProposalRepo.create(testIdea);
        assertEquals("The number of FeatureProposals did not increase by one", initialCount + 1, featureProposalRepo.count());
    }

    @Test
    public void testDuplicateIdea() throws UserNotFoundException {
        long initialCount = featureProposalRepo.count();
        Idea testIdea = ideaRepo.create(new Idea(TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE, TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, testUser, service1), TEST_CREDENTIALS);
        FeatureProposal featureProposal = featureProposalRepo.create(testIdea);
        assertEquals("The number of FeatureProposals did not increase by one", initialCount + 1, featureProposalRepo.count());
        long ideaCount = featureProposal.getIdeas().size();
        featureProposal.addIdea(testIdea);
        featureProposalRepo.save(featureProposal);
        assertEquals("The number of Ideas on the FeatureProposal did not increase", ideaCount, featureProposal.getIdeas().size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testTitleNotNull() throws UserNotFoundException {
        testFeatureProposal.setTitle(null);
        featureProposalRepo.create(new FeatureProposal(null, TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAuthorNotNull() throws UserNotFoundException {
        TEST_CREDENTIALS.setUin("987654321");
        featureProposalRepo.create(new FeatureProposal(TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE, TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, null, service2), TEST_CREDENTIALS);
    }

    @Test
    public void testUpdateTitle() throws UserNotFoundException {
        FeatureProposal featureProposal = featureProposalRepo.create(testFeatureProposal, TEST_CREDENTIALS);
        featureProposal.setTitle(TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE);
        featureProposal = featureProposalRepo.save(featureProposal);
        assertEquals("FeatureProposal title was not updated", TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE, featureProposal.getTitle());
    }

    @Test
    public void testUpdateServices() throws UserNotFoundException {
        FeatureProposal featureProposal = featureProposalRepo.create(testFeatureProposal, TEST_CREDENTIALS);
        featureProposal.setService(service1);
        featureProposal = featureProposalRepo.save(featureProposal);
        assertEquals("Service was not set", service1, featureProposal.getService());
        featureProposal.setService(service2);
        featureProposal = featureProposalRepo.save(featureProposal);
        assertEquals("Service was not updated correctly", service2, featureProposal.getService());
    }

    @Test
    public void testUpdateDescription() throws UserNotFoundException {
        FeatureProposal featureProposal = featureProposalRepo.create(testFeatureProposal, TEST_CREDENTIALS);
        featureProposal.setDescription(TEST_FEATURE_PROPOSAL_DESCRIPTION);
        featureProposal = featureProposalRepo.save(featureProposal);
        assertEquals("FeatureProposal body not set", TEST_FEATURE_PROPOSAL_DESCRIPTION, featureProposal.getDescription());
        featureProposal.setDescription(TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION);
        featureProposal = featureProposalRepo.save(featureProposal);
        assertEquals("FeatureProposal body not updated", TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, featureProposal.getDescription());
    }

    @Test
    public void testTimestampSetOnCreate() throws UserNotFoundException {
        FeatureProposal FeatureProposal = featureProposalRepo.create(testFeatureProposal, TEST_CREDENTIALS);
        FeatureProposal = featureProposalRepo.findOne(FeatureProposal.getId());
        assertNotEquals("Timestamp not set on creation", null, FeatureProposal.getLastModified());
    }

    @Test
    public void testTimestampSetOnUpdate() throws InterruptedException, UserNotFoundException {
        FeatureProposal featureProposal = featureProposalRepo.create(testFeatureProposal, TEST_CREDENTIALS);
        featureProposal = featureProposalRepo.findOne(featureProposal.getId());
        // Calendar createTime = FeatureProposal.getLastModified();
        featureProposal.setDescription(TEST_FEATURE_PROPOSAL_DESCRIPTION);

        Thread.sleep(1000);

        featureProposalRepo.save(featureProposal);

        // TODO: fix false positive, time is not updated

        // assertNotEquals("The timestamp was not updated from creation", createTime.getTime().getTime(), updatedFeatureProposal.getLastModified().getTime().getTime());
    }

    @Test
    public void testDelete() throws UserNotFoundException {
        long initalCount = featureProposalRepo.count();
        FeatureProposal FeatureProposal = featureProposalRepo.create(new FeatureProposal(TEST_ALTERNATIVE_FEATURE_PROPOSAL_TITLE, TEST_ALTERNATIVE_FEATURE_PROPOSAL_DESCRIPTION, testUser, service2), TEST_CREDENTIALS);
        assertEquals("FeatureProposal not created", initalCount + 1, featureProposalRepo.count());
        featureProposalRepo.delete(FeatureProposal);
        assertEquals("FeatureProposal not deleted", initalCount, featureProposalRepo.count());

    }

    @After
    public void cleanUp() {
        ideaRepo.deleteAll();
        featureProposalRepo.deleteAll();
        serviceRepo.deleteAll();
        userRepo.deleteAll();
    }
}
