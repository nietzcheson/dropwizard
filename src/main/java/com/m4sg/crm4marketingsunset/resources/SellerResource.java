package com.m4sg.crm4marketingsunset.resources;

import com.m4c.model.entity.Sellers;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import com.m4sg.crm4marketingsunset.services.SellerService;
import com.m4sg.crm4marketingsunset.util.Utils;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Fernando on 12/08/2015.
 */
@Path("/v1/sellers")
@Produces(MediaType.APPLICATION_JSON)
public class SellerResource extends GenericResource<Sellers>{
    public SellerResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getSellers(@QueryParam("callcenter") List<String> callcenter,
                               @QueryParam("tipo") List<String> types,
                               @Auth SimplePrincipal isAuthenticated){

        List<Long>callCenterLst= Utils.transformStringToLong(callcenter);
        List<CommonDTO> commonDTOLst=new SellerService().findAllTransformCommon(callCenterLst, types);

        return Response.ok(commonDTOLst).build();
    }
}
