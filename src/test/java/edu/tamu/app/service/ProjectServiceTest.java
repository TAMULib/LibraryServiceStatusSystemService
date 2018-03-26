package edu.tamu.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.mock.projects.MockProjects;
import edu.tamu.app.model.request.AbstractRequest;
import edu.tamu.app.model.request.ProjectRequest;
import edu.tamu.app.model.response.Project;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAll() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        ApiResponse response = projectService.getAll();
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        List<Project> projects = objectMapper.convertValue(response.getPayload().get("ArrayList<Project>"), new TypeReference<List<Project>>() {});
        List<Project> mockProjects = mockReader.getAllProjects();
        assertEquals("Projects response size was not as expected!", mockProjects.size(), projects.size());
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            assertEquals(i + " project did not have the correct id!", mockProjects.get(i).getId(), project.getId());
            assertEquals(i + " project did not have the correct name!", mockProjects.get(i).getName(), project.getName());
        }
    }

    @Test
    public void getById() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        Long id = 1L;
        ApiResponse response = projectService.getById(id);
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        Project project = objectMapper.convertValue(response.getPayload().get("Project"), Project.class);
        assertNotNull("Project is null!", project);
        Project mockProject = mockReader.getProjectById(id);
        assertEquals("Project did not have the correct id!", mockProject.getId(), project.getId());
        assertEquals("Project did not have the correct name!", mockProject.getName(), project.getName());
    }

    @Test
    public void submitFeatureRequest() {
        ProjectRequest request = new ProjectRequest(AbstractRequest.RequestType.FEATURE, "Test feature request", "This is a test feature request on project 1", 1L);
        ApiResponse response = projectService.submitRequest(request);
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        assertEquals("Response message was not correct!", "Successfully submitted " + request.getType().getName() + " request!", response.getMeta().getMessage());
    }

    @Test
    public void submitIssueRequest() {
        ProjectRequest request = new ProjectRequest(AbstractRequest.RequestType.ISSUE, "Test issue request", "This is a test issue request on project 1", 1L);
        ApiResponse response = projectService.submitRequest(request);
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        assertEquals("Response message was not correct!", "Successfully submitted " + request.getType().getName() + " request!", response.getMeta().getMessage());
    }

}
