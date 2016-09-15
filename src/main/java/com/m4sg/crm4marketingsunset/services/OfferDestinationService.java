package com.m4sg.crm4marketingsunset.services;

import com.m4c.model.entity.Destination;
import com.m4c.model.entity.Offer;
import com.m4sg.crm4marketingsunset.dao.OfferDestinationDAO;

/**
 * Created by Fernando on 15/04/2015.
 */
public class OfferDestinationService extends GenericDAOService {
    public OfferDestinationService() {
        super(OfferDestinationDAO.class);
    }

    public boolean delete(Offer offer,Destination destination){

        return offerDestinationDAO.delete(offer,destination);
    }
}
