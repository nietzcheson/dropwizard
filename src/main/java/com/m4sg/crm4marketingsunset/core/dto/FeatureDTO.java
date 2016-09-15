package com.m4sg.crm4marketingsunset.core.dto;

import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.FeatureService;

import javax.validation.constraints.NotNull;
import java.util.List;

/*import com.sun.org.apache.xalan.internal.utils.FeatureManager;*/

/**
 * Created by desarrollo1 on 07/04/2015.
 */
public class FeatureDTO extends ValidateConstraint {
    private final Long id;
    @NotNull
    private final String name;

    public FeatureDTO() {
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
        FeatureService service;
        if(validationMessages.isEmpty()){
        }
        return null;
    }


}
