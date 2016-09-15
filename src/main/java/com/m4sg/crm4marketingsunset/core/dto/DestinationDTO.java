package com.m4sg.crm4marketingsunset.core.dto;

import org.hibernate.validator.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


/**
 * Created by Juan on 2/16/2015.
 */
public class DestinationDTO  {

    @NotBlank
    @Length(min = 2, max = 255)
    private final String name;



    private final Long stateId;

    @javax.validation.constraints.NotNull
    @Length(min = 2, max = 255)
    private final String countryId;

    @NotBlank
    @Length(min = 2, max = 2000)
    private final String description;

    @NotNull
    private final Boolean active;

    public DestinationDTO() {
        stateId = null;
        countryId=null;
        name = null;
        description = null;
        active = null;
    }

    public DestinationDTO(String name, String description, Boolean active,Long stateId,String countryId) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.stateId = stateId;
        this.countryId=countryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
    }

    public Long getStateId() {
        return stateId;
    }

    public String getCountryId() {
        return countryId;
    }
}
