package edu.tamu.app.model.request;

public class ServiceRequest extends AbstractRequest {

    private static final long serialVersionUID = -6953745180846929244L;

    private Long service;

    private String email;

    public ServiceRequest() {
        super();
    }

    public ServiceRequest(RequestType type, String title, String description, Long service) {
        super(type, title, description);
        setService(service);
    }

    public ServiceRequest(RequestType type, String title, String description, Long service, String email) {
        this(type, title, description, service);
        setEmail(email);
    }

    public Long getService() {
        return service;
    }

    public void setService(Long service) {
        this.service = service;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
