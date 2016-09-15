package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Program;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Fernando on 05/11/2015.
 */
public class ProgramDAO extends GenericDAO<Program> {
    public ProgramDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Program> findAll() {
        return null;
    }

    @Override
    public Optional<Program> find(long id) {
        return Optional.fromNullable(this.get(id));
    }

    @Override
    public Program saveOrUpdate(Program entity) {
        return null;
    }
}
