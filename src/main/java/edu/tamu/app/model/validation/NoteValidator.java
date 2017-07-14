package edu.tamu.app.model.validation;

import edu.tamu.framework.enums.InputValidationType;
import edu.tamu.framework.validation.BaseModelValidator;
import edu.tamu.framework.validation.InputValidator;

public class NoteValidator extends BaseModelValidator {

    public NoteValidator() {
        String titleProperty = "title";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notes require a title", titleProperty, true));
        this.addInputValidator(new InputValidator(InputValidationType.minlength, "Note title must be at least 3 characters", titleProperty, 3));

        String servicesProperty = "services";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notes require at least one Service", servicesProperty, true));
        
        String noteTypeProperty = "noteType";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notes require a type", noteTypeProperty, true));
    }
}
