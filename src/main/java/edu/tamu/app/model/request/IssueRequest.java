package edu.tamu.app.model.request;

import edu.tamu.weaver.auth.model.Credentials;

public class IssueRequest extends AbstractRequest {

    private static final long serialVersionUID = -6953745180846929244L;

    private String service;

    private Credentials credentials;

    public IssueRequest() {
        super();
    }

    public IssueRequest(ServiceRequest serviceRequest) {
        super(serviceRequest.getType(), serviceRequest.getTitle(), serviceRequest.getDescription());
    }

    public IssueRequest(RequestType type, String title, String description) {
        super(type, title, description);
    }

    public IssueRequest(RequestType type, String title, String description, String service, Credentials credentials) {
        this(type, title, description);
        this.service = service;
        this.credentials = credentials;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

}