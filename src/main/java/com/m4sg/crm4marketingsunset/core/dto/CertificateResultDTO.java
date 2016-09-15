package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by desarrollo1 on 23/03/2016.
 */
public class CertificateResultDTO {
    private String certificate;
    private String result;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public CertificateResultDTO() {
        this.certificate = null;
        this.result= null;
    }
}
