package edu.tamu.app.model.repo.custom;

import edu.tamu.app.model.Note;
import edu.tamu.framework.model.Credentials;

public interface NoteRepoCustom {

    public Note create(Note note, Credentials credentials);

    public Note update(Note note);

    public void delete(Note note);

}
