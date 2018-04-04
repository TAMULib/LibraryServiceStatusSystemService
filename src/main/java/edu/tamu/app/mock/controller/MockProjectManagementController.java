package edu.tamu.app.mock.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.mock.projects.MockProjects;
import edu.tamu.app.model.request.FeatureRequest;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@Profile("test")
@RequestMapping("/mock/projects")
public class MockProjectManagementController {

    @Autowired
    private MockProjects mockProjects;

    @RequestMapping
    public ApiResponse getAll() {
        return new ApiResponse(SUCCESS, mockProjects.getAllProjects());
    }

    @RequestMapping("/{id}")
    public ApiResponse getById(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, mockProjects.getProjectById(id));
    }

    @RequestMapping("/issue")
    public ApiResponse submitIssue(@RequestBody FeatureRequest request) {
        return new ApiResponse(SUCCESS, mockProjects.submitRequest(request));
    }

    @RequestMapping("/feature")
    public ApiResponse submitFeature(@RequestBody FeatureRequest request) {
        return new ApiResponse(SUCCESS, mockProjects.submitRequest(request));
    }

}
