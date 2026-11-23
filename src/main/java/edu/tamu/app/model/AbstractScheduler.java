package edu.tamu.app.model;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.InheritanceType.JOINED;
import static org.hibernate.annotations.FetchMode.SELECT;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.Fetch;

import edu.tamu.weaver.validation.model.ValidatingBaseEntity;

@Entity
@Inheritance(strategy = JOINED)
public abstract class AbstractScheduler extends ValidatingBaseEntity implements Scheduler {

    @OneToMany(fetch = EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @Fetch(value = SELECT)
    private List<Schedule> schedules;

    @Column(nullable = false)
    private Boolean withinSchedule;

    public AbstractScheduler() {
        super();
        setSchedules(new ArrayList<Schedule>());
        setWithinSchedule(false);
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }

    public void removeSchedule(Schedule schedule) {
        this.schedules.remove(schedule);
    }

    public Boolean getWithinSchedule() {
        return withinSchedule;
    }

    public void setWithinSchedule(Boolean withinSchedule) {
        this.withinSchedule = withinSchedule;
    }

    public abstract String getType();

}
