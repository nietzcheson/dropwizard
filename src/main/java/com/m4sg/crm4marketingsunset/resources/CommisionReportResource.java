package com.m4sg.crm4marketingsunset.resources;

import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.CommisionReportBean;
import com.m4sg.crm4marketingsunset.services.CommisionReportService;
import com.m4sg.crm4marketingsunset.util.DateUtils;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Created by Fernando on 12/09/2016.
 */
@Path("/v1/commision")
@Produces(MediaType.APPLICATION_JSON)
public class CommisionReportResource {


    @GET
    @Path("/report/cobranza")
    @UnitOfWork
    public Response reportCobranza(@Auth SimplePrincipal isAuthenticated,@QueryParam("beginningDate")String beginning, @QueryParam("endingDate")String ending, @QueryParam("username") String username){


        Date beginningDate=DateUtils.stringToDateYYYYMMDD(beginning);
        Date endingDate=DateUtils.stringToDateYYYYMMDD(ending);


        List<CommisionReportBean> reportBeans= new CommisionReportService().commisionReportCobranza(beginningDate,endingDate,username);



        return Response.ok(reportBeans).build();

    }
}
