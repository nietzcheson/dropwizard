package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.CardType;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Usuario on 21/07/2015.
 */
public class CardTypeDAO  extends GenericDAO<CardType> {
    public CardTypeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<CardType> findAll() {
        Criteria criteria=this.currentSession().createCriteria(CardType.class);

        criteria.add(Restrictions.isNotNull("alias"));

        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    @Override
    public Optional<CardType> find(long id) {
        return null;
    }

    @Override
    public CardType saveOrUpdate(CardType entity) {
        return null;
    }
}
