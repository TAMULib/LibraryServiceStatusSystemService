package edu.tamu.app.model.request;

public class IssueRequest extends AbstractRequest {

    private static final long serialVersionUID = -6953745180846929244L;

    private String service;

    public IssueRequest() {
        super();
    }

    public IssueRequest(ServiceRequest serviceRequest) {
        super(serviceRequest.getType(), serviceRequest.getTitle(), serviceRequest.getDescription());
    }

    public IssueRequest(RequestType type, String title, String description) {
        super(type, title, description);
    }

    public IssueRequest(RequestType type, String title, String description, String service) {
        this(type, title, description);
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

}
