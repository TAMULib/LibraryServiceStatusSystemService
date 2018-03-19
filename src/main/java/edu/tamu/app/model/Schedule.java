package edu.tamu.app.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import edu.tamu.weaver.data.model.BaseEntity;

@Entity
public class Schedule extends BaseEntity {

    @Column(nullable = false)
    private Long scheduledPostingStart;

    @Column(nullable = false)
    private Long scheduledPostingEnd;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("schedules")
    private AbstractScheduler scheduler;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    @CollectionTable(name = "schedule_data", joinColumns = @JoinColumn(name = "schedule_id"))
    private Map<String, String> scheduleData;

    public Schedule() {
        super();
        setScheduleData(new HashMap<String, String>());
    }

    public Schedule(Long start, Long end) {
        this();
        setScheduledPostingStart(start);
        setScheduledPostingEnd(end);
    }

    public Long getScheduledPostingStart() {
        return scheduledPostingStart;
    }

    public void setScheduledPostingStart(Long scheduledPostingStart) {
        this.scheduledPostingStart = scheduledPostingStart;
    }

    public Long getScheduledPostingEnd() {
        return scheduledPostingEnd;
    }

    public void setScheduledPostingEnd(Long scheduledPostingEnd) {
        this.scheduledPostingEnd = scheduledPostingEnd;
    }

    public AbstractScheduler getScheduler() {
        return scheduler;
    }

    public Map<String, String> getScheduleData() {
        return scheduleData;
    }

    public void setScheduleData(Map<String, String> scheduleData) {
        this.scheduleData = scheduleData;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(value = { @JsonSubTypes.Type(value = Note.class, name = "note"), @JsonSubTypes.Type(value = Notification.class, name = "notification"), @JsonSubTypes.Type(value = Service.class, name = "service") })
    public void setScheduler(AbstractScheduler scheduler) {
        this.scheduler = scheduler;
    }

}
