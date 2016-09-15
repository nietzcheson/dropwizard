package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.DNCL;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Fernando on 05/11/2015.
 */
public class DNCLDAO extends GenericDAO<DNCL> {
    public DNCLDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<DNCL> findAll() {
        return null;
    }

    @Override
    public Optional<DNCL> find(long id) {
        return Optional.fromNullable(this.get(id));
    }

    @Override
    public DNCL saveOrUpdate(DNCL entity) {
        return null;
    }
}
