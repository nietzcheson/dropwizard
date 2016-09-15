package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.online.CertCustomer;
import com.m4c.model.entity.online.CertLogin;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.CertLoginDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.core.dto.PreviewBrokerDTO;
import com.m4sg.crm4marketingsunset.dao.CampaignDAO;
import com.m4sg.crm4marketingsunset.dao.CertCustomerDAO;
import com.m4sg.crm4marketingsunset.dao.CertLoginDAO;
import com.m4sg.crm4marketingsunset.dao.StateDAO;
import com.m4sg.crm4marketingsunset.util.FindSafely;
import org.hibernate.QueryException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by Usuario on 31/03/2015.
 */
public class CertLoginService extends GenericDAOService {
    public CertLoginService() {
        super(CertCustomerDAO.class, StateDAO.class, CertLoginDAO.class, CampaignDAO.class);
    }
    public CertLogin createCertLogin(CertLoginDTO certLoginDTO){

        Optional<CertLogin> certLoginOptional=certLoginDAO.find(certLoginDTO.getUser());

        if(certLoginOptional.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("user", "User already exist.")).build());
        }
        Optional<CertCustomer> certCustomer= certCustomerDAO.find(new Long(certLoginDTO.getCertCustomerId()));

        Long lngCamp=new Long(certLoginDTO.getCampana());
       /* System.out.println("Campana "+lngCamp);*/
        Optional<Campaign> campaignOptional=campaignDAO.find(lngCamp);
        CertLogin certLogin=new CertLogin(certLoginDTO.getUser(),campaignOptional.get(),certLoginDTO.getPassword(),certCustomer.get(),certLoginDTO.getEmail(),true,certLoginDTO.getName());
        return certLoginDAO.saveOrUpdate(certLogin);
    }
    public Optional<CertLogin> findById(String user) {
        return certLoginDAO.find(user);
    }

    public PaginationDTO<CertLogin> list(int sizePage, int page) {
        return certLoginDAO.findAll(sizePage, page);
    }

    public PaginationDTO<CertLogin> list(int sizePage, int page,Long certCustomerId) {
        return certLoginDAO.findAllMb(sizePage, page, certCustomerId);
    }

    public PaginationDTO<CertLogin> list(String orderBy,String dir,int sizePage, int page) {
        PaginationDTO<CertLogin>paginationDTO;
        try{
            //paginationDTO=certCustomerDAO.findAll(orderBy,dir,sizePage,page);
            paginationDTO=certLoginDAO.findAll(sizePage,page);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }
    public PaginationDTO<CertLogin> list(String search,String orderBy,String dir,int sizePage, int page) {
        PaginationDTO<CertLogin> paginationDTO;

        try{
            paginationDTO=certLoginDAO.globalSearch(search, orderBy, dir, sizePage, page);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;

    }
    public Optional<CertLogin> getCertLogin(String certLoginId){
        return certLoginDAO.find(certLoginId);
    }

    public Optional<CertLogin> getAuthLogin(String certLoginId,String password){
        return certLoginDAO.auth(certLoginId,password);
    }

    public PaginationDTO<PreviewBrokerDTO> getCertLoginMB(int sizePage, int page,Long certCustomerId){
        PaginationDTO<PreviewBrokerDTO> paginationDTO;
        paginationDTO=certLoginDAO.findAllMbNt(sizePage, page, certCustomerId);
        return  paginationDTO;

    }

    public CertLogin updateCertLogin(String id, CertLoginDTO certLoginDTO) throws IOException {
        // Validate if exists before
        Optional<CertLogin> certCustomerOptional = certLoginDAO.find(id);
        CertLogin certLogin = (CertLogin) FindSafely.findSafely(certCustomerOptional, CertLogin.TAG);
        // Validate if it's not a duplicate name
       /* List<CertCustomer> all = certCustomerDAO.findAll(certCustomerDTO.getCompanyName());
        if((all.indexOf(hotel) < 0 && all.size() > 0) ||
                all.size() > 1) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("companyName", "Company name cannot be duplicated.")).build());
        }*/
        setParams(certLoginDTO,certCustomerOptional.get());
        return certLoginDAO.saveOrUpdate(certLogin);
    }
    private void setParams(CertLoginDTO certLoginDTO, CertLogin certLogin) {
        if (null != certLoginDTO.getEmail()) {
            certLogin.setEmail(certLoginDTO.getEmail());
        }

        if (null != certLoginDTO.getPassword()) {

            certLogin.setPassword(certLoginDTO.getPassword());
        }

        if (null != certLoginDTO.getName()) {
            certLogin.setName(certLoginDTO.getName());
        }
       /* if (null != certLoginDTO.getCampana()) {
            Optional<Campaign> campaignOptional=campaignDAO.find(new Long(certLoginDTO.getCampana()));
            certLogin.setCampaign(campaignOptional.get());

        }*/

    }

}
