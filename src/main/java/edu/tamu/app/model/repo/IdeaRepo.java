package edu.tamu.app.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.tamu.app.model.Idea;
import edu.tamu.app.model.repo.custom.IdeaRepoCustom;

public interface IdeaRepo extends JpaRepository<Idea, Long>, IdeaRepoCustom, JpaSpecificationExecutor<Idea> {

}
