package com.m4sg.crm4marketingsunset.core.dto;


import com.google.common.base.Optional;
import com.m4c.model.entity.*;
import com.m4c.model.entity.online.Country;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by sistemas on 05/01/2015.
 */
public class CampaignDTO extends ValidateConstraint {

    private final Long id;
    @NotNull
    @Size(min = 5)
    private final String name;
    /*@NotNull
    @Size(min = 3)
    private final String code;*/
    @Min(1)
    private final Integer expiration;
    @NotNull
    private final String merchant;
    private final String description;
    @NotNull
    @Min(1)
    private final Long offerId;
    @NotNull
    private final String country;
    @NotNull
    @Min(1)
    private final Long segment;
    @NotNull
    @Min(1)
    private final Long callcenter;
    @NotNull
    @Min(1)
    private final Integer reservationGroup;
    private final String userToken;
    @NotNull
    @Min(value = 1)
    private final Integer typeCert;//  1- E-CERT   2- FISICOS
    @NotNull
    @Min(value = 1)
    private final Integer typeFolio; //  1- Generico  2- Unico
    private final Long cerCustomerId;
    private final  String[] brokerId;
    private final String slug;

    public CampaignDTO() {
        id = null;
        name = null;
        //code = null;
        expiration = null;
        userToken = null;
        merchant = null;
        offerId = null;
        country = null;
        segment = null;
        reservationGroup = null;
        typeCert=null;
        typeFolio=null;
        cerCustomerId=null;
        brokerId=null;
        callcenter=null;
        description=null;
        slug= null;
    }



    public String getName() {
        return name;
    }

    /*public String getCode() {
        return code;
    }*/

    public String getMerchant() {
        return merchant;
    }

    public Long getOfferId() {
        return offerId;
    }

    public String getCountry() {
        return country;
    }

    public Long getId() {
        return id;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public Long getSegment() {
        return segment;
    }

    public Integer getReservationGroup() {
        return reservationGroup;
    }

    public String getUserToken() {
        return userToken;
    }

    public Integer getTypeCert() {
        return typeCert;
    }

    public Integer getTypeFolio() {
        return typeFolio;
    }

    public Long getCerCustomerId() {
        return cerCustomerId;
    }

    public  String[] getBrokerId() {
        return brokerId;
    }

    public Long getCallcenter() {
        return callcenter;
    }
    public String getDescription() {
        return description;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public List<ErrorRepresentation> validate() {
        // TODO: Use annotation, e.g. createOffer(@Valid OfferDTO offerDTO)
        //validateHibernateAnnotation(validator, this);

        if (validationMessages.isEmpty()) {// Validaciones de existencia de información
            OfferService offerService = new OfferService();
            CountryService countryService = new CountryService();
            MerchantService merchantService = new MerchantService();
            SegmentService segmentService= new SegmentService();
            CampaignService campaignService= new CampaignService();
            CallCenterService callCenterService=new CallCenterService();

            Optional<Offer> optionalOffer = offerService.getOffer(getOfferId());
            Optional<CallCenter> optionalCallCenterOptional = callCenterService.findById(getCallcenter());
            Optional<Country> optionalCountry = countryService.getCountry(getCountry());
            Optional<Merchant> optionalMerchant= merchantService.findByClave(getMerchant());
            Optional<Segment> optionalSegment= segmentService.findById(getSegment());

            evaluateError(optionalOffer, "offer", "Offer does not exist");
            evaluateError(optionalCallCenterOptional, "callcenter", "Call center does not exist");
            evaluateError(optionalCountry, "country", "Country does not exist");
            evaluateError(optionalMerchant, "merchant", "Merchant does not exist");
            evaluateError(optionalSegment, "segment", "segment does not exist");
            if(getSlug()!= null && !getSlug().isEmpty()){
                Optional<Campaign> optionalCampaign=campaignService.findBySlug(getSlug());
                evaluateError(optionalCampaign, "slug", "Campaign code already exist");
            }
        }
        return validationMessages;
    }
    public List<ErrorRepresentation> validateUpdate() {
        CampaignService campaignService=new CampaignService();
        Optional<Campaign> optionalCampaign=campaignService.findById(getId());
        evaluateError(optionalCampaign, "campaign", "campaign does not exist");
        if(getSlug()!= null && !getSlug().isEmpty()){
            Optional<Campaign> optionalCampaignSlug=campaignService.findBySlug(getSlug());
            //evaluateError(optionalCampaignSlug, "slug", "Campaign code already exist");
            if(optionalCampaignSlug.isPresent()) {
                if(!getId().equals(optionalCampaign.get().getId()))
               validationMessages.add(new ErrorRepresentation("slug", "Campaign code already exist"));

            }
        }
        return validationMessages;
    }

}
