package edu.tamu.app.model.repo.custom;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Idea;
import edu.tamu.weaver.auth.model.Credentials;

public interface IdeaRepoCustom {

    public Idea create(Idea idea, Credentials credentials) throws UserNotFoundException;

    public Idea update(Idea idea);

    public void delete(Idea idea);

    public Idea reject(Idea idea);

}
