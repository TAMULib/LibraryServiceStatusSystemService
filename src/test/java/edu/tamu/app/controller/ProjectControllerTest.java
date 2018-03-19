package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.service.ProjectService;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ProjectControllerTest {

    private final static List<JsonNode> PROJECTS = new ArrayList<JsonNode>();

    @Value("classpath:mock/projects.json")
    private Resource resource;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @Before
    public void setup() throws JsonProcessingException, IOException {
        MockitoAnnotations.initMocks(this);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode projectsNode = mapper.readTree(resource.getFile());
        Iterator<JsonNode> projectNodesIterator = projectsNode.elements();
        while (projectNodesIterator.hasNext()) {
            PROJECTS.add(projectNodesIterator.next());
        }
        when(projectService.getAll()).thenReturn(new ApiResponse(SUCCESS, PROJECTS));
        when(projectService.getById(any(Long.class))).thenReturn(new ApiResponse(SUCCESS, PROJECTS.get(0)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getAll() throws JsonProcessingException, IOException {
        ApiResponse response = projectController.getAll();
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        List<JsonNode> projects = (List<JsonNode>) response.getPayload().get("ArrayList<ObjectNode>");
        assertEquals("Projects response size was not as expected!", PROJECTS.size(), projects.size());
        for (int i = 0; i < projects.size(); i++) {
            JsonNode project = projects.get(i);
            assertEquals(i + " project did not have the correct id!", PROJECTS.get(i).get("id"), project.get("id"));
            assertEquals(i + " project did not have the correct name!", PROJECTS.get(i).get("name"), project.get("name"));
        }
    }

    @Test
    public void getById() throws JsonProcessingException, IOException {
        ApiResponse response = projectController.getById(1L);
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        JsonNode project = (JsonNode) response.getPayload().get("ObjectNode");
        assertNotNull("Project is null!", project);
        assertEquals("Project did not have the correct id!", PROJECTS.get(0).get("id"), project.get("id"));
        assertEquals("Project did not have the correct name!", PROJECTS.get(0).get("name"), project.get("name"));
    }

}
