package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.UPDATE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.IdeaRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.request.FilteredPageRequest;
import edu.tamu.app.model.request.IssueRequest;
import edu.tamu.app.model.request.ServiceRequest;
import edu.tamu.app.service.ProjectService;
import edu.tamu.weaver.auth.annotation.WeaverCredentials;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private ProjectService projectService;

    @RequestMapping
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse getAllServices() {
        return new ApiResponse(SUCCESS, serviceRepo.findAllByOrderByStatusDescNameAsc());
    }

    @RequestMapping("/public")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse getPublicServices() {
        return new ApiResponse(SUCCESS, serviceRepo.findByIsPublicOrderByStatusDescNameAsc(true));
    }

    @RequestMapping("/page")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse page(@RequestBody FilteredPageRequest filteredPageRequest) {
        return new ApiResponse(SUCCESS, serviceRepo.findAll(filteredPageRequest.getServiceSpecification(), filteredPageRequest.getPageRequest()));
    }

    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse getService(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, serviceRepo.findOne(id));
    }

    @RequestMapping("/create")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse createService(@WeaverValidatedModel Service service, @WeaverCredentials Credentials credentials) {
        return new ApiResponse(SUCCESS, serviceRepo.create(service));
    }

    @RequestMapping("/update")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
    public ApiResponse updateService(@WeaverValidatedModel Service service, @WeaverCredentials Credentials credentials) {
        return new ApiResponse(SUCCESS, serviceRepo.update(service));
    }

    @RequestMapping("/remove")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
    public ApiResponse removeService(@WeaverValidatedModel Service service) {
        serviceRepo.delete(service);
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping("/issue")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse submitIssueRequest(@RequestBody ServiceRequest request, @WeaverCredentials Credentials credentials) {
        IssueRequest issueRequest = new IssueRequest(request);
        Service service = serviceRepo.findOne(request.getService());
        issueRequest.setService(service.getName());
        issueRequest.setCredentials(credentials);
        return projectService.submitIssueRequest(issueRequest);
    }

    @RequestMapping("/feature")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse submitFeatureRequest(@RequestBody ServiceRequest request, @WeaverCredentials Credentials credentials) throws UserNotFoundException {
        Service service = serviceRepo.findOne(request.getService());
        Idea idea = new Idea(request);
        idea.setService(service);
        ideaRepo.create(idea, credentials);
        return new ApiResponse(ApiStatus.SUCCESS, "Your feature request for " + service.getName() + " has been submitted as an idea!");
    }

}
