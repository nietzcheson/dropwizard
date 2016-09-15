package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Optional;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.services.PaymentService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 20/07/2015.
 */
public class PaymentDTO  {

    private final Long terminal;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final AuditDTO audit;
    private final Double amount;
    private final Long currency;
    private final String authorizationNumber;
    private final String paymentDate;
    private final String userCommission;
    private final String userPayment;
    private final Double exchangeRate;
    private final CreditCardDTO creditCardDTO;
    private final List<PaymentServiceResponseDTO> providedServicesDTO;

    public PaymentDTO() {
        this.terminal = null;
        this.amount = null;
        this.currency = null;
        this.authorizationNumber = null;
        this.paymentDate = null;
        this.userCommission = null;
        this.userPayment = null;
        this.exchangeRate = null;

        this.creditCardDTO = null;
        this.providedServicesDTO = null;
        this.audit = null;
    }

    public PaymentDTO(Long terminal,Double amount,Long currency,
                      Double exchangeRate,String authorizationNumb,String paymentDate,
                      String userPayment,String userCommission, String name,
                      String lastName, String nCard, String cvv,
                      String exp, String typeCard,String usernameLogged) {
        this.exchangeRate = exchangeRate;
        this.userPayment = userPayment;
        this.userCommission = userCommission;
        this.paymentDate = paymentDate;
        this.authorizationNumber = authorizationNumb;
        this.currency = currency;
        this.amount = amount;
        this.terminal = terminal;
        this.creditCardDTO=new PaymentService().decryptCreditCard(new CreditCardDTO(name,lastName,nCard,cvv,exp,typeCard),usernameLogged );
        providedServicesDTO = new ArrayList<PaymentServiceResponseDTO>();
        audit = null;
    }

    public Long getTerminal() {
        return terminal;
    }

    public Double getAmount() {
        return amount;
    }

    public Long getCurrency() {
        return currency;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getUserCommission() {
        return userCommission;
    }

    public String getUserPayment() {
        return userPayment;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public CreditCardDTO getCreditCardDTO() {
        return creditCardDTO;
    }

    public AuditDTO getAudit() {
        return audit;
    }

    public List<PaymentServiceResponseDTO> getProvidedServicesDTO() {
        return providedServicesDTO;
    }

    @JsonIgnore
    public List<ErrorRepresentation> validationMessages = new ArrayList<ErrorRepresentation>();


    @JsonIgnore

    public List<ErrorRepresentation> validate() {
        if(validationMessages.isEmpty()) {// Validaciones de existencia de informaci�n
            Double amountUSD=amount;
            Double totalServicesUSD=0.0;

            if(currency==0){
                amountUSD=amount/exchangeRate;
            }


            for (PaymentServiceResponseDTO providedService:providedServicesDTO) {
                totalServicesUSD+= providedService.getAmount();
            }

            System.out.println("amountTemp "+amountUSD+ " totalServices "+totalServicesUSD);
            if(amountUSD.intValue() > totalServicesUSD.intValue()){
                System.out.println("SALUDOS");

                 evaluateError(null, "amount", "Amount paid is greater than services amount PAID "+amountUSD +" USD , SERVICES "+totalServicesUSD+" USD");
            }else if(amountUSD.intValue() < totalServicesUSD.intValue()){
                System.out.println("SALUDOS");

                 evaluateError(null, "amount", "Amount paid is less than services amount PAID "+amountUSD +" USD , SERVICES "+totalServicesUSD+" USD");
            }

        }
        return validationMessages;
    }

    public void evaluateError(Optional optional,String propertyName,String message) {

            validationMessages.add(new ErrorRepresentation(propertyName, message));
        
    }

}

