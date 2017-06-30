package edu.tamu.app.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.custom.ServiceRepoCustom;

public interface ServiceRepo extends JpaRepository<Service, Long>, ServiceRepoCustom {

    public List<Service> findByIsPublic(Boolean isPublic);
    public List<Service> findByIsAuto(Boolean isAuto);
}
