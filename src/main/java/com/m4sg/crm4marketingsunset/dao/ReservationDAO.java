package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Reservation;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Fernando on 06/10/2015.
 */
public class ReservationDAO  extends GenericDAO<Reservation>  {
    public ReservationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Reservation> findAll() {
        return null;
    }

    @Override
    public Optional<Reservation> find(long id) {

            return Optional.fromNullable(get(id));

    }


    @Override
    public Reservation saveOrUpdate(Reservation entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }
}
