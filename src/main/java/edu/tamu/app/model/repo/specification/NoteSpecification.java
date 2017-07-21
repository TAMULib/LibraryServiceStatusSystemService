package edu.tamu.app.model.repo.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import edu.tamu.app.model.Note;

public class NoteSpecification implements Specification<Note> {
    
    private Map<String, String[]> filters;
    
    public NoteSpecification(Map<String, String[]> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<Note> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        
        List<Predicate> timePredicate = new ArrayList<Predicate>();
        
        
        
        return null;
    }

    
}
