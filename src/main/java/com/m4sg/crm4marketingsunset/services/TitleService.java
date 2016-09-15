package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Title;
import com.m4sg.crm4marketingsunset.dao.TitleDAO;

import java.util.List;

/**
 * Created by desarrollo1 on 07/04/2015.
 */
public class TitleService extends GenericDAOService {
    public TitleService() {
        super(TitleDAO.class);
    }

    public Optional<Title> find(Long id)
    {
        return titleDAO.find(id);
    }
    public Optional<Title> getTitle(Long id){
        return titleDAO.find(id);
    }
    public List<Title> listTitles(){
        return titleDAO.findAll();
    }
}
