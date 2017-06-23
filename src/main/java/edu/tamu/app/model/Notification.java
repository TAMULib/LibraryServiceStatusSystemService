package edu.tamu.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import edu.tamu.framework.model.BaseEntity;

@Entity
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String body;
    
    public Notification() {}
    
    public Notification(String name, String body) {
        this();
        setName(name);
        setBody(body);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    
}
