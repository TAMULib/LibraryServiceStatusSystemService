package edu.tamu.app.model.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.tamu.app.model.Idea;
import edu.tamu.app.model.repo.custom.IdeaRepoCustom;
import edu.tamu.weaver.data.model.repo.WeaverRepo;

public interface IdeaRepo extends WeaverRepo<Idea>, IdeaRepoCustom, JpaSpecificationExecutor<Idea> {

    public Page<Idea> findAll(Specification<Idea> specification, Pageable pageable);

    public List<Idea> findAllByServiceId(Long id);

}
