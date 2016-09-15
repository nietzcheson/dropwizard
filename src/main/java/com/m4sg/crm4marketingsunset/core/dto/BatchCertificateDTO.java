package com.m4sg.crm4marketingsunset.core.dto;

import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by sistemas on 12/01/2015.
 */
public class BatchCertificateDTO extends ValidateConstraint {


    @NotNull
    @Min(value = 1)
    private final Integer quantityFolios;



    public BatchCertificateDTO() {
        this.quantityFolios = null;

    }

    public int getQuantityFolios() {
        return quantityFolios;
    }



    @Override
    public List<ErrorRepresentation> validate() {
        // TODO: Use annotation, e.g. createOffer(@Valid OfferDTO offerDTO)
        // HibernateAnnotationValidator.validate(this, validator);


        return validationMessages;
    }




}
