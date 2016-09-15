package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.ReservationGroup;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * Created by Jhon on 30/01/2015.
 */
public class ReservationGroupDAO extends GenericDAO<ReservationGroup> {
    public ReservationGroupDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<ReservationGroup> findAll() {
        return currentSession().createCriteria(ReservationGroup.class).addOrder(Order.asc("name")).list();
    }

    @Override
    public Optional<ReservationGroup> find(long id) {
        return Optional.fromNullable(this.get(id));
    }

    @Override
    public ReservationGroup saveOrUpdate(ReservationGroup entity) {
        return null;
    }
}
