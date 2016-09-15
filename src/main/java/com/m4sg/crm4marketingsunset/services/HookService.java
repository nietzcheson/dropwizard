package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Hook;
import com.m4sg.crm4marketingsunset.dao.HookDAO;

import java.util.List;

/**
 * Created by Juan on 1/16/2015.
 */
public class HookService extends GenericDAOService {
    public HookService() {
        super(HookDAO.class);
    }

    public List<Hook> listHooks(){
        return hookDAO.findAll();
    }

    public Optional<Hook> getHook(long hookId) {
        return hookDAO.find(hookId);
    }
}
