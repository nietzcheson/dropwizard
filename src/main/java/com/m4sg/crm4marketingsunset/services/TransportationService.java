package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Transportation;
import com.m4sg.crm4marketingsunset.dao.TransportationDAO;

import java.util.List;

/**
 * Created by Juan on 1/16/2015.
 */
public class TransportationService extends GenericDAOService {

    public TransportationService() {
        super( TransportationDAO.class);
    }

    public List<Transportation> listTransportation(){
        return transportationDAO.findAll();
    }

    public Optional<Transportation> getTransportation(long transportationId) {
        return transportationDAO.find(transportationId);
    }
}
