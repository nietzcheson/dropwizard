package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.CustomerNotePk;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by desarrollo1 on 08/05/2015.
 */
public class CustomerNotePkDAO extends GenericDAO<CustomerNotePk> {

    public CustomerNotePkDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<CustomerNotePk> findAll() {
        return null;
    }

    @Override
    public Optional<CustomerNotePk> find(long id){
        return Optional.fromNullable(get(id));
    }

    @Override
    public CustomerNotePk saveOrUpdate(CustomerNotePk entity) {
        return  null;
    }
}
