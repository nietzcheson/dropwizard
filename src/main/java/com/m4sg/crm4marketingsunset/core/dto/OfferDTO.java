package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Optional;
import com.m4c.model.entity.Destination;
import com.m4c.model.entity.Hook;
import com.m4c.model.entity.MealPlan;
import com.m4c.model.entity.Transportation;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.DestinationService;
import com.m4sg.crm4marketingsunset.services.HookService;
import com.m4sg.crm4marketingsunset.services.MealPlanService;
import com.m4sg.crm4marketingsunset.services.TransportationService;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;
import java.util.Set;

/**
 * Created by Juan on 1/13/2015.
 */
public class OfferDTO extends ValidateConstraint {
    @NotBlank
    @Length(min = 2, max = 255)
    private final String name;

    @NotNull
    private final Integer mealPlanId;

    @NotNull
    private final Double activationFee;

    @NotNull
    private final Double price;

    @NotNull
    private final Double taxes;

    @NotBlank
    @Length(min = 1, max = 3)
    private final String countryCode;

    @NotNull
    private final Integer[] destinationIds;

    @NotNull
    private final Integer status;

    @NotNull
    private final Integer transportationId;

    @NotBlank
    private final String description;

    @NotNull
    private final Integer hookId;

    @NotNull
    private final Integer nights;

    @NotNull
    private final Set<OfferLanguageDTO> translations;

    public OfferDTO(String name, Integer mealPlanId, Double activationFee, Double price, Double taxes, String countryCode, Integer[] destinationIds, Integer status, Integer transportationId, String description, Integer hookId, Integer nights, Set<OfferLanguageDTO> translations) {
        this.name = name;
        this.mealPlanId = mealPlanId;
        this.activationFee = activationFee;
        this.price = price;
        this.taxes = taxes;
        this.countryCode = countryCode;
        this.destinationIds = destinationIds;
        this.status = status;
        this.transportationId = transportationId;
        this.description = description;
        this.hookId = hookId;
        this.nights = nights;
        this.translations = translations;
    }

    public OfferDTO() {
        name = null;
        mealPlanId = null;
        activationFee = null;
        taxes = null;
        countryCode = null;
        destinationIds = null;
        status = null;
        transportationId = null;
        description = null;
        hookId = null;
        price = null;
        nights = null;
        translations = null;
    }

    public String getName() {
        return name;
    }

    public Integer getMealPlanId() {
        return mealPlanId;
    }

    public Double getActivationFee() {
        return activationFee;
    }

    public Double getTaxes() {
        return taxes;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Integer[] getDestinationIds() {
        return destinationIds;
    }


    public Integer getTransportationId() {
        return transportationId;
    }

    public String getDescription() {
        return description;
    }

    public Integer getHookId() {
        return hookId;
    }


    public Double getPrice() {
        return price;
    }

    @JsonIgnore
    @Override
    public List<ErrorRepresentation> validate() {

        if(validationMessages.isEmpty()) {
            MealPlanService mealPlanService = new MealPlanService();
            DestinationService destinationService = new DestinationService();
            HookService hookService = new HookService();

            TransportationService transportationService = new TransportationService();

            Optional<MealPlan> mealPlanOptional = mealPlanService.getMealPlan(mealPlanId);

            for (int i = 0; i < destinationIds.length; i++) {
                Optional<Destination> destinationOptional = destinationService.getDestination(destinationIds[0]);
                evaluateError(destinationOptional, "destinationIds", "Destination with the id " + destinationIds[i] + " does not exist");
            }

            Optional<Hook> hookOptional = hookService.getHook(hookId);
            Optional<Transportation> transportationOptional = transportationService.getTransportation(transportationId);

            evaluateError(mealPlanOptional, "mealPlanId", "MealPlan does not exist");

            evaluateError(hookOptional, "hookId", "Hook does not exist");
            evaluateError(transportationOptional, "hookId", "Hook does not exist");

        }
        return validationMessages;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getNights() {
        return nights;
    }

    public Set<OfferLanguageDTO> getTranslations() {
        return translations;
    }
}
