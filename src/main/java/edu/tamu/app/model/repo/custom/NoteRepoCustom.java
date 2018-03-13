package edu.tamu.app.model.repo.custom;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Note;
import edu.tamu.weaver.auth.model.Credentials;

public interface NoteRepoCustom {

    public Note create(Note note, Credentials credentials) throws UserNotFoundException;

    public Note update(Note note);

    public void delete(Note note);

}
