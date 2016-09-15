package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.m4c.model.entity.ContractService;
import com.m4c.model.entity.Payment;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.*;
import com.m4sg.crm4marketingsunset.services.SaleService;
import com.m4sg.crm4marketingsunset.services.SubServiceService;
import com.m4sg.crm4marketingsunset.services.UserIntranetService;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fernando on 21/07/2015.
 */
@Path("/v1/sales")
@Produces(MediaType.APPLICATION_JSON)
public class SaleResource extends GenericResource<Payment>  {

    private com.m4sg.crm4marketingsunset.services.PaymentService paymentService=new com.m4sg.crm4marketingsunset.services.PaymentService();
    private SubServiceService subServiceService= new SubServiceService();
    private SaleService saleService=new SaleService();

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleResource.class);
    public SaleResource(Validator validator) {
        super(validator);
    }

    @GET
    @Path("/{idBooking}/payments")
    @Timed
    @UnitOfWork
    public Response paymentsByIdBooking(@PathParam("idBooking") String id,@Auth SimplePrincipal isAuthenticated){

        Long idBooking=Long.parseLong(id);

        List<PaymentServiceResponseDTO> paymentResponseDTOLst=paymentService.getPaymentsResponse(idBooking);

        return Response.ok(paymentResponseDTOLst).build();
    }
    @POST
    @Path("/{idBooking}/payments/{idPago}")
    @Timed
    @UnitOfWork
    public Response paymentByIdBooking(@Valid AuditDTO auditDTO,@PathParam("idBooking") String id,@PathParam("idPago") String idPago,@Auth SimplePrincipal isAuthenticated) {

        try {
            Long idBooking = Long.parseLong(id);
            Long idPayment = Long.parseLong(idPago);

            paymentService.saveLog(auditDTO);
            PaymentDTO paymentDTO = paymentService.getPaymentDetail(idBooking, idPayment, auditDTO.getUsername());

            return Response.ok(paymentDTO).build();

        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("idBooking or idPago", "is not a number..")).build();
        }
    }

    @PUT
    @Path("/{idBooking}/payments/{idPago}")
    @Timed
    @UnitOfWork
    public Response updatePaymentByIdBooking(@Valid PaymentDTO paymentDTO,@PathParam("idBooking") String id,@PathParam("idPago") String idPago,@Auth SimplePrincipal isAuthenticated) {

        try {
            Long idBooking = Long.parseLong(id);
            Long idPayment = Long.parseLong(idPago);

            paymentService.saveLog(paymentDTO.getAudit());
            List<ErrorRepresentation> errorRepresentations = paymentDTO.validate();
            if(!errorRepresentations.isEmpty()) {
                return Response.status(Response.Status.CONFLICT).entity(errorRepresentations).build();
            }
            UserIntranetService userIntranetService=new UserIntranetService();
            boolean hasPermission=userIntranetService.hasPermission(Constants.PERMISSION_UPDATE_DELETE_PAYMENT,paymentDTO.getAudit().getUsername());
//            boolean hasPermission=true;

            if(hasPermission) {
                ResponseRegisterPaymentDTO responseRegisterPaymentDTO=paymentService.updatePayment(idBooking,paymentDTO,idPayment);

             return Response.ok(responseRegisterPaymentDTO).build();
            }else{
                return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorRepresentation("username","Unauthorized")).build();
            }

        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("idBooking or idPago", "is not a number..")).build();
        }
    }
    @POST
    @Path("/{idBooking}/payments")
    @Timed
    @UnitOfWork
    public Response savePayment(@PathParam("idBooking") String id,@Valid PaymentDTO paymentDTO,@Auth SimplePrincipal isAuthenticated) throws JsonProcessingException {

        Long idBooking=Long.parseLong(id);
        LoggerJsonObject.logObject(paymentDTO, LOGGER);
        List<ErrorRepresentation> errorRepresentations = paymentDTO.validate();
        if(!errorRepresentations.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).entity(errorRepresentations).build();
        }

        ResponseRegisterPaymentDTO responseRegisterPaymentDTO=paymentService.createPayment(idBooking, paymentDTO);


        return Response.ok(responseRegisterPaymentDTO).build();
    }
    @POST
    @Path("/{idBooking}/payments/{idPago}/delete")
    @Timed
    @UnitOfWork
    public Response deletePayment(@PathParam("idBooking") String id,@PathParam("idPago") String idPago,@Valid AuditDTO auditDTO,@Auth SimplePrincipal isAuthenticated) throws JsonProcessingException {

        try {
            Long idBooking = Long.parseLong(id);
            Long idPayment = Long.parseLong(idPago);
            UserIntranetService userIntranetService=new UserIntranetService();
            boolean hasPermission=userIntranetService.hasPermission(Constants.PERMISSION_UPDATE_DELETE_PAYMENT,auditDTO.getUsername());
//            boolean hasPermission=true;

            if(hasPermission) {

                paymentService.saveLog(auditDTO);
                paymentService.deletePayment(idBooking,idPayment);

                return Response.ok().entity("success").build();

            }else{
                return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorRepresentation("username","Unauthorized")).build();
            }



        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("idBooking or idPago", "is not a number..")).build();
        }


    }

    @GET
    @Path("/{idBooking}/providedServices")
    @Timed
    @UnitOfWork
    public Response getProvidedServices(@PathParam("idBooking") String id,@Auth SimplePrincipal isAuthenticated){


        try {

            Long idBooking = Long.parseLong(id);
            List<PaymentServiceResponseDTO> lst=paymentService.getProvidedServices(idBooking);

            return  Response.ok(lst).build();
        } catch (NumberFormatException ex) {
                return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("idBooking or idPago", "is not a number..")).build();
            }

    }


    @POST
    @Path("/{idBooking}/providedServices")
    @Timed
    @UnitOfWork
    public Response registerProvidedServices(@PathParam("idBooking") String id,@Auth SimplePrincipal isAuthenticated,SubServiceDTO subServiceDTO){

        HashMap<String,String> response=new HashMap<String, String>();

        try {

            paymentService.saveLog(subServiceDTO.getAudit());

            Long idBooking = Long.parseLong(id);
            subServiceService.register(subServiceDTO,idBooking);
            response.put("status","success");
            return Response.ok(response).build();


        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("idBooking or idPago", "is not a number..")).build();
        }

    }
    @GET
    @Path("/{idBooking}/providedServices/{idservicio}")
    @Timed
    @UnitOfWork
    public Response getProvidedServices(@PathParam("idBooking") String id,@Auth SimplePrincipal isAuthenticated,@PathParam("idservicio") String idservicio){


        try {

            Long idBooking = Long.parseLong(id);
            Long idService = Long.parseLong(idservicio);

            ContractService lst=subServiceService.getContractService(idBooking,idService);
            SubServiceDTO subServiceDTO= new SubServiceDTO();
            subServiceDTO.setAmount(lst.getAmount());
            subServiceDTO.setAdults(lst.getPax());
            subServiceDTO.setMinor(lst.getChilds());
            subServiceDTO.setIdsubservicio(lst.getSubService().getId());
            subServiceDTO.setDescription(lst.getDescription());

            return  Response.ok(subServiceDTO).build();
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("idBooking or idPago", "is not a number..")).build();
        }

    }

    @POST
    @Path("/{idBooking}/providedServices/{idservicio}")
    @Timed
    @UnitOfWork
    public Response updateProvidedServices(@PathParam("idBooking") String id,@Auth SimplePrincipal isAuthenticated,@PathParam("idservicio") String idservicio,SubServiceDTO subServiceDTO){

        HashMap<String,String> response=new HashMap<String, String>();
        try {

            Long idBooking = Long.parseLong(id);
            Long idService = Long.parseLong(idservicio);
            paymentService.saveLog(subServiceDTO.getAudit());
            subServiceService.update(subServiceDTO,idBooking,idService);

            response.put("status","success");
            return Response.ok(response).build();

        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("idBooking or idPago", "is not a number..")).build();
        }

    }

    @POST
    @Path("/{idBooking}/providedServices/{idservicio}/delete")
    @Timed
    @UnitOfWork
    public Response deleteProvidedServices(@PathParam("idBooking") String id,@Auth SimplePrincipal isAuthenticated,@PathParam("idservicio") String idservicio,@Valid AuditDTO auditDTO){

        HashMap<String,String> response=new HashMap<String, String>();
        try {

            Long idBooking = Long.parseLong(id);
            Long idService = Long.parseLong(idservicio);
            paymentService.saveLog(auditDTO);
            ContractService contractService=subServiceService.getContractService(idBooking,idService);
            List<com.m4c.model.entity.PaymentService> lstPaymentServices= paymentService.getPaymentsByIdContractService(idBooking, contractService);
            for (int i = 0; i < lstPaymentServices.size(); i++) {
                com.m4c.model.entity.PaymentService payment=lstPaymentServices.get(i);
                paymentService.deletePayment(idBooking,payment.getId());
            }

            subServiceService.delete(idBooking,idService);
            response.put("status","success");
            return Response.ok(response).build();

        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("idBooking or idPago", "is not a number..")).build();
        }

    }

    @POST
    @Path("/{idBooking}")
    @Timed
    @UnitOfWork
    public Response updateSales(@PathParam("idBooking") String id,@Auth SimplePrincipal isAuthenticated,@Valid SaleCertificateDTO saleCertificateDTO){
        paymentService.saveLog(saleCertificateDTO.getAudit());
        Long idBooking = Long.parseLong(id);
        SaleCertificateDTO saleCertificateDTO1= saleService.updateSale(saleCertificateDTO, idBooking);
        return Response.ok(saleCertificateDTO1).build();

    }

    @POST
    @Path("/{idBooking}/detail")
    @Timed
    @UnitOfWork
    public Response getSale(@PathParam("idBooking") String id,@Auth SimplePrincipal isAuthenticated,@Valid AuditDTO auditDTO){
        paymentService.saveLog(auditDTO);
        Long idBooking = Long.parseLong(id);
        SaleCertificateDTO saleCertificateDTO1= saleService.getSale( idBooking);
        return Response.ok(saleCertificateDTO1).build();
    }



}
