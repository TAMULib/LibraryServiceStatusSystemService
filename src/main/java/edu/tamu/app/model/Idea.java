package edu.tamu.app.model;

import javax.persistence.Entity;

import edu.tamu.app.model.request.ServiceRequest;

@Entity
public class Idea extends AbstractIdea {

    public Idea() {
        super();
    }

    public Idea(String title, String description) {
        super(title, description);
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

}
