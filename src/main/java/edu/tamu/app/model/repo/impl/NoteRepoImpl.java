package edu.tamu.app.model.repo.impl;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Note;
import edu.tamu.app.model.Schedule;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.custom.NoteRepoCustom;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

public class NoteRepoImpl implements NoteRepoCustom {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Note create(Note note, Credentials credentials) throws UserNotFoundException {
        Optional<User> user = userRepo.findByUsername(credentials.getUin());
        if (user.isPresent()) {
            note.setAuthor(user.get());
            note = noteRepo.save(note);
            simpMessagingTemplate.convertAndSend("/channel/notes/create", new ApiResponse(SUCCESS, note));
            return note;
        }
        throw new UserNotFoundException("Unable to find user with uin " + credentials.getUin());
    }

    @Override
    public Note update(Note note) {
        for (Schedule schedule : note.getSchedules()) {
            schedule.setScheduler(note);
        }
        note = noteRepo.save(note);
        simpMessagingTemplate.convertAndSend("/channel/notes/update", new ApiResponse(SUCCESS, note));
        return note;
    }

    @Override
    public void delete(Note note) {
        noteRepo.delete(note.getId());
        simpMessagingTemplate.convertAndSend("/channel/notes/delete", new ApiResponse(SUCCESS, note.getId()));
    }

}
