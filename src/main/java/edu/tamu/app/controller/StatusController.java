package edu.tamu.app.controller;

import edu.tamu.app.model.OverallStatus;
import edu.tamu.app.service.AppRoleService;
import edu.tamu.app.service.OverallStatusService;
import edu.tamu.framework.aspect.annotation.ApiCredentials;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.Auth;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiMapping("/status")
public class StatusController {
	
	@Autowired
	OverallStatusService overallStatusService;
	
	@Autowired
	AppRoleService appRoleService;
	
	@ApiMapping("/overall-full")
	@Auth(role = "ROLE_STAFF")
	public ApiResponse overallFull(@ApiCredentials Credentials credentials) {
		OverallStatus overallStatus = overallStatusService.getOverallStatusFull();		
        return new ApiResponse(SUCCESS, overallStatus);
	}
	
	@ApiMapping("/overall-public")
	@Auth(role = "ROLE_ANONYMOUS")
	public ApiResponse overallPublic(@ApiCredentials Credentials credentials) {		
		OverallStatus overallStatus = overallStatusService.getOverallStatusPublic();
        return new ApiResponse(SUCCESS, overallStatus);
	}
	
}
