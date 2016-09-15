package com.m4sg.crm4marketingsunset.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.hubspot.jackson.jaxrs.PropertyFiltering;
import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.online.CertLogin;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.*;
import com.m4sg.crm4marketingsunset.services.CertLoginService;
import com.m4sg.crm4marketingsunset.services.CustomerService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Usuario on 31/03/2015.
 */
@Path("/v1/certLogin")
@Produces(MediaType.APPLICATION_JSON)
public class CertLoginResource extends GenericResource<CertLogin> {
    public CertLoginResource(Validator validator) {
        super(validator);
    }

    private CertLoginService service = new CertLoginService();
    private CustomerService customerService=new CustomerService();
    private static final Logger LOGGER = LoggerFactory.getLogger(CertLogin.class);
    @Context
    UriInfo uriInfo;

    @POST
    @UnitOfWork
    public Response createCertCustomer(@Valid CertLoginDTO certLoginDTO,
                                       @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException, URISyntaxException, IntrospectionException {

        LoggerJsonObject.logObject(certLoginDTO, LOGGER);
        List<ErrorRepresentation> errorRepresentations = certLoginDTO.validate();

        if (!errorRepresentations.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).entity(errorRepresentations).build();
        }
        CertLogin newCertCertificate = service.createCertLogin(certLoginDTO);
        //LoggerJsonObject.logObject(newCertCertificate, LOGGER);
        String idAsString = String.valueOf(newCertCertificate.getUser());
        return Response.created(new URI(idAsString)).entity(newCertCertificate).build();

    }

    @GET
    @UnitOfWork
    public Response list(@DefaultValue("") @QueryParam("q") String search,
                         @DefaultValue("10") @QueryParam("pageLength") String pageLength,
                         @DefaultValue("") @QueryParam("orderBy") String orderBy,
                         @DefaultValue("ASC") @QueryParam("order") String order,
                         @DefaultValue("1") @QueryParam("page") String page,
                         @Auth SimplePrincipal isAuthenticated) {

        PaginationDTO<CertLogin> paginationDTO;

        if (!search.isEmpty()) {
            paginationDTO = service.list(search, orderBy, order, Integer.parseInt(pageLength), Integer.parseInt(page));
        } else if (!orderBy.isEmpty() && !order.isEmpty()) {
            paginationDTO = service.list(orderBy, order, Integer.parseInt(pageLength), Integer.parseInt(page));
        } else {
            //paginationDTO=service.list(Integer.parseInt(pageLength), Integer.parseInt(page));
            paginationDTO = service.list(Integer.parseInt(pageLength), Integer.parseInt(page));
        }

        return Response.ok(paginationDTO).build();
    }

    @GET
    @Path("/Mb/{id}")
    @UnitOfWork
    public Response listMB(@DefaultValue("") @QueryParam("q") String search,
                           @DefaultValue("10") @QueryParam("pageLength") String pageLength,
                           @DefaultValue("") @QueryParam("orderBy") String orderBy,
                           @DefaultValue("ASC") @QueryParam("order") String order,
                           @DefaultValue("1") @QueryParam("page") String page,
                           @PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) {

        PaginationDTO<PreviewBrokerDTO> paginationDTO;
        long idAsLong = Long.parseLong(id);
        //paginationDTO=service.list(Integer.parseInt(pageLength), Integer.parseInt(page));
        paginationDTO = service.getCertLoginMB(Integer.parseInt(pageLength), Integer.parseInt(page), idAsLong);


        return Response.ok(paginationDTO).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @PropertyFiltering
    public Response getCertLogin(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException {
        String idAsLong = (id);
        final Optional<CertLogin> certCustomerOptional = service.getCertLogin(idAsLong);
        CertLogin certLogin = findSafely(certCustomerOptional, CertLogin.TAG);
        return Response.ok(certLogin).build();
    }

    @POST
    @UnitOfWork
    @Path("/{id}")
    public Response updateCertLogin(@PathParam("id") String id, CertLoginDTO certLoginDTO,
                                @Auth SimplePrincipal isAuthenticated) throws IOException, URISyntaxException {
        LoggerJsonObject.logObject(certLoginDTO, LOGGER);
        String idAsLong = (id);
        CertLogin updatedCertLogin = service.updateCertLogin(idAsLong, certLoginDTO);
        return Response.ok(updatedCertLogin).build();
    }

    @POST
    @UnitOfWork
    @Path("/auth")
    @PropertyFiltering
    public Response getLogin(CertLoginDTO certLoginDTO, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException {
        LoggerJsonObject.logObject(certLoginDTO, LOGGER);
        String user= certLoginDTO.getUser().isEmpty() ? "" :certLoginDTO.getUser().trim();
        String pasword= certLoginDTO.getPassword().isEmpty() ? "" :certLoginDTO.getPassword().trim();
        final Optional<CertLogin> certCustomerOptional = service.getAuthLogin(user, pasword);
        CertLogin certLogin = findSafely(certCustomerOptional, CertLogin.TAG);
        //System.out.println("Masterbroker" +certLogin.getMasterBroker());
        //System.out.println("Broker" +certLogin.getCertCustomer());
        if(certLogin.getMasterBroker()!=null){
           // System.out.println(" Dentro Masterbroker" );
            certLogin.setIsMasterBroker(true);
            System.out.println("IsMasterBroker" +certLogin.getMasterBroker().getCampaigns());
            //service.getCertLoginMB(100, 1, certLogin.getMasterBroker().getId());
            if(certLogin.getMasterBroker().getCampaigns().size()==0){
                System.out.println("ERROr No hay campaña");
                return Response.ok().status(Response.Status.NOT_FOUND).build();
            }
        }else{
            certLogin.setIsMasterBroker(false);
            if(certLogin.getCampaign()==null){
                System.out.println("ERROr No hay campaña");
                return Response.ok().status(Response.Status.NOT_FOUND).build();
            }
        }

        //System.out.println("IsMasterBroker" +certLogin.getIsMasterBroker());
        return Response.ok(certLogin).build();
    }

    @GET
    @UnitOfWork
    @Path("/customers/{id}")
    public Response getCustomers(@DefaultValue("") @QueryParam("q") String search,
                                 @PathParam("id") String id,
                                 @DefaultValue("10") @QueryParam("pageLength") String pageLength,
                                 @DefaultValue("") @QueryParam("orderBy") String orderBy,
                                 @DefaultValue("ASC") @QueryParam("order") String order,
                                 @DefaultValue("1")@QueryParam("page") String  page,
                                 @QueryParam("fromDate") String from,@QueryParam("toDate") String to,@DefaultValue("") @QueryParam("status") String status){
        PaginationDTO<CustomerDataLongDTO> paginationDTO;

        String idAsLong = (id);

        final Optional<CertLogin> certCustomerOptional = service.getCertLogin(idAsLong);

            paginationDTO=customerService.listExtra(search, orderBy, order, Integer.parseInt(pageLength), Integer.parseInt(page),certCustomerOptional.get(),from,to,status);

        return Response.ok(paginationDTO).build();
    }
    @GET
    @UnitOfWork
    @Path("/{id}/campaigns")
    public Response getCampaigns(@PathParam("id") String id, @Auth SimplePrincipal isAuthenticated){

        String idAsLong = (id);
        final Optional<CertLogin> certCustomerOptional = service.getCertLogin(idAsLong);
        CertLogin certLogin = findSafely(certCustomerOptional, CertLogin.TAG);
        List<ResultCampaignDTO> list= new ArrayList<ResultCampaignDTO>();
        if(certLogin.getMasterBroker()!=null){
            System.out.println(" Dentro Masterbroker" );
            for (int i = 0; i <certLogin.getMasterBroker().getCampaigns().size() ; i++) {
                //stringBuilder.append(user.getMasterBroker().getCampaigns().get(i).getId());
                Campaign campaign=certLogin.getMasterBroker().getCampaigns().get(i);
                ResultCampaignDTO resultCampaignDTO=new ResultCampaignDTO();
                resultCampaignDTO.setID(campaign.getId());
                resultCampaignDTO.setNAME(campaign.getName());
                list.add(resultCampaignDTO);
            }
            Collections.sort(list, new Comparator<ResultCampaignDTO>() {
                @Override
                public int compare(ResultCampaignDTO o1, ResultCampaignDTO o2) {
                    return o1.getNAME().compareTo(o2.getNAME());
                }
            });
            return  Response.ok(list).build();
        }else{
            System.out.println(" Dentro Broker" );
            ResultCampaignDTO resultCampaignDTO=new ResultCampaignDTO();

            resultCampaignDTO.setID(certLogin.getCampaign().getId());
            resultCampaignDTO.setNAME(certLogin.getCampaign().getName());
            return  Response.ok(resultCampaignDTO).build();
        }




    }

}
