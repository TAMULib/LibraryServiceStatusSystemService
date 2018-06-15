package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.model.response.Project;
import edu.tamu.app.service.ProjectService;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@RunWith(SpringRunner.class)
public class ProjectControllerTest {

    private static List<Project> projects = new ArrayList<Project>();

    @Value("classpath:mock/projects.json")
    private Resource resource;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @Before
    public void setup() throws JsonParseException, JsonMappingException, IOException {
        MockitoAnnotations.initMocks(this);
        projects = objectMapper.readValue(resource.getFile(), new TypeReference<List<Project>>() {});
        when(projectService.getAll()).thenReturn(new ApiResponse(SUCCESS, projects));
        when(projectService.getById(any(Long.class))).thenReturn(new ApiResponse(SUCCESS, projects.get(0)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getAll() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        ApiResponse response = projectController.getAll();
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        List<Project> projects = (List<Project>) response.getPayload().get("ArrayList<Project>");
        assertEquals("Projects response size was not as expected!", projects.size(), projects.size());
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            assertEquals(i + " project did not have the correct id!", projects.get(i).getId(), project.getId());
            assertEquals(i + " project did not have the correct name!", projects.get(i).getName(), project.getName());
        }
    }

    @Test
    public void getById() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        ApiResponse response = projectController.getById(1L);
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        Project project = (Project) response.getPayload().get("Project");
        assertNotNull("Project is null!", project);
        assertEquals("Project did not have the correct id!", projects.get(0).getId(), project.getId());
        assertEquals("Project did not have the correct name!", projects.get(0).getName(), project.getName());
    }

}
