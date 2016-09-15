package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.online.Country;
import com.m4c.model.entity.online.State;
import com.m4sg.crm4marketingsunset.core.dto.CountryDTO;
import com.m4sg.crm4marketingsunset.dao.CountryDAO;
import com.m4sg.crm4marketingsunset.dao.StateDAO;

import java.util.List;

/**
 * Created by Juan on 1/26/2015.
 */
public class StateService extends GenericDAOService {
    public StateService() {
        super(StateDAO.class,CountryDAO.class);
    }

    public List<State> listStates(){
        return stateDAO.findAll();
    }

    public Optional<State> getState(long stateId) {
        return stateDAO.find(stateId);
    }
    public Optional<State> getState(Long code) {
        return stateDAO.find(code);
    }
    public Optional<State> getState(String name) {
        return stateDAO.findByName(name);
    }
    public Optional<State> getStateCompose(String stateId, String countryCode){

        Country country=countryDAO.find(countryCode).get();
        return stateDAO.findCompose(stateId,country);

    }

    public CountryDTO getListStates( String countryCode){

        Country country=countryDAO.find(countryCode).get();

        CountryDTO countryDTO=new CountryDTO();
        countryDTO.setCountry(country);
        countryDTO.setStateList(stateDAO.findStates(country));
        return countryDTO;

    }
}

