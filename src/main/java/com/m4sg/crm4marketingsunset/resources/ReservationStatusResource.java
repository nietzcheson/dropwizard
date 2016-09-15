package com.m4sg.crm4marketingsunset.resources;

import com.m4c.model.entity.ReservationStatus;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import com.m4sg.crm4marketingsunset.services.ReservationStatusService;
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
 * Created by Usuario on 17/08/2015.
 */
@Path("/v1/status")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationStatusResource extends GenericResource<ReservationStatus>{

    public ReservationStatusResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getSellers(@QueryParam("tipo") List<String> types,
                               @Auth SimplePrincipal isAuthenticated){

        List<Long>callCenterLst= Utils.transformStringToLong(types);
        List<CommonDTO> commonDTOLst=new ReservationStatusService().list(callCenterLst);

        return Response.ok(commonDTOLst).build();
    }
}
