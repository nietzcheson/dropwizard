package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.m4c.model.entity.Hook;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.HookService;
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
 * Created by Juan on 1/16/2015.
 */
@Path("/v1/hooks")
@Produces(MediaType.APPLICATION_JSON)
public class HookResource extends GenericResource<Hook> {
    public HookResource(Validator validator) {
        super(validator);
    }

    HookService hookService = new HookService();

    @GET
    @UnitOfWork
    public Response getHooks(@Auth SimplePrincipal isAuthenticated) {
        List<Hook> hooks = hookService.listHooks();
        return Response.ok(hooks).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getHook(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) {
        long idAsLong = Long.parseLong(id);
        final Optional<Hook> hookOptional = hookService.getHook(idAsLong);
        Hook hook = findSafely(hookOptional, Hook.TAG);
        return Response.ok(hook).build();
    }
}
