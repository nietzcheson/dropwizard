package com.m4sg.crm4marketingsunset.resources;

import com.m4c.model.entity.online.NoteType;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import com.m4sg.crm4marketingsunset.services.NoteTypeService;
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
 * Created by Fernando on 25/08/2015.
 */
@Path("/v1/noteTypes")
@Produces(MediaType.APPLICATION_JSON)
public class NoteTypeResource extends GenericResource<NoteType>{

    public NoteTypeResource(Validator validator) {
        super(validator);
    }

    private NoteTypeService noteTypeService=new NoteTypeService();
    @GET
    @UnitOfWork

    public Response list( @QueryParam("ignore_dept") List<String> ignoreDepts,
                          @Auth SimplePrincipal isAuthenticated){


        List<Long>ignoreDeptLst= Utils.transformStringToLong(ignoreDepts);

        List<CommonDTO> noteTypes=noteTypeService.list(ignoreDeptLst);

        return Response.ok(noteTypes).build();

    }
}
