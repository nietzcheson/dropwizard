package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;

/**
 * Created by Fernando on 17/08/2015.
 */
public class SaleCertificateDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final @Valid AuditDTO audit ;
    @NotNull
    private final Long campaignId;
    private final String numCert;
    @NotBlank
    @Length(min = 3)
    private final String agent;
    @NotBlank
    @Length(min = 3)
    private final String supervisor;
    private final String checker;
    @NotNull
    private final Long status;

    private final String numRecord;
    private final String comment;
    private final Long extraBonus;
    private final String collectionDate;
    private final String collectionAgent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final CreditCardDTO creditCardDTO;

    public SaleCertificateDTO() {
        campaignId = null;
        agent = null;
        supervisor = null;
        status = null;
        numRecord = null;
        comment = null;
        extraBonus = null;
        collectionDate = null;
        collectionAgent = null;
        numCert = null;
        checker = null;
        audit = null;
        creditCardDTO=null;
    }

    public SaleCertificateDTO(Long campaignId, String numCert, String agent, String supervisor, Long status, String numRecord, String comment, Long extraBonus, String collectionDate, String collectionAgent,String checker) {
        this.campaignId = campaignId;
        this.numCert = numCert;
        this.agent = agent;
        this.supervisor = supervisor;
        this.status = status;
        this.numRecord = numRecord;
        this.comment = comment;
        this.extraBonus = extraBonus;
        this.collectionDate = collectionDate;
        this.collectionAgent = collectionAgent;
        this.checker = checker;
        creditCardDTO=null;
        audit = null;
    }

    public AuditDTO getAudit() {
        return audit;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public String getAgent() {
        return agent;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public Long getStatus() {
        return status;
    }

    public String getNumRecord() {
        return numRecord;
    }

    public String getComment() {
        return comment;
    }

    public Long getExtraBonus() {
        return extraBonus;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public String getCollectionAgent() {
        return collectionAgent;
    }

    public String getNumCert() {
        return numCert;
    }

    public String getChecker() {
        return checker;
    }

    public CreditCardDTO getCreditCardDTO() {
        return creditCardDTO;
    }
}
