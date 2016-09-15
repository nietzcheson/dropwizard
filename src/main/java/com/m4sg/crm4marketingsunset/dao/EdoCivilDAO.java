package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.EdoCivil;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by desarrollo1 on 07/04/2015.
 */
public class EdoCivilDAO extends GenericDAO<EdoCivil> {

    @Override
    public List<EdoCivil> findAll() {
        return list(namedQuery("com.m4c.model.entity.EdoCivil.findAll"));
    }

    public Optional<EdoCivil> findID(String id) {
        return Optional.fromNullable(get(id));
    }
    @Override
    public Optional<EdoCivil> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public EdoCivil saveOrUpdate(EdoCivil entity) {
        return null;
    }

    public EdoCivilDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}

