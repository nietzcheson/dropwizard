package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.m4c.model.entity.Features;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.FeatureService;
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
@Path("/v1/features")
@Produces(MediaType.APPLICATION_JSON)
public class FeatureResource extends GenericResource<Features> {
    FeatureService featureService = new FeatureService();
    public FeatureResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getFeatures(@Auth SimplePrincipal isAuthenticated) {
        List<Features> titles = featureService.listFeatures();
        return Response.ok(titles).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getFeature(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) {
        long idAsLong = Long.parseLong(id);
        final Optional<Features> featureOptional = featureService.getFeature(idAsLong);
        Features feature = findSafely(featureOptional, Features.TAG);
        return Response.ok(feature).build();
    }
}