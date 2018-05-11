package com.isep.recommendator.app.handler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomValidationException extends ConstraintViolationException {

    public CustomValidationException(ConstraintViolationException e) {
        super(e.getConstraintViolations());
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.add(violation.getPropertyPath() + " : " + violation.getMessage());
        }
    }
}
