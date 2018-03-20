package edu.tamu.app.model.request;

public class ProjectRequest {

    private RequestType type;

    private String title;

    private String description;

    private Long project;

    public ProjectRequest() {

    }

    public ProjectRequest(RequestType type, String title, String description, Long project) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.project = project;
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

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
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
