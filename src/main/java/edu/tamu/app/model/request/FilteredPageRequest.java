package edu.tamu.app.model.request;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class FilteredPageRequest extends PageRequest {

    private static final long serialVersionUID = -190530029013941566L;
    private Map<String, String[]> filters;
    
    public FilteredPageRequest() {
        super(0, 2);
    }
    
    public FilteredPageRequest(int page, int size, Sort.Direction direction, String properties, Map<String, String[]> filters) {
        super(page, size, direction, properties);
        setFilters(filters);
    }

    public Map<String, String[]> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String[]> filters) {
        this.filters = filters;
    }
}
