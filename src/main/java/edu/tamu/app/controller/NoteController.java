package edu.tamu.app.controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.model.Note;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.ApiValidatedModel;
import edu.tamu.framework.aspect.annotation.ApiVariable;
import edu.tamu.framework.aspect.annotation.Auth;
import edu.tamu.framework.model.ApiResponse;

@RestController
@ApiMapping("/note")
public class NoteController {

    @Autowired
    private NoteRepo noteRepo;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @ApiMapping("/all")
    @Auth(role="ROLE_ANONYMOUS")
    public ApiResponse getAllNotes() {
        return new ApiResponse(SUCCESS, noteRepo.findAll());
    }
    
    @ApiMapping("/get/{id}")
    @Auth(role="ROLE_ANONYMOUS")
    public ApiResponse getNote(@ApiVariable Long id) {
        return new ApiResponse(SUCCESS, noteRepo.findOne(id));
    }
    
    @ApiMapping("/create")
    @Auth(role="ROLE_SERVICE_MANAGER")
    public ApiResponse create(@ApiValidatedModel Note note) {
        note = noteRepo.create(note.getTitle(), note.getAuthor());
        simpMessagingTemplate.convertAndSend("/channel/note", new ApiResponse(SUCCESS, noteRepo.findOne(note.getId())));
        return new ApiResponse(SUCCESS, note);
    }
    
    @ApiMapping("/update")
    @Auth(role="ROLE_SERVICE_MANAGER")
    public ApiResponse update(@ApiValidatedModel Note note) {
        note = noteRepo.save(note);
        simpMessagingTemplate.convertAndSend("/channel/note", new ApiResponse(SUCCESS, noteRepo.findOne(note.getId())));
        return new ApiResponse(SUCCESS, note);
    }
    
    @ApiMapping("/remove")
    @Auth(role="ROLE_SERVICE_MANAGER")
    public ApiResponse remove(@ApiValidatedModel Note note) {
        noteRepo.delete(note);
        simpMessagingTemplate.convertAndSend("/channel/note", new ApiResponse(SUCCESS, noteRepo.findAll()));
        return new ApiResponse(SUCCESS);
    }
}