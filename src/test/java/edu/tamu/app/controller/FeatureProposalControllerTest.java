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

import edu.tamu.app.enums.FeatureProposalState;
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

@ExtendWith(SpringExtension.class)
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
    private static final String TEST_FEEDBACK = "Test Rejection Feedback";

    private static Service TEST_SERVICE = new Service(TEST_SERVICE_NAME, Status.UP, false, true, true, "", "");
    private static FeatureProposal TEST_FEATURE_PROPOSAL1 = new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE1, TEST_FEATURE_PROPOSAL_DESCRIPTION1, TEST_USER1);
    private static FeatureProposal TEST_FEATURE_PROPOSAL2 = new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE2, TEST_FEATURE_PROPOSAL_DESCRIPTION2, TEST_USER1);
    private static FeatureProposal TEST_FEATURE_PROPOSAL3 = new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE3, TEST_FEATURE_PROPOSAL_DESCRIPTION3, TEST_USER1);
    private static FeatureProposal TEST_MODIFIED_FEATURE_PROPOSAL = new FeatureProposal(TEST_MODIFIED_FEATURE_PROPOSAL_TITLE, TEST_MODIFIED_FEATURE_PROPOSAL_DESCRIPTION, TEST_USER2, TEST_SERVICE);
    private static FeatureProposal featureProposalWithFeedback = new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE1, TEST_FEATURE_PROPOSAL_DESCRIPTION1, TEST_USER1);
    private static List<FeatureProposal> mockFeatureProposalList = new ArrayList<FeatureProposal>(Arrays.asList(new FeatureProposal[] { TEST_FEATURE_PROPOSAL1, TEST_FEATURE_PROPOSAL2, TEST_FEATURE_PROPOSAL3 }));
    private static Page<FeatureProposal> mockPageableFeatureProposalList = new PageImpl<FeatureProposal>(Arrays.asList(new FeatureProposal[] { TEST_FEATURE_PROPOSAL1, TEST_FEATURE_PROPOSAL2, TEST_FEATURE_PROPOSAL3 }));

    private FeatureProposal rejectedFeatureProposal = new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE1, TEST_FEATURE_PROPOSAL_DESCRIPTION1, TEST_USER1);

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

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() throws UserNotFoundException {
        rejectedFeatureProposal.setState(FeatureProposalState.REJECTED);
        featureProposalWithFeedback.setFeedback(TEST_FEEDBACK);
        MockitoAnnotations.openMocks(this);
        when(credentials.getUin()).thenReturn("123456789");
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(featureProposalRepo.findAll()).thenReturn(mockFeatureProposalList);
        when(featureProposalRepo.findAll(any(FeatureProposalSpecification.class), any(Pageable.class))).thenReturn(mockPageableFeatureProposalList);
        when(featureProposalRepo.getById(any(Long.class))).thenReturn(TEST_FEATURE_PROPOSAL1);
        when(featureProposalRepo.create(any(FeatureProposal.class), any(Credentials.class))).thenReturn(TEST_FEATURE_PROPOSAL1);
        when(featureProposalRepo.create(any(Idea.class))).thenReturn(TEST_FEATURE_PROPOSAL1);
        when(featureProposalRepo.update(any(FeatureProposal.class))).thenReturn(TEST_MODIFIED_FEATURE_PROPOSAL);
        when(featureProposalRepo.reject(featureProposalWithFeedback)).thenReturn(rejectedFeatureProposal);
        when(serviceRepo.getById(any(Long.class))).thenReturn(TEST_SERVICE);
        doNothing().when(featureProposalRepo).delete(any(FeatureProposal.class));
        doNothing().when(featureProposalRepo).delete(any(FeatureProposal.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPage() {
        FilteredPageRequest mockFilter = new FilteredPageRequest();
        response = featureProposalController.getAllFeatureProposalsByService(mockFilter);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting paged FeatureProposals");

        Page<FeatureProposal> page = (Page<FeatureProposal>) response.getPayload().get("PageImpl");
        assertEquals(mockPageableFeatureProposalList.getSize(), page.getSize(), "The paged list of FeatureProposals is the wrong length");
    }

    @Test
    public void testFeatureProposal() {
        TEST_FEATURE_PROPOSAL1.setId(1L);
        response = featureProposalController.getFeatureProposal(TEST_FEATURE_PROPOSAL1.getId());
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at getting requested FeatureProposal");
        FeatureProposal featureProposal = (FeatureProposal) response.getPayload().get("FeatureProposal");
        assertEquals(TEST_FEATURE_PROPOSAL1.getId(), featureProposal.getId(), "Did not get the expected service");
    }

    @Test
    public void testCreate() throws UserNotFoundException {
        response = featureProposalController.create(TEST_FEATURE_PROPOSAL1, credentials);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not sucessful at creating FeatureProposal");
    }

    @Test
    public void testElevate() throws UserNotFoundException {
        Idea idea = new Idea(TEST_FEATURE_PROPOSAL_TITLE1, TEST_FEATURE_PROPOSAL_DESCRIPTION1, TEST_USER1, TEST_SERVICE);
        response = featureProposalController.elevate(idea);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not sucessful at elevating Idea to FeatureProposal");
    }

    @Test
    public void testUpdate() {
        response = featureProposalController.update(TEST_MODIFIED_FEATURE_PROPOSAL);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at updating featureProposal");
        FeatureProposal featureProposal = (FeatureProposal) response.getPayload().get("FeatureProposal");
        assertEquals(TEST_MODIFIED_FEATURE_PROPOSAL.getTitle(), featureProposal.getTitle(), "Notification Title was not properly updated");
        assertEquals(TEST_MODIFIED_FEATURE_PROPOSAL.getAuthor(), featureProposal.getAuthor(), "Notification Author was not properly updated");
    }

    @Test
    public void testReject() {
        response = featureProposalController.reject(featureProposalWithFeedback);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at rejecting feature proposal");
        FeatureProposal featureProposal = (FeatureProposal) response.getPayload().get("FeatureProposal");
        assertEquals(rejectedFeatureProposal.getState(), featureProposal.getState(), "State was not set to Rejected");
    }
    
    @Test
    public void testInvalidReject() {
        response = featureProposalController.reject(TEST_FEATURE_PROPOSAL1);
        assertEquals(INVALID, response.getMeta().getStatus(), "Feature proposal without feedback was not rejected");
    }

    @Test
    public void testRemove() {
        response = featureProposalController.remove(TEST_MODIFIED_FEATURE_PROPOSAL);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at removing FeatureProposal");
    }

    @Test
    public void testVote() {
        response = featureProposalController.vote(1L, TEST_USER1);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Not successful at removing FeatureProposal");
    }

}
