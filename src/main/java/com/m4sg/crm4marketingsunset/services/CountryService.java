package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.online.Country;
import com.m4sg.crm4marketingsunset.dao.CountryDAO;

import java.util.List;

/**
 * Created by sistemas on 15/01/2015.
 */
public class CountryService extends GenericDAOService{

    public CountryService() {
        super(CountryDAO.class);
    }
    public Optional<Country> getCountry(String code){
        return countryDAO.find(code);
    }
    public List<Country> listCountries(){
        return countryDAO.findAll();
    }
}
