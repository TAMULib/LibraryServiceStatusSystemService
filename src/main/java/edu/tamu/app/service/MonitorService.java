package edu.tamu.app.service;

import edu.tamu.app.model.OverallStatus;

public interface MonitorService {

    public void updateAll();

    public OverallStatus getOverallStatus();

    public OverallStatus getOverallStatusPublic();

}
