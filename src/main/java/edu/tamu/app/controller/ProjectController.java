package edu.tamu.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.tamu.app.model.request.ProjectRequest;
import edu.tamu.app.service.ProjectService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse getAll() throws JsonProcessingException, IOException {
        return projectService.getAll();
    }

    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse getById(@PathVariable Long id) throws JsonProcessingException, IOException {
        return projectService.getById(id);
    }

    @RequestMapping("/issue")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse submitIssueRequest(@RequestBody ProjectRequest request) {
        return projectService.submitRequest(request);
    }

    @RequestMapping("/feature")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse submitFeatureRequest(@RequestBody ProjectRequest request) {
        return projectService.submitRequest(request);
    }

}
