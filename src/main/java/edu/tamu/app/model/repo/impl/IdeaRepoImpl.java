package edu.tamu.app.model.repo.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.IdeaRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.custom.IdeaRepoCustom;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.data.model.repo.impl.AbstractWeaverRepoImpl;

public class IdeaRepoImpl extends AbstractWeaverRepoImpl<Idea, IdeaRepo> implements IdeaRepoCustom {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    @Override
    public Idea create(Idea idea, Credentials credentials) throws UserNotFoundException {
        Optional<User> user = userRepo.findByUsername(credentials.getUin());
        if (user.isPresent()) {
            idea.setAuthor(user.get());
            return ideaRepo.create(idea);
        }
        throw new UserNotFoundException("Unable to find user with uin " + credentials.getUin());
    }

    @Override
    protected String getChannel() {
        return "/channel/ideas";
    }

}
