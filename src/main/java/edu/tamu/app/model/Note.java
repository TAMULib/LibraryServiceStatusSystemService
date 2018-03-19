package edu.tamu.app.model;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

import java.util.Calendar;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import edu.tamu.app.enums.NoteType;
import edu.tamu.app.model.validation.NoteValidator;

@Entity
public class Note extends AbstractScheduler {

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private NoteType noteType;

    @Column(columnDefinition = "text", nullable = true)
    private String body;

    @Column(nullable = false)
    private Boolean pinned;

    @Column(nullable = false)
    private Boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Calendar lastModified;

    @ManyToOne(fetch = EAGER, cascade = MERGE, optional = false)
    private Service service;

    @ManyToOne(cascade = REFRESH, optional = false)
    private User author;

    public Note() {
        super();
        setModelValidator(new NoteValidator());
        setPinned(false);
        setActive(false);
        setService(new Service());
    }

    public Note(String title, User author) {
        this();
        setTitle(title);
        setAuthor(author);
    }

    public Note(String title, User author, NoteType noteType, String body) {
        this(title, author);
        setNoteType(noteType);
        setBody(body);
    }

    public Note(String title, User author, NoteType noteType, String body, Service service) {
        this(title, author, noteType, body);
        setService(service);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String getType() {
        return "note";
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
