package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.LogM4SG;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Fernando on 27/07/2015.
 */
public class LogM4SGDAO extends GenericDAO<LogM4SG>  {

    public LogM4SGDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<LogM4SG> findAll() {
        return null;
    }

    @Override
    public Optional<LogM4SG> find(long id) {
        return null;
    }

    @Override
    public LogM4SG saveOrUpdate(LogM4SG entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }
}
