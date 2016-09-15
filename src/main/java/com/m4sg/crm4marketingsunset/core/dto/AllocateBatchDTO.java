package com.m4sg.crm4marketingsunset.core.dto;

import com.google.common.base.Optional;
import com.m4c.model.entity.Campaign;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.CampaignService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by desarrollo1 on 16/06/2015.
 */
public class AllocateBatchDTO extends ValidateConstraint    {

    @NotNull
    private Long idCampaign;

    @NotNull
    private Long start;

    @NotNull
    private Long end;


    public AllocateBatchDTO() {
        idCampaign = null;
        start = null;
        end = null;
    }

    public Long getIdCampaign() {
        return idCampaign;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public void setIdCampaign(Long idCampaign) {
        this.idCampaign = idCampaign;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    @Override
    public List<ErrorRepresentation> validate() {
        if (validationMessages.isEmpty()) {// Validaciones de existencia de información
            CampaignService campaignService= new CampaignService();
            Optional<Campaign> optionalCampaign= campaignService.findById(getIdCampaign());
            if(!optionalCampaign.isPresent()) {
                validationMessages.add(new ErrorRepresentation("name", "Campaign doesn't exist"));
            }
            if(getStart() > getEnd()){
                validationMessages.add(new ErrorRepresentation("Range", "End is bigger than Begin"));
            }

        }
        return validationMessages;
    }
}
