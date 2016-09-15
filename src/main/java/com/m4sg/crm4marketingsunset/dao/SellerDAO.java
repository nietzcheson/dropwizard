package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Sellers;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Fernando on 14/08/2015.
 */
public class SellerDAO extends GenericDAO<Sellers>{
    public SellerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<Sellers> findAll(List<Long> callCenterLst,List<String>typeLst){

        StringBuilder hql=new StringBuilder();
        Query query;
        
        hql.append("select s from Sellers s ");
        hql.append("where s.isActive=true ");
        if(!callCenterLst.isEmpty()) {
            hql.append("and s.idCallCenter in (:callCenters) ");
        }if(!typeLst.isEmpty()) {
            hql.append("and s.sellerType in (:types)");
        }
        hql.append("order by s.name ");

        query=currentSession().createQuery(hql.toString());
        if(!typeLst.isEmpty()) {
            query.setParameterList("types", typeLst);
        }
        if(!callCenterLst.isEmpty()) {
            query.setParameterList("callCenters", callCenterLst);
        }

        return query.list();
    }

    @Override
    public List<Sellers> findAll() {
        return null;
    }

    @Override
    public Optional<Sellers> find(long id) {
        return null;
    }
    public Optional<Sellers> find(String id) {
        return Optional.fromNullable((Sellers)criteria().add(Restrictions.ilike("id",id)).uniqueResult());
    }

    @Override
    public Sellers saveOrUpdate(Sellers entity) {
        return null;
    }
}
