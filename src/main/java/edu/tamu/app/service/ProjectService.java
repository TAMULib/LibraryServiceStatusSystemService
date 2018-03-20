package edu.tamu.app.service;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.model.request.ProjectRequest;
import edu.tamu.weaver.response.ApiResponse;;

@Service
public class ProjectService {

    @Value("${app.projects.url}")
    private String projectsUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    public ApiResponse getAll() throws JsonProcessingException, IOException {
        JsonNode projectsNode = objectMapper.readTree(new URL(projectsUrl));
        return objectMapper.convertValue(projectsNode, ApiResponse.class);
    }

    public ApiResponse getById(Long id) throws JsonProcessingException, IOException {
        JsonNode projectsNode = objectMapper.readTree(new URL(projectsUrl + "/" + id));
        return objectMapper.convertValue(projectsNode, ApiResponse.class);
    }

    public ApiResponse submitRequest(ProjectRequest request) {
        return restTemplate.postForObject(projectsUrl + "/" + request.getType().getName(), request, ApiResponse.class);
    }

}
