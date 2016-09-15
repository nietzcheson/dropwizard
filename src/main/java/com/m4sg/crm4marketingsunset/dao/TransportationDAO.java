package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Transportation;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Juan on 1/14/2015.
 */
public class TransportationDAO extends GenericDAO<Transportation> {

    public TransportationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Transportation> findAll() {
        return list(namedQuery("com.m4c.model.entity.Transportation.findAll"));
    }

    @Override
    public Optional<Transportation> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public Transportation saveOrUpdate(Transportation entity) {
        return null;
    }
}
