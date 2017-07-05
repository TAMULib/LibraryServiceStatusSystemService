package edu.tamu.app.service;

import static edu.tamu.app.enums.OverallMessageType.ERROR;
import static edu.tamu.app.enums.OverallMessageType.SUCCESS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.OverallStatus;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.ServiceRepo;

@org.springframework.stereotype.Service
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
				
		List<Service> servicesFull = serviceRepo.findAll();
		List<Service> servicesPublic = serviceRepo.findByIsPublic(true);
		
		boolean fullServicesAreUp = true;
		boolean publicServicesAreUp = true;
		
		for(Service service : servicesFull) {
			if(service.getStatus().equals(Status.DOWN)) {
				fullServicesAreUp = false;
				break;
			}
		}
		
		for(Service service : servicesPublic) {
			if(service.getStatus().equals(Status.DOWN)) {
				publicServicesAreUp = false;
				break;
			}
		}
		
		if(fullServicesAreUp) {
			this.overallStatusFull = new OverallStatus(SUCCESS, SUCCESS_MESSAGE);
		} else {
			this.overallStatusFull = new OverallStatus(ERROR, ERROR_MESSAGE);
		}
		
		if(publicServicesAreUp) {
			this.overallStatusPublic = new OverallStatus(SUCCESS, SUCCESS_MESSAGE);
		} else {
			this.overallStatusPublic = new OverallStatus(ERROR, ERROR_MESSAGE);
		}

	}
}
