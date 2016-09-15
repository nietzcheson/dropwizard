package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Segment;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by sistemas on 07/01/2015.
 */
public class SegmentDAO extends GenericDAO<Segment> {

    public SegmentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Segment> findAll() {
        return currentSession().createCriteria(Segment.class).addOrder(Order.asc("name")).list();
    }

    @Override
    public Optional<Segment> find(long id) {
        return Optional.of((Segment) this.currentSession().get(Segment.class, id));
    }

    @Override
    public Segment saveOrUpdate(Segment entity) {
        return null;
    }

    public List<Segment> findByName(String name){
        Criteria criteria=this.currentSession().createCriteria(Segment.class);
        criteria.add(Restrictions.like("name", "%" + name + "%"));

        return criteria.list();

    }

    public List<Segment> findById(Long[] ids) {
        Criteria criteria=this.currentSession().createCriteria(Segment.class);
        criteria.add(Restrictions.in("id",ids));
        return criteria.list();
    }
}
