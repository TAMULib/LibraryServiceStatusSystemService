package edu.tamu.app.model;

import static javax.persistence.CascadeType.REFRESH;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import edu.tamu.framework.model.BaseEntity;

@Entity
public class Service extends BaseEntity {

    @Column(nullable = false)
    private String name;
    
    @ManyToMany(cascade = REFRESH)
    private List<Note> notes;

}
