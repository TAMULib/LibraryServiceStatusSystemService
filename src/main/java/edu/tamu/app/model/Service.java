package edu.tamu.app.model;

import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

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

    @Column(nullable = false, unique = true)
    private String name;

    @ElementCollection(fetch = EAGER)
    @Fetch(FetchMode.SELECT)
    private List<String> aliases;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private boolean isAuto;

    @Column(nullable = true)
    private String serviceUrl;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Boolean onShortList;

    @Column(columnDefinition = "text", nullable = true)
    private String description;

    public Service() {
        setModelValidator(new ServiceValidator());
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
