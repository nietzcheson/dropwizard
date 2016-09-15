package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Destination;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Juan on 1/14/2015.
 */
public class DestinationDAO extends GenericDAO<Destination> {

    public DestinationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Destination> findAll() {
        return list(namedQuery("com.m4c.model.entity.Destination.findAll"));
    }

    @Override
    public Optional<Destination> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public Optional<Destination> find(String name) {
        String hql = "from Destination where upper(name) = :name";
        Query query = currentSession().createQuery(hql);
        query.setString("name", name.toUpperCase());
        query.setMaxResults(1);
        Destination destination = uniqueResult(query);
        return Optional.fromNullable(destination);
    }

    public List<Destination> findAll(String name) {
        String hql = "from Destination where name = :name";
        Query query = currentSession().createQuery(hql);
        query.setString("name", name);
        return list(query);
    }

    @Override
    public Destination saveOrUpdate(Destination destination) {
        return persist(destination);
    }
}
