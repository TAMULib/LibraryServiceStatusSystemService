package edu.tamu.app.job;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.tamu.app.service.MonitorService;
import edu.tamu.framework.model.ApiResponse;

@Service
public class UpdateServiceStatuses {

    @Autowired
    MonitorService monitorService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(fixedRate = (10 * 60 * 1000))
    public void updateOveralStatuses() {
        monitorService.updateAll();
        simpMessagingTemplate.convertAndSend("/channel/status/overall-full", new ApiResponse(SUCCESS, monitorService.getOverallStatus()));
        simpMessagingTemplate.convertAndSend("/channel/status/overall-public", new ApiResponse(SUCCESS, monitorService.getOverallStatusPublic()));
    }

}
