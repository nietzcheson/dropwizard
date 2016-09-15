package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.m4c.model.entity.sistemas.UserIntranet;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.UserSecuritySunsetDTO;
import com.m4sg.crm4marketingsunset.services.UserIntranetService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by desarrollo1 on 30/03/2015.
 */
@Path("/v1/intranet")
@Produces(MediaType.APPLICATION_JSON)
public class UserIntranetResource extends GenericResource<UserIntranet>{
    private UserIntranetService service=new UserIntranetService();

    public UserIntranetResource(Validator validator) {
        super(validator);
    }

    @POST
    @Path("/login")
    @Timed
    @UnitOfWork
    public Response login(@FormParam("username") String username,
                          @FormParam("password") String password,
                          @Auth SimplePrincipal isAuthenticated){



        UserSecuritySunsetDTO userSecuritySunsetDTO = service.login(username, password);
        if(userSecuritySunsetDTO.ERROR!=null) {
            return Response.status(Response.Status.CONFLICT).entity("FAILURE").build();
        }

        return Response.ok(userSecuritySunsetDTO).build();
    }






}
