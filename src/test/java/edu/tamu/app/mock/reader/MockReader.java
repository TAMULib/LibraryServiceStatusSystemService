package edu.tamu.app.mock.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MockReader {

    private final static List<JsonNode> PROJECTS = new ArrayList<JsonNode>();

    @Value("classpath:mock/projects.json")
    private Resource resource;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void loadProjects() throws JsonProcessingException, IOException {
        JsonNode projectsNode = objectMapper.readTree(resource.getFile());
        Iterator<JsonNode> projectNodesIterator = projectsNode.elements();
        while (projectNodesIterator.hasNext()) {
            PROJECTS.add(projectNodesIterator.next());
        }
    }

    public List<JsonNode> getAllProjects() {
        return PROJECTS;
    }

    public JsonNode getProjectById(Long id) {
        JsonNode project = null;
        for (JsonNode currentProject : PROJECTS) {
            Optional<Long> currentId = Optional.ofNullable(Long.valueOf(currentProject.get("id").asLong()));
            if (currentId.isPresent()) {
                if (currentId.get().equals(id)) {
                    project = currentProject;
                    break;
                }
            }
        }
        return project;
    }

}
