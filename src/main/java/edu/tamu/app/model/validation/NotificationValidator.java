package edu.tamu.app.model.validation;

import edu.tamu.weaver.validation.model.InputValidationType;
import edu.tamu.weaver.validation.validators.BaseModelValidator;
import edu.tamu.weaver.validation.validators.InputValidator;

public class NotificationValidator extends BaseModelValidator {

    public NotificationValidator() {
        String nameProperty = "name";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notifications require a name", nameProperty, true));
        this.addInputValidator(new InputValidator(InputValidationType.minlength, "Notification name must be at least 3 characters", nameProperty, 3));

        String bodyProperty = "body";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notifications require a body", bodyProperty, true));
        this.addInputValidator(new InputValidator(InputValidationType.minlength, "Notification body must be at least 3 characters", bodyProperty, 3));

        String locationsProperty = "locations";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notifications must have a display location", locationsProperty, true));
    }

}