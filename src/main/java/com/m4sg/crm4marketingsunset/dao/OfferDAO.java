package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Offer;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.util.Constants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Juan on 1/12/2015.
 */
public class OfferDAO extends GenericDAO<Offer> {

    public OfferDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public PaginationDTO<Offer> findAll(int pageLength, int page, String order, String orderBy) throws QueryException {
        Criteria criteria;
        if (order.equals(Constants.ORDER_ASC)) {
            criteria = criteria().addOrder(Order.asc(orderBy));
        } else {
            // Default desc
            criteria = criteria().addOrder(Order.desc(orderBy));
        }
        return pagination(criteria, pageLength, page);
    }

    @Override
    public List<Offer> findAll() {
        return list(namedQuery("com.m4c.model.entity.Offer.findAll"));
    }

    public List<Offer> findAll(String name) {
        String hql = "from Offer where name = :name";
        Query query = currentSession().createQuery(hql);
        query.setString("name", name);
        return list(query);
    }

    @Override
    public Optional<Offer> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public Optional<Offer> find(String name) {
        String hql = "from Offer where name = :name";
        Query query = currentSession().createQuery(hql);
        query.setString("name", name);
        query.setMaxResults(1);
        Offer offer = uniqueResult(query);
        return Optional.fromNullable(offer);
    }

    @Override
    public Offer saveOrUpdate(Offer offer) {
         this.currentSession().saveOrUpdate(offer);
        return offer;
    }

    public PaginationDTO<Offer> findAll(int pageLength, int page, String order, String orderBy, String query) throws QueryException {
        Criteria criteria;
        if (order.equals(Constants.ORDER_ASC)) {
            criteria = criteria().addOrder(Order.asc(orderBy)).add(Restrictions.ilike("name", query, MatchMode.ANYWHERE));
        } else {
            // Default desc
            criteria = criteria().addOrder(Order.desc(orderBy)).add(Restrictions.ilike("name", query, MatchMode.ANYWHERE));
        }
        return pagination(criteria, pageLength, page);
    }
}
