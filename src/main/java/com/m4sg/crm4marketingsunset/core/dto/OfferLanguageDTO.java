package com.m4sg.crm4marketingsunset.core.dto;

import com.google.common.base.Optional;
import com.m4c.model.entity.Language;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;
import com.m4sg.crm4marketingsunset.services.LanguageService;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created by Juan on 19/02/2015.
 */
public class OfferLanguageDTO extends ValidateConstraint {

    @NotBlank
    @Length(min = 2, max = 7)
    private final String languageCode;
    private final String websiteDescription;
    private final String websiteDetails;
    private final String websiteTerms;

    public OfferLanguageDTO(String languageCode, String websiteDescription, String websiteDetails, String websiteTerms) {
        this.languageCode = languageCode;
        this.websiteDescription = websiteDescription;
        this.websiteDetails = websiteDetails;
        this.websiteTerms = websiteTerms;
    }

    public OfferLanguageDTO() {
        languageCode = null;
        websiteDescription = null;
        websiteDetails = null;
        websiteTerms = null;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getWebsiteDescription() {
        return websiteDescription;
    }

    public String getWebsiteDetails() {
        return websiteDetails;
    }

    public String getWebsiteTerms() {
        return websiteTerms;
    }

    @Override
    public List<ErrorRepresentation> validate() {
        if(validationMessages.isEmpty()) {
            LanguageService languageService = new LanguageService();
            Optional<Language> languageOptional = languageService.getLanguage(languageCode);
            evaluateError(languageOptional, "languageCode", "Language does not exist");
        }
        return validationMessages;
    }
}
