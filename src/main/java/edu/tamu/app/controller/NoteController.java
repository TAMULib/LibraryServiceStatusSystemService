package edu.tamu.app.controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static edu.tamu.framework.enums.BusinessValidationType.CREATE;
import static edu.tamu.framework.enums.BusinessValidationType.EXISTS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.model.Note;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.request.FilteredPageRequest;
import edu.tamu.framework.aspect.annotation.ApiCredentials;
import edu.tamu.framework.aspect.annotation.ApiData;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.ApiValidatedModel;
import edu.tamu.framework.aspect.annotation.ApiValidation;
import edu.tamu.framework.aspect.annotation.ApiVariable;
import edu.tamu.framework.aspect.annotation.Auth;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

@RestController
@ApiMapping("/note")
public class NoteController {

    @Autowired
    private NoteRepo noteRepo;

    @ApiMapping("/query")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse getAllNotesByService(@ApiData FilteredPageRequest filteredPageRequest) {
        return new ApiResponse(SUCCESS, noteRepo.findAll(filteredPageRequest.getSpecification(), filteredPageRequest.getPageRequest()));
    }

    @ApiMapping("/{id}")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse getNote(@ApiVariable Long id) {
        return new ApiResponse(SUCCESS, noteRepo.findOne(id));
    }

    @ApiMapping("/create")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = CREATE), @ApiValidation.Business(value = EXISTS) })
    public ApiResponse create(@ApiValidatedModel Note note, @ApiCredentials Credentials credentials) {
        note = noteRepo.create(note, credentials);
        return new ApiResponse(SUCCESS, note);
    }

    @ApiMapping("/update")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    public ApiResponse update(@ApiValidatedModel Note note) {
        return new ApiResponse(SUCCESS, noteRepo.update(note));
    }

    @Transactional
    @ApiMapping("/remove")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    public ApiResponse remove(@ApiValidatedModel Note note) {
        noteRepo.delete(note);
        return new ApiResponse(SUCCESS);
    }

    @ApiMapping("/page")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse page(@ApiData FilteredPageRequest filteredPageRequest) {
        return new ApiResponse(SUCCESS, noteRepo.findAll(filteredPageRequest.getPageRequest()));
    }

}
