package edu.tamu.app.job;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.tamu.app.service.MonitorService;
import edu.tamu.weaver.response.ApiResponse;

@Service
public class UpdateServiceStatuses {

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(fixedRate = (10 * 60 * 1000))
    public void updateOveralStatuses() {
        monitorService.updateAll();
        simpMessagingTemplate.convertAndSend("/channel/status/overall-full", new ApiResponse(SUCCESS, monitorService.getOverallStatus()));
        simpMessagingTemplate.convertAndSend("/channel/status/overall-public", new ApiResponse(SUCCESS, monitorService.getOverallStatusPublic()));
    }

}
