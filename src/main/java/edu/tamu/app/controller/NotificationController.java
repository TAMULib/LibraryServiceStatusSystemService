package edu.tamu.app.controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static edu.tamu.framework.enums.BusinessValidationType.CREATE;
import static edu.tamu.framework.enums.BusinessValidationType.DELETE;
import static edu.tamu.framework.enums.BusinessValidationType.EXISTS;
import static edu.tamu.framework.enums.BusinessValidationType.NONEXISTS;
import static edu.tamu.framework.enums.BusinessValidationType.UPDATE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.Notification;
import edu.tamu.app.model.repo.NotificationRepo;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.ApiValidatedModel;
import edu.tamu.framework.aspect.annotation.ApiValidation;
import edu.tamu.framework.aspect.annotation.ApiVariable;
import edu.tamu.framework.aspect.annotation.Auth;
import edu.tamu.framework.aspect.annotation.SkipAop;
import edu.tamu.framework.model.ApiResponse;

@RestController
@ApiMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationRepo notificationRepo;
    
    @ApiMapping("/all")
    @Auth(role="ROLE_STAFF")
    public ApiResponse getAllNotifications() {
        return new ApiResponse(SUCCESS, notificationRepo.findAll());
    }
    
    @ApiMapping("/{id}")
    @Auth(role="ROLE_STAFF")
    public ApiResponse getNotification(@ApiVariable Long id) {
        return new ApiResponse(SUCCESS, notificationRepo.findOne(id));
    }
    
    @ApiMapping("/create")
    @Auth(role="ROLE_WEB_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = CREATE), @ApiValidation.Business(value = EXISTS) })
    public ApiResponse create(@ApiValidatedModel Notification notification) {
        notification = notificationRepo.create(notification.getName(), notification.getBody(), notification.getIsActive(), notification.getLocations());
        return new ApiResponse(SUCCESS, notification);
    }
    
    @ApiMapping("/update")
    @Auth(role="ROLE_WEB_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = UPDATE), @ApiValidation.Business(value = NONEXISTS) })
    public ApiResponse update(@ApiValidatedModel Notification notification) {
        notification = notificationRepo.update(notification);
        return new ApiResponse(SUCCESS, notification);
    }
    
    @ApiMapping("/remove")
    @Auth(role="ROLE_WEB_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = DELETE), @ApiValidation.Business(value = NONEXISTS) })
    public ApiResponse remove(@ApiValidatedModel Notification notification) {
        notificationRepo.delete(notification);
        return new ApiResponse(SUCCESS);
    }
    
    @SkipAop
    @RequestMapping("/notification/active")
    public String getActiveNotifications(@RequestParam(value = "location", defaultValue = "ALL") String locationString) {
        String notificationString = "";
        List<Notification> notificationList;
        if (locationString.equals("ALL")) {
            notificationList = notificationRepo.findByIsActive(true);
        } else {
            try {
                NotificationLocation location = NotificationLocation.valueOf(locationString);
                notificationList = notificationRepo.findByIsActiveAndLocations(true, location);
            } catch (IllegalArgumentException e) {
                notificationList = new ArrayList<Notification>();
            }
        }
        for (Notification notification : notificationList) {
            notificationString += "<p>" + notification.getBody() + "</p>";
        }
        
        return notificationString;
    }
    
}
