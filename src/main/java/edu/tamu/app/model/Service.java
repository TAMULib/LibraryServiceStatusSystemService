package edu.tamu.app.model;

import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.validation.ServiceValidator;

@Entity
public class Service extends AbstractScheduler {

    @Column(nullable = false, unique = true)
    private String name;

    @ElementCollection(fetch = EAGER)
    @Fetch(FetchMode.SELECT)
    private List<String> aliases;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Boolean isAuto;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Boolean onShortList;

    @Column(nullable = true)
    private String serviceUrl;

    @Column(columnDefinition = "text", nullable = true)
    private String description;

    @Column(nullable = true)
    private String website;

    @Column(nullable = true)
    private String software;

    @Column(nullable = true)
    private Long projectId;

    public Service() {
        super();
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

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String getType() {
        return "service";
    }

    @Override
    public void scheduleStart(Map<String, String> scheduleData) {
        setStatus(Status.valueOf(scheduleData.get("nextStatus")));
    }

    @Override
    public void scheduleEnd(Map<String, String> scheduleData) {
        setStatus(Status.valueOf(scheduleData.get("previousStatus")));
    }

}
