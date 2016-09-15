package com.m4sg.crm4marketingsunset.core.dto;

import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.Customer;

/**
 * Created by Usuario on 23/11/2015.
 */
public class CertificateCustomerDTO extends  CertificateCampaignDTO {
    public Customer customer;
    public CertificateCustomerDTO(String certificate, Campaign campaign) {
        super(certificate, campaign);
    }

    public CertificateCustomerDTO(String certificate, Campaign campaign, Customer customer) {
        super(certificate, campaign);
        this.customer = customer;
    }
}
