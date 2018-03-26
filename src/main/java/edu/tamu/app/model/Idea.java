package edu.tamu.app.model;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import edu.tamu.app.model.request.ServiceRequest;
import edu.tamu.app.model.validation.IdeaValidator;
import edu.tamu.weaver.validation.model.ValidatingBaseEntity;

@Entity
public class Idea extends ValidatingBaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = true)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Calendar lastModified;

    @ManyToOne(fetch = EAGER, cascade = MERGE, optional = false)
    private Service service;

    @ManyToOne(cascade = REFRESH, optional = false)
    private User author;

    public Idea() {
        super();
        this.modelValidator = new IdeaValidator();
    }

    public Idea(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public Idea(ServiceRequest serviceRequest) {
        this(serviceRequest.getTitle(), serviceRequest.getDescription());
    }

    public Idea(String title, String description, User author) {
        this(title, description);
        this.author = author;
    }

    public Idea(String title, String description, User author, Service service) {
        this(title, description, author);
        this.service = service;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
