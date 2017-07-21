package edu.tamu.app.model.request;

import org.springframework.data.domain.PageRequest;

public class NotePageable extends PageRequest {

    private String sortField;
    private String sortDirection;
    
    public NotePageable() {
        super(0,10);
        this.sortField = "name";
        this.sortDirection = "asc";
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
