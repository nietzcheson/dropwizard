package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.BatchCertificates;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.dao.BatchCertificatesDAO;

/**
 * Created by jhon on 16/06/15.
 */
public class InventoryService extends GenericDAOService{


    public InventoryService() {
        super(BatchCertificatesDAO.class);
    }

    public PaginationDTO<BatchCertificates> list(String orderBy,String dir,int sizePage,int page){
         return batchCertificatesDAO.findAll(orderBy,dir,sizePage,page);
    }
    public Optional<BatchCertificates> findById(long id) {
        return batchCertificatesDAO.find(id);
    }
}
