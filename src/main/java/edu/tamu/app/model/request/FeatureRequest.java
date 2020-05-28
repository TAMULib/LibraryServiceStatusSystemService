package edu.tamu.app.model.request;

import edu.tamu.app.model.FeatureProposal;

public class FeatureRequest extends AbstractRequest {

    private static final long serialVersionUID = -6953745180846929244L;

    private Long productId;

    public FeatureRequest() {
        super();
    }

    public FeatureRequest(ServiceRequest serviceRequest) {
        super(serviceRequest.getType(), serviceRequest.getTitle(), serviceRequest.getDescription());
    }

    public FeatureRequest(RequestType type, String title, String description) {
        super(type, title, description);
    }

    public FeatureRequest(RequestType type, String title, String description, Long productId) {
        this(type, title, description);
        this.productId = productId;
    }

    public FeatureRequest(FeatureProposal proposal) {
        this(RequestType.FEATURE, proposal.getTitle(), proposal.getDescription(), proposal.getService().getProductId());
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
