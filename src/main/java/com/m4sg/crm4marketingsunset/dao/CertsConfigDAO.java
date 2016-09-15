package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.online.CertsConfig;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Usuario on 11/05/2015.
 */
public class CertsConfigDAO extends GenericDAO<CertsConfig> {
    public CertsConfigDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<CertsConfig> findAll() {
        return null;
    }

    @Override
    public Optional<CertsConfig> find(long id) {
        return Optional.fromNullable((CertsConfig) this.currentSession().
                createCriteria(CertsConfig.class).
                add(Restrictions.eq("id", id)).setMaxResults(1).uniqueResult());
    }
    public Long findMax(){
        Long max= (Long)criteria().setProjection(Projections.max("id")).uniqueResult();
        return max+1;
    }
    public Optional<CertsConfig> findbyIdcamping(Long campaign) {
        return Optional.fromNullable((CertsConfig) this.currentSession().
                createCriteria(CertsConfig.class).
                add(Restrictions.eq("campaign", campaign)).setMaxResults(1).uniqueResult());
//        return Optional.fromNullable(get(id));
    }

    @Override
    public CertsConfig saveOrUpdate(CertsConfig entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }

}
