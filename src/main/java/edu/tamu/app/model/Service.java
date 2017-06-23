package edu.tamu.app.model;

import static javax.persistence.CascadeType.REFRESH;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import edu.tamu.app.enums.Status;
import edu.tamu.framework.model.BaseEntity;

@Entity
public class Service extends BaseEntity {

    @Size(min = 1)
    @Column(nullable = false)
    private String name;
    
    @ElementCollection
    private List<String> aliases;
    
    @Column(nullable = false)
    private Status status;
    
    @Column(nullable = true)
    private String serviceUrl;
    
    @ManyToMany(cascade = REFRESH)
    private List<Note> notes;
    
    public Service() {
        setNotes(new ArrayList<Note>());
    }
    
    public Service(String name, Status status) {
        this();
        setName(name);
        setStatus(status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    
    
    
}
