package edu.tamu.app.model;

import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.validation.ServiceValidator;
import edu.tamu.framework.model.BaseEntity;

/**
 * @author rladdusaw
 *
 */
@Entity
public class Service extends BaseEntity {

    @Size(min = 3)
    @Column(nullable = false, unique = true)
    private String name;

    @Fetch(FetchMode.SELECT)
    @ElementCollection(fetch = EAGER)
    private List<String> aliases;

    @Column(nullable = false, unique = false)
    private Status status;

    @Column(nullable = false)
    private boolean isAuto;

    @Column(nullable = true)
    private String serviceUrl;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Boolean onShortList;
    
    @Lob
    @Column(nullable = true)
    private String description;

    @OneToMany(fetch = EAGER, cascade = { REFRESH, REMOVE }, mappedBy = "service")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, scope = Note.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Note> notes;

    public Service() {
        setModelValidator(new ServiceValidator());
        setNotes(new HashSet<Note>());
        setAliases(new ArrayList<String>());
    }

    public Service(String name, Status status, Boolean isAuto, Boolean isPublic, Boolean onShortList, String serviceUrl, String description) {
        this();
        setName(name);
        setStatus(status);
        setIsAuto(isAuto);
        setIsPublic(isPublic);
        setOnShortList(onShortList);
        setServiceUrl(serviceUrl);
        setDescription(description);
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

    public Boolean getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(Boolean isAuto) {
        this.isAuto = isAuto;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }

    public void removeNote(Note note) {
        this.notes.remove(note);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
