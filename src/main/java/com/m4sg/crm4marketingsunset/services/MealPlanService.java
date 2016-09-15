package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.MealPlan;
import com.m4sg.crm4marketingsunset.dao.MealPlanDAO;

import java.util.List;

/**
 * Created by Juan on 1/13/2015.
 */
public class MealPlanService  extends GenericDAOService{

    public MealPlanService() {
        super( MealPlanDAO.class);
    }

    public List<MealPlan> listMealPlans(){
        return mealPlanDAO.findGeneral();
    }

    public List<MealPlan> listMealPlansAll(){
        return mealPlanDAO.findAll();
    }

    public Optional<MealPlan> getMealPlan(long mealPlanId){
        return mealPlanDAO.find(mealPlanId);
    }
}
