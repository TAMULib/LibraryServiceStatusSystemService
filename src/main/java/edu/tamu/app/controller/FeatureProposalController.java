package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.enums.FeatureProposalState;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.FeatureProposalRepo;
import edu.tamu.app.model.request.FilteredPageRequest;
import edu.tamu.weaver.auth.annotation.WeaverCredentials;
import edu.tamu.weaver.auth.annotation.WeaverUser;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/feature-proposals")
public class FeatureProposalController {

    @Autowired
    private FeatureProposalRepo featureProposalRepo;

    @RequestMapping("/page")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse getAllFeatureProposalsByService(@RequestBody FilteredPageRequest filteredPageRequest) {
        return new ApiResponse(SUCCESS, featureProposalRepo.findAll(filteredPageRequest.getFeatureProposalSpecification(), filteredPageRequest.getPageRequest()));
    }

    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse getFeatureProposal(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, featureProposalRepo.findOne(id));
    }

    @RequestMapping("/create")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse create(@WeaverValidatedModel FeatureProposal featureProposal, @WeaverCredentials Credentials credentials) throws UserNotFoundException {
        return new ApiResponse(SUCCESS, featureProposalRepo.create(featureProposal, credentials));
    }

    @RequestMapping("/elevate")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse elevate(@WeaverValidatedModel Idea idea) throws UserNotFoundException {
        return new ApiResponse(SUCCESS, featureProposalRepo.create(idea));
    }

    @RequestMapping("/update")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse update(@WeaverValidatedModel FeatureProposal featureProposal) {
        return new ApiResponse(SUCCESS, featureProposalRepo.update(featureProposal));
    }

    @Transactional
    @RequestMapping("/remove")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse remove(@WeaverValidatedModel FeatureProposal featureProposal) {
        featureProposalRepo.delete(featureProposal);
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping("/{id}/vote")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse vote(@PathVariable Long id, @WeaverUser User voter) {
        FeatureProposal featureProposal = featureProposalRepo.findOne(id);
        featureProposal.addVoter(voter);
        return new ApiResponse(SUCCESS, featureProposalRepo.update(featureProposal));
    }

    @RequestMapping("/{id}/reject")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse reject(@PathVariable Long id, @WeaverUser User voter) {
        FeatureProposal featureProposal = featureProposalRepo.findOne(id);
        featureProposal.setState(FeatureProposalState.REJECTED);
        return new ApiResponse(SUCCESS, featureProposalRepo.update(featureProposal));
    }

}
