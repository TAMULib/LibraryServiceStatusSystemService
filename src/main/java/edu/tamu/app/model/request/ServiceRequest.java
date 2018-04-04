package edu.tamu.app.model.request;

public class ServiceRequest extends AbstractRequest {

    private static final long serialVersionUID = -6953745180846929244L;

    private Long service;

    public ServiceRequest() {
        super();
    }

    public ServiceRequest(RequestType type, String title, String description, Long service) {
        super(type, title, description);
        this.service = service;
    }

    public Long getService() {
        return service;
    }

    public void setService(Long service) {
        this.service = service;
    }

}
