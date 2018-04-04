package edu.tamu.app.model.validation;

import edu.tamu.weaver.validation.model.InputValidationType;
import edu.tamu.weaver.validation.validators.BaseModelValidator;
import edu.tamu.weaver.validation.validators.InputValidator;

public class NoteValidator extends BaseModelValidator {

    public NoteValidator() {
        String titleProperty = "title";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notes require a title", titleProperty, true));
        this.addInputValidator(new InputValidator(InputValidationType.minlength, "Note title must be at least 3 characters", titleProperty, 3));

        String serviceProperty = "service";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notes require a Service", serviceProperty, true));

        String noteTypeProperty = "noteType";
        this.addInputValidator(new InputValidator(InputValidationType.required, "Notes require a type", noteTypeProperty, true));
    }

}
