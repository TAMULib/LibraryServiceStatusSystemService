package edu.tamu.app.service;

import static edu.tamu.app.enums.OverallMessageType.ERROR;
import static edu.tamu.app.enums.OverallMessageType.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.OverallStatus;
import edu.tamu.app.model.repo.ServiceRepo;

@Service
public class OverallStatusService {

    @Autowired
    private ServiceRepo serviceRepo;

    private OverallStatus overallStatusFull;

    private OverallStatus overallStatusPublic;

    private static final String SUCCESS_MESSAGE = "All services are working.";
    private static final String ERROR_MESSAGE = "Some services are experiencing problems.";

    public OverallStatus getOverallStatusFull() {
        return overallStatusFull;
    }

    public OverallStatus getOverallStatusPublic() {
        return overallStatusPublic;
    }

    public void updateStatuses() {

        boolean fullServicesAreUp = true;
        boolean publicServicesAreUp = true;

        for (edu.tamu.app.model.Service service : serviceRepo.findAll()) {
            if (service.getStatus().equals(Status.DOWN)) {
                fullServicesAreUp = false;
                break;
            }
        }

        for (edu.tamu.app.model.Service service : serviceRepo.findByIsPublicOrderByStatusDescNameAsc(true)) {
            if (service.getStatus().equals(Status.DOWN)) {
                publicServicesAreUp = false;
                break;
            }
        }

        if (fullServicesAreUp) {
            this.overallStatusFull = new OverallStatus(SUCCESS, SUCCESS_MESSAGE);
        } else {
            this.overallStatusFull = new OverallStatus(ERROR, ERROR_MESSAGE);
        }

        if (publicServicesAreUp) {
            this.overallStatusPublic = new OverallStatus(SUCCESS, SUCCESS_MESSAGE);
        } else {
            this.overallStatusPublic = new OverallStatus(ERROR, ERROR_MESSAGE);
        }

    }

}
