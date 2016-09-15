package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.EdoCivil;
import com.m4sg.crm4marketingsunset.dao.EdoCivilDAO;

import java.util.List;

/**
 * Created by desarrollo1 on 07/04/2015.
 */
public class EdoCivilService extends GenericDAOService {
    public EdoCivilService() {
        super(EdoCivilDAO.class);
    }

    public Optional<EdoCivil> find(String id)
    {
        return edoCivilDAO.findID(id);
    }
    public Optional<EdoCivil> getEdoCivil(String id){
        return edoCivilDAO.findID(id);
    }
    public List<EdoCivil> listEdoCivil(){
        return edoCivilDAO.findAll();
    }
}
