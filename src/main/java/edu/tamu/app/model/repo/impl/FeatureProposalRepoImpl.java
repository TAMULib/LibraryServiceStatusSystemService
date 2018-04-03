package edu.tamu.app.model.repo.impl;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.FeatureProposalRepo;
import edu.tamu.app.model.repo.IdeaRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.custom.FeatureProposalRepoCustom;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

public class FeatureProposalRepoImpl implements FeatureProposalRepoCustom {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private FeatureProposalRepo featureProposalRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public FeatureProposal create(FeatureProposal featureProposal, Credentials credentials) throws UserNotFoundException {
        Optional<User> user = userRepo.findByUsername(credentials.getUin());
        if (user.isPresent()) {
            featureProposal.setAuthor(user.get());
            featureProposal.getIdeas().forEach(idea -> {
                idea.setElevated(true);
                idea = ideaRepo.save(idea);
                simpMessagingTemplate.convertAndSend("/channel/ideas/update", new ApiResponse(SUCCESS, idea));
            });
            featureProposal = featureProposalRepo.save(featureProposal);
            simpMessagingTemplate.convertAndSend("/channel/feature-proposals/create", new ApiResponse(SUCCESS, featureProposal));
            return featureProposal;
        }
        throw new UserNotFoundException("Unable to find user with uin " + credentials.getUin());
    }

    @Override
    public FeatureProposal create(Idea idea) {
        idea.setElevated(true);
        idea = ideaRepo.save(idea);
        simpMessagingTemplate.convertAndSend("/channel/ideas/update", new ApiResponse(SUCCESS, idea));
        FeatureProposal featureProposal = featureProposalRepo.save(new FeatureProposal(idea));
        simpMessagingTemplate.convertAndSend("/channel/feature-proposals/create", new ApiResponse(SUCCESS, featureProposal));
        return featureProposal;
    }

    @Override
    public FeatureProposal update(FeatureProposal featureProposal) {
        featureProposal.getIdeas().forEach(idea -> {
            idea.setElevated(true);
            idea = ideaRepo.save(idea);
            simpMessagingTemplate.convertAndSend("/channel/ideas/update", new ApiResponse(SUCCESS, idea));
        });
        featureProposal = featureProposalRepo.save(featureProposal);
        simpMessagingTemplate.convertAndSend("/channel/feature-proposals/update", new ApiResponse(SUCCESS, featureProposal));
        return featureProposal;
    }

    @Override
    public void delete(FeatureProposal featureProposal) {
        featureProposal.getIdeas().forEach(idea -> {
            idea.setElevated(false);
            idea = ideaRepo.save(idea);
            simpMessagingTemplate.convertAndSend("/channel/ideas/update", new ApiResponse(SUCCESS, idea));
        });
        featureProposalRepo.delete(featureProposal.getId());
        simpMessagingTemplate.convertAndSend("/channel/feature-proposals/delete", new ApiResponse(SUCCESS, featureProposal.getId()));
    }

}
