package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Usuario on 06/07/2015.
 */
public class ResultCampaignDTO {
    private Long ID;
    private String NAME;
    private Date DATEUPDATED;
    private String CERTCUSTOMER;
    private String OFFER;

    @JsonProperty("id")
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    @JsonProperty("name")
    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @JsonProperty("dateUpdated")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Date getDATEUPDATED() {
        return DATEUPDATED;
    }

    public void setDATEUPDATED(Date DATEUPDATED) {
        this.DATEUPDATED = DATEUPDATED;
    }

    @JsonProperty("certCustomer")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getCERTCUSTOMER() {
        return CERTCUSTOMER;
    }

    public void setCERTCUSTOMER(String CERTCUSTOMER) {
        this.CERTCUSTOMER = CERTCUSTOMER;
    }

    @JsonProperty("offer")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getOFFER() {
        return OFFER;
    }

    public void setOFFER(String OFFER) {
        this.OFFER = OFFER;
    }
}
