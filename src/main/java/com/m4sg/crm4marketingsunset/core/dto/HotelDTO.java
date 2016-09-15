package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Optional;
import com.m4c.model.entity.Destination;
import com.m4c.model.entity.online.Country;
import com.m4c.model.entity.online.State;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.CountryService;
import com.m4sg.crm4marketingsunset.services.DestinationService;
import com.m4sg.crm4marketingsunset.services.StateService;

import java.util.List;
import java.util.Set;

/**
 * Created by Juan on 2/5/2015.
 */
public class HotelDTO extends ValidateConstraint {

    private final String name;
    private final Long destinationId;
    private final String countryCode;
    private final Long stateId;
    private final String city;
    private final String zipCode;
    private final String address;
    private final String website;
    private final Set<HotelLanguageDTO> translations;

    public HotelDTO(String name, Long destinationId, String countryCode, Long stateId, String city, String zipCode, String address, Set<HotelLanguageDTO> translations,String website) {
        this.name = name;
        this.destinationId = destinationId;
        this.countryCode = countryCode;
        this.stateId = stateId;
        this.city = city;
        this.zipCode = zipCode;
        this.address = address;
        this.translations = translations;
        this.website=website;
    }

    public HotelDTO() {
        name = null;
        countryCode = null;
        stateId = null;
        city = null;
        zipCode = null;
        address = null;
        destinationId = null;
        translations = null;
        website=null;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Long getStateId() {
        return stateId;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getAddress() {
        return address;
    }
    public String getWebsite() {
        return website;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public Set<HotelLanguageDTO> getTranslations() {
        return translations;
    }

    @JsonIgnore
    @Override
    public List<ErrorRepresentation> validate() {
        if(validationMessages.isEmpty()) {// Validaciones de existencia de información
            DestinationService destinationService = new DestinationService();
            CountryService countryService = new CountryService();
            StateService stateService = new StateService();

            Optional<Destination> destinationOptional = destinationService.getDestination(getDestinationId());
            Optional<Country> optionalCountry = countryService.getCountry(getCountryCode());
            Optional<State> stateOptional = stateService.getState(getStateId());

            evaluateError(destinationOptional, "offer", "Offer does not exist");
            evaluateError(optionalCountry, "country", "Country does not exist");
            evaluateError(stateOptional, "state", "State does not exist");

        }
        return validationMessages;
    }

}
