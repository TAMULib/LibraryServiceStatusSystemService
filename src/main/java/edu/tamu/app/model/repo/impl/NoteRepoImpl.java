package edu.tamu.app.model.repo.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.app.enums.NoteType;
import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.Note;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.custom.NoteRepoCustom;

public class NoteRepoImpl implements NoteRepoCustom {
    
    @Autowired
    NoteRepo noteRepo;
    
    @Autowired
    ServiceRepo serviceRepo;
    
    @Override
    public Note create(String title, AppUser author) {
        return noteRepo.save(new Note(title, author));
    }
    
    public Note create(String title, AppUser author, NoteType noteType, String body, Set<Service> services) {
        Note note =  noteRepo.save(new Note(title, author, noteType, body, services));
        for (Service service : services) {
            service.addNote(note);
            serviceRepo.save(service);
        }
        return note;
    }
    
    @Override
    public void delete(Note note) {
        for (Service service : note.getServices()) {
            service.removeNote(note);
            serviceRepo.save(service);
        }
        noteRepo.delete(note.getId());
    }

}
