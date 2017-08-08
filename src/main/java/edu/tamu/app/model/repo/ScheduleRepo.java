package edu.tamu.app.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tamu.app.model.Schedule;

public interface ScheduleRepo extends JpaRepository<Schedule, Long> {

    public List<Schedule> findByScheduledPostingStartLessThanEqualAndScheduledPostingEndGreaterThanEqualAndSchedulerWithinScheduleFalse(Long before, Long after);

    public List<Schedule> findByScheduledPostingEndLessThanEqualAndSchedulerWithinScheduleTrue(Long before);

}
