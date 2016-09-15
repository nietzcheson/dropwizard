package com.m4sg.crm4marketingsunset.resources;

import com.m4c.model.entity.SubService;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.SubServiceDTO;
import com.m4sg.crm4marketingsunset.services.SubServiceService;
import com.m4sg.crm4marketingsunset.util.Utils;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 11/08/2015.
 */
@Path("/v1/subservices")
@Produces(MediaType.APPLICATION_JSON)
public class SubServiceResource extends GenericResource<SubService> {
    public SubServiceResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getSubService(@DefaultValue("false") @QueryParam("for_certificates_area") String certificateArea,
                                  @QueryParam("callcenter") List<String> callcenter,@Auth SimplePrincipal isAuthenticated) {

        boolean isCertificateArea=new Boolean(certificateArea);

        List<Long>callCenterLst=Utils.transformStringToLong(callcenter);

        SubServiceService subServiceService=new SubServiceService();

        List<SubServiceDTO> subServiceLst=subServiceService.find(isCertificateArea,new ArrayList<Long>(callCenterLst));

        return Response.ok(subServiceLst).build();
    }
}
