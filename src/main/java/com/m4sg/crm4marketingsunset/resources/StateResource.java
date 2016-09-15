package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.m4c.model.entity.online.Country;
import com.m4c.model.entity.online.State;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.CountryDTO;
import com.m4sg.crm4marketingsunset.services.StateService;
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
 * Created by Juan on 1/26/2015.
 */
@Path("/v1/states")
@Produces(MediaType.APPLICATION_JSON)
public class StateResource extends GenericResource<State> {

    StateService stateService = new StateService();

    public StateResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getStates(@Auth SimplePrincipal isAuthenticated /*//activar cuando se empiece a poner seguridad*/) {
        List<State> states = stateService.listStates();
        return Response.ok(states).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getState(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) {
        long idAsLong = Long.parseLong(id);
        final Optional<State> stateOptional = stateService.getState(idAsLong);
        State state = findSafely(stateOptional, State.TAG);
        return Response.ok(state).build();
    }
    @GET
    @UnitOfWork
    @Path("/country/{code}")
    public Response getCountry(@PathParam("code") String code,
                               @Auth SimplePrincipal isAuthenticated) {
        CountryDTO countryOptional = stateService.getListStates(code);

        return Response.ok(countryOptional).build();
    }
}
