package edu.tamu.app.model;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.EAGER;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import edu.tamu.app.model.validation.IdeaValidator;
import edu.tamu.weaver.validation.model.ValidatingBaseEntity;

@Entity
public class Idea extends ValidatingBaseEntity {
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = EAGER, cascade = MERGE, optional = false)
    private Service service;

    public Idea() {
        super();
        this.modelValidator = new IdeaValidator();
    }

}
