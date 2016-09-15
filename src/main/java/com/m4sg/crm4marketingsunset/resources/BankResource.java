package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.m4c.model.entity.Bank;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.BankService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Usuario on 21/07/2015.
 */
@Path("/v1/banks")
@Produces(MediaType.APPLICATION_JSON)
public class BankResource  extends GenericResource<Bank>  {
    BankService bankService=new BankService();
    public BankResource(Validator validator) {
        super(validator);
    }

    @GET
    @Timed
    @UnitOfWork
    public Response list(

                          @Auth SimplePrincipal isAuthenticated){

        List<Bank> banks = bankService.list();
        return Response.ok(banks).build();


    }

}
