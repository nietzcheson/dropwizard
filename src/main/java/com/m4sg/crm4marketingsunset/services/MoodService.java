package com.m4sg.crm4marketingsunset.services;

import com.m4sg.crm4marketingsunset.core.dto.MoodDTO;
import com.m4sg.crm4marketingsunset.dao.StatusDAO;

import java.util.List;

/**
 * Created by Fernando on 25/08/2015.
 */
public class MoodService extends GenericDAOService {
    public MoodService() {
        super(StatusDAO.class);
    }

    public List<MoodDTO> list(){

        return statusDAO.list();
    }
}
