package com.m4sg.crm4marketingsunset.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.hubspot.jackson.jaxrs.PropertyFiltering;
import com.m4c.model.entity.Offer;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.OfferDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.services.HotelService;
import com.m4sg.crm4marketingsunset.services.OfferService;
import com.m4sg.crm4marketingsunset.util.Constants;
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
 * Created by Juan on 1/12/2015.
 */
@Path("/v1/offers")
@Produces(MediaType.APPLICATION_JSON)
public class OfferResource extends GenericResource<Offer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferResource.class);


    OfferService offerService = new OfferService();
    HotelService hotelService=new HotelService();

    public OfferResource(Validator validator) {
        super(validator);
    }

    @POST
    @UnitOfWork
    public Response createOffer(@Valid OfferDTO offerDTO, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException, URISyntaxException {
        LoggerJsonObject.logObject(offerDTO, LOGGER);
        List<ErrorRepresentation> errorRepresentations = offerDTO.validate();
        if(!errorRepresentations.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).entity(errorRepresentations).build();
        }
        Offer newOffer;
        newOffer = offerService.createOffer(offerDTO);
        String idAsString = String.valueOf(newOffer.getId());
        return Response.created(new URI(idAsString)).entity(newOffer).build();
    }

    @POST
    @UnitOfWork
    @Path("/{id}")
    public Response updateOffer(@PathParam("id") String id, OfferDTO offerDTO,
                                @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException, URISyntaxException {
        long idAsLong = Long.parseLong(id);
//        LoggerJsonObject.logObject(offerDTO, LOGGER);
        Offer updatedOffer = offerService.updateOffer(idAsLong, offerDTO);
//        LoggerJsonObject.logObject(updatedOffer, LOGGER);
        return Response.ok(updatedOffer).build();
    }

    @GET
    @UnitOfWork
    @PropertyFiltering
    public Response getOffers(
            @DefaultValue("10") @QueryParam("pageLength") String pageLength,
            @DefaultValue("1") @QueryParam("page") String  page,
            @DefaultValue(Constants.ORDER_DESC) @QueryParam("order") String order,
            @DefaultValue("id") @QueryParam("orderBy") String orderBy,
            @DefaultValue("") @QueryParam("q") String query,
            @Auth SimplePrincipal isAuthenticated) {
        int pageLengthAsInt = Integer.parseInt(pageLength);
        int pageAsInt = Integer.parseInt(page);
        PaginationDTO<Offer> offers;
        if(query.equals("")) {
            offers = offerService.listOffers(pageLengthAsInt, pageAsInt, order, orderBy);
        } else {
            offers = offerService.listOffers(pageLengthAsInt, pageAsInt, order, orderBy, query);
        }

        return Response.ok(offers).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @PropertyFiltering
    public Response getOffer(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) {
        long idAsLong = Long.parseLong(id);
        final Optional<Offer> offerOptional = offerService.getOffer(idAsLong);
        Offer offer = findSafely(offerOptional, Offer.TAG);
        return Response.ok(offer).build();
    }

    @GET
    @Path("/{id}/hotels")
    @UnitOfWork
    public Response getHotels( @PathParam("id") String offerIdParam, @Auth SimplePrincipal isAuthenticated) {
        long offerId = Long.parseLong(offerIdParam);
        final Optional<Offer> offerOptional = offerService.getOffer(offerId);

        if(offerOptional.isPresent()){
            return Response.ok(hotelService.getHotelsByOffer(offerOptional.get())).build();
        }
        else{
            return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("offer","Offer does not exists..")).build();
        }

    }
}
