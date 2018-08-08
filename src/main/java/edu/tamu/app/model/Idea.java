package edu.tamu.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import edu.tamu.app.enums.IdeaState;
import edu.tamu.app.model.request.ServiceRequest;
import edu.tamu.app.model.validation.IdeaValidator;

@Entity
public class Idea extends AbstractIdea {

    @Column(nullable = false)
    private IdeaState state;

    public Idea() {
        super();
        this.modelValidator = new IdeaValidator();
        this.state = IdeaState.WAITING_ON_REVIEW;
    }

    public Idea(String title, String description) {
        super(title, description);
        this.state = IdeaState.WAITING_ON_REVIEW;
    }

    public Idea(ServiceRequest serviceRequest) {
        this(serviceRequest.getTitle(), serviceRequest.getDescription());
    }

    public Idea(String title, String description, User author) {
        super(title, description, author);
        this.state = IdeaState.WAITING_ON_REVIEW;
    }

    public Idea(String title, String description, User author, Service service) {
        super(title, description, author, service);
        this.state = IdeaState.WAITING_ON_REVIEW;
    }

    public IdeaState getState() {
        return state;
    }

    public void setState(IdeaState state) {
        this.state = state;
    }
}


