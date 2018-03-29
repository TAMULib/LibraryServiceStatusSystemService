package edu.tamu.app.model;

import javax.persistence.Entity;

import edu.tamu.app.model.request.ServiceRequest;

@Entity
public class Idea extends AbstractIdea {
    
    private Boolean elevated;

    public Idea() {
        super();
        this.elevated = false;
    }

    public Idea(String title, String description) {
        super(title, description);
        this.elevated = false;
    }
    
    public Idea(String title, String description, Boolean elevated) {
        super(title, description);
        this.elevated = elevated;
    }

    public Idea(ServiceRequest serviceRequest) {
        this(serviceRequest.getTitle(), serviceRequest.getDescription());
    }

    public Idea(String title, String description, User author) {
        super(title, description, author);
    }

    public Idea(String title, String description, User author, Service service) {
        super(title, description, author, service);
    }

    public Boolean getElevated() {
        return elevated;
    }

    public void setElevated(Boolean elevated) {
        this.elevated = elevated;
    }

}
