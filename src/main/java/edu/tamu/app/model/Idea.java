package edu.tamu.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import edu.tamu.app.model.request.ServiceRequest;

@Entity
public class Idea extends AbstractIdea {

    @Column(nullable = false)
    private boolean elevated;

    public Idea() {
        super();
        this.elevated = false;
    }

    public Idea(String title, String description) {
        super(title, description);
        this.elevated = false;
    }

    public Idea(ServiceRequest serviceRequest) {
        this(serviceRequest.getTitle(), serviceRequest.getDescription());
    }

    public Idea(String title, String description, User author) {
        super(title, description, author);
        this.elevated = false;
    }

    public Idea(String title, String description, User author, Service service) {
        super(title, description, author, service);
        this.elevated = false;
    }

    public boolean isElevated() {
        return elevated;
    }

    public void setElevated(boolean elevated) {
        this.elevated = elevated;
    }

}
