package edu.tamu.app.model.repo.impl;

import static edu.tamu.app.enums.AppRole.ROLE_ANONYMOUS;
import static edu.tamu.app.enums.AppRole.ROLE_USER;
import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.AppUserRepo;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.custom.ServiceRepoCustom;
import edu.tamu.app.service.SystemMonitorService;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

public class ServiceRepoImpl implements ServiceRepoCustom {

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private SystemMonitorService systemMonitorService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Service create(String name, Status status, Boolean isAuto, Boolean isPublic, Boolean onShortList, String serviceUrl, String description) {
        Service service = serviceRepo.save(new Service(name, status, isAuto, isPublic, onShortList, serviceUrl, description));
        simpMessagingTemplate.convertAndSend("/channel/service/create", new ApiResponse(SUCCESS, service));
        return service;
    }

    @Override
    public Service update(Service service) {
        service = serviceRepo.save(service);
        simpMessagingTemplate.convertAndSend("/channel/service/" + service.getId(), new ApiResponse(SUCCESS, service));
        return service;
    }

    @Override
    public void delete(Service service) {
        noteRepo.findAllByService(service).parallelStream().forEach(note -> {
            noteRepo.delete(note);
        });
        serviceRepo.delete(service.getId());
        simpMessagingTemplate.convertAndSend("/channel/service/delete", new ApiResponse(SUCCESS, service.getId()));
    }

    @Override
    public void sendStatusUpdate(Service service, Credentials credentials) {
        AppUser user = userRepo.findByUin(credentials.getUin());
        if (user.getRole() == ROLE_ANONYMOUS || user.getRole() == ROLE_USER) {
            simpMessagingTemplate.convertAndSend("/channel/status/overall-public", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatusPublic()));
        } else {
            simpMessagingTemplate.convertAndSend("/channel/status/overall-full", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatus()));
        }
    }

}
