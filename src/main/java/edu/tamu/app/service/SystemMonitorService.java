package edu.tamu.app.service;

import static edu.tamu.app.enums.OverallMessageType.ERROR;
import static edu.tamu.app.enums.OverallMessageType.SUCCESS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.OverallStatus;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.framework.util.HttpUtility;

@Service
public class SystemMonitorService implements MonitorService {
	@Autowired
	ServiceRepo serviceRepo;
	
	@Autowired
	HttpUtility httpUtility;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private static final String SUCCESS_MESSAGE = "All services are working.";
	private static final String ERROR_MESSAGE = "Some services are experiencing problems.";

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void updateAll() {
		//get all monitor-able services from ServiceRepo
		serviceRepo.findByIsAuto(true).forEach(service -> {
			try {
				Status serviceStatus = getServiceStatus(service.getServiceUrl());
				//update the service status if it's changed
				if (serviceStatus != service.getStatus()) {
					service.setStatus(serviceStatus);
					serviceRepo.save(service);
				}
			} catch(MalformedURLException e) {
				logger.debug("Did not check the status of ["+service.getName()+"] due to a malformed URL");
			} catch(IOException e) {
				logger.debug("Attempt to check the status of ["+service.getName()+"] failed due to an IOException");
			}
		});
	}
	
	public OverallStatus getOverallStatus() {
		Long downCount = serviceRepo.countByStatus(Status.DOWN);
		if (downCount == 0) {
			return new OverallStatus(SUCCESS, SUCCESS_MESSAGE);
		}
		return new OverallStatus(ERROR, ERROR_MESSAGE);
	}
	
	public OverallStatus getOverallStatusPublic() {		
		Long downCount = serviceRepo.countByStatusAndIsPublic(Status.DOWN,true);
		if (downCount == 0) {
			return new OverallStatus(SUCCESS, SUCCESS_MESSAGE);
		}
		return new OverallStatus(ERROR, ERROR_MESSAGE);
	}	
	protected Status getServiceStatus(String serviceUrl) throws MalformedURLException,IOException {
		String rawStatusResponse = httpUtility.makeHttpRequest(serviceUrl, "GET");
		List<Map<String,String>> mappedStatusResponse  = objectMapper.readValue(rawStatusResponse, new TypeReference<List<Map<String, String>>>(){});
		return Status.valueOf(mappedStatusResponse.get(0).get("service").toUpperCase());
	}

}