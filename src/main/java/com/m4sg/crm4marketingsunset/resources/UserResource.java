package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.m4c.model.entity.User;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.UserService;
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
 * Created by Usuario on 20/07/2015.
 */
@Path("/v1/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource extends GenericResource<User>{

    UserService userService=new UserService();
    public UserResource(Validator validator) {
        super(validator);
    }

    @GET
    @Timed
    @UnitOfWork
    public Response list( @QueryParam("group_username") String group_username,
                          @QueryParam("group") String group,
                          @QueryParam("ignore_username") List<String> ignore_username,

                         @Auth SimplePrincipal isAuthenticated){

        List<User> titles = userService.listTitles(group_username,group,ignore_username);
        return Response.ok(titles).build();


    }
}
