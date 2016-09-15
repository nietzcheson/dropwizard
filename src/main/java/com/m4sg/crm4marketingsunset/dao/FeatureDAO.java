package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Features;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Fernando on 31/03/2015.
 */
public class FeatureDAO extends GenericDAO<Features> {
    public FeatureDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Features> findAll() {
        return list(namedQuery("com.m4c.model.entity.Features.findAll"));
    }

    @Override
    public Optional<Features> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public Features saveOrUpdate(Features entity) {
        return null;
    }
}
