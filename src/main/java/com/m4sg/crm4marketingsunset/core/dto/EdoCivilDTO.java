package com.m4sg.crm4marketingsunset.core.dto;

import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.EdoCivilService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by desarrollo1 on 07/04/2015.
 */
public class EdoCivilDTO extends ValidateConstraint {
    private final String id;
    @NotNull
    private final String civil;
    private final String civilEng;

    public EdoCivilDTO() {
        this.id=null;
        this.civil=null;
        this.civilEng=null;
    }

    public String getId() {
        return id;
    }
    public String getCivil() {
        return civil;
    }
    public String getCivilEng() {
        return civilEng;
    }

    @Override
    public List<ErrorRepresentation> validate() {
        EdoCivilService service;
        if(validationMessages.isEmpty()){
        }
        return null;
    }
}