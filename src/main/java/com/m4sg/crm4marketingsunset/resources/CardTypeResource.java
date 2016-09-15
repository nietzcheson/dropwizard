package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.m4c.model.entity.CardType;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.CardTypeService;
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
@Path("/v1/cardTypes")
@Produces(MediaType.APPLICATION_JSON)
public class CardTypeResource extends GenericResource<CardType> {

    CardTypeService cardTypeService=new CardTypeService();

    public CardTypeResource(Validator validator) {
        super(validator);
    }


    @GET
    @Timed
    @UnitOfWork
    public Response list(

            @Auth SimplePrincipal isAuthenticated){

        List<CardType> cardTypeList = cardTypeService.list();
        return Response.ok(cardTypeList).build();


    }
}
