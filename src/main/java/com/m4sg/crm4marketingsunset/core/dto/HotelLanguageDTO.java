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
 * Created by Juan on 09/03/2015.
 */
public class HotelLanguageDTO extends ValidateConstraint {
    @NotBlank
    @Length(min = 2, max = 7)
    private final String languageCode;
    private final String websiteDescription;

    public HotelLanguageDTO(String languageCode, String websiteDescription) {
        this.languageCode = languageCode;
        this.websiteDescription = websiteDescription;
    }

    public HotelLanguageDTO() {
        languageCode = null;
        websiteDescription = null;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getWebsiteDescription() {
        return websiteDescription;
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
