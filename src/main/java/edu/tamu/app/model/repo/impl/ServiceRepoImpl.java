package edu.tamu.app.model.repo.impl;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.custom.ServiceRepoCustom;
import edu.tamu.app.service.SystemMonitorService;
import edu.tamu.framework.model.ApiResponse;

public class ServiceRepoImpl implements ServiceRepoCustom {

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private SystemMonitorService systemMonitorService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Service create(String name, Status status, Boolean isAuto, Boolean isPublic, Boolean onShortList, String serviceUrl, String description) {
        Service service = serviceRepo.save(new Service(name, status, isAuto, isPublic, onShortList, serviceUrl, description));
        simpMessagingTemplate.convertAndSend("/channel/service/create", new ApiResponse(SUCCESS, service));
        sendStatusUpdate();
        return service;
    }

    @Override
    public Service update(Service service) {
        service = serviceRepo.save(service);
        simpMessagingTemplate.convertAndSend("/channel/service/update", new ApiResponse(SUCCESS, service));
        sendStatusUpdate();
        return service;
    }

    @Override
    public void delete(Service service) {
        noteRepo.findAllByService(service).parallelStream().forEach(note -> {
            noteRepo.delete(note);
        });
        serviceRepo.delete(service.getId());
        simpMessagingTemplate.convertAndSend("/channel/service/delete", new ApiResponse(SUCCESS, service.getId()));
        sendStatusUpdate();
    }

    private void sendStatusUpdate() {
        simpMessagingTemplate.convertAndSend("/channel/status/overall-public", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatusPublic()));
        simpMessagingTemplate.convertAndSend("/channel/status/overall-full", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatus()));
    }

}
