package com.m4sg.crm4marketingsunset.core;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Optional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Created by sistemas on 13/01/2015.
 */
public abstract class ValidateConstraint{

    public abstract List<ErrorRepresentation> validate();

    @JsonIgnore
    public List<ErrorRepresentation> validationMessages = new ArrayList<ErrorRepresentation>();

    public void evaluateError(Optional optional,String propertyName,String message) {
        if (!optional.isPresent()) {
            validationMessages.add(new ErrorRepresentation(propertyName, message));
        }
    }
    public boolean isThisDateValid(String dateToValidate, String dateFromat){
        if(dateToValidate == null){
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {
            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {
            validationMessages.add(new ErrorRepresentation("date", "No valid date"));
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
