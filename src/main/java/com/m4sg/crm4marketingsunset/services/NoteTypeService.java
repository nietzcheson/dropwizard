package com.m4sg.crm4marketingsunset.services;

import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import com.m4sg.crm4marketingsunset.dao.NoteTypeDAO;

import java.util.List;

/**
 * Created by Fernando on 25/08/2015.
 */
public class NoteTypeService extends GenericDAOService {
    public NoteTypeService() {
        super(NoteTypeDAO.class);
    }

    public List<CommonDTO> list(List<Long> ignoreDeptLst){
        return noteTypeDAO.findAll(ignoreDeptLst);
    }
}
