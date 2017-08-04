package edu.tamu.app.model;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

import java.util.Calendar;

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
import edu.tamu.framework.model.BaseEntity;

@Entity
public class Note extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private NoteType noteType;

    @Column(columnDefinition = "text", nullable = true)
    private String body;

    @Column(nullable = false)
    private boolean pinned;

    @Temporal(TemporalType.DATE)
    private Calendar scheduledPostingStart;

    @Temporal(TemporalType.DATE)
    private Calendar scheduledPostingEnd;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Calendar lastModified;

    @ManyToOne(fetch = EAGER, cascade = MERGE)
    private Service service;

    @ManyToOne(cascade = REFRESH, optional = false)
    private AppUser author;

    public Note() {
        setPinned(false);
        setModelValidator(new NoteValidator());
        setService(new Service());
    }

    public Note(String title, AppUser author) {
        this();
        setTitle(title);
        setAuthor(author);
    }

    public Note(String title, AppUser author, NoteType noteType, String body) {
        this(title, author);
        setNoteType(noteType);
        setBody(body);
    }

    public Note(String title, AppUser author, NoteType noteType, String body, Service service) {
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

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
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
