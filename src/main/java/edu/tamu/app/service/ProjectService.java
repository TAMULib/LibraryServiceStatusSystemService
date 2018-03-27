package edu.tamu.app.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.model.request.FeatureRequest;
import edu.tamu.app.model.request.IssueRequest;
import edu.tamu.weaver.response.ApiResponse;;

@Service
public class ProjectService {

    @Value("${app.projects.url}")
    private String projectsUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    public ApiResponse getAll() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        return objectMapper.readValue(new URL(projectsUrl), ApiResponse.class);
    }

    public ApiResponse getById(Long id) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        return objectMapper.readValue(new URL(projectsUrl + "/" + id), ApiResponse.class);
    }

    public ApiResponse submitFeatureRequest(FeatureRequest request) {
        return restTemplate.postForObject(projectsUrl + "/feature", request, ApiResponse.class);
    }

    public ApiResponse submitIssueRequest(IssueRequest request) {
        return restTemplate.postForObject(projectsUrl + "/issue", request, ApiResponse.class);

    }

}
