package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.m4c.model.entity.Customer;
import com.m4c.model.entity.Sale;
import com.m4c.model.entity.online.CertsConfig;
import com.m4c.model.entity.sistemas.UserIntranet;
import com.m4sg.crm4marketingsunset.Roles;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.*;
import com.m4sg.crm4marketingsunset.services.*;
import com.m4sg.crm4marketingsunset.util.Constants;
import com.m4sg.crm4marketingsunset.util.DateUtils;
import com.m4sg.crm4marketingsunset.util.LoggerJsonObject;
import com.m4sg.crm4marketingsunset.util.Utils;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by Fernando on 30/03/2015.
 */
@Path("/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource extends GenericResource<Customer>  {

    private CustomerService service=new CustomerService();
    private SaleService saleService=new SaleService();
    private CustomerNoteService customerNoteService=new CustomerNoteService();
    private static final Logger LOGGER = LoggerFactory.getLogger(Customer.class);
    public CustomerResource(Validator validator) {
        super(validator);
    }
    @POST
    @UnitOfWork
    public Response createCustomerAndSale(@Valid CustomerDTO customerDTO, @Auth SimplePrincipal isAuthenticated) throws IOException, URISyntaxException {

        Customer customer;
        LoggerJsonObject.logObject(customerDTO, LOGGER);
        customer=service.createCustomerSale(customerDTO);

        service.refresh(customer);
        customer=service.find(customer.getId()).get();
        if(customerDTO.getSale().getComment()!=null){
            if(!customerDTO.getSale().getComment().isEmpty()){
                customerNoteService.createCustomerNotefromSale(customer,customerDTO);
            }
        }
        Long[] callCentersIds = Constants.callCentersIds;

        if (isAuthenticated.isUserInRole(Roles.USER) && isAuthenticated.getSendEmail()) {
            String body="Congratulations, you have 12 months from today to book your Cancun vacation. To reserve your family vacation, just call the number on the certificate and start enjoying a relaxing week in paradise.";
            Optional<CertsConfig> certsConfigOptional = new CertsConfigService().getCertConfig(customer.getSaleSet().iterator().next().getCampaign().getId());
            CertsConfig certsConfig = certsConfigOptional.get();
            String report=certsConfig.getCertificate();
            service.flush();
            String idBooking=customer.getSaleSet().iterator().next().getId().toString();
            service.commit();
            MailService.sendEmail(customer.getEmail(),"","",customer.getFirstName()+", your Cancun vacation certificate has arrived",body,report,idBooking, true, true);

            return Response.ok(service.setParamsDTO(customer)).build();
        }else if(!Arrays.asList(callCentersIds).contains(customer.getCallCenter().getId())){
            return Response.ok(service.setParamsDTO(customer)).build();
        }else{
            Optional<CertsConfig> certsConfigOptional = new CertsConfigService().getCertConfig(customer.getSaleSet().iterator().next().getCampaign().getId());
            String report="";
            if(certsConfigOptional.isPresent()){
                CertsConfig certsConfig = certsConfigOptional.get();
                report=certsConfig.getCertificate();
            }
            service.flush();
            Sale sale = customer.getSaleSet().iterator().next();
            String idBooking=sale.getId().toString();
            service.commit();
            if(customerDTO.getSendMail()!=null){
                Boolean ecert= sale.getCampaign().getTypeCertificate() == 2 ? true: false;
                if (customerDTO.getSendMail())
                    MailService.sendEmail(customer.getEmail(),"","",customerDTO.getEmailDTO().getSubject(),customerDTO.getEmailDTO().getBody(),report, idBooking, ecert, false);
            }
        }
        return Response.ok(customer).build();
    }
  /*  @POST
    @UnitOfWork
    public Response createSaleAndCustomer(@Valid PaymentDTO paymentDTO,@Auth SimplePrincipal isAuthenticated){




        return Response.ok().build();
    }*/
    @GET
    @UnitOfWork
    public Response list(@DefaultValue("") @QueryParam("q") String search,
                         @DefaultValue("10") @QueryParam("pageLength") String pageLength,
                         @DefaultValue("") @QueryParam("orderBy") String orderBy,
                         @DefaultValue("ASC") @QueryParam("order") String order,
                         @DefaultValue("1")@QueryParam("page") String  page,
                         @QueryParam("fromDate") String from,@QueryParam("toDate") String to,
                         @Auth SimplePrincipal isAuthenticated){

        PaginationDTO<PreviewCustomerDTO> paginationDTO;
        if(!search.isEmpty()) {
            paginationDTO=service.list(search, orderBy, order, Integer.parseInt(pageLength), Integer.parseInt(page),from,to);
        }
        else if(!orderBy.isEmpty() && !order.isEmpty()){
            paginationDTO=service.list(orderBy,order,Integer.parseInt(pageLength), Integer.parseInt(page), from, to);
        }else{
            paginationDTO=service.list(Integer.parseInt(pageLength), Integer.parseInt(page), from, to);
        }

        return Response.ok(paginationDTO).build();
    }
    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") String idParam, @Auth SimplePrincipal isAuthenticated){

        long id = Long.parseLong(idParam);
        Optional<Customer>customerOptional=service.find(id);
        Customer customer = findSafely(customerOptional, Customer.TAG);
        Integer age= DateUtils.getAge( customer.getDOBDay(), customer.getDOBMonth(),customer.getDOBYear());
        customer.setAge(age);
        return Response.ok(customer).build();

    }
    @POST
    @UnitOfWork
    @Path("/{id}")
    public Response update(@PathParam("id") String idParam, CustomerDTO customerDTO, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException, URISyntaxException {
        long id = Long.parseLong(idParam);
        Customer customer=service.updateCustomer(customerDTO, id);
        Integer age= DateUtils.getAge(customer.getDOBDay(), customer.getDOBMonth(), customer.getDOBYear());
        customer.setAge(age);
        if (isAuthenticated.isUserInRole(Roles.USER))
            return Response.ok(service.setParamsDTO(customer)).build();
        return Response.ok(customer).build();
    }
    @POST
    @UnitOfWork
    @Path("/{id}/sales")
    public Response saveSale(@PathParam("id") String idParam,@Valid SaleCertificateDTO saleCertificateDTO, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException, URISyntaxException {

        Long  idBooking=null;
        long id = Long.parseLong(idParam);
        HashMap response=new HashMap();

        saleService.saveLog(saleCertificateDTO.getAudit());
        idBooking=saleService.createSale(saleCertificateDTO,id,saleCertificateDTO.getAudit().getUsername());

        if(idBooking!=null){
            response.put("idBooking",idBooking);
            return Response.ok(response).build();
        }else {
            response.put("status","failure");
            return Response.ok(response).build();
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    @Path("/import")
    public Response importCustomers( @FormDataParam("username") String username,
                                     @FormDataParam("file") InputStream uploadedInputStream,
                                     @FormDataParam("file") FormDataContentDisposition fileDetail,
                                     @FormDataParam("file") FormDataBodyPart bodyPart) throws IOException, URISyntaxException {
        if(username.isEmpty()){
            return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("username","does not exists.")).build();
        }
        Optional<UserIntranet> userIntranetOptional=new UserIntranetService().find(username);
        Utils.isValid(userIntranetOptional, "userName");

        service.insertCustomerSaleFromExcel(uploadedInputStream,username);
        return Response.status(200).build();
    }

    @POST
    @UnitOfWork
    @Path("/{id}/notes/list")
    public Response getListNotes(@PathParam("id") String idParam, @Auth SimplePrincipal isAuthenticated,@Valid AuditDTO auditDTO) throws JsonProcessingException, URISyntaxException {
        long id = Long.parseLong(idParam);
        saleService.saveLog(auditDTO);
        Optional<Customer>customerOptional=service.find(id);
        Customer customer = findSafely(customerOptional, Customer.TAG);
        List<CustomerNoteDTO> customerNoteDTO=customerNoteService.findAll(customer);
      return  Response.ok(customerNoteDTO).build();
    }

    @POST
    @Path("/{idcliente}/notes/{idNota}")
    @Timed
    @UnitOfWork
    public Response getNoteDetail(@PathParam("idcliente") String id,@Auth SimplePrincipal isAuthenticated,@PathParam("idNota") String idservicio,@Valid AuditDTO auditDTO){
        saleService.saveLog(auditDTO);
        Long idCliente = Long.parseLong(id);
        Integer idNota = Integer.parseInt(idservicio);
        CustomerNoteDTO customerNoteDTO=customerNoteService.find(idCliente, idNota);
        return  Response.ok(customerNoteDTO).build();
    }

    @POST
    @UnitOfWork
    @Path("/{idcliente}/notes")
    public Response setCreateNote(@PathParam("idcliente") String idParam, @Auth SimplePrincipal isAuthenticated,@Valid CustomerNoteDTO customerNoteDTO) throws JsonProcessingException, URISyntaxException {
        long id = Long.parseLong(idParam);
        saleService.saveLog(customerNoteDTO.getAudit());
        customerNoteDTO=  customerNoteService.createCustomerNote(customerNoteDTO,id);
        return  Response.ok(customerNoteDTO).build();
    }

    @POST
    @UnitOfWork
    @Path("/{idcampaign}/report")
    public Response reportbyCampaign(@PathParam("idcampaign") String idcampaign, @Valid ReportDTO reportDTO, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException, URISyntaxException {
        List<ErrorRepresentation> validationMessageLst=reportDTO.validate();
        if(validationMessageLst.isEmpty()) {
            long id = Long.parseLong(idcampaign);
            List<ResultbyDateDTO> resultReportDTO = service.report(reportDTO, id);
            return  Response.ok(resultReportDTO).build();
        }else{
            return Response.status(Response.Status.CONFLICT).entity(validationMessageLst).build();
        }
    }
}
