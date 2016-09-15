package com.m4sg.crm4marketingsunset.core.dto;

import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.TitleService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by desarrollo1 on 07/04/2015.
 */
public class TitleDTO extends ValidateConstraint {
    private final Long id;
    @NotNull
    private final String name;

    public TitleDTO() {
        this.id=null;
        this.name=null;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    @Override
    public List<ErrorRepresentation> validate() {
        TitleService service;
        if(validationMessages.isEmpty()){
        }
        return null;
    }
}
