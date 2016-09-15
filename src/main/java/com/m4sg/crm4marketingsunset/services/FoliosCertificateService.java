package com.m4sg.crm4marketingsunset.services;

import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.FoliosCertificado;
import com.m4sg.crm4marketingsunset.core.dto.CertificateDTO;

/**
 * Created by sistemas on 12/01/2015.
 */
public class FoliosCertificateService extends GenericDAOService {

    public FoliosCertificateService( Class<?>... daos) {
        super( daos);
    }

    public FoliosCertificado create(CertificateDTO certificateDTO,Campaign campaign){
        FoliosCertificado foliosCertificate=new FoliosCertificado(
                certificateDTO.getQuantityFolios(),
                campaign
        );
        return create(foliosCertificate);
    }
    public FoliosCertificado create(FoliosCertificado foliosCertificado){
        return foliosCertificateDAO.saveOrUpdate(foliosCertificado);
    }
}
