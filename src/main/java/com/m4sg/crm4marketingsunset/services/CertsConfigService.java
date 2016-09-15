package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.ImageCertificate;
import com.m4c.model.entity.online.CertCustomer;
import com.m4c.model.entity.online.CertsConfig;
import com.m4sg.crm4marketingsunset.core.dto.CertsConfigDTO;
import com.m4sg.crm4marketingsunset.dao.CampaignDAO;
import com.m4sg.crm4marketingsunset.dao.CertCustomerDAO;
import com.m4sg.crm4marketingsunset.dao.CertsConfigDAO;
import com.m4sg.crm4marketingsunset.dao.ImageCertificateDAO;
import com.m4sg.crm4marketingsunset.util.FindSafely;

/**
 * Created by Usuario on 11/05/2015.
 */
public class CertsConfigService extends GenericDAOService  {

    public CertsConfigService() {
        super(CertsConfigDAO.class, CertCustomerDAO.class, CampaignDAO.class, ImageCertificateDAO.class);
    }

    public CertsConfig createCertsConfig(CertsConfigDTO certsConfigDTO){
        Optional<CertCustomer> certCustomer= certCustomerDAO.find(new Long(certsConfigDTO.getCertCustomerId()));
        //Todo: Validar que no existan ninguno asociado a la campaña
        Optional <CertsConfig> certsConfigOptional=certsConfigDAO.findbyIdcamping(certsConfigDTO.getCampania());
        CertsConfig certsConfig;
        if(certsConfigOptional.isPresent()){
            certsConfig = certsConfigOptional.get();
            setParams(certsConfigDTO, certsConfig);
        }else{
            certsConfig= new CertsConfig(certCustomer.get(),certsConfigDTO.getCampania(),certsConfigDTO.getImageCert(),certsConfigDTO.getCertificateType(),certsConfigDTO.getImageCustomer());
            Long id= certsConfigDAO.findMax();
            certsConfig.setId(id);
        }
        return certsConfigDAO.saveOrUpdate(certsConfig);

    }
    public Optional <CertsConfig> getCertConfig(Long certLoginId){
        Optional <CertsConfig> certsConfigOptional=certsConfigDAO.findbyIdcamping(certLoginId);
        return certsConfigOptional;
    }
    public Optional <ImageCertificate> getImageCertificate(String name){
        Optional <ImageCertificate> imageCertificateOptional=imageCertificateDAO.find(name);
        return imageCertificateOptional;
    }
    public CertsConfig editCertsConfig(Long id, CertsConfigDTO certsConfigDTO){
        Optional<CertsConfig> certsConfigOptional=certsConfigDAO.find(id);
        CertsConfig certsConfig=(CertsConfig) FindSafely.findSafely(certsConfigOptional, CertsConfig.TAG);
        setParams(certsConfigDTO, certsConfig);
        return certsConfigDAO.saveOrUpdate(certsConfig);

    }

    private void setParams(CertsConfigDTO certsConfigDTO, CertsConfig certsConfig) {
        if(certsConfigDTO.getCampania() != null){
            certsConfig.setCampaign(new Long(certsConfigDTO.getCampania()));
        }
        if(certsConfigDTO.getImageCert() !=null){
            certsConfig.setImagencert(certsConfigDTO.getImageCert());
        }
        if(certsConfigDTO.getCertCustomerId() !=null){
            CertCustomer certCustomer= certCustomerDAO.find(Long.parseLong(certsConfigDTO.getCertCustomerId())).get();
            certsConfig.setCertCustomer(certCustomer);
        }
        if(certsConfigDTO.getCertificateType() !=null){
            certsConfig.setCertificate(certsConfigDTO.getCertificateType());
        }
        if(certsConfigDTO.getImageCustomer() != null){
            certsConfig.setLogo(certsConfigDTO.getImageCustomer());
        }
    }
}
