package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Optional;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;

import java.util.List;

/**
 * Created by Usuario on 31/03/2015.
 */
public class CertLoginDTO  extends ValidateConstraint {
    private final String user;
    private final String campana;
    private final String name;
    private final String password;
    private final String email;

    private final String certCustomerId;

    public CertLoginDTO() {
        user=null;
        campana=null;
        name=null;
        password=null;
        email=null;
        certCustomerId=null;
    }

    public CertLoginDTO(String user, String campana, String name, String password, String email, String certCustomerId) {
        this.user = user;
        this.campana = campana;
        this.name = name;
        this.password = password;
        this.email = email;
        this.certCustomerId = certCustomerId;
    }

    public String getUser() {
        return user;
    }

    public String getCampana() {
        return campana;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCertCustomerId() {
        return certCustomerId;
    }
    @JsonIgnore
    @Override
    public List<ErrorRepresentation> validate() {
        if(validationMessages.isEmpty()) {// Validaciones de existencia de información
           /* CertLoginService campaignService=new CertLoginService();
            Optional<CertLogin> certLoginOptional=campaignService.findById(getUser());
            if(certLoginOptional.isPresent()){
                validationMessages.add(new ErrorRepresentation("userName", "Existing user "));
            }*/
            //evaluateError(certLoginOptional, "user", "User does not exist");

        }

        return validationMessages;
    }

    @Override
    public void evaluateError(Optional optional, String propertyName, String message) {
        super.evaluateError(optional, propertyName, message);
    }
}
