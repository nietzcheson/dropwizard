package com.m4sg.crm4marketingsunset.core.dto;

import org.hibernate.validator.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by desarrollo1 on 19/02/2016.
 */
public class SearchCertificateDTO {

    @NotEmpty
    @Length(min = 3)
    private String certificate;
    private Long[] campaigns;
    private int limit;

    public SearchCertificateDTO(){
        this.certificate=null;
        this.campaigns=null;
        this.limit= 10;
    }

    public Long[] getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(Long[] campaigns) {
        this.campaigns = campaigns;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
