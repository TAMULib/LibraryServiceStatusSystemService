package edu.tamu.app.model.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.custom.ServiceRepoCustom;

public interface ServiceRepo extends JpaRepository<Service, Long>, ServiceRepoCustom {

    public Page<Service> findAll(Specification<Service> specification, Pageable pageable);

    public List<Service> findByIsPublicOrderByStatusDescNameAsc(Boolean isPublic);

    public List<Service> findByIsAuto(Boolean isAuto);

    public List<Service> findAllByOrderByStatusDescNameAsc();

    public Long countByStatus(Status status);

    public Long countByStatusAndIsPublic(Status status, Boolean isPublic);

    public void delete(Service service);

}
