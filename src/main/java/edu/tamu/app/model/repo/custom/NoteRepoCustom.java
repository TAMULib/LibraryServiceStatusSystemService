package edu.tamu.app.model.repo.custom;

import java.util.List;

import edu.tamu.app.enums.NoteType;
import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.Note;
import edu.tamu.app.model.Service;

public interface NoteRepoCustom {

    public Note create(String title, AppUser author);
    
    public Note create(String title, AppUser author, NoteType noteType, String body, List<Service> services);
    
}
