package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Segment;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.dao.SegmentDAO;
import org.hibernate.QueryException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by sistemas on 16/01/2015.
 */
public class SegmentService extends GenericDAOService {
    public SegmentService() {
        super(SegmentDAO.class);
    }

    public Optional<Segment> findById(long id){
        return segmentDAO.find(id);
    }

    public List<Segment> findAll(){
        return segmentDAO.findAll();
    }

    public List<Segment> findByName(String name){
        List<Segment>paginationDTO;
        try{
            paginationDTO=segmentDAO.findByName("CERTIFICADOS");
            //paginationDTO=certCustomerDAO.findAll(sizePage,page);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }
}
