package edu.tamu.app.model.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.tamu.app.model.Note;
import edu.tamu.app.model.repo.custom.NoteRepoCustom;

public interface NoteRepo extends JpaRepository<Note, Long>, NoteRepoCustom, JpaSpecificationExecutor<Note> {

    public Page<Note> findAll(Specification<Note> specification, Pageable pageable);

    public List<Note> findAllByServiceId(Long id);

    public void delete(Note note);

}
