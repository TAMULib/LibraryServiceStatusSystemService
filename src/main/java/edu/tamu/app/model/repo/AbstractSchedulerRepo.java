package edu.tamu.app.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tamu.app.model.AbstractScheduler;

public interface AbstractSchedulerRepo extends JpaRepository<AbstractScheduler, Long> {

}
