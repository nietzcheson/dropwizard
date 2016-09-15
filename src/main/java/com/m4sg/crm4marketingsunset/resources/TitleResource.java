package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.m4c.model.entity.Title;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.TitleService;
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
@Path("/v1/titles")
@Produces(MediaType.APPLICATION_JSON)
public class TitleResource extends GenericResource<Title> {
    TitleService titleService = new TitleService();
    public TitleResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getTitles(@Auth SimplePrincipal isAuthenticated) {
        List<Title> titles = titleService.listTitles();
        return Response.ok(titles).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getTitle(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated){
        long idAsLong = Long.parseLong(id);
        final Optional<Title> titleOptional = titleService.getTitle(idAsLong);
        Title title = findSafely(titleOptional, Title.TAG);
        return Response.ok(title).build();
    }
}
