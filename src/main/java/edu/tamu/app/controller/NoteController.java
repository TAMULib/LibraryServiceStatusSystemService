package edu.tamu.app.controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static edu.tamu.framework.enums.BusinessValidationType.CREATE;
import static edu.tamu.framework.enums.BusinessValidationType.EXISTS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import edu.tamu.app.model.Note;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
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
    
    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @ApiMapping("/all")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse getAllNotes() {
        return new ApiResponse(SUCCESS, noteRepo.findAll());
    }

    @ApiMapping("/get/{id}")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse getNote(@ApiVariable Long id) {
        return new ApiResponse(SUCCESS, noteRepo.findOne(id));
    }

    @ApiMapping("/create")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = CREATE), @ApiValidation.Business(value = EXISTS) })
    public ApiResponse create(@ApiValidatedModel Note note, @ApiCredentials Credentials credentials) {
        note = noteRepo.create(note, credentials);
        System.out.println("Note value: " + note);
        ApiResponse response = new ApiResponse(SUCCESS, note);
        simpMessagingTemplate.convertAndSend("/channel/note/new", response);
        return response;
    }

    @ApiMapping("/update")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    public ApiResponse update(@ApiValidatedModel Note note) {
        note = noteRepo.save(note);
        simpMessagingTemplate.convertAndSend("/channel/note", new ApiResponse(SUCCESS, noteRepo.findAll()));
        return new ApiResponse(SUCCESS, note);
    }

    @Transactional
    @ApiMapping("/remove")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    public ApiResponse remove(@ApiValidatedModel Note note) {
        noteRepo.delete(note);
        simpMessagingTemplate.convertAndSend("/channel/note", new ApiResponse(SUCCESS, noteRepo.findAll()));
        return new ApiResponse(SUCCESS, serviceRepo.getOne(note.getService().getId()));
    }
    
    @ApiMapping("/page")
    @Auth(role="ROLE_ANONYMOUS")
    public ApiResponse page(@ApiData JsonNode dataNode) {
        Direction sortDirection;
        if (dataNode.get("direction").get("direction").asText().equals("ASC")) {
            sortDirection = Sort.Direction.ASC;
        } else {
            sortDirection = Sort.Direction.DESC;
        }
        
        Map<String, String[]> filters = new HashMap<String, String[]>();
        filters.put("title", arrayNodeToStringArray((ArrayNode) dataNode.get("filters").get("title")));
        FilteredPageRequest filteredPageRequest = new FilteredPageRequest(dataNode.get("page").get("number").asInt(), dataNode.get("page").get("size").asInt(), sortDirection, dataNode.get("direction").get("properties").asText(), filters);
        Page<Note> notes = noteRepo.findAll(filteredPageRequest);
        return new ApiResponse(SUCCESS, notes);
    }
    
    private String[] arrayNodeToStringArray(ArrayNode arrayNode) {
        String[] array = new String[arrayNode.size()];
        Iterator<JsonNode> arrayIterator = arrayNode.elements();
        int i = 0;
        while (arrayIterator.hasNext()) {
            array[i++] = arrayIterator.next().asText();
        }
        return array;
    }
}
