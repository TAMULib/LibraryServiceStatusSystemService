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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.enums.Status;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.FeatureProposalRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.specification.FeatureProposalSpecification;
import edu.tamu.app.model.request.FilteredPageRequest;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

@RunWith(SpringRunner.class)
public class FeatureProposalControllerTest {

    private static User TEST_USER1 = new User("123456789");
    private static User TEST_USER2 = new User("987654321");

    private static final String TEST_FEATURE_PROPOSAL_TITLE1 = "Test Feature Proposal Title 1";
    private static final String TEST_FEATURE_PROPOSAL_TITLE2 = "Test Feature Proposal Title 2";
    private static final String TEST_FEATURE_PROPOSAL_TITLE3 = "Test Feature Proposal Title 3";
    private static final String TEST_FEATURE_PROPOSAL_DESCRIPTION1 = "Test Feature Proposal Description 1";
    private static final String TEST_FEATURE_PROPOSAL_DESCRIPTION2 = "Test Feature Proposal Description 2";
    private static final String TEST_FEATURE_PROPOSAL_DESCRIPTION3 = "Test Feature Proposal Description 3";
    private static final String TEST_MODIFIED_FEATURE_PROPOSAL_TITLE = "Modified Feature Proposal Title";
    private static final String TEST_MODIFIED_FEATURE_PROPOSAL_DESCRIPTION = "Modified Feature Proposal Description";
    private static final String TEST_SERVICE_NAME = "Test Service";

    private static Service TEST_SERVICE = new Service(TEST_SERVICE_NAME, Status.UP, false, true, true, "", "");
    private static FeatureProposal TEST_FEATURE_PROPOSAL1 = new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE1, TEST_FEATURE_PROPOSAL_DESCRIPTION1, TEST_USER1);
    private static FeatureProposal TEST_FEATURE_PROPOSAL2 = new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE2, TEST_FEATURE_PROPOSAL_DESCRIPTION2, TEST_USER1);
    private static FeatureProposal TEST_FEATURE_PROPOSAL3 = new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE3, TEST_FEATURE_PROPOSAL_DESCRIPTION3, TEST_USER1);
    private static FeatureProposal TEST_MODIFIED_FEATURE_PROPOSAL = new FeatureProposal(TEST_MODIFIED_FEATURE_PROPOSAL_TITLE, TEST_MODIFIED_FEATURE_PROPOSAL_DESCRIPTION, TEST_USER2, TEST_SERVICE);
    private static List<FeatureProposal> mockFeatureProposalList = new ArrayList<FeatureProposal>(Arrays.asList(new FeatureProposal[] { TEST_FEATURE_PROPOSAL1, TEST_FEATURE_PROPOSAL2, TEST_FEATURE_PROPOSAL3 }));
    private static Page<FeatureProposal> mockPageableFeatureProposalList = new PageImpl<FeatureProposal>(Arrays.asList(new FeatureProposal[] { TEST_FEATURE_PROPOSAL1, TEST_FEATURE_PROPOSAL2, TEST_FEATURE_PROPOSAL3 }));

    private static User user = new User("123456789");

    private static ApiResponse response;

    @Mock
    private UserRepo userRepo;

    @Mock
    private FeatureProposalRepo featureProposalRepo;

    @Mock
    private ServiceRepo serviceRepo;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private Credentials credentials;

    @InjectMocks
    private FeatureProposalController featureProposalController;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() throws UserNotFoundException {
        MockitoAnnotations.initMocks(this);
        when(credentials.getUin()).thenReturn("123456789");
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(featureProposalRepo.findAll()).thenReturn(mockFeatureProposalList);
        when(featureProposalRepo.findAll(any(FeatureProposalSpecification.class), any(Pageable.class))).thenReturn(mockPageableFeatureProposalList);
        when(featureProposalRepo.findOne(any(Long.class))).thenReturn(TEST_FEATURE_PROPOSAL1);
        when(featureProposalRepo.create(any(FeatureProposal.class), any(Credentials.class))).thenReturn(TEST_FEATURE_PROPOSAL1);
        when(featureProposalRepo.create(any(Idea.class))).thenReturn(TEST_FEATURE_PROPOSAL1);
        when(featureProposalRepo.update(any(FeatureProposal.class))).thenReturn(TEST_MODIFIED_FEATURE_PROPOSAL);
        when(serviceRepo.findOne(any(Long.class))).thenReturn(TEST_SERVICE);
        doNothing().when(featureProposalRepo).delete(any(FeatureProposal.class));
        doNothing().when(featureProposalRepo).delete(any(FeatureProposal.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPage() {
        FilteredPageRequest mockFilter = new FilteredPageRequest();
        response = featureProposalController.getAllFeatureProposalsByService(mockFilter);
        assertEquals("Not successful at getting paged FeatureProposals", SUCCESS, response.getMeta().getStatus());

        Page<FeatureProposal> page = (Page<FeatureProposal>) response.getPayload().get("PageImpl");
        assertEquals("The paged list of FeatureProposals is the wrong length", mockPageableFeatureProposalList.getSize(), page.getSize());
    }

    @Test
    public void testFeatureProposal() {
        response = featureProposalController.getFeatureProposal(TEST_FEATURE_PROPOSAL1.getId());
        assertEquals("Not successful at getting requested FeatureProposal", SUCCESS, response.getMeta().getStatus());
        FeatureProposal featureProposal = (FeatureProposal) response.getPayload().get("FeatureProposal");
        assertEquals("Did not get the expected service", TEST_FEATURE_PROPOSAL1.getId(), featureProposal.getId());
    }

    @Test
    public void testCreate() throws UserNotFoundException {
        response = featureProposalController.create(TEST_FEATURE_PROPOSAL1, credentials);
        assertEquals("Not sucessful at creating FeatureProposal", SUCCESS, response.getMeta().getStatus());
    }

    @Test
    public void testElevate() throws UserNotFoundException {
        Idea idea = new Idea(TEST_FEATURE_PROPOSAL_TITLE1, TEST_FEATURE_PROPOSAL_DESCRIPTION1, TEST_USER1, TEST_SERVICE);
        response = featureProposalController.elevate(idea);
        assertEquals("Not sucessful at elevating Idea to FeatureProposal", SUCCESS, response.getMeta().getStatus());
    }

    @Test
    public void testUpdate() {
        response = featureProposalController.update(TEST_MODIFIED_FEATURE_PROPOSAL);
        assertEquals("Not successful at updating featureProposal", SUCCESS, response.getMeta().getStatus());
        FeatureProposal featureProposal = (FeatureProposal) response.getPayload().get("FeatureProposal");
        assertEquals("Notification Title was not properly updated", TEST_MODIFIED_FEATURE_PROPOSAL.getTitle(), featureProposal.getTitle());
        assertEquals("Notification Author was not properly updated", TEST_MODIFIED_FEATURE_PROPOSAL.getAuthor(), featureProposal.getAuthor());
    }

    @Test
    public void testRemove() {
        response = featureProposalController.remove(TEST_MODIFIED_FEATURE_PROPOSAL);
        assertEquals("Not successful at removing FeatureProposal", SUCCESS, response.getMeta().getStatus());
    }

    @Test
    public void testVote() {
        response = featureProposalController.vote(1L, TEST_USER1);
        assertEquals("Not successful at removing FeatureProposal", SUCCESS, response.getMeta().getStatus());
    }

}
