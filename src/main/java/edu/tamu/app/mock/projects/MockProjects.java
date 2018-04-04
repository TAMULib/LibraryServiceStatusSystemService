package edu.tamu.app.mock.projects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.model.request.FeatureRequest;
import edu.tamu.app.model.response.Project;

@Service
@Profile("test")
public class MockProjects {

    private static List<Project> projects = new ArrayList<Project>();

    @Value("classpath:mock/projects.json")
    private Resource resource;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void loadProjects() throws JsonParseException, JsonMappingException, IOException {
        projects = objectMapper.readValue(resource.getFile(), new TypeReference<List<Project>>() {});
    }

    public List<Project> getAllProjects() {
        return projects;
    }

    public Project getProjectById(Long id) {
        Project project = null;
        for (Project currentProject : projects) {
            Optional<Long> currentId = Optional.ofNullable(Long.valueOf(currentProject.getId()));
            if (currentId.isPresent()) {
                if (currentId.get().equals(id)) {
                    project = currentProject;
                    break;
                }
            }
        }
        return project;
    }

    public String submitRequest(FeatureRequest request) {
        return "Successfully submitted " + request.getType().getName() + " request!";
    }

}
