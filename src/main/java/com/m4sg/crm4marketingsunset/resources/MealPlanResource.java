package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.m4c.model.entity.MealPlan;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.MealPlanService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Juan on 1/13/2015.
 */
@Path("/v1/mealplans")
@Produces(MediaType.APPLICATION_JSON)
public class MealPlanResource extends GenericResource<MealPlan> {

    MealPlanService mealPlanService = new MealPlanService();

    public MealPlanResource(Validator validator) {
        super( validator);
    }

    @GET
    @UnitOfWork
    public Response getMealPlans(@Auth SimplePrincipal isAuthenticated) {
      List<MealPlan> mealPlans = mealPlanService.listMealPlans();
        return Response.ok(mealPlans).build();

    }
    @GET
    @Path("/all")
    @UnitOfWork
    public Response getMealPlansAll(@Auth SimplePrincipal isAuthenticated) {
      List<MealPlan> mealPlans = mealPlanService.listMealPlansAll();
        return Response.ok(mealPlans).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getMealPlan(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) {
        long idAsLong = Long.parseLong(id);
        final Optional<MealPlan> mealPlanOptional = mealPlanService.getMealPlan(idAsLong);
        MealPlan mealPlan = findSafely(mealPlanOptional, MealPlan.TAG);
        return Response.ok(mealPlan).build();
    }
}
