package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.service.MonitorService;
import edu.tamu.app.service.RoleService;
import edu.tamu.weaver.auth.annotation.WeaverCredentials;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    MonitorService monitorService;

    @Autowired
    RoleService appRoleService;

    @RequestMapping("/overall-full")
    @PreAuthorize("hasRole('STAFF')")
    public ApiResponse overallFull(@WeaverCredentials Credentials credentials) {
        return new ApiResponse(SUCCESS, monitorService.getOverallStatus());
    }

    @RequestMapping("/overall-public")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse overallPublic(@WeaverCredentials Credentials credentials) {
        return new ApiResponse(SUCCESS, monitorService.getOverallStatusPublic());
    }

}
