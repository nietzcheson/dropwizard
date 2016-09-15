package com.m4sg.crm4marketingsunset.services;

import com.m4c.model.entity.ImageCertificate;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.dao.ImageCertificateDAO;
import org.hibernate.QueryException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by jhon on 07/05/15.
 */
public class EcertService extends GenericDAOService {

    public EcertService() {
        super(ImageCertificateDAO.class);
    }

    public ImageCertificate createImageCertificate(String image,Long type,String url){

        ImageCertificate imageCertificate=new ImageCertificate(image,type,url);
        return imageCertificateDAO.saveOrUpdate(imageCertificate);

    }
    public PaginationDTO<ImageCertificate> listImage(int sizePage, int page) {
        return imageCertificateDAO.list(sizePage, page);
    }
    public PaginationDTO<ImageCertificate> listImage(String orderBy,String dir,int sizePage, int page) {
        PaginationDTO<ImageCertificate>paginationDTO;
        try{
            paginationDTO=imageCertificateDAO.findAll(orderBy, dir, sizePage, page);
            //paginationDTO=certCustomerDAO.findAll(sizePage,page);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }
    public PaginationDTO<ImageCertificate> listImage(String search,String orderBy,String dir,int sizePage, int page) {
        PaginationDTO<ImageCertificate> paginationDTO;

        try{
            paginationDTO=imageCertificateDAO.globalSearch(search, orderBy, dir, sizePage, page);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;

    }

}
