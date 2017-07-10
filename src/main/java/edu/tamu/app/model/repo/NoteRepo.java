package edu.tamu.app.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tamu.app.model.Note;
import edu.tamu.app.model.repo.custom.NoteRepoCustom;

public interface NoteRepo extends JpaRepository<Note, Long>, NoteRepoCustom {
    public void delete(Note note);    
}
