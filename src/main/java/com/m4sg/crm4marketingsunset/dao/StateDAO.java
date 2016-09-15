package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.online.Country;
import com.m4c.model.entity.online.State;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Juan on 1/26/2015.
 */
public class StateDAO extends GenericDAO<State> {
    public StateDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<State> findAll() {
        return list(namedQuery("com.m4c.model.entity.State.findAll"));
    }

    @Override
    public Optional<State> find(long id) {
        return Optional.fromNullable(get(id));
    }
    public Optional<State> find(String code) {
        return Optional.fromNullable((State)criteria().add(Restrictions.eq("i",code)).uniqueResult());
    }

    public Optional<State> findByName(String name) {
        return Optional.fromNullable((State)criteria().add(Restrictions.ilike("name",name)).uniqueResult());
    }
    public Optional<State> findCompose(String code,Country countryCode) {

        Criteria criteria= currentSession().createCriteria(State.class);

        if(StringUtils.isNumeric(code)){
            criteria.add(Restrictions.eq("id", new Long (code))).add((Restrictions.eq("country", countryCode))).uniqueResult();
        }else{
            criteria.add(Restrictions.eq("code", code)).add((Restrictions.eq("country", countryCode))).uniqueResult();
        }
        return Optional.fromNullable((State)criteria.uniqueResult());
    }
    public List<State> findStates(Country countryCode) {

        Criteria criteria= currentSession().createCriteria(State.class);


            //criteria.add((Restrictions.eq("country", countryCode))).add((Restrictions.ne("code","NIN")));
        criteria.add((Restrictions.eq("country", countryCode)));
        return criteria.list();
    }
    public   Optional<State> findStateNone(Country countryCode){
        Criteria criteria= currentSession().createCriteria(State.class);


        criteria.add((Restrictions.eq("country", countryCode))).add((Restrictions.eq("code","NIN")));
        //criteria.add((Restrictions.eq("country", countryCode)));
        return  Optional.fromNullable((State)criteria.uniqueResult());
    }

    @Override
    public State saveOrUpdate(State entity) {
        return null;
    }
}
