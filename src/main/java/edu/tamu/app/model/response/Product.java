package edu.tamu.app.model.response;

import java.io.Serializable;

public class Product implements Serializable {

    private static final long serialVersionUID = -6339048161691523620L;

    private Long id;

    private String name;

    public Product() {
        super();
    }

    public Product(Long id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
