package com.m4sg.crm4marketingsunset.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.m4c.model.entity.LogM4SG;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.AuditDTO;
import com.m4sg.crm4marketingsunset.services.SaleService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
/**
 * Created by desarrollo1 on 16/02/2016.
 */
@Path("/v1/audit")
@Produces(MediaType.APPLICATION_JSON)
public class AuditResource extends GenericResource<LogM4SG>{
    private static final Logger LOGGER = LoggerFactory.getLogger(LogM4SG.class);
    public AuditResource(Validator validator) {
        super(validator);
    }
    private SaleService saleService=new SaleService();
    @POST
    @UnitOfWork
    public Response CreateAudit(@Auth SimplePrincipal isAuthenticated, @Valid AuditDTO auditDTO) throws JsonProcessingException, URISyntaxException {
        saleService.saveLog(auditDTO);
        return  Response.ok(auditDTO).build();
    }
}
