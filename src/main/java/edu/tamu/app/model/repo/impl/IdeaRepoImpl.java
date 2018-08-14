package edu.tamu.app.model.repo.impl;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.enums.IdeaState;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.IdeaRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.custom.IdeaRepoCustom;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.email.service.EmailSender;
import edu.tamu.weaver.response.ApiResponse;

public class IdeaRepoImpl implements IdeaRepoCustom {

    private static final String REJECTION_SUBJECT = "Your idea has been rejected.";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private EmailSender emailService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Idea create(Idea idea, Credentials credentials) throws UserNotFoundException {
        Optional<User> user = userRepo.findByUsername(credentials.getUin());
        if (user.isPresent()) {
            idea.setAuthor(user.get());
            idea = ideaRepo.save(idea);
            simpMessagingTemplate.convertAndSend("/channel/ideas/create", new ApiResponse(SUCCESS, idea));
            return idea;
        }
        throw new UserNotFoundException("Unable to find user with uin " + credentials.getUin());
    }

    @Override
    public Idea update(Idea idea) {
        idea = ideaRepo.save(idea);
        simpMessagingTemplate.convertAndSend("/channel/ideas/update", new ApiResponse(SUCCESS, idea));
        return idea;
    }

    @Override
    public void delete(Idea idea) {
        ideaRepo.delete(idea.getId());
        simpMessagingTemplate.convertAndSend("/channel/ideas/delete", new ApiResponse(SUCCESS, idea.getId()));
    }

    public Idea reject(Idea idea) {
        idea.setState(IdeaState.REJECTED);
        String body = "Your idea " + idea.getTitle() + ", for " + idea.getService().getName() + ", has been rejected for the following reason:\n" + idea.getFeedback();
        try {
            if (idea.getEmail() != null && !idea.getEmail().isEmpty()) {
                emailService.sendEmail(idea.getEmail(), REJECTION_SUBJECT, body);
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ideaRepo.update(idea);
    }
}
