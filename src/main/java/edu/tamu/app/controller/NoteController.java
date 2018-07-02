package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Note;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.request.FilteredPageRequest;
import edu.tamu.weaver.auth.annotation.WeaverCredentials;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteRepo noteRepo;

    @RequestMapping("/page")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse page(@RequestBody FilteredPageRequest filteredPageRequest) {
        return new ApiResponse(SUCCESS, noteRepo.findAll(filteredPageRequest.getNoteSpecification(), filteredPageRequest.getPageRequest()));
    }

    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse getById(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, noteRepo.findOne(id));
    }

    @RequestMapping("/create")
    @PreAuthorize("hasRole('WEB_MANAGER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse create(@WeaverValidatedModel Note note, @WeaverCredentials Credentials credentials) throws UserNotFoundException {
        return new ApiResponse(SUCCESS, noteRepo.create(note, credentials));
    }

    @RequestMapping("/update")
    @PreAuthorize("hasRole('WEB_MANAGER')")
    public ApiResponse update(@WeaverValidatedModel Note note) {
        return new ApiResponse(SUCCESS, noteRepo.update(note));
    }

    @Transactional
    @RequestMapping("/remove")
    @PreAuthorize("hasRole('WEB_MANAGER')")
    public ApiResponse remove(@WeaverValidatedModel Note note) {
        noteRepo.delete(note);
        return new ApiResponse(SUCCESS);
    }

}
