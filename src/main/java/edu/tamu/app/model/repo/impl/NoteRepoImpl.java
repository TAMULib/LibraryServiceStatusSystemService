package edu.tamu.app.model.repo.impl;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.model.Note;
import edu.tamu.app.model.repo.AppUserRepo;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.custom.NoteRepoCustom;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

public class NoteRepoImpl implements NoteRepoCustom {

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Note create(Note note, Credentials credentials) {
        note.setAuthor(userRepo.findByUin(credentials.getUin()));
        note = noteRepo.save(note);
        simpMessagingTemplate.convertAndSend("/channel/note/create", new ApiResponse(SUCCESS, note));
        return note;
    }

    @Override
    public Note update(Note note) {
        note = noteRepo.save(note);
        simpMessagingTemplate.convertAndSend("/channel/note/update", new ApiResponse(SUCCESS, note));
        return note;
    }

    @Override
    public void delete(Note note) {
        noteRepo.delete(note.getId());
        simpMessagingTemplate.convertAndSend("/channel/note/delete", new ApiResponse(SUCCESS, note.getId()));
    }

}
