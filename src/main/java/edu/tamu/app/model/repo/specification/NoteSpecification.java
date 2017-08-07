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

    private Map<String, String[]> filters;

    public NoteSpecification(Map<String, String[]> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> pinnedPredicates = new ArrayList<Predicate>();
        List<Predicate> servicePredicates = new ArrayList<Predicate>();

        for (Map.Entry<String, String[]> entry : filters.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();

            switch (key) {
            case "pinned":
                for (String value : values) {
                    pinnedPredicates.add(cb.like(cb.lower(root.get(key).as(String.class)), "%" + value.toLowerCase() + "%"));
                }
                break;
            case "service":
                for (String value : values) {
                    servicePredicates.add(cb.like(cb.lower(root.get(key).get("id").as(String.class)), "%" + value.toLowerCase() + "%"));
                }
                break;
            default:
                break;
            }

        }

        return cb.and(cb.and(pinnedPredicates.toArray(new Predicate[pinnedPredicates.size()])), cb.and(servicePredicates.toArray(new Predicate[servicePredicates.size()])));
    }

}
