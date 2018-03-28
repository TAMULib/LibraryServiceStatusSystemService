package edu.tamu.app.model.repo.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public abstract class AbstractSpecification<E> implements Specification<E> {

    protected Map<String, String[]> filters;

    public AbstractSpecification(Map<String, String[]> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();
        List<Predicate> repeatPredicates = new ArrayList<Predicate>();

        List<String> keysUsed = new ArrayList<String>();

        for (Map.Entry<String, String[]> entry : filters.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();

            switch (key) {
            case "service":
                for (String value : values) {
                    if (keysUsed.contains(key)) {
                        repeatPredicates.add(cb.like(cb.lower(root.get(key).get("id").as(String.class)), "%" + value.toLowerCase() + "%"));
                    } else {
                        predicates.add(cb.like(cb.lower(root.get(key).get("id").as(String.class)), "%" + value.toLowerCase() + "%"));
                        keysUsed.add(key);
                    }
                }
                break;
            case "service.name":
                for (String value : values) {
                    if (keysUsed.contains(key)) {
                        repeatPredicates.add(cb.like(cb.lower(root.get("service").get("name").as(String.class)), "%" + value.toLowerCase() + "%"));
                    } else {
                        predicates.add(cb.like(cb.lower(root.get("service").get("name").as(String.class)), "%" + value.toLowerCase() + "%"));
                        keysUsed.add(key);
                    }
                }
                break;
            default:
                for (String value : values) {
                    if (keysUsed.contains(key)) {
                        repeatPredicates.add(cb.like(cb.lower(root.get(key).as(String.class)), "%" + value.toLowerCase() + "%"));
                    } else {
                        predicates.add(cb.like(cb.lower(root.get(key).as(String.class)), "%" + value.toLowerCase() + "%"));
                        keysUsed.add(key);
                    }

                }
                break;
            }

            keysUsed.add(key);
        }

        query.orderBy(cb.desc(root.get("lastModified")));

        Predicate predicate;

        if (repeatPredicates.size() > 0) {
            predicate = cb.or(cb.and(predicates.toArray(new Predicate[predicates.size()])), cb.or(repeatPredicates.toArray(new Predicate[repeatPredicates.size()])));
        } else {
            predicate = cb.and(predicates.toArray(new Predicate[predicates.size()]));
        }

        return predicate;
    }

}
