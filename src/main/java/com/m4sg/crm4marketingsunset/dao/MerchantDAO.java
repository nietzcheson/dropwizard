package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Merchant;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by sistemas on 07/01/2015.
 */
public class MerchantDAO extends GenericDAO<Merchant> {

    public MerchantDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Merchant> findAll() {
        return currentSession().createCriteria(Merchant.class).addOrder(Order.asc("description")).list();
    }

    @Override
    public Optional<Merchant> find(long id) {
        return null;
    }
    public Optional<Merchant> findByClave(String code){
        return Optional.fromNullable((Merchant) this.currentSession().
                createCriteria(Merchant.class).
                add(Restrictions.eq("clave", code))
                .uniqueResult());
    }

    @Override
    public Merchant saveOrUpdate(Merchant entity) {
        return null;
    }
    public List<Merchant> findByName(String name){
        Criteria criteria=this.currentSession().createCriteria(Merchant.class);
        criteria.add(Restrictions.like("description", "%" + name + "%"));

        return criteria.list();

    }
}
