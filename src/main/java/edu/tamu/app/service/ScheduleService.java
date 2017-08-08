package edu.tamu.app.service;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.tamu.app.model.AbstractScheduler;
import edu.tamu.app.model.repo.AbstractSchedulerRepo;
import edu.tamu.app.model.repo.ScheduleRepo;
import edu.tamu.framework.model.ApiResponse;

@Service
public class ScheduleService {

    private Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private AbstractSchedulerRepo abstractSchedulerRepo;

    @Autowired
    private SystemMonitorService systemMonitorService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(cron = "5 0/1 * * * ?")
    private void checkSchedules() {

        Date date = new Date();
        Long now = date.getTime();

        logger.info("Checking for ending schedules");
        scheduleRepo.findByScheduledPostingEndLessThanEqualAndSchedulerWithinScheduleTrue(now).forEach(schedule -> {
            AbstractScheduler scheduler = schedule.getScheduler();
            scheduler.scheduleEnd(schedule.getScheduleData());
            scheduler.setWithinSchedule(false);
            scheduler = abstractSchedulerRepo.save(scheduler);
            logger.info("Ending schedule for " + scheduler);
            broadcastUpdate(scheduler);
        });

        logger.info("Checking for starting schedules");
        scheduleRepo.findByScheduledPostingStartLessThanEqualAndScheduledPostingEndGreaterThanEqualAndSchedulerWithinScheduleFalse(now, now).forEach(schedule -> {
            AbstractScheduler scheduler = schedule.getScheduler();
            scheduler.scheduleStart(schedule.getScheduleData());
            scheduler.setWithinSchedule(true);
            scheduler = abstractSchedulerRepo.save(scheduler);
            logger.info("Starting schedule for " + scheduler);
            broadcastUpdate(scheduler);
        });

    }

    private void broadcastUpdate(AbstractScheduler scheduler) {
        simpMessagingTemplate.convertAndSend("/channel/" + scheduler.getType() + "/update", new ApiResponse(SUCCESS, scheduler));
        if (scheduler.getType().equals("service")) {
            simpMessagingTemplate.convertAndSend("/channel/status/overall-public", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatusPublic()));
            simpMessagingTemplate.convertAndSend("/channel/status/overall-full", new ApiResponse(SUCCESS, systemMonitorService.getOverallStatus()));
        }
    }

}
