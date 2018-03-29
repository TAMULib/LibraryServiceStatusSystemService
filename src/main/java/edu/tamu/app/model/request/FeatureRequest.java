package edu.tamu.app.model.request;

import edu.tamu.app.model.FeatureProposal;

public class FeatureRequest extends AbstractRequest {

    private static final long serialVersionUID = -6953745180846929244L;

    private Long projectId;

    public FeatureRequest() {
        super();
    }

    public FeatureRequest(ServiceRequest serviceRequest) {
        super(serviceRequest.getType(), serviceRequest.getTitle(), serviceRequest.getDescription());
    }

    public FeatureRequest(RequestType type, String title, String description) {
        super(type, title, description);
    }

    public FeatureRequest(RequestType type, String title, String description, Long projectId) {
        this(type, title, description);
        this.projectId = projectId;
    }

    public FeatureRequest(FeatureProposal proposal) {
        this(RequestType.FEATURE, proposal.getTitle(), proposal.getDescription(), proposal.getService().getProjectId());
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

}
