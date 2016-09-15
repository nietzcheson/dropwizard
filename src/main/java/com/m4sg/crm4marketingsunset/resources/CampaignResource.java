package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.FoliosCertificado;
import com.m4sg.crm4marketingsunset.Roles;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.*;
import com.m4sg.crm4marketingsunset.services.CampaignService;
import com.m4sg.crm4marketingsunset.util.Utils;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sistemas on 05/01/2015.
 */
@Path("/v1/campaign")
@Produces(MediaType.APPLICATION_JSON)
public class CampaignResource extends GenericResource<Campaign> {

    private CampaignService service=new CampaignService();

    public CampaignResource(Validator validator) {
        super(validator);
    }

    @POST
    @Path("/create")
    @Timed
    @UnitOfWork
    public Response create(@Valid CampaignDTO campaignDTO, @Auth SimplePrincipal isAuthenticated){
        List<ErrorRepresentation> validationMessageLst=campaignDTO.validate();
        Campaign newCampaign;
        if(validationMessageLst.isEmpty()) {

            newCampaign = service.createCampaign(campaignDTO);
        }else{
            return Response.status(Response.Status.CONFLICT).entity(validationMessageLst).build();
        }
        return Response.status(Response.Status.CREATED).entity(newCampaign).build();
    }
    @POST
    @Path("/update")
    @Timed
    @UnitOfWork
    public Response update(CampaignDTO campaignDTO, @Auth SimplePrincipal isAuthenticated){
        List<ErrorRepresentation> validationMessageLst=campaignDTO.validateUpdate();
        Campaign campaignUpdate;
        if(validationMessageLst.isEmpty()) {
            campaignUpdate=service.updateCampaign(campaignDTO);
        }else{
            return Response.status(Response.Status.CONFLICT).entity(validationMessageLst).build();
        }
        return Response.ok(campaignUpdate).build();
    }
    @GET
    @Path("/list")
    @Timed
    @UnitOfWork
    public Response list(@DefaultValue("") @QueryParam("q") String search,
                         @DefaultValue("10") @QueryParam("pageLength") String pageLength,
                         @DefaultValue("") @QueryParam("orderBy") String orderBy,
                         @DefaultValue("ASC") @QueryParam("order") String order,
                         @DefaultValue("1")@QueryParam("page") String  page,
                         @Auth SimplePrincipal isAuthenticated){

        PaginationDTO<ResultCampaignDTO> paginationDTO = null;

        if(!search.isEmpty()) {
            paginationDTO=service.list(search, orderBy, order, Integer.parseInt(pageLength), Integer.parseInt(page));
        }else if(!orderBy.isEmpty() && !order.isEmpty()){
            paginationDTO=service.list(orderBy,order,Integer.parseInt(pageLength), Integer.parseInt(page));
        }else{
            paginationDTO=service.list(Integer.parseInt(pageLength), Integer.parseInt(page));
        }

        return Response.ok(paginationDTO).build();
    }
    @POST
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Response getCampaign(@PathParam("id") long id, @Auth SimplePrincipal isAuthenticated){

        Optional<Campaign> campaignOptional=service.findById(id);
        Campaign campaign = findSafely(campaignOptional, Campaign.TAG);
        if(campaign.getCertCustomer()!=null){
            campaign.getCertCustomer().setCampaigns(null);
        }
        return Response.ok(campaign).build();
    }

    @GET
    @Path("/listByCallcenter")
    @Timed
    @UnitOfWork
    public Response listbyCallCenter(@Auth SimplePrincipal isAuthenticated, @QueryParam("callCenters") String callCenters){
        if(isAuthenticated.isUserInRole(Roles.USER))
            callCenters= "7135,7366,7522,6663,6662,7523";
        HashMap<String, List<CampaignPackageDTO>> hashMap=service.getCampaignsByCallcenter(callCenters);
        return Response.ok(hashMap).build();
    }

    @POST
    @Path("/certificate/create")
    @Timed
    @UnitOfWork
    public Response createCerticates(@Valid CertificateDTO certificateDTO, @Auth SimplePrincipal isAuthenticated){
        List<ErrorRepresentation> validationMessageLst=certificateDTO.validate();
        HashMap<String,String> response=new HashMap<String, String>();

        if(validationMessageLst.isEmpty()) {
            service.createCertificates(certificateDTO);
        }else{
            return Response.status(Response.Status.CONFLICT).entity(validationMessageLst).build();
        }


        response.put("status","success");
        return Response.ok(response).build();

    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("{idCampaign}/certificates")
    @UnitOfWork
    public Response insertCerticates(@PathParam("idCampaign") Long idCampaign,
    @FormDataParam("file") InputStream uploadedInputStream,
    @FormDataParam("file") FormDataContentDisposition fileDetail,
    @FormDataParam("file") FormDataBodyPart bodyPart,
     @FormDataParam("action") String action,
    @Auth SimplePrincipal isAuthenticated
    ) throws IOException, URISyntaxException {
        /*  Servicio para la inserción de nuevos folios de certificados */
        List<CertificateResultDTO> certificateResultDTOList = service.insertCertificates(
                uploadedInputStream, idCampaign, action);
        return Response.ok(certificateResultDTOList).build();

    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("{idCampaign}/certificates/reassign")
    @UnitOfWork
    public Response moveCertificates(@FormDataParam("idBatch") Long idBatch,
    @FormDataParam("file") InputStream uploadedInputStream,
    @FormDataParam("file") FormDataContentDisposition fileDetail,
    @FormDataParam("file") FormDataBodyPart bodyPart, @PathParam("idCampaign") Long idCampaign,
    @FormDataParam("idFolio") Long idFolio,
    @Auth SimplePrincipal isAuthenticated)
            throws IOException, URISyntaxException
    {
        /*  Servicio para la reasignación de folios de certificados */
        List<CertificateResultDTO> certificateResultDTOList = service.moveCertificates(
                uploadedInputStream, idBatch, idCampaign, idFolio);
        return Response.ok(certificateResultDTOList).build();
    }

    @GET
    @Path("/{id}/seriesCertificate")
    @Timed
    @UnitOfWork
    public Response getSeriesCertificateList(@PathParam("id") Long campaignId,
                                             @DefaultValue("10") @QueryParam("pageLength") String pageLength,
                                             @DefaultValue("1")@QueryParam("page") String  page,
                                             @Auth SimplePrincipal isAuthenticated){


        PaginationDTO<FoliosCertificado> foliosCertificadoPaginationDTO=
                service.seriesCertificateList(campaignId, Integer.parseInt(pageLength), Integer.parseInt(page));

        return Response.ok(foliosCertificadoPaginationDTO).build();
    }

    @POST
    @Path("/{batchId}/batch/create")
    @Timed
    @UnitOfWork
    public Response createLoteBatch(@Valid AllocateBatchDTO allocateBatchDTO, @PathParam("batchId") Long batchId,@Auth SimplePrincipal isAuthenticated){
        List<ErrorRepresentation> validationMessageLst=allocateBatchDTO.validate();
        HashMap<String,String> response=new HashMap<String, String>();
        if(validationMessageLst.isEmpty()) {
            service.createBatch(allocateBatchDTO, batchId);
        }else{
            return Response.status(Response.Status.CONFLICT).entity(validationMessageLst).build();
        }
        response.put("status","success");
        return Response.ok(response).build();
    }

    @GET
    @Path("/test")
    @Timed
    @UnitOfWork
    public Response test(@Auth SimplePrincipal isAuthenticated) throws IOException {
Utils utils=new Utils();
        return Response.ok(utils.getPropValue("directory_images")).build();


    }
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    @Path("/{id}/import")
    public Response importFile( @PathParam("id") String id,
                                @FormDataParam("file") InputStream uploadedInputStream,
                                @FormDataParam("file") FormDataContentDisposition fileDetail,
                                @FormDataParam("file") FormDataBodyPart bodyPart) throws IOException, URISyntaxException {



        HSSFWorkbook workbook = new HSSFWorkbook(uploadedInputStream);
        HSSFSheet worksheet = workbook.getSheetAt(0);


        for (Row myrow : worksheet) {
            for (Cell mycell : myrow) {
                //set foreground color here
                String b1Val = mycell.toString();
                System.out.println("B1: " + b1Val);
            }
        }
        long idAsLong = Long.parseLong(id);


        return Response.status(200).build();
    }

    @POST
    @Path("/batchcertificate/create")
    @Timed
    @UnitOfWork
    public Response createBatchCerticates(@Valid BatchCertificateDTO batchCertificateDTO, @Auth SimplePrincipal isAuthenticated){
        List<ErrorRepresentation> validationMessageLst=batchCertificateDTO.validate();
        HashMap<String,String> response=new HashMap<String, String>();

        if(validationMessageLst.isEmpty()) {
            service.createBatchCertificates(batchCertificateDTO);
        }else{
            return Response.status(Response.Status.CONFLICT).entity(validationMessageLst).build();
        }


        response.put("status","success");
        return Response.ok(response).build();

    }

    @GET
    @Timed
    @UnitOfWork
    public Response getCampaigns(@QueryParam("callcenter") List<String> callcenter,  @Auth SimplePrincipal isAuthenticated){

        List<Long>callCenterLst=Utils.transformStringToLong(callcenter);

        List<CommonDTO>commonDTOs=service.getCampaigns(callCenterLst);

        return Response.ok(commonDTOs).build();
    }

    @POST
    @Path("/{id}/setEmailConfig")
    @Timed
    @UnitOfWork
    public Response setEmailConfig(@PathParam("id") long id, @Valid EmailCampaignDTO emailCampaignDTO, @Auth SimplePrincipal isAuthenticated){
        Optional<Campaign> campaignOptional=service.findById(id);
        if(!campaignOptional.isPresent()){
            return Response.status(Response.Status.CONFLICT).build();
        }else{
            HashMap<String,String> response=new HashMap<String, String>();
            if(service.updateEmailConfig(id, emailCampaignDTO));
                response.put("status","success");
            return Response.ok(response).build();
        }
    }

}
