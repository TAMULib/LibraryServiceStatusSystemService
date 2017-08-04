package edu.tamu.app.model.request;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class FilteredPageRequest {

    private int pageNumber;

    private int pageSize;

    private String direction;

    private List<String> properties;

    private Map<String, String[]> filters;

    public FilteredPageRequest() {

    }

    public PageRequest toPageRequest() {
        return new PageRequest(pageNumber - 1, pageSize, new Sort(Sort.Direction.fromString(direction), properties));
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public Map<String, String[]> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String[]> filters) {
        this.filters = filters;
    }

}
