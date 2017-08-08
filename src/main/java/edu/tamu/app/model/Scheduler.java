package edu.tamu.app.model;

import java.util.Map;

public interface Scheduler {

    public void scheduleStart(Map<String, String> scheduleData);

    public void scheduleEnd(Map<String, String> scheduleData);

}
