package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.MealPlan;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Juan on 1/13/2015.
 */
public class MealPlanDAO extends GenericDAO<MealPlan> {
    public MealPlanDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<MealPlan> findAll() {
        return list(namedQuery("com.m4c.model.entity.MealPlan.findAll"));
    }
    public List<MealPlan> findGeneral() {
        return list(namedQuery("com.m4c.model.entity.MealPlan.findGeneral"));
    }

    @Override
    public Optional<MealPlan> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public MealPlan saveOrUpdate(MealPlan entity) {
        return null;
    }
}
