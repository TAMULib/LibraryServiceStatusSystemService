package edu.tamu.app.model;

import static javax.persistence.FetchType.EAGER;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import edu.tamu.app.enums.NotificationLocation;
import edu.tamu.app.model.validation.NotificationValidator;
import edu.tamu.framework.model.BaseEntity;

@Entity
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String body;
    
    @Column(nullable = false)
    private boolean isActive;
    
    @ElementCollection(fetch = EAGER)
    private List<NotificationLocation> locations; 
    
    public Notification() {
        setModelValidator(new NotificationValidator());
    }
    
    public Notification(String name, String body, boolean isActive, List<NotificationLocation> locations) {
        this();
        setName(name);
        setBody(body);
        setIsActive(isActive);
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<NotificationLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<NotificationLocation> locations) {
        this.locations = locations;
    }
}
