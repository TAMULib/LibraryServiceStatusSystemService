package edu.tamu.app.model.validation;

import edu.tamu.weaver.validation.model.InputValidationType;
import edu.tamu.weaver.validation.validators.BaseModelValidator;
import edu.tamu.weaver.validation.validators.InputValidator;

public class FeatureProposalValidator extends BaseModelValidator {

    public FeatureProposalValidator() {
        String titleProperty = "title";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Feature Proposals require a title", titleProperty, true));
        
        String serviceProperty = "service";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Feature Proposals require a Service", serviceProperty, true));
    }
}
