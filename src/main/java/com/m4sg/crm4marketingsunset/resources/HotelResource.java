package com.m4sg.crm4marketingsunset.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.hubspot.jackson.jaxrs.PropertyFiltering;
import com.m4c.model.entity.Hotel;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.HotelDTO;
import com.m4sg.crm4marketingsunset.services.HotelService;
import com.m4sg.crm4marketingsunset.util.Constants;
import com.m4sg.crm4marketingsunset.util.LoggerJsonObject;
import com.m4sg.crm4marketingsunset.util.StringTools;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by Juan on 1/26/2015.
 */
@Path("/v1/hotels")
@Produces(MediaType.APPLICATION_JSON)
public class HotelResource extends GenericResource<Hotel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelResource.class);

    public HotelResource(Validator validator) {
        super(validator);
    }

    HotelService hotelService = new HotelService();

    @Context
    UriInfo uriInfo;

    @POST
    @UnitOfWork
    public Response createHotel(@Valid HotelDTO hotelDTO, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException, URISyntaxException, IntrospectionException {
        LoggerJsonObject.logObject(hotelDTO, LOGGER);
        List<ErrorRepresentation> errorRepresentations = hotelDTO.validate();
        if (!errorRepresentations.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).entity(errorRepresentations).build();
        }
        Hotel newHotel = hotelService.createHotel(hotelDTO);
        LoggerJsonObject.logObject(newHotel, LOGGER);
        String idAsString = String.valueOf(newHotel.getId());
        return Response.created(new URI(idAsString)).entity(newHotel).build();
    }

    @GET
    @UnitOfWork
    @PropertyFiltering
    public Response getHotels(@Auth SimplePrincipal isAuthenticated,@DefaultValue("") @QueryParam("offerId") String offerIdParam) {
        Long offerId=!offerIdParam.isEmpty()?Long.parseLong(offerIdParam):null;
        System.out.println(""+offerId);

        List<Hotel> hotels;
        if(offerId==null) {
            hotels = hotelService.listHotels();
        }else{
            hotels = hotelService.findByOffer(offerId);
        }
        return Response.ok(hotels).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @PropertyFiltering
    public Response getHotel(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException {
        long idAsLong = Long.parseLong(id);
        final Optional<Hotel> hotelOptional = hotelService.getHotel(idAsLong);
        Hotel hotel = findSafely(hotelOptional, Hotel.TAG);
        return Response.ok(hotel).build();
    }




    @POST
    @UnitOfWork
    @Path("/{id}")
    public Response updateHotel(@PathParam("id") String id, HotelDTO hotelDTO, @Auth SimplePrincipal isAuthenticated) throws IOException, URISyntaxException {
        LoggerJsonObject.logObject(hotelDTO, LOGGER);
        long idAsLong = Long.parseLong(id);
        Hotel updatedHotel = hotelService.updateHotel(idAsLong, hotelDTO);
        LoggerJsonObject.logObject(updatedHotel, LOGGER);
        return Response.ok(updatedHotel).build();
    }

    @POST
    @UnitOfWork
    @Path("/{id}/images")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createHotelImage(
            @PathParam("id") String id,
            @FormDataParam("image") InputStream uploadedInputStream,
            @FormDataParam("image") FormDataContentDisposition fileDetail,
            @FormDataParam("image") FormDataBodyPart bodyPart) throws IOException, URISyntaxException {
        long idAsLong = Long.parseLong(id);
        LoggerJsonObject.logObject(fileDetail, LOGGER);
        if(!bodyPart.getMediaType().isCompatible(MediaType.valueOf("image/jpeg")) &&
                !bodyPart.getMediaType().isCompatible(MediaType.valueOf("image/jpg")) &&
                !bodyPart.getMediaType().isCompatible(MediaType.valueOf("image/png"))){
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("file", "File must be an image")).build();
        }
        String token=StringTools.generaToken(10);
        String fileName = token +"."+bodyPart.getMediaType().getSubtype();
        String  directory = Constants.PATH_HOTEL_IMAGES + id + "/";

        Files.createDirectories(FileSystems.getDefault().getPath(directory));
        java.nio.file.Path newPath=FileSystems.getDefault().getPath(directory,fileName);
        Files.copy(uploadedInputStream,newPath);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI imageUri = uriBuilder.path(fileName).build();
        System.out.println(""+newPath.toString());
        Hotel hotel = hotelService.createHotelImage(idAsLong,newPath.toString(),
                                                    token,
                                                    imageUri.toString());

        return Response.created(new URI(fileName)).entity(hotel).build();
    }

    @DELETE
    @UnitOfWork
    @Path("/{id}/images/{tokenImage}")
    public Response deleteHotelImage( @PathParam("id") String id, @PathParam("tokenImage") String tokenImage, @Auth SimplePrincipal isAuthenticated){
        long idAsLong = Long.parseLong(id);
        //Delete file from db
        Hotel hotel = hotelService.deleteHotelImage(idAsLong, tokenImage);

        return Response.ok(hotel).build();
    }

    @GET
    @Path("/{id}/images/{image}")
    @Produces("image/*")
    @UnitOfWork
    public Response getHotelImage( @PathParam("id") String id, @PathParam("image") String image) {

        String uploadedFileLocation = Constants.PATH_HOTEL_IMAGES + id + "/" + image;

        File file = new File(uploadedFileLocation);
        if (!file.exists()) {
            throw new NotFoundException("No such image.");
        }

        MediaType mediaType = MediaType.valueOf("image/jpeg");
        return Response.ok(file, mediaType).build();

    }

}
