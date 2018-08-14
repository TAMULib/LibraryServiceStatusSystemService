package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.INVALID;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.enums.IdeaState;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.repo.IdeaRepo;
import edu.tamu.app.model.request.FilteredPageRequest;
import edu.tamu.app.model.request.IssueRequest;
import edu.tamu.app.service.ProjectService;
import edu.tamu.weaver.auth.annotation.WeaverCredentials;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/ideas")
public class IdeaController {

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/page")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse page(@RequestBody FilteredPageRequest filteredPageRequest) {
        return new ApiResponse(SUCCESS, ideaRepo.findAll(filteredPageRequest.getIdeaSpecification(), filteredPageRequest.getPageRequest()));
    }

    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse getById(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, ideaRepo.findOne(id));
    }

    @RequestMapping("/create")
    @PreAuthorize("hasRole('ANONYMOUS')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse create(@WeaverValidatedModel Idea idea, @WeaverCredentials Credentials credentials) throws UserNotFoundException {
        return new ApiResponse(SUCCESS, ideaRepo.create(idea, credentials));
    }

    @RequestMapping("/update")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse update(@WeaverValidatedModel Idea idea) {
        return new ApiResponse(SUCCESS, ideaRepo.update(idea));
    }
    
    @RequestMapping("/reject")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse reject(@WeaverValidatedModel Idea idea) {
        ApiResponse response;
        if (idea.getFeedback() == null || idea.getFeedback().equals("")) {
            response = new ApiResponse(INVALID, "You must provide feedback to reject an idea.");
        } else {
            response = new ApiResponse(SUCCESS, ideaRepo.reject(idea));
        }
        return response;
    }

    @RequestMapping("/helpdesk")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse helpdesk(@WeaverValidatedModel Idea idea, @WeaverCredentials Credentials credentials) {
        idea.setState(IdeaState.SENT_TO_HELPDESK);
        idea = ideaRepo.update(idea);
        IssueRequest request = new IssueRequest(idea, credentials);
        return projectService.submitIssueRequest(request);
    }

    @Transactional
    @RequestMapping("/remove")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse remove(@WeaverValidatedModel Idea idea) {
        ideaRepo.delete(idea);
        return new ApiResponse(SUCCESS);
    }

}
