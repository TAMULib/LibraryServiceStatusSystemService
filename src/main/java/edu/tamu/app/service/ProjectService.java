package edu.tamu.app.service;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.weaver.response.ApiResponse;;

@Service
public class ProjectService {

    @Value("${app.projects.url}")
    private String projectsUrl;

    @Autowired
    private ObjectMapper objectMapper;

    public ApiResponse getAll() throws JsonProcessingException, IOException {
        JsonNode projectsNode = objectMapper.readTree(new URL(projectsUrl + "/all"));
        return objectMapper.convertValue(projectsNode, ApiResponse.class);
    }

    public ApiResponse getById(Long id) throws JsonProcessingException, IOException {
        JsonNode projectsNode = objectMapper.readTree(new URL(projectsUrl + "/" + id));
        return objectMapper.convertValue(projectsNode, ApiResponse.class);
    }

}
