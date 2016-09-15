package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.online.CertCustomer;
import com.m4c.model.entity.online.CertLogin;
import com.m4c.model.entity.online.State;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.CertCustomerDTO;
import com.m4sg.crm4marketingsunset.core.dto.MasterBrokerDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.core.dto.ResultSearchMBDTO;
import com.m4sg.crm4marketingsunset.dao.CertCustomerDAO;
import com.m4sg.crm4marketingsunset.dao.CertLoginDAO;
import com.m4sg.crm4marketingsunset.dao.StateDAO;
import com.m4sg.crm4marketingsunset.util.FindSafely;
import org.hibernate.QueryException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * Created by Usuario on 26/03/2015.
 */
public class CertCustomerService extends GenericDAOService {
    public CertCustomerService() {
        super(CertCustomerDAO.class, StateDAO.class, CertLoginDAO.class);
    }

    public CertCustomer createCertCustomer(CertCustomerDTO certCustomerDTO){
        /*Long stateId=new Long(certCustomerDTO.getStateId());
        State state=stateDAO.find(stateId).get();*/

        Optional<CertLogin> certLoginOptional=certLoginDAO.find(certCustomerDTO.getUser());

        if(certLoginOptional.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("user", "User already exist.")).build());
        }
        CertLogin certLogin=new CertLogin(certCustomerDTO.getUser(),certCustomerDTO.getPassword(),certCustomerDTO.getEmail(),certCustomerDTO.getPhone(),certCustomerDTO.getResponsableName(),true);
       CertLogin certLogin1=   certLoginDAO.saveOrUpdate(certLogin);
        CertCustomer certCustomer=new CertCustomer(certCustomerDTO.getCompanyName(),certCustomerDTO.getResponsableName(),certCustomerDTO.getPhone(),certCustomerDTO.getCountryCode(),certCustomerDTO.getStateId(),certCustomerDTO.getCity(),certLogin1,certCustomerDTO.getAddress());

        if(certCustomer.getId()==null){
            certCustomer.setId(certCustomerDAO.findMax());
        }
        certLogin1.setMasterBroker(certCustomer);
        //certCustomer.addCertLogin(certLogin1);
        return certCustomerDAO.saveOrUpdate(certCustomer) ;
    }

    public PaginationDTO<MasterBrokerDTO> list(int sizePage, int page) {
        return certCustomerDAO.globalSearch("", "", "", sizePage, page);
    }
    public PaginationDTO<MasterBrokerDTO> list(String orderBy,String dir,int sizePage, int page) {
        PaginationDTO<MasterBrokerDTO>paginationDTO;
        try{
            paginationDTO=certCustomerDAO.globalSearch("", orderBy, dir, sizePage, page);
            //paginationDTO=certCustomerDAO.findAll(sizePage,page);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }
    public PaginationDTO<MasterBrokerDTO> list(String search,String orderBy,String dir,int sizePage, int page) {
        PaginationDTO<MasterBrokerDTO> paginationDTO;

        try{
            paginationDTO=certCustomerDAO.globalSearch(search,orderBy,dir,sizePage,page);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;

    }


    public CertCustomer updateCertCustomer(long id, CertCustomerDTO certCustomerDTO) throws IOException {
        // Validate if exists before
        Optional<CertCustomer> certCustomerOptional = certCustomerDAO.find(id);
        CertCustomer certCustomer = (CertCustomer) FindSafely.findSafely(certCustomerOptional, CertCustomer.TAG);
        // Validate if it's not a duplicate name
       /* List<CertCustomer> all = certCustomerDAO.findAll(certCustomerDTO.getCompanyName());
        if((all.indexOf(hotel) < 0 && all.size() > 0) ||
                all.size() > 1) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("companyName", "Company name cannot be duplicated.")).build());
        }*/
    setParams(certCustomerDTO,certCustomerOptional.get());
        return certCustomerDAO.saveOrUpdate(certCustomer);
    }
    private void setParams(CertCustomerDTO certCustomerDTO, CertCustomer certCustomer) {
        if (null != certCustomerDTO.getCompanyName()) {
            certCustomer.setCompanyName(certCustomerDTO.getCompanyName());
        }

        if (null != certCustomerDTO.getStateId()) {
            Long stateId=new Long(certCustomerDTO.getStateId());
            State state=stateDAO.find(stateId).get();

            certCustomer.setState(certCustomerDTO.getStateId());
        }

        if (null != certCustomerDTO.getCity()) {
            certCustomer.setCity(certCustomerDTO.getCity());
        }
        if (null != certCustomerDTO.getCountryCode()) {
            certCustomer.setCountry(certCustomerDTO.getCountryCode());
        }
        if (null != certCustomerDTO.getAddress()) {
            certCustomer.setInvoiceAddress(certCustomerDTO.getAddress());
        }
        if (null != certCustomerDTO.getPhone()) {
            certCustomer.setPhone(certCustomerDTO.getPhone());
        }
        if (null != certCustomerDTO.getResponsableName()) {
            certCustomer.setResponsableName(certCustomerDTO.getResponsableName());
        }

        Optional<CertLogin> certLoginOptional=certLoginDAO.find(certCustomerDTO.getUser());
        if(certLoginOptional.isPresent()){
            CertLogin certLogin = certLoginOptional.get();
            if(null!=certCustomerDTO.getUser()){
                certLogin.setUser(certCustomerDTO.getUser());
            }
            if(null!=certCustomerDTO.getPassword()){
                certLogin.setPassword(certCustomerDTO.getPassword());
            }
            if(null!=certCustomerDTO.getEmail()){
                certLogin.setEmail(certCustomerDTO.getEmail());
            }
            if (null != certCustomerDTO.getResponsableName()) {
                certLogin.setName(certCustomerDTO.getResponsableName());
            }
            certLoginDAO.saveOrUpdate(certLogin);
        }

    }
    public Optional<CertCustomer> getCertCustomer(long certCustomerId){
        return certCustomerDAO.find(certCustomerId);
    }

    public List<ResultSearchMBDTO>searchMB(String term){

        return certCustomerDAO.searchMB(term);

    }
}
