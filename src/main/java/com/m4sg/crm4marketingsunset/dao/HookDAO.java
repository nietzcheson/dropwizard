package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Hook;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Juan on 1/16/2015.
 */
public class HookDAO extends GenericDAO<Hook> {

    public HookDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Hook> findAll() {
        return list(namedQuery("com.m4c.model.entity.Hook.findAll"));
    }

    @Override
    public Optional<Hook> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public Hook saveOrUpdate(Hook entity) {
        return null;
    }
}
