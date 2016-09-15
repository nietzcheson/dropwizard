package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * Created by Fernando on 21/07/2015.
 */
public class PaymentServiceResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long idBooking;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String subService;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer adults;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long numPay;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String bank;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double amount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double amountPaid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Double remaining;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String currency;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="UTC")
    private final Date paymentDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String userPayment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String userService;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Boolean isRelated;

    public PaymentServiceResponseDTO(Long id, Long numPay, String bank,
                                     Double amount, String currency, Date paymentDate,
                                     String userPayment) {
        this.id = id;
        this.numPay = numPay;
        this.bank = bank;
        this.amount = amount;
        this.currency = currency;
        this.paymentDate = paymentDate;
        this.userPayment = userPayment;
        idBooking = null;
        userService = null;
        description = null;
        subService = null;
        adults = null;
        isRelated = null;
        amountPaid = null;
        remaining = null;
    }

    public PaymentServiceResponseDTO(Long idBooking,Long id,Integer adults,
                                     Double amount,Double paid,Double remaining,
                                     String userService,String description,
                                     String subService,Boolean isRelated
                                     ) {
        this.userService = userService;
        this.id = id;
        this.idBooking = idBooking;
        this.subService = subService;
        this.description = description;
        this.adults = adults;
        this.amount = amount;
        this.isRelated = isRelated;
        amountPaid = paid;
        this.remaining = remaining;
        paymentDate = null;
        currency = null;
        numPay = null;
        userPayment = null;
        bank = null;
    }

    public Long getId() {
        return id;
    }

    public Long getIdBooking() {
        return idBooking;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public Double getRemaining() {
        return remaining;
    }

    public String getSubService() {
        return subService;
    }

    public String getDescription() {
        return description;
    }

    public Integer getAdults() {
        return adults;
    }

    public Long getNumPay() {
        return numPay;
    }

    public String getBank() {
        return bank;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public String getUserPayment() {
        return userPayment;
    }

    public String getUserService() {
        return userService;
    }

    public Boolean getIsRelated() {
        return isRelated;
    }

    public PaymentServiceResponseDTO() {

        id = null;
        idBooking = null;
        subService = null;
        description = null;
        paymentDate = null;
        currency = null;
        numPay = null;
        userPayment = null;
        bank = null;
        adults = null;
        isRelated = null;
        amountPaid = null;
        remaining = null;
        amount=null;
        userService = null;
    }
}
