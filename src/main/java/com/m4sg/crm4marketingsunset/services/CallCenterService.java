package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.CallCenter;
import com.m4c.model.entity.Segment;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.CallCenterDTO;
import com.m4sg.crm4marketingsunset.dao.CallCenterDAO;
import com.m4sg.crm4marketingsunset.dao.SegmentDAO;
import org.hibernate.QueryException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Usuario on 21/04/2015.
 */
public class CallCenterService  extends GenericDAOService {
    public CallCenterService() {
        super(CallCenterDAO.class, SegmentDAO.class);
    }


    public List<CallCenter> list(){
        List<CallCenter>paginationDTO;
        try{
            paginationDTO=callCenterDAO.findByName("Certifi","Travel Club");
            //paginationDTO=certCustomerDAO.findAll(sizePage,page);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }
    public Optional<CallCenter> findById(long id){

        Optional<CallCenter> optional=callCenterDAO.find(id);

        return  optional;

    }

    public List<CallCenterDTO> listBySegment(Boolean isOTA){
        return callCenterDAO.findBySegment(isOTA);
    }
}
