package edu.tamu.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.mock.reader.MockProjects;
import edu.tamu.app.model.request.ProjectRequest;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WebServerInit.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class ProjectServiceTest {

    @Autowired
    private MockProjects mockReader;

    @Autowired
    private ProjectService projectService;

    @Test
    @SuppressWarnings("unchecked")
    public void getAll() throws JsonProcessingException, IOException {
        ApiResponse response = projectService.getAll();
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        List<JsonNode> projects = (List<JsonNode>) response.getPayload().get("ArrayList<ObjectNode>");
        List<JsonNode> mockProjects = mockReader.getAllProjects();
        assertEquals("Projects response size was not as expected!", mockProjects.size(), projects.size());
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < projects.size(); i++) {
            JsonNode project = mapper.valueToTree(projects.get(i));
            assertEquals(i + " project did not have the correct id!", mockProjects.get(i).get("id"), project.get("id"));
            assertEquals(i + " project did not have the correct name!", mockProjects.get(i).get("name"), project.get("name"));
        }

    }

    @Test
    public void getById() throws JsonProcessingException, IOException {
        Long id = 1L;
        ApiResponse response = projectService.getById(id);
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode project = mapper.valueToTree(response.getPayload().get("ObjectNode"));
        assertNotNull("Project is null!", project);
        JsonNode mockProject = mockReader.getProjectById(id);
        assertEquals("Project did not have the correct id!", mockProject.get("id"), project.get("id"));
        assertEquals("Project did not have the correct name!", mockProject.get("name"), project.get("name"));
    }

    @Test
    public void submitFeatureRequest() {
        ProjectRequest request = new ProjectRequest(ProjectRequest.RequestType.FEATURE, "Test feature request", "This is a test feature request on project 1", 1L);
        ApiResponse response = projectService.submitRequest(request);
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        assertEquals("Response message was not correct!", "Successfully submitted " + request.getType().getName() + " request!", response.getMeta().getMessage());
    }

    @Test
    public void submitIssueRequest() {
        ProjectRequest request = new ProjectRequest(ProjectRequest.RequestType.ISSUE, "Test issue request", "This is a test issue request on project 1", 1L);
        ApiResponse response = projectService.submitRequest(request);
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        assertEquals("Response message was not correct!", "Successfully submitted " + request.getType().getName() + " request!", response.getMeta().getMessage());
    }

}
