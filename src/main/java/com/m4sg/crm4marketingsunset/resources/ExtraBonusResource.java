package com.m4sg.crm4marketingsunset.resources;

import com.m4c.model.entity.ExtraBonus;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import com.m4sg.crm4marketingsunset.services.ExtraBonusService;
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
@Path("/v1/extrabonos")
@Produces(MediaType.APPLICATION_JSON)
public class ExtraBonusResource  extends GenericResource<ExtraBonus>{

    public ExtraBonusResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getSellers(@QueryParam("callcenter") List<String> callcenter,
                               @Auth SimplePrincipal isAuthenticated){

        List<Long>callCenterLst= Utils.transformStringToLong(callcenter);
        List<CommonDTO> commonDTOLst=new ExtraBonusService().list(callCenterLst);

        return Response.ok(commonDTOLst).build();
    }
}
