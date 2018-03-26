package edu.tamu.app.model.repo.impl;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.app.model.Schedule;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.custom.ServiceRepoCustom;
import edu.tamu.app.service.SystemMonitorService;
import edu.tamu.weaver.response.ApiResponse;

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
    public Service create(Service service) {
        service = serviceRepo.save(service);
        simpMessagingTemplate.convertAndSend("/channel/services/create", new ApiResponse(SUCCESS, service));
        sendStatusUpdate();
        return service;
    }

    @Override
    public Service update(Service service) {
        for (Schedule schedule : service.getSchedules()) {
            schedule.setScheduler(service);
        }
        service = serviceRepo.save(service);
        simpMessagingTemplate.convertAndSend("/channel/services/update", new ApiResponse(SUCCESS, service));
        sendStatusUpdate();
        return service;
    }

    @Override
    public void delete(Service service) {
        noteRepo.findAllByServiceId(service.getId()).parallelStream().forEach(note -> {
            noteRepo.delete(note);
        });
        serviceRepo.delete(service.getId());
        simpMessagingTemplate.convertAndSend("/channel/services/delete", new ApiResponse(SUCCESS, service.getId()));
        sendStatusUpdate();
    }

    private void sendStatusUpdate() {
        simpMessagingTemplate.convertAndSend("/channel/status/overall-public", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatusPublic()));
        simpMessagingTemplate.convertAndSend("/channel/status/overall-full", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatus()));
    }

}
