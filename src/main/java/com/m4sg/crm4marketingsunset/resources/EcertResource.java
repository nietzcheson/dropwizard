package com.m4sg.crm4marketingsunset.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.m4c.model.entity.ImageCertificate;
import com.m4c.model.entity.online.CertsConfig;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.CertsConfigDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.services.CertsConfigService;
import com.m4sg.crm4marketingsunset.services.EcertService;
import com.m4sg.crm4marketingsunset.util.Constants;
import com.m4sg.crm4marketingsunset.util.FileIO;
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

import javax.imageio.ImageIO;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by jhon on 07/05/15.
 */
@Path("/v1/ecert")
@Produces(MediaType.APPLICATION_JSON)
public class EcertResource extends GenericResource<ImageCertificate> {

    private EcertService service=new EcertService();
    private CertsConfigService certsConfigService=new CertsConfigService();
    private static final Logger LOGGER = LoggerFactory.getLogger(CertsConfig.class);

    @Context
    UriInfo uriInfo;

    public EcertResource(Validator validator) {
        super(validator);
    }

    @POST
    @UnitOfWork
    @Path("/{identifierImage}/images")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createEcertImage(
            @PathParam("identifierImage") String type,
            @FormDataParam("image") InputStream uploadedInputStream,
            @FormDataParam("image") FormDataContentDisposition fileDetail,
            @FormDataParam("image") FormDataBodyPart bodyPart) throws IOException, URISyntaxException {

        int identifierImage=Integer.parseInt(type);
        BufferedImage imBuff;
        String token;
        String fileName;
        String  directory=null ;
        String pathPrivateServer="";
        String pathPublicServer="";
        if(identifierImage<=0 || identifierImage>2){
            Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("type","Ivalid")).build();
        }

        if(!bodyPart.getMediaType().isCompatible(MediaType.valueOf("image/jpeg")) &&
                !bodyPart.getMediaType().isCompatible(MediaType.valueOf("image/jpg")) &&
                !bodyPart.getMediaType().isCompatible(MediaType.valueOf("image/png"))){

            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("file", "File must be an image")).build();
        }

        imBuff = ImageIO.read( new BufferedInputStream(uploadedInputStream));
         token= StringTools.generaToken(10);
         fileName = token +"."+bodyPart.getMediaType().getSubtype();

                switch(identifierImage){ // Valida Dimensiones y especifica la ruta para almacenar las imágenes
                    case 1: directory=Constants.PATH_ECERT_IMAGES ;
                        if(imBuff.getWidth()!=1320 && imBuff.getHeight()!=1020){
                            return Response.status(Response.Status.CONFLICT)
                                    .entity(new ErrorRepresentation("Dimensions", "Image must be 1320 x 1020")).build();
                        }
                        pathPrivateServer=Constants.PATH_PRIVATE_SERVER_ECERT_IMAGES;
                        pathPublicServer=Constants.PATH_PUBLIC_SERVER_ECERT_IMAGES;
                        break;
                    case 2: directory=Constants.PATH_BROKER_IMAGES;
                        if(imBuff.getWidth()!=400 && imBuff.getHeight()!=80){
                            return Response.status(Response.Status.CONFLICT)
                                    .entity(new ErrorRepresentation("Dimensions", "Image must be 400 x 80")).build();
                        }
                        pathPrivateServer=Constants.PATH_PRIVATE_SERVER_BROKER_IMAGES;
                        pathPublicServer=Constants.PATH_PUBLIC_SERVER_BROKER_IMAGES;
                         break;
                    case 3: directory=Constants.PATH_CERT_TYPE_IMAGES;
                        pathPublicServer=Constants.PATH_PUBLIC_SERVER_CERT_TYPE_IMAGES;
                        pathPrivateServer=Constants.PATH_PRIVATE_SERVER_CERT_TYPE_IMAGES;
                         break ;
                }

        Files.createDirectories(FileSystems.getDefault().getPath(directory));
        java.nio.file.Path newPath=FileSystems.getDefault().getPath(directory,fileName);
        ImageIO.write(imBuff, bodyPart.getMediaType().getSubtype(), new File(newPath.toString()));
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI imageUri = uriBuilder.path(fileName).build();
        ImageCertificate imageCertificate=service.createImageCertificate(fileName, (long)identifierImage, imageUri.toString());

        FileIO.scp(Constants.PUBLIC_SERVER,Constants.PUBLIC_SERVER_PORT_SSH, Constants.PUBLIC_SERVER_FTP_USERNAME, Constants.PUBLIC_SERVER_FTP_PASSWORD,  newPath.toString(), pathPublicServer);
        FileIO.scp(Constants.PRIVATE_SERVER,Constants.PRIVATE_SERVER_PORT_SSH,Constants.PRIVATE_SERVER_FTP_USERNAME,Constants.PRIVATE_SERVER_FTP_PASSWORD,newPath.toString(),pathPrivateServer);

        return Response.created(new URI(fileName)).entity(imageCertificate).build();
    }

    @GET
    @Path("/{identifierImage}/images/{image}")
    @Produces("image/*")
    @UnitOfWork
    public Response getEcertImage(@PathParam("identifierImage") String type,
                                  @PathParam("image") String image) {

        int identifierImage=Integer.parseInt(type);
        String directory=null ;

        if(identifierImage<=0 || identifierImage>2){
            Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("type","Ivalid")).build();
        }

        switch(identifierImage){
            case 1: directory=Constants.PATH_ECERT_IMAGES ;
                break;
            case 2: directory=Constants.PATH_BROKER_IMAGES;
                break;
            case 3: directory=Constants.PATH_CERT_TYPE_IMAGES;
                break;
        }


        File file = new File(directory+image);
        if (!file.exists()) {
                throw new NotFoundException("No such image.");
        }

        MediaType mediaType = MediaType.valueOf("image/jpeg");
        return Response.ok(file, mediaType).build();

    }
    @GET
    @Path("/images")
    @UnitOfWork
    public Response list(@DefaultValue("") @QueryParam("q") String search,
                         @DefaultValue("10") @QueryParam("pageLength") String pageLength,
                         @DefaultValue("id") @QueryParam("orderBy") String orderBy,
                         @DefaultValue("DESC") @QueryParam("order") String order,
                         @DefaultValue("1")@QueryParam("page") String  page){

        PaginationDTO<ImageCertificate> paginationDTO ;
        if(!search.isEmpty()) {
            paginationDTO=service.listImage(search, orderBy, order, Integer.parseInt(pageLength), Integer.parseInt(page));
        }else if(!orderBy.isEmpty() && !order.isEmpty()){
            paginationDTO=service.listImage(orderBy, order, Integer.parseInt(pageLength), Integer.parseInt(page));
        }else{
            //paginationDTO=service.list(Integer.parseInt(pageLength), Integer.parseInt(page));
            paginationDTO=service.listImage(Integer.parseInt(pageLength), Integer.parseInt(page));
        }
        paginationDTO.getElements();

        return Response.ok(paginationDTO).build();
    }
    @POST
    @UnitOfWork
    @Path("/createImageConfig")
    public Response createImageConfig(@Valid CertsConfigDTO certsConfigDTO) throws JsonProcessingException, URISyntaxException {
        LoggerJsonObject.logObject(certsConfigDTO, LOGGER);
        List<ErrorRepresentation> errorRepresentations = certsConfigDTO.validate();
        if (!errorRepresentations.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).entity(errorRepresentations).build();
        }
        CertsConfig newCertCertificate = certsConfigService.createCertsConfig(certsConfigDTO);
        String idAsString = String.valueOf(newCertCertificate.getId());

        return Response.created(new URI(idAsString)).entity(newCertCertificate).build();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Response getDestination(@PathParam("id") Long id, @Auth SimplePrincipal isAuthenticated) {
        final Optional<CertsConfig> certsConfigOptional = certsConfigService.getCertConfig(id);
        if(!certsConfigOptional.isPresent())
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("CertConfig", "not exist")).build());
        CertsConfig certsConfig = certsConfigOptional.get();
        final Optional<ImageCertificate> imageCertificate=certsConfigService.getImageCertificate(certsConfig.getImagencert());
        if(imageCertificate.isPresent())
            certsConfig.setUrl(imageCertificate.get().getUrl());
        return Response.ok(certsConfig).build();
    }




    @POST
    @UnitOfWork
    @Path("/updateImageConfig/{id}")
    public Response updateImageConfig(CertsConfigDTO certsConfigDTO, @PathParam("id") String id) throws JsonProcessingException, URISyntaxException {
        long idAsLong = Long.parseLong(id);
        LoggerJsonObject.logObject(certsConfigDTO, LOGGER);
        List<ErrorRepresentation> errorRepresentations = certsConfigDTO.validate();
        if (!errorRepresentations.isEmpty())
            return Response.status(Response.Status.CONFLICT).entity(errorRepresentations).build();

        CertsConfig editCertCertificate = certsConfigService.editCertsConfig(idAsLong, certsConfigDTO);
        String idAsString = String.valueOf(editCertCertificate.getId());
        return Response.created(new URI(idAsString)).entity(editCertCertificate).build();
    }
}
