package edu.tamu.app.model.repo.impl;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.model.Note;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.AppUserRepo;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.custom.NoteRepoCustom;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

public class NoteRepoImpl implements NoteRepoCustom {

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Note create(Note note, Credentials credentials) {
        note.setAuthor(userRepo.findByUin(credentials.getUin()));
        note = noteRepo.save(note);
        for (Service service : note.getServices()) {
            service.addNote(note);
            service = serviceRepo.save(service);
            simpMessagingTemplate.convertAndSend("/channel/service/" + service.getId(), new ApiResponse(SUCCESS, service));
        }
        return note;
    }

    @Override
    public void delete(Note note) {
        for (Service service : note.getServices()) {
            service.removeNote(note);
            service = serviceRepo.save(service);
            simpMessagingTemplate.convertAndSend("/channel/service/" + service.getId(), new ApiResponse(SUCCESS, service));
        }        
        noteRepo.delete(note.getId());
    }

}
