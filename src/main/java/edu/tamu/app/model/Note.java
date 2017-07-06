package edu.tamu.app.model;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import edu.tamu.app.enums.NoteType;
import edu.tamu.app.model.validation.NoteValidator;
import edu.tamu.framework.model.BaseEntity;

@Entity
public class Note extends BaseEntity {

    @Size(min = 3)
    @Column(nullable = false)
    private String title;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = { REFRESH, MERGE}, fetch = EAGER)
    private List<Service> services;
    
    private NoteType noteType;

    private String body;

    @Temporal(TemporalType.DATE)
    private Calendar scheduledPostingStart;
    
    @Temporal(TemporalType.DATE)
    private Calendar scheduledPostingEnd;
    
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Calendar lastModified;
    
    @JoinColumn(nullable = false)
    @ManyToOne(cascade = REFRESH)
    private AppUser author;
    
    public Note() {
        setModelValidator(new NoteValidator());
        setServices(new ArrayList<Service>());
    }
    
    public Note(String title, AppUser author) {
        this();
        setTitle(title);
        setAuthor(author);
    }
    
    public Note(String title, AppUser author, NoteType noteType, String body, List<Service> services) {
        this(title, author);
        setNoteType(noteType);
        setBody(body);
        setServices(services);
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<Service> getServices() {
        return services;
    }
    
    public void setServices(List<Service> services) {
        this.services = services;
    }
    
    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Calendar getScheduledPostingStart() {
        return scheduledPostingStart;
    }

    public void setScheduledPostingStart(Calendar scheduledPostingStart) {
        this.scheduledPostingStart = scheduledPostingStart;
    }

    public Calendar getScheduledPostingEnd() {
        return scheduledPostingEnd;
    }

    public void setScheduledPostingEnd(Calendar scheduledPostingEnd) {
        this.scheduledPostingEnd = scheduledPostingEnd;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public AppUser getAuthor() {
        return author;
    }

    public void setAuthor(AppUser author) {
        this.author = author;
    }
}
