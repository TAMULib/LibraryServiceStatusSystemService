package edu.tamu.app.model.repo.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FeatureProposalSpecification<E> extends AbstractSpecification<E> {

    public FeatureProposalSpecification(Map<String, String[]> filters) {
        super(filters);
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> servicePredicates = new ArrayList<Predicate>();

        for (Map.Entry<String, String[]> entry : filters.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();

            switch (key) {
            case "service":
                for (String value : values) {
                    servicePredicates.add(cb.like(cb.lower(root.get(key).get("id").as(String.class)), "%" + value.toLowerCase() + "%"));
                }
                break;
            default:
                break;
            }

        }

        query.orderBy(cb.desc(root.get("lastModified")));

        Predicate predicate = cb.and(servicePredicates.toArray(new Predicate[servicePredicates.size()]));

        return predicate;
    }

}
