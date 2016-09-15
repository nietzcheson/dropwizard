package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.DNCL;
import com.m4sg.crm4marketingsunset.dao.DNCLDAO;

/**
 * Created by Fernando on 05/11/2015.
 */
public class DNCLService  extends GenericDAOService {
    public DNCLService() {
        super(DNCLDAO.class);
    }

    public boolean isCallable(long phone){

        return dnclDAO.find(phone).isPresent()?false:true;

    }


}
