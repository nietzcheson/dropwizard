package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.ECert;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by jhon on 15/05/15.
 */
public class ECertDAO  extends GenericDAO<ECert> {
    public ECertDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<ECert> findAll() {
        StringBuilder hql=new StringBuilder();
        hql.append("select e from ECert e ");
        return currentSession().createQuery(hql.toString()).list();
        //return currentSession().createCriteria(ECert.class).addOrder(Order.desc("id")).list();
    }

    @Override
    public Optional<ECert> find(long id) {
        return null;
    }

    @Override
    public ECert saveOrUpdate(ECert entity) {
        return null;
    }
}
