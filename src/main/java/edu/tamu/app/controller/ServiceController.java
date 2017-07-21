package edu.tamu.app.controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static edu.tamu.framework.enums.BusinessValidationType.CREATE;
import static edu.tamu.framework.enums.BusinessValidationType.DELETE;
import static edu.tamu.framework.enums.BusinessValidationType.EXISTS;
import static edu.tamu.framework.enums.BusinessValidationType.NONEXISTS;
import static edu.tamu.framework.enums.BusinessValidationType.UPDATE;
import static edu.tamu.app.enums.AppRole.ROLE_ANONYMOUS;
import static edu.tamu.app.enums.AppRole.ROLE_USER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.AppUserRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.service.SystemMonitorService;
import edu.tamu.framework.aspect.annotation.ApiCredentials;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.ApiValidatedModel;
import edu.tamu.framework.aspect.annotation.ApiValidation;
import edu.tamu.framework.aspect.annotation.ApiVariable;
import edu.tamu.framework.aspect.annotation.Auth;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

@RestController
@ApiMapping("/service")
public class ServiceController {
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ServiceRepo serviceRepo;
    
    @Autowired
    private SystemMonitorService systemMonitorService;
    
    @Autowired
    private AppUserRepo userRepo;

    @ApiMapping("/all")
    @Auth(role="ROLE_ANONYMOUS")
    public ApiResponse getAllServices() {
        return new ApiResponse(SUCCESS, serviceRepo.findAll());
    }
    
    @ApiMapping("/public")
    @Auth(role="ROLE_ANONYMOUS")
    public ApiResponse getPublicServices() {
        return new ApiResponse(SUCCESS, serviceRepo.findByIsPublic(true));
    }
    
    @ApiMapping("/get/{id}")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse getService(@ApiVariable Long id) {
        Service service = serviceRepo.findOne(id);
        return new ApiResponse(SUCCESS, service);
    }
    
    @ApiMapping("/create")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = CREATE), @ApiValidation.Business(value = EXISTS) })
    public ApiResponse createService(@ApiValidatedModel Service service, @ApiCredentials Credentials credentials) {
        service = serviceRepo.create(service.getName(), service.getStatus(), service.getIsAuto(), service.getIsPublic(), service.getOnShortList(), service.getServiceUrl());
        simpMessagingTemplate.convertAndSend("/channel/service", new ApiResponse(SUCCESS, serviceRepo.findAll()));
        sendStatusUpdate(service, credentials);
        return new ApiResponse(SUCCESS, service);
    }
    
    @ApiMapping("/update")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = UPDATE), @ApiValidation.Business(value = NONEXISTS) })
    public ApiResponse updateService(@ApiValidatedModel Service service, @ApiCredentials Credentials credentials) {
        service = serviceRepo.save(service);
        simpMessagingTemplate.convertAndSend("/channel/service/" + service.getId(), new ApiResponse(SUCCESS, service));
        sendStatusUpdate(service, credentials);
        return new ApiResponse(SUCCESS, service);
    }
    
    private void sendStatusUpdate(Service service, Credentials credentials) {
        System.out.println("Uhhhh ya!");
        AppUser user = userRepo.findByUin(credentials.getUin());
        if (user.getRole() == ROLE_ANONYMOUS || user.getRole() == ROLE_USER) {
            simpMessagingTemplate.convertAndSend("/private/queue/status/overall-public", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatusPublic()));
        } else {
            simpMessagingTemplate.convertAndSend("/private/queue/status/overall-full", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatus()));
        }
    }
    
    @ApiMapping("/remove")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = DELETE), @ApiValidation.Business(value = NONEXISTS) })
    public ApiResponse removeService(@ApiValidatedModel Service service) {
        serviceRepo.delete(service);
        simpMessagingTemplate.convertAndSend("/channel/service", new ApiResponse(SUCCESS, serviceRepo.findAll()));
        return new ApiResponse(SUCCESS);
    }
}
