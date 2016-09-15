package com.m4sg.crm4marketingsunset.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.m4c.model.entity.Destination;
import com.m4c.model.entity.Offer;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.DestinationDTO;
import com.m4sg.crm4marketingsunset.services.DestinationService;
import com.m4sg.crm4marketingsunset.util.LoggerJsonObject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Juan on 1/14/2015.
 */
@Path("/v1/destinations")
@Produces(MediaType.APPLICATION_JSON)
public class DestinationResource extends GenericResource<Destination> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationResource.class);

    DestinationService destinationService = new DestinationService();

    public DestinationResource(Validator validator) {
        super(validator);
    }


    @GET
    @UnitOfWork
    public Response getDestinations(@Auth SimplePrincipal isAuthenticated) {
        List<Destination> destinations = destinationService.listDestinations();
        return Response.ok(destinations).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getDestination(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) {
        long idAsLong = Long.parseLong(id);
        final Optional<Destination> destinationOptional = destinationService.getDestination(idAsLong);
        Destination destination = findSafely(destinationOptional, Offer.TAG);
        return Response.ok(destination).build();
    }

    @POST
    @UnitOfWork
    public Response createDestination(@Valid DestinationDTO destinationDTO, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException, URISyntaxException {
        LoggerJsonObject.logObject(destinationDTO, LOGGER);
        Destination newDestination = destinationService.createDestination(destinationDTO);
        LoggerJsonObject.logObject(newDestination, LOGGER);
        String idAsString = String.valueOf(newDestination.getId());
        return Response.created(new URI(idAsString)).entity(newDestination).build();
    }

    @POST
    @UnitOfWork
    @Path("/{id}")
    public Response updateDestination(@PathParam("id") String id, DestinationDTO destinationDTO, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException, URISyntaxException {
        long idAsLong = Long.parseLong(id);
        LoggerJsonObject.logObject(destinationDTO, LOGGER);
        Destination updatedDestination = destinationService.updateDestination(idAsLong, destinationDTO);
        LoggerJsonObject.logObject(updatedDestination, LOGGER);
        String idAsString = String.valueOf(updatedDestination.getId());
        return Response.created(new URI(idAsString)).entity(updatedDestination).build();
    }

}
