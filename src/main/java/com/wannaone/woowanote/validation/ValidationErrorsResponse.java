package com.wannaone.woowanote.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorsResponse extends RestStatus {
    private List<ValidationError> errors;

    public ValidationErrorsResponse() {
        super(false);
        errors = new ArrayList<>();
    }

    public void addValidationError(ValidationError error) {
        errors.add(error);
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
