package edu.tamu.app.model;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.validation.ServiceValidator;
import edu.tamu.framework.model.BaseEntity;

/**
 * @author rladdusaw
 *
 */
@Entity
public class Service extends BaseEntity {

    @Size(min = 1)
    @Column(nullable = false, unique = true)
    private String name;
    
    @Fetch(FetchMode.SELECT)
    @ElementCollection(fetch = EAGER)
    private List<String> aliases;
    
    @Column(nullable = false, unique = false)
    private Status status;
    
    @Column(nullable = true)
    private String serviceUrl;
    
    @Column(nullable = false)
    private Boolean isPublic;
    
    @Column(nullable = false)
    private Boolean onShortList;
    
    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = { REFRESH, MERGE }, fetch = EAGER)
    private List<Note> notes;
    
    public Service() {
        setModelValidator(new ServiceValidator());
        setNotes(new ArrayList<Note>());
        setAliases(new ArrayList<String>());
    }
    
    public Service(String name, Status status, Boolean isPublic, Boolean onShortList) {
        this();
        setName(name);
        setStatus(status);
        setIsPublic(isPublic);
        setOnShortList(onShortList);
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getOnShortList() {
        return onShortList;
    }

    public void setOnShortList(Boolean onShortList) {
        this.onShortList = onShortList;
    }
}
