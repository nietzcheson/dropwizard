package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.m4c.model.entity.Merchant;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.MerchantService;
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
@Path("/v1/merchant")
@Produces(MediaType.APPLICATION_JSON)
public class MerchantResource extends GenericResource<Merchant> {
    private MerchantService merchantService=new MerchantService();
    public MerchantResource(Validator validator) {
        super(validator);
    }
    @GET
    @Path("/getMerchants")
    @Timed
    @UnitOfWork
    public Response getMerchants(@Auth SimplePrincipal isAuthenticated){


        return Response.ok(merchantService.findByName("Yucatan")).build();
    }
}
