package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.service.MonitorService;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private MonitorService monitorService;

    @RequestMapping("/overall-full")
    @PreAuthorize("hasRole('STAFF')")
    public ApiResponse overallFull() {
        return new ApiResponse(SUCCESS, monitorService.getOverallStatus());
    }

    @RequestMapping("/overall-public")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse overallPublic() {
        return new ApiResponse(SUCCESS, monitorService.getOverallStatusPublic());
    }

}
