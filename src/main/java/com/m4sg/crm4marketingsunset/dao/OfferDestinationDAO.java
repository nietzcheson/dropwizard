package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Destination;
import com.m4c.model.entity.Offer;
import com.m4c.model.entity.OfferDestination;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Fernando on 15/04/2015.
 */
public class OfferDestinationDAO extends GenericDAO<OfferDestination> {
    public OfferDestinationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<OfferDestination> findAll() {
        return null;
    }

    @Override
    public Optional<OfferDestination> find(long id) {
        return null;
    }

    @Override
    public OfferDestination saveOrUpdate(OfferDestination entity) {
        return null;
    }
    public boolean delete(Offer offer,Destination destination){

        StringBuilder query=new StringBuilder();

        query.append("delete from OfferDestination od where od.offerDestinationId.offer=:OFFER ");
        query.append("and od.offerDestinationId.destination=:DESTINATION ");

        Query q=currentSession().createQuery(query.toString());
        q.setParameter("OFFER",offer);
        q.setParameter("DESTINATION",destination);

        return q.executeUpdate()>0?true:false;
    }
}
