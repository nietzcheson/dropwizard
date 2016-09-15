package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.online.Country;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by sistemas on 13/01/2015.
 */
public class CountryDAO extends GenericDAO<Country> {
    public CountryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Country> findAll() {
        return list(namedQuery("com.m4c.model.entity.Country.findAll"));
    }

    @Override
    public Optional<Country> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public Optional<Country> find(String code) {
        return Optional.fromNullable((Country)criteria().add(Restrictions.ilike("code", code)).uniqueResult());
    }
    @Override
    public Country saveOrUpdate(Country entity) {
        return null;
    }
}
