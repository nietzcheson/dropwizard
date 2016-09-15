package com.m4sg.crm4marketingsunset.resources;

import com.m4c.model.entity.Status;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.MoodDTO;
import com.m4sg.crm4marketingsunset.services.MoodService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Fernando on 25/08/2015.
 */
@Path("/v1/moods")
@Produces(MediaType.APPLICATION_JSON)
public class MoodResource extends GenericResource<Status>{

    public MoodResource(Validator validator) {
        super(validator);
    }
    private MoodService moodService=new MoodService();
    @GET
    @UnitOfWork

    public Response list(@Auth SimplePrincipal isAuthenticated){

        List<MoodDTO> noteTypes=moodService.list();

        return Response.ok(noteTypes).build();

    }
}
