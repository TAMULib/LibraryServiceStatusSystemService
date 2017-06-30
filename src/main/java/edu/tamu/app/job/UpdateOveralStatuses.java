package edu.tamu.app.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.tamu.app.service.OverallStatusService;
import edu.tamu.framework.model.ApiResponse;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

@Service
public class UpdateOveralStatuses {

	@Autowired
	OverallStatusService overallStatusService;
	
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	
	@Scheduled(fixedRate=(1 * 30 * 1000))
	public void updateOveralStatuses() {
		overallStatusService.updateStatuses();
		
		simpMessagingTemplate.convertAndSend("/channel/status/overall-full", new ApiResponse(SUCCESS, overallStatusService.getOverallStatusFull()));
		simpMessagingTemplate.convertAndSend("/channel/status/overall-public", new ApiResponse(SUCCESS, overallStatusService.getOverallStatusPublic()));
		
	}

}
