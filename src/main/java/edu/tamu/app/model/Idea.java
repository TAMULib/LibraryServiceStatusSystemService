package edu.tamu.app.model;

import static javax.persistence.FetchType.EAGER;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import edu.tamu.app.enums.IdeaState;
import edu.tamu.app.model.request.ServiceRequest;
import edu.tamu.app.model.validation.IdeaValidator;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "id", "feature_proposal_id" }))
public class Idea extends AbstractIdea {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdeaState state;
    
    @Column(nullable = true)
    private String feedback;

    @Column(nullable = true)
    private String email;

    @ManyToOne(fetch = EAGER, cascade = { CascadeType.REFRESH, CascadeType.DETACH }, optional = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, scope = FeatureProposal.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private FeatureProposal featureProposal;

    public Idea() {
        super();
        this.modelValidator = new IdeaValidator();
        this.state = IdeaState.WAITING_ON_REVIEW;
    }

    public Idea(String title, String description) {
        super(title, description);
        this.state = IdeaState.WAITING_ON_REVIEW;
    }

    public Idea(String title, String description, String email) {
        this(title, description);
        setEmail(email);
    }

    public Idea(ServiceRequest serviceRequest) {
        this(serviceRequest.getTitle(), serviceRequest.getDescription(), serviceRequest.getEmail());
    }

    public Idea(String title, String description, User author) {
        super(title, description, author);
        this.state = IdeaState.WAITING_ON_REVIEW;
    }

    public Idea(String title, String description, User author, Service service) {
        super(title, description, author, service);
        this.state = IdeaState.WAITING_ON_REVIEW;
    }
    
    public Idea(String title, String description, User author, Service service, String email) {
        this(title, description, author, service);
        this.email = email;
    }

    public IdeaState getState() {
        return state;
    }

    public void setState(IdeaState state) {
        this.state = state;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FeatureProposal getFeatureProposal() {
        return featureProposal;
    }

    public void setFeatureProposal(FeatureProposal featureProposal) {
        this.featureProposal = featureProposal;
    }

}
