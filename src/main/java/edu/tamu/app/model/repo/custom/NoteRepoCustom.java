package edu.tamu.app.model.repo.custom;

import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.Note;

public interface NoteRepoCustom {

    public Note create(String title, AppUser author);
    
}
