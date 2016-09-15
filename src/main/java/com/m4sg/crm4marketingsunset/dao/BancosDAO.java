package com.m4sg.crm4marketingsunset.dao;

//import com.m4c.model.entity.Bank;

import com.google.common.base.Optional;
import com.m4c.model.entity.Bank;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by sistemas on 22/12/2014.
 */
public class BancosDAO  extends GenericDAO<Bank>  {

    public BancosDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Bank> findAll() {
        Criteria criteria=this.currentSession().createCriteria(Bank.class);

        criteria.add(Restrictions.eq("active",true));

        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    @Override
    public Optional<Bank> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public Bank saveOrUpdate(Bank entity) {
        return null;
    }

}
