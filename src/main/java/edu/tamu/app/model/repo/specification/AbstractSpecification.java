package edu.tamu.app.model.repo.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

public abstract class AbstractSpecification<E> implements Specification<E> {

    protected Map<String, String[]> filters;

    public AbstractSpecification(Map<String, String[]> filters) {
        this.filters = filters;
    }

}
