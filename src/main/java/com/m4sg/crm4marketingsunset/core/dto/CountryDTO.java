package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.m4c.model.entity.online.Country;
import com.m4c.model.entity.online.State;

import java.util.List;

/**
 * Created by Usuario on 30/11/2015.
 */
public class CountryDTO {
    public Country country;
    public List<State> stateList;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @JsonProperty(value = "states")
    public List<State> getStateList() {
        return stateList;
    }

    public void setStateList(List<State> stateList) {
        this.stateList = stateList;
    }
}
