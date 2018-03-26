package edu.tamu.app.model.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.tamu.app.model.Idea;
import edu.tamu.app.model.repo.custom.IdeaRepoCustom;

public interface IdeaRepo extends JpaRepository<Idea, Long>, IdeaRepoCustom, JpaSpecificationExecutor<Idea> {

    public Page<Idea> findAll(Specification<Idea> specification, Pageable pageable);

    public List<Idea> findAllByServiceId(Long id);

    public void delete(Idea idea);

}
