package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Merchant;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.dao.MerchantDAO;
import org.hibernate.QueryException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by sistemas on 16/01/2015.
 */
public class MerchantService extends GenericDAOService {
    public MerchantService() {
        super(MerchantDAO.class);
    }

    public Optional<Merchant> findByClave(String code){
        return merchantDAO.findByClave(code);

    }
    public List<Merchant> findAll(){
        return merchantDAO.findAll();
    }

    public List<Merchant> findByName(String name){
        List<Merchant> paginationDTO;
        try{
            paginationDTO=merchantDAO.findByName("Yucatan");
            //paginationDTO=certCustomerDAO.findAll(sizePage,page);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }
}
