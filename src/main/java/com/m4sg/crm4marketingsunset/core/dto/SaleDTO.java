package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.Valid;

import javax.validation.constraints.NotNull;


/**
 * Created by Fernando on 30/03/2015.
 */
public class SaleDTO {

    private @Valid String  certificate;
    private  String comment;
    private  Long campaignId;
    private Long bookingId;
    private Integer numberOfPayments;
    private Double downpayment;
    private String observations;
    private Long bank;
    private Integer currency;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getNumberOfPayments() {
        return numberOfPayments;
    }

    public void setNumberOfPayments(Integer numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double getDownpayment() {
        return downpayment;
    }

    public void setDownpayment(Double downpayment) {
        this.downpayment = downpayment;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

//    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getBank() {
        return bank;
    }

    public void setBank(Long bank){
        this.bank = bank;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }
}
