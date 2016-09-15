package com.m4sg.crm4marketingsunset.core.dto;

import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;

import java.util.List;

/**
 * Created by Usuario on 11/05/2015.
 */
public class CertsConfigDTO extends ValidateConstraint {

    public Long campania;
    public String imageCert;
    public String certificateType;
    public String imageCustomer;
    public String certCustomerId;

    public Long getCampania() {
        return campania;
    }

    public void setCampania(Long campania) {
        this.campania = campania;
    }

    public String getImageCert() {
        return imageCert;
    }

    public void setImageCert(String imageCert) {
        this.imageCert = imageCert;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getImageCustomer() {
        return imageCustomer;
    }

    public void setImageCustomer(String imageCustomer) {
        this.imageCustomer = imageCustomer;
    }

    public String getCertCustomerId() {
        return certCustomerId;
    }

    public void setCertCustomerId(String certCustomerId) {
        this.certCustomerId = certCustomerId;
    }

    @Override
    public List<ErrorRepresentation> validate() {
        return validationMessages;
    }
}
