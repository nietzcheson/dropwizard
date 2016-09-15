package com.m4sg.crm4marketingsunset.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.m4c.model.entity.CustomerNote;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.CustomerNoteDTO;
import com.m4sg.crm4marketingsunset.core.dto.CustomerNoteUserDTO;
import com.m4sg.crm4marketingsunset.services.CustomerNoteService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by desarrollo1 on 08/05/2015.
 */
@Path("/v1/customernotes")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerNoteResource extends GenericResource<CustomerNote>{
    public CustomerNoteResource(Validator validator) {
        super(validator);
    }
    private CustomerNoteService customerNoteService=new CustomerNoteService();

    @POST
    @UnitOfWork
    public Response createCustomerNote(@Valid CustomerNoteDTO customerNoteDTO, @Auth SimplePrincipal isAuthenticated) throws JsonProcessingException{
        List<ErrorRepresentation> errorRepresentations = customerNoteDTO.validate();

        if (!errorRepresentations.isEmpty()) {
            return Response.status(Response.Status.CONFLICT).entity(errorRepresentations).build();
        }
       // CustomerNote customerNote = customerNoteService.createCustomerNote(customerNoteDTO,1);
        return Response.ok("").build();

    }

    @GET
    @UnitOfWork
    @Path("/{userid}")
    public Response getNotesbyUser(@PathParam("userid") String userid,
                               @Auth SimplePrincipal isAuthenticated) {
        List<CustomerNoteUserDTO> customerNoteList=customerNoteService.findCustomerNotebyUser(userid);
        return Response.ok(customerNoteList).build();
    }
}
