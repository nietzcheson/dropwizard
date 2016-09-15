package com.m4sg.crm4marketingsunset.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.hubspot.jackson.jaxrs.PropertyFiltering;
import com.m4c.model.entity.online.CertCustomer;
import com.m4c.model.entity.online.State;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.CertCustomerDTO;
import com.m4sg.crm4marketingsunset.core.dto.MasterBrokerDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.core.dto.ResultSearchMBDTO;
import com.m4sg.crm4marketingsunset.services.CertCustomerService;
import com.m4sg.crm4marketingsunset.services.StateService;
import com.m4sg.crm4marketingsunset.util.LoggerJsonObject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Usuario on 26/03/2015.
 */
@Path("/v1/certCustomer")
@Produces(MediaType.APPLICATION_JSON)
public class CertCustomerResource  extends GenericResource<CertCustomer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CertCustomer.class);
    // private CampaignService service=new CampaignService();
    private CertCustomerService service=new CertCustomerService();
    StateService stateService = new StateService();
    public CertCustomerResource(Validator validator) {
        super(validator);
    }
    @Context
    UriInfo uriInfo;

    @GET
    @UnitOfWork
    public Response list(@DefaultValue("") @QueryParam("q") String search,
                         @DefaultValue("10") @QueryParam("pageLength") String pageLength,
                         @DefaultValue("id") @QueryParam("orderBy") String orderBy,
                         @DefaultValue("DESC") @QueryParam("order") String order,
                         @DefaultValue("1")@QueryParam("page") String  page,
                         @Auth SimplePrincipal isAuthenticated){

        PaginationDTO<MasterBrokerDTO> paginationDTO ;

        if(!search.isEmpty()) {
            paginationDTO=service.list(search, orderBy, order, Integer.parseInt(pageLength), Integer.parseInt(page));
        }else if(!orderBy.isEmpty() && !order.isEmpty()){
            paginationDTO=service.list(orderBy,order,Integer.parseInt(pageLength), Integer.parseInt(page));
        }else{
            //paginationDTO=service.list(Integer.parseInt(pageLength), Integer.parseInt(page));
            paginationDTO=service.list(Integer.parseInt(pageLength), Integer.parseInt(page));
        }
        paginationDTO.getElements();

        return Response.ok(paginationDTO).build();
    }

    @POST
    @UnitOfWork
    public Response createCertCustomer(@Valid CertCustomerDTO certCustomerDTO,
                                       @Auth SimplePrincipal isAuthenticated)
            throws JsonProcessingException, URISyntaxException, IntrospectionException {
        LoggerJsonObject.logObject(certCustomerDTO, LOGGER);
        List<ErrorRepresentation> errorRepresentations = certCustomerDTO.validate();

        if (!errorRepresentations.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).entity(errorRepresentations).build();
        }
        CertCustomer certCustomer = service.createCertCustomer(certCustomerDTO);
        if(certCustomerDTO.getStateId()!=null &&! certCustomerDTO.getStateId().isEmpty()) {
            final Optional<State> stateOptional = stateService.getStateCompose(certCustomer.getState(), certCustomer.getCountry());
            certCustomer.setStateId(stateOptional.get().getId());
        }

        LoggerJsonObject.logObject(certCustomer, LOGGER);
        String idAsString = String.valueOf(certCustomer.getId());
        return Response.created(new URI(idAsString)).entity(certCustomer).build();

    }
    @POST
    @UnitOfWork
    @Path("/{id}")
    public Response updateHotel(@PathParam("id") String id,
                                CertCustomerDTO certCustomerDTO,
                                @Auth SimplePrincipal isAuthenticated) throws IOException, URISyntaxException {
        LoggerJsonObject.logObject(certCustomerDTO, LOGGER);
        long idAsLong = Long.parseLong(id);
        CertCustomer updatedCertCustomer = service.updateCertCustomer(idAsLong, certCustomerDTO);
        return Response.ok(updatedCertCustomer).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @PropertyFiltering
    public Response getHotel(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException {
        long idAsLong = Long.parseLong(id);
        final Optional<CertCustomer> certCustomerOptional = service.getCertCustomer(idAsLong);
        CertCustomer certCustomer = findSafely(certCustomerOptional, CertCustomer.TAG);
        final Optional<State> stateOptional=stateService.getStateCompose(certCustomer.getState(), certCustomer.getCountry());
        if(stateOptional.isPresent())
        certCustomer.setStateId(stateOptional.get().getId());
        return Response.ok(certCustomer).build();
    }
    @GET
    @UnitOfWork
    @Path("/search")
    @PropertyFiltering
    public Response search(@QueryParam("q") String term, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException {

        if(term.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorRepresentation("q","parameter required.")).build();
        }
        List<ResultSearchMBDTO> resultSearchMBDTO=service.searchMB(term);

        return Response.ok(resultSearchMBDTO).build();
    }
}
