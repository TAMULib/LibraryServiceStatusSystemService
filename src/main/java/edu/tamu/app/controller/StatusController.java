package edu.tamu.app.controller;

import edu.tamu.app.enums.AppRole;
import edu.tamu.app.model.OverallStatus;
import edu.tamu.app.service.AppRoleService;
import edu.tamu.app.service.OverallStatusService;
import edu.tamu.framework.aspect.annotation.ApiCredentials;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static edu.tamu.app.enums.OverallMessageType.ERROR;
import static edu.tamu.app.enums.AppRole.ROLE_STAFF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiMapping("/status")
public class StatusController {
	
	@Autowired
	OverallStatusService overallStatusService;
	
	@Autowired
	AppRoleService appRoleService;
	
	@ApiMapping("/overall")
	public ApiResponse overall(@ApiCredentials Credentials credentials) {
		
		OverallStatus overallStatus = null;
		
		AppRole role = (AppRole) appRoleService.valueOf(credentials.getRole());
		
		if(role.compareTo(ROLE_STAFF) >= 0) {
			overallStatus = overallStatusService.getOverallStatusFull();
		} else {
			overallStatus = overallStatusService.getOverallStatusPublic();
		}
		
		if(overallStatus == null) overallStatus = new OverallStatus(ERROR, "Service Statuses could not be retrieved");
		
        return new ApiResponse(SUCCESS, overallStatus);
	}
	
}
