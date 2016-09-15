package com.m4sg.crm4marketingsunset.resources;

import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.ConnectorElasticSearch;
import com.m4sg.crm4marketingsunset.services.DNCLService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.elasticsearch.action.search.SearchResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Fernando on 03/11/2015.
 */
@Path("/v1/dncl")
@Produces(MediaType.APPLICATION_JSON)
public class DNCLResource {

    @GET
    @UnitOfWork
    @Path("/iscallable")
    public Response verifyPhone(@Auth SimplePrincipal isAuthenticated, @DefaultValue("") @QueryParam("q") Long phone) {

        //SearchResponse searchResponse=new ConnectorElasticSearch().search(query);

        boolean isCallable=new DNCLService().isCallable(phone);
        return Response.ok(isCallable).build();
    }
}
