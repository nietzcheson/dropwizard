package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by Fernando on 20/07/2015.
 */
public class ProvidedServicesDTO {

    private final Long id;
    private final Double amount;

    public ProvidedServicesDTO() {
        id = null;
        amount = null;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }
}
