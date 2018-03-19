package edu.tamu.app.mock.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.mock.reader.MockReader;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("/mock/projects")
public class MockProjectManagementController {

    @Autowired
    private MockReader mockReader;

    @RequestMapping("/all")
    public ApiResponse getAll() {
        return new ApiResponse(SUCCESS, mockReader.getAllProjects());
    }

    @RequestMapping("/{id}")
    public ApiResponse getById(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, mockReader.getProjectById(id));
    }

}
