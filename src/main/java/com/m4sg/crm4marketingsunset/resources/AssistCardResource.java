package com.m4sg.crm4marketingsunset.resources;

import com.m4c.model.entity.Reservation;
import com.m4sg.crm4marketingsunset.services.AssistCardService;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 05/10/2015.
 */
@Path("/v1/assistcard")
@Produces(MediaType.APPLICATION_JSON)
public class AssistCardResource  extends GenericResource<Reservation>  {


    public AssistCardResource(javax.validation.Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    @Path("/emitir")
    public Response emitir(@DefaultValue("0") @QueryParam("idReservacion") Long idReservation,
                           @DefaultValue("0") @QueryParam("idServicioContratado") Long idProvidedService,
                           @DefaultValue("en") @QueryParam("lang") String lang

                           ){


       return Response.ok( new AssistCardService().emitir(idReservation,idProvidedService,lang)).build();


    }
}
