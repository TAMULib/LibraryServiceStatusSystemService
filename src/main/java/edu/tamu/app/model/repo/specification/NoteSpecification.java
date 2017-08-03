package edu.tamu.app.model.repo.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class NoteSpecification<E> implements Specification<E> {

    @SuppressWarnings("unused")
    private Map<String, String[]> filters;

    public NoteSpecification(Map<String, String[]> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> datePredicates = new ArrayList<Predicate>();
        return cb.and(datePredicates.toArray(new Predicate[datePredicates.size()]));
    }

}
