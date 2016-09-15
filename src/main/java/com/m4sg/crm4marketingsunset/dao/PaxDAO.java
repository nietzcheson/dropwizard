package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Pax;
import com.m4c.model.entity.Reservation;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Fernando on 06/10/2015.
 */
public class PaxDAO extends GenericDAO<Pax> {

    public PaxDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Pax> findAll() {
        return null;
    }

    @Override
    public Optional<Pax> find(long id) {
        return null;
    }
    public List<Pax> findByReservation(Reservation reservation){

        return criteria().add(Restrictions.eq("reservation",reservation)).list();
    }

    @Override
    public Pax saveOrUpdate(Pax entity) {
        return null;
    }
}
