package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.m4c.model.entity.Alert;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.AlertService;
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
 * Created by desarrollo1 on 17/02/2016.
 */
@Path("/v1/alerts")
@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
public class AlertResource  extends GenericResource<Alert>{
    public AlertResource(Validator validator) {
        super(validator);
    }
    private AlertService alertService=new AlertService();

    @GET
    @Path("/{user}")
    @Timed
    @UnitOfWork
    public Response getAlertByUser(@PathParam("user") String user, @Auth SimplePrincipal isAuthenticated){
        List<Alert> alerts=alertService.findAlertByUser(user);
        return Response.ok(alerts).build();
    }

}
