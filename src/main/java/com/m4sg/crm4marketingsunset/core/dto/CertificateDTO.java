package com.m4sg.crm4marketingsunset.core.dto;

import com.google.common.base.Optional;
import com.m4c.model.entity.Campaign;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.CampaignService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by sistemas on 12/01/2015.
 */
public class CertificateDTO extends ValidateConstraint {


    @NotNull
    @Min(value = 1)
    private final Integer quantityFolios;

    private Long campaignId;

    public CertificateDTO() {
        this.quantityFolios = null;
        this.campaignId= null;
    }

    public int getQuantityFolios() {
        return quantityFolios;
    }


    public long getCampaignId() {
        return campaignId;
    }
    @Override
    public List<ErrorRepresentation> validate() {
        // TODO: Use annotation, e.g. createOffer(@Valid OfferDTO offerDTO)
        // HibernateAnnotationValidator.validate(this, validator);
        if(validationMessages.isEmpty()) {// Validaciones de existencia de información
            CampaignService campaignService=new CampaignService();
            Optional<Campaign> OptionalCampaign=campaignService.findById(getCampaignId());
            evaluateError(OptionalCampaign, "campaign", "campaign does not exist");

        }

        return validationMessages;
    }




}
