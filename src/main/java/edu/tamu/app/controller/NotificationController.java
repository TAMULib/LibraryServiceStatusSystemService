package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.UPDATE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.model.Notification;
import edu.tamu.app.model.repo.NotificationRepo;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepo notificationRepo;

    @RequestMapping
    @PreAuthorize("hasRole('STAFF')")
    public ApiResponse getAllNotifications() {
        return new ApiResponse(SUCCESS, notificationRepo.findAllByOrderByIdDesc());
    }

    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ApiResponse getById(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, notificationRepo.findOne(id));
    }

    @RequestMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE_ADMIN','NOTICE_MANAGER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse create(@WeaverValidatedModel Notification notification) {
        return new ApiResponse(SUCCESS, notificationRepo.create(notification));
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE_ADMIN','NOTICE_MANAGER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
    public ApiResponse update(@WeaverValidatedModel Notification notification) {
        return new ApiResponse(SUCCESS, notificationRepo.update(notification));
    }

    @RequestMapping("/remove")
    @PreAuthorize("hasAnyRole('ADMIN','SERVICE_ADMIN','NOTICE_MANAGER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
    public ApiResponse remove(@WeaverValidatedModel Notification notification) {
        notificationRepo.delete(notification);
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping("/notification/active")
    public String getActiveNotifications(@RequestParam(value = "location", defaultValue = "ALL") String location) {
        return buildNotificationHtml(notificationRepo.activeNotificationsByLocation(location.toUpperCase()));
    }

    private String buildNotificationHtml(List<Notification> notifications) {
        StringBuilder notificationHtmlBuilder = new StringBuilder("");
        for (Notification notification : notifications) {
            notificationHtmlBuilder.append("<p>").append(notification.getBody()).append("</p>");
        }
        return notificationHtmlBuilder.toString();
    }

}
