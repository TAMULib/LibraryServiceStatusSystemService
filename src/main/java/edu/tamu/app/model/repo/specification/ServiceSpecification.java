package edu.tamu.app.model.repo.specification;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ServiceSpecification<E> extends AbstractSpecification<E> {

    public ServiceSpecification(Map<String, String[]> filters) {
        super(filters);
    }

    @Override
    protected void toPredicateDefaultQueryOrderBy(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        query.orderBy(cb.desc(root.get("name")));
    }
}
