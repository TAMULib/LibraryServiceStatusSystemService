package edu.tamu.app.model.request;

public class ProjectRequest extends AbstractRequest {

    private static final long serialVersionUID = -6953745180846929244L;

    private Long project;

    public ProjectRequest() {
        super();
    }

    public ProjectRequest(ServiceRequest serviceRequest) {
        super(serviceRequest.getType(), serviceRequest.getTitle(), serviceRequest.getDescription());
    }

    public ProjectRequest(RequestType type, String title, String description) {
        super(type, title, description);
    }

    public ProjectRequest(RequestType type, String title, String description, Long project) {
        this(type, title, description);
        this.project = project;
    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

}
