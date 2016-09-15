package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Program;
import com.m4sg.crm4marketingsunset.dao.ProgramDAO;

/**
 * Created by Fernando on 05/11/2015.
 */
public class ProgramService extends GenericDAOService{
    public ProgramService() {
        super(ProgramDAO.class);
    }

    public Optional<Program> find(long id){

        return programDAO.find(id);

    }



}
