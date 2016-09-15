package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Language;
import com.m4sg.crm4marketingsunset.dao.LanguageDAO;

import java.util.List;

/**
 * Created by Juan on 1/28/2015.
 */
public class LanguageService extends GenericDAOService {
    public LanguageService() {
        super(LanguageDAO.class);
    }

    public List<Language> listLanguages(){
        return languageDAO.findAll();
    }

    public Optional<Language> getLanguage(String code){
        return languageDAO.find(code);
    }
}
