package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.m4c.model.entity.Segment;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.SegmentService;
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
@Path("/v1/segment")
@Produces(MediaType.APPLICATION_JSON)
public class SegmentResource extends GenericResource<Segment> {
    private SegmentService service=new SegmentService();
    public SegmentResource(Validator validator) {
        super(validator);
    }
    @GET
    @Path("/getSegments")
    @Timed
    @UnitOfWork
    public Response getSegments(@Auth SimplePrincipal isAuthenticated){
            return Response.ok(service.findByName("Certifi")).build();
    }
}
