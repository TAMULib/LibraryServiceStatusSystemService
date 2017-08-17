package edu.tamu.app.model;

import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.FetchMode.SELECT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.annotations.Fetch;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.validation.NotificationValidator;

@Entity
public class Notification extends AbstractScheduler {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text", nullable = false)
    private String body;

    @Column(nullable = false)
    private Boolean active;

    @ElementCollection(fetch = EAGER)
    @Fetch(value = SELECT)
    private List<NotificationLocation> locations;

    public Notification() {
        super();
        setModelValidator(new NotificationValidator());
        setActive(false);
        setLocations(new ArrayList<NotificationLocation>());
    }

    public Notification(String name, String body, List<NotificationLocation> locations) {
        this();
        setName(name);
        setBody(body);
        setLocations(locations);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<NotificationLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<NotificationLocation> locations) {
        this.locations = locations;
    }

    @Override
    public String getType() {
        return "notification";
    }

    @Override
    public void scheduleStart(Map<String, String> scheduleData) {
        setActive(true);
    }

    @Override
    public void scheduleEnd(Map<String, String> scheduleData) {
        setActive(false);
    }

}
