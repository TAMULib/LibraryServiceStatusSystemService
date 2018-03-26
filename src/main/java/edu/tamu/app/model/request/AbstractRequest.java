package edu.tamu.app.model.request;

import java.io.Serializable;

public abstract class AbstractRequest implements Serializable {

    private static final long serialVersionUID = 5685012937446553807L;

    private RequestType type;

    private String title;

    private String description;

    public AbstractRequest() {
        super();
    }

    public AbstractRequest(RequestType type, String title, String description) {
        super();
        this.type = type;
        this.title = title;
        this.description = description;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static enum RequestType {

        FEATURE("feature"), ISSUE("issue");

        private String name;

        RequestType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
