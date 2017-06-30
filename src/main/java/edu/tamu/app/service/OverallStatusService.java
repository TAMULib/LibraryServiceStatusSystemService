package edu.tamu.app.service;

import org.springframework.stereotype.Service;

import edu.tamu.app.model.OverallStatus;

@Service
public class OverallStatusService {
	
	private OverallStatus overallStatusFull;
	
	private OverallStatus overallStatusPublic;
	
	public OverallStatus getOverallStatusFull() {
		return overallStatusFull;
	}
	
	public OverallStatus getOverallStatusPublic() {
		return overallStatusPublic;
	}
	
}
