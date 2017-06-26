package edu.tamu.app.model.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.Note;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.custom.NoteRepoCustom;

public class NoteRepoImpl implements NoteRepoCustom {
    
    @Autowired
    NoteRepo noteRepo;
    
    @Override
    public Note create(String title, AppUser author) {
        return noteRepo.save(new Note(title, author));
    }

}
