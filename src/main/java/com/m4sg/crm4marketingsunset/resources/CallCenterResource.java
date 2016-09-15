package com.m4sg.crm4marketingsunset.resources;

import com.m4c.model.entity.CallCenter;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.CallCenterService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Usuario on 21/04/2015.
 */
@Path("/v1/callCenter")
@Produces(MediaType.APPLICATION_JSON)
public class CallCenterResource extends GenericResource<CallCenter> {

    CallCenterService callCenterService =new CallCenterService();
    public CallCenterResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response list(@Auth SimplePrincipal isAuthenticated){
        List<CallCenter> paginationDTO ;
        paginationDTO=callCenterService.list();
        return Response.ok(paginationDTO).build();
    }

    @GET
    @Path("/list")
    @UnitOfWork
    public Response listBySegment(@DefaultValue("") @QueryParam("isOTA") String isOTA, @Auth SimplePrincipal isAuthenticated){
        return Response.ok(callCenterService.listBySegment(Boolean.parseBoolean(isOTA))).build();
    }
}
