package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Optional;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.CertLoginService;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created by Usuario on 26/03/2015.
 */
public class CertCustomerDTO extends ValidateConstraint {

    /*private final Long id;*/

    private final String countryCode;
    private final String stateId;
    private final String city;
    private final String address;
    private final String phone;
    private final String user;
    private final String companyName;
    private final String responsableName;
    @NotBlank
    private final String password;
    private final String email;


    public CertCustomerDTO() {

        countryCode = null;
        stateId = null;
        city = null;
        address = null;
        phone = null;
        user=null;
        companyName=null;
        responsableName=null;
        password=null;
        email=null;
    }

    public CertCustomerDTO( String countryCode, String stateId, String city, String address, String phone,String user,String companyName,String responsableName,String password,String email) {

        this.countryCode = countryCode;
        this.stateId = stateId;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.user=user;
        this.companyName=companyName;
        this.responsableName=responsableName;
        this.password=password;
        this.email=email;
    }



    public String getCountryCode() {
        return countryCode;
    }

    public String getStateId() {
        return stateId;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getResponsableName() {
        return responsableName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public List<ErrorRepresentation> validate() {
        CertLoginService certLoginService=new CertLoginService();
        if(validationMessages.isEmpty()){


            /*evaluateError(certLoginOptional, "user", "Existing user ");*/
        }

        return validationMessages;
    }

    @Override
    public void evaluateError(Optional optional, String propertyName, String message) {
        super.evaluateError(optional, propertyName, message);
    }
}
