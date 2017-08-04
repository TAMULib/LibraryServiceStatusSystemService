package edu.tamu.app.model.validation;

import edu.tamu.framework.enums.InputValidationType;
import edu.tamu.framework.validation.BaseModelValidator;
import edu.tamu.framework.validation.InputValidator;

public class NotificationValidator extends BaseModelValidator {
    
    public NotificationValidator() {
        String nameProperty = "name";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notifications require a name", nameProperty, true));
        this.addInputValidator(new InputValidator(InputValidationType.minlength, "Notification name must be at least 3 characters", nameProperty, 3));
        
        String bodyProperty = "body";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notifications require a body", bodyProperty, true));
        this.addInputValidator(new InputValidator(InputValidationType.minlength, "Notification body must be at least 3 characters", bodyProperty, 3));
        this.addInputValidator(new InputValidator(InputValidationType.maxlength, "Notifications must be no more than 140 cahracters", bodyProperty, 140));
        
        String isActiveProperty = "isActive";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notifcations must have a value for is active", isActiveProperty, true));
        
        String locationsProperty = "locations";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notifications must have a display location", locationsProperty, true));
    }
}