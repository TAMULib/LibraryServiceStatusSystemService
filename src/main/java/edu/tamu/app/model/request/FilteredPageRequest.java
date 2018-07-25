package edu.tamu.app.model.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.model.Idea;
import edu.tamu.app.model.Note;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.specification.FeatureProposalSpecification;
import edu.tamu.app.model.repo.specification.IdeaSpecification;
import edu.tamu.app.model.repo.specification.NoteSpecification;
import edu.tamu.app.model.repo.specification.ServiceSpecification;

public class FilteredPageRequest {

    private int pageNumber;

    private int pageSize;

    private List<DirectionSort> sort;

    private Map<String, String[]> filters;

    public FilteredPageRequest() {
        sort = new ArrayList<DirectionSort>();
        filters = new HashMap<String, String[]>();
    }

    @JsonIgnore
    public NoteSpecification<Note> getNoteSpecification() {
        return new NoteSpecification<Note>(filters);
    }

    @JsonIgnore
    public IdeaSpecification<Idea> getIdeaSpecification() {
        return new IdeaSpecification<Idea>(filters);
    }

    @JsonIgnore
    public ServiceSpecification<Service> getServiceSpecification() {
        return new ServiceSpecification<Service>(filters);
    }

    @JsonIgnore
    public FeatureProposalSpecification<FeatureProposal> getFeatureProposalSpecification() {
        return new FeatureProposalSpecification<FeatureProposal>(filters);
    }

    @JsonIgnore
    public PageRequest getPageRequest() {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        sort.forEach(sort -> {
            orders.add(new Sort.Order(sort.getDirection(), sort.getProperty()));
        });
        PageRequest pageRequest;
        if (orders.isEmpty()) {
            pageRequest = new PageRequest(pageNumber > 0 ? pageNumber - 1 : 0, pageSize > 0 ? pageSize : 10);
        } else {
            pageRequest = new PageRequest(pageNumber > 0 ? pageNumber - 1 : 0, pageSize > 0 ? pageSize : 10, new Sort(orders));
        }
        return pageRequest;
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

    public List<DirectionSort> getSort() {
        return sort;
    }

    public void setSort(List<DirectionSort> sort) {
        this.sort = sort;
    }

    public Map<String, String[]> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String[]> filters) {
        this.filters = filters;
    }

}
