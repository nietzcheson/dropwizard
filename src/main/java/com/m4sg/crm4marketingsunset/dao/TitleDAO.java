package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Title;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Fernando on 30/03/2015.
 */
public class TitleDAO  extends GenericDAO<Title> {

    @Override
    public List<Title> findAll() {
        return list(namedQuery("com.m4c.model.entity.Title.findAll"));
    }

    @Override
    public Optional<Title> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public Title saveOrUpdate(Title entity) {
        return null;
    }

    public TitleDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
