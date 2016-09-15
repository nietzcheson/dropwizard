package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.m4c.model.entity.Department;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.MoodDTO;
import com.m4sg.crm4marketingsunset.services.DepartmentService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Usuario on 11/09/2015.
 */
@Path("/v1/areas")
@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
public class DepartmentResource extends GenericResource<Department>  {
    public DepartmentResource(Validator validator) {
        super(validator);
    }

    DepartmentService departmentService=new DepartmentService();
    @GET
    @UnitOfWork
    public Response list(@Auth SimplePrincipal isAuthenticated){

        List<MoodDTO> noteTypes=departmentService.list();

        return Response.ok(noteTypes).build();

    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Response getDepartment(@PathParam("id") long id, @Auth SimplePrincipal isAuthenticated){
        Optional<Department> departmentOptional=departmentService.findById(id);
        Department department = findSafely(departmentOptional, Department.TAG);
        return Response.ok(department).build();
    }

}
