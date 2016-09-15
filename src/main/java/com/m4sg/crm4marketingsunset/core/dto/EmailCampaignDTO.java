package com.m4sg.crm4marketingsunset.core.dto;

import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.CampaignService;
import com.sun.istack.NotNull;

import java.util.List;

/**
 * Created by desarrollo1 on 07/12/2015.
 */
public class EmailCampaignDTO{
    @NotNull
    private final String subject;
    @NotNull
    private final String body;

    public EmailCampaignDTO() {
        this.subject = null;
        this.body = null;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }
}