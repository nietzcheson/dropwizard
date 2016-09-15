package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.m4c.model.entity.Transportation;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.TransportationService;
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
 * Created by Juan on 1/14/2015.
 */
@Path("/v1/transportation")
@Produces(MediaType.APPLICATION_JSON)
public class TransportationResource extends GenericResource<Transportation> {
    public TransportationResource(Validator validator) {
        super(validator);
    }
    TransportationService transportationService = new TransportationService();

    @GET
    @UnitOfWork
    public Response getTransportationList(@Auth SimplePrincipal isAuthenticated) {
        List<Transportation> transportationList = transportationService.listTransportation();
        return Response.ok(transportationList).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getTransportation(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) {
        long idAsLong = Long.parseLong(id);
        final Optional<Transportation> transportationOptional = transportationService.getTransportation(idAsLong);
        Transportation transportation = findSafely(transportationOptional, Transportation.TAG);
        return Response.ok(transportation).build();
    }
}
