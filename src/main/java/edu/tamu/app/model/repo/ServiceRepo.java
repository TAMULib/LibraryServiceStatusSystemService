package edu.tamu.app.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.custom.ServiceRepoCustom;

public interface ServiceRepo extends JpaRepository<Service, Long>, ServiceRepoCustom {

}
