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

public class NoteSpecification<E> implements Specification<E> {
    
    private Map<String, String[]> filters;
    
    public NoteSpecification(Map<String, String[]> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        
        List<Predicate> datePredicates = new ArrayList<Predicate>();
//        System.out.println("entry set is: " + filters);
//        for (Map.Entry<String, String[]> entry : filters.entrySet()) {
//            String key = entry.getKey();
//            String[] values = entry.getValue();
//            
//            switch (key) {
//                case "date":
//                    for (String value : values) {
//                        datePredicates.add(cb.like(cb.lower(root.get(key).as(String.class)), value.toLowerCase()));
//                        break;
//                    }
//                default:
//                    break;
//            }
//        }
       
        return cb.and(datePredicates.toArray(new Predicate[datePredicates.size()]));
    }   
}
