package edu.tamu.app.model;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.InheritanceType.JOINED;
import static org.hibernate.annotations.FetchMode.SELECT;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;

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
