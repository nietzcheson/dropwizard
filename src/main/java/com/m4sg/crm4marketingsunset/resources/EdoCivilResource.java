package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.m4c.model.entity.EdoCivil;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.EdoCivilService;
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
 * Created by desarrollo1 on 07/04/2015.
 */
@Path("/v1/maritalstatus")
@Produces(MediaType.APPLICATION_JSON)
public class EdoCivilResource extends GenericResource<EdoCivil> {
    EdoCivilService edoCivilService = new EdoCivilService();
    public EdoCivilResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getEdoCivil(@Auth SimplePrincipal isAuthenticated) {
        List<EdoCivil> edocivil = edoCivilService.listEdoCivil();
        return Response.ok(edocivil).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getTitle(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated){
        final Optional<EdoCivil> titleOptional = edoCivilService.getEdoCivil(id);
        EdoCivil title = findSafely(titleOptional, EdoCivil.TAG);
        return Response.ok(title).build();
    }
}