package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by Fernando on 20/07/2015.
 */
public class CreditCardDTO {

    private final String name;
    private final String lastName;
    private final String nCard;
    private final String cvv;
    private final String exp;
    private final String typeCard;

    public CreditCardDTO() {
        name = null;
        lastName = null;
        nCard = null;
        cvv = null;
        exp = null;
        typeCard = null;
    }

    public CreditCardDTO(String name, String lastName, String nCard, String cvv, String exp, String typeCard) {
        this.name = name;
        this.lastName = lastName;
        this.nCard = nCard;
        this.cvv = cvv;
        this.exp = exp;
        this.typeCard = typeCard;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getnCard() {
        return nCard;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExp() {
        return exp;
    }

    public String getTypeCard() {
        return typeCard;
    }
}
