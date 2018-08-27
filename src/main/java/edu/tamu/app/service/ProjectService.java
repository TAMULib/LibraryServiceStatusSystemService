package edu.tamu.app.service;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.enums.FeatureProposalState;
import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.model.repo.FeatureProposalRepo;
import edu.tamu.app.model.request.FeatureRequest;
import edu.tamu.app.model.request.IssueRequest;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;;

@Service
public class ProjectService {

    @Value("${app.projects.url}")
    private String projectsUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FeatureProposalRepo featureProposalRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public ApiResponse getAll() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        return objectMapper.readValue(new URL(projectsUrl), ApiResponse.class);
    }

    public ApiResponse getById(Long id) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        return objectMapper.readValue(new URL(projectsUrl + "/" + id), ApiResponse.class);
    }

    public ApiResponse submitFeatureRequest(FeatureProposal proposal) {
        ApiResponse response = restTemplate.postForObject(projectsUrl + "/feature", new FeatureRequest(proposal), ApiResponse.class);
        if (response.getMeta().getStatus().equals(ApiStatus.SUCCESS)) {
            proposal.setState(FeatureProposalState.SUBMITTED);
            proposal = featureProposalRepo.save(proposal);
            simpMessagingTemplate.convertAndSend("/channel/feature-proposals/update", new ApiResponse(SUCCESS, proposal));
        }
        return response;
    }

    public ApiResponse submitIssueRequest(IssueRequest request) {
        return restTemplate.postForObject(projectsUrl + "/issue", request, ApiResponse.class);

    }

}
