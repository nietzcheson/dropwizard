package com.m4sg.crm4marketingsunset.resources;

import com.m4c.model.entity.ReservationGroup;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.ReservationGroupService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Fernando on 11/02/2015.
 */
@Path("/v1/reservationGroup")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationGroupResource extends GenericResource<ReservationGroup> {

    private ReservationGroupService reservationGroupService=new ReservationGroupService();
    public ReservationGroupResource(Validator validator) {
        super(validator);
    }
    @GET
    @UnitOfWork
    @Path("/getReservationGroups")
    public Response getReservationGroups(@Auth SimplePrincipal isAuthenticated){
        return Response.ok(reservationGroupService.findAll()).build();
    }
}
