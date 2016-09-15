package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Bank;
import com.m4sg.crm4marketingsunset.dao.BancosDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 21/07/2015.
 */
public class BankService extends GenericDAOService {
    public BankService() {
        super(BancosDAO.class);
    }

    public List<Bank> list(){

        List<Bank> userList=new ArrayList<Bank>();

        userList=bancosDAO.findAll();
        return userList;

    }

    public Optional<Bank> find(Long id){
        return bancosDAO.find(id);
    }
}
