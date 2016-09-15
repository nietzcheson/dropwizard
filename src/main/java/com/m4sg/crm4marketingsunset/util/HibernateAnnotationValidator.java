package com.m4sg.crm4marketingsunset.util;

import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Juan on 2/9/2015.
 */
public final class HibernateAnnotationValidator {

    private HibernateAnnotationValidator() {}
    /**
     * Valida anotaciones hibernate
     * @param object
     * @param validator
     * @return
     */

    public static List<ErrorRepresentation> validate(Object object, Validator validator) {

        List<ErrorRepresentation> validationMessages = new ArrayList<ErrorRepresentation>();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);

        for (ConstraintViolation<Object> violation : violations) {

            validationMessages.add(new ErrorRepresentation(
                    violation.getPropertyPath().toString(),
                    violation.getMessage()));
        }

        return validationMessages;
    }
}
