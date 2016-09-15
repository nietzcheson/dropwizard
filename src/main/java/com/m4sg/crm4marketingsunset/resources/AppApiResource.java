package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.m4c.model.entity.AppApi;
import com.m4sg.crm4marketingsunset.services.AppApiService;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Usuario on 03/12/2015.
 */
@Path("/v1/appApi")
@Produces(MediaType.APPLICATION_JSON)
public class AppApiResource   extends GenericResource<AppApi>  {
    public AppApiResource(Validator validator) {
        super(validator);
    }

    @POST
    @Path("/{name}/create")
    @Timed
    @UnitOfWork
    public Response create(@PathParam("name") String name) throws Exception {

        AppApi appApi=new AppApiService().createAppApi(name);
        appApi.setPublicKey(appApi.generatePublicToken());
        return Response.ok(appApi).build();
    }

    @GET
    @Timed
    @UnitOfWork
    public Response getAppApi() throws Exception {

        List<AppApi> banks = new AppApiService().list();
        for (int i = 0; i <banks.size() ; i++) {
            banks.get(i).setPublicKey(banks.get(i).generatePublicToken());
        }
        return Response.ok(banks).build();


    }
}
