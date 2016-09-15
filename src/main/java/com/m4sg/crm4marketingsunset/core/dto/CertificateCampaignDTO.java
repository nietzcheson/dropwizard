package com.m4sg.crm4marketingsunset.core.dto;

import com.m4c.model.entity.Campaign;

/**
 * Created by jhon on 04/05/15.
 */
public class CertificateCampaignDTO {

    private String certificate;
    private Campaign campaign;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public CertificateCampaignDTO(String certificate, Campaign campaign) {
        this.certificate = certificate;
        this.campaign = campaign;
    }
}
