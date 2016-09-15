package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Language;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Juan on 1/28/2015.
 */
public class LanguageDAO extends GenericDAO<Language> {

    public LanguageDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Language> findAll() {
        return list(namedQuery("com.m4c.model.entity.Language.findAll"));
    }

    @Override
    public Optional<Language> find(long id) {
        return null;
    }

    @Override
    public Language saveOrUpdate(Language entity) {
        return null;
    }

    public Optional<Language> find(String code) {
        return Optional.fromNullable(get(code));
    }
}
