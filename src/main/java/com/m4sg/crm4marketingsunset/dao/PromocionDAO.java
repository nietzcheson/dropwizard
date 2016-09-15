package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Usuario on 22/06/2015.
 */
public class PromocionDAO  extends GenericDAO<PromocionDAO>{
    @Override
    public List<PromocionDAO> findAll() {
        return null;
    }

    @Override
    public Optional<PromocionDAO> find(long id) {
        return null;
    }

    @Override
    public PromocionDAO saveOrUpdate(PromocionDAO entity) {
        return null;
    }

    public PromocionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
