package edu.tamu.app.model.validation;

import edu.tamu.framework.enums.InputValidationType;
import edu.tamu.framework.validation.BaseModelValidator;
import edu.tamu.framework.validation.InputValidator;

public class ServiceValidator extends BaseModelValidator {

    public ServiceValidator() {
        System.out.println("validating");
        String nameProperty = "name";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Service requires a name", nameProperty, true));
        this.addInputValidator(new InputValidator(InputValidationType.minlength, "Service name must be at least 1 characters", nameProperty, 1));
        
        String statusProperty = "status";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Service requires an enum status", statusProperty, true));
        
        String isAutoProperty = "isAuto";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Service required a Boolean indicating whether it is automatically managed", isAutoProperty, true));

        String isPublicProperty = "isPublic";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Service required a Boolean indicating whether it is public", isPublicProperty, true));
        
        String onShortListProperty = "onShortList";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Service required a Boolean indicating whether it is on the short list", onShortListProperty, true));
    }
}