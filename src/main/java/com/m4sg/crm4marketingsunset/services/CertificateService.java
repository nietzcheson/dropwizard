package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.BatchCertificates;
import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.FoliosCertificado;
import com.m4c.model.entity.online.Certificate;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.AllocateBatchDTO;
import com.m4sg.crm4marketingsunset.core.dto.CertificateCustomerDTO;
import com.m4sg.crm4marketingsunset.core.dto.SearchCertificateDTO;
import com.m4sg.crm4marketingsunset.dao.*;
import com.m4sg.crm4marketingsunset.util.StringTools;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Created by sistemas on 09/01/2015.
 */
public class CertificateService extends GenericDAOService {

    public CertificateService() {
        super(CertificateDAO.class, FoliosCertificateDAO.class, CampaignDAO.class, BatchCertificatesDAO.class, SaleDAO.class);
    }

    public List<Certificate> getCertificates(SearchCertificateDTO searchCertificateDTO){
        String certificate= !searchCertificateDTO.getCertificate().isEmpty() ?
                searchCertificateDTO.getCertificate().trim().toLowerCase() : "";
        List<Certificate> certificateCustomerDTOList=
                certificateDAO.findByCert(
                        certificate,
                        searchCertificateDTO.getCampaigns(),
                        searchCertificateDTO.getLimit()
                );
        return certificateCustomerDTOList;
    }

    public List<Certificate> getCertificate(String number){
        return certificateDAO.findByNumber(number);
    }
    public List<Certificate> findFree(String number){
        return certificateDAO.findFree(number);
    }

    public void create(int quantity,FoliosCertificado foliosCertificate){
        Certificate cert;
        String certNumPrefix=foliosCertificate.getCampaign().getId().toString();
        String certNum;
        //  1- E-CERT   2- FISICOS
        for(int i=0;i<quantity;i++){
            certNum=certNumPrefix+StringTools.generaToken(8);
            cert=new Certificate(certNum,foliosCertificate.getId(),foliosCertificate.getCampaign().getId());
            certificateDAO.saveOrUpdate(cert);
        }
    }
    public Integer update(FoliosCertificado foliosCertificate,BatchCertificates batchCertificates,
                       AllocateBatchDTO allocateBatchDTO, Campaign campaign){
        Integer i=certificateDAO.updateFoliosCertificado(batchCertificates.getId(), allocateBatchDTO, foliosCertificate.getId(), campaign.getId());
        return i;
//        for(int i=(int) (long)allocateBatchDTO.getStart();i<=allocateBatchDTO.getEnd();i++){
//
//            cert=certificateDAO.findbySequenceBatch(batchCertificates,i).get();
//            cert.setFolioCertificadoId(foliosCertificate.getId());
//            cert.setCampaign(campaign.getId());
//            certificateDAO.saveOrUpdate(cert);
//        }
    }
    public List<Certificate> getCertificates(Long idFolioCertificado){
        List<Certificate> certificateList ;
        certificateList=certificateDAO.findByIdFolioCertificado(idFolioCertificado);
        return  certificateList;
    }
    public void saveOrUpdate(Certificate certificate){
        certificateDAO.saveOrUpdate(certificate);
    }

    public Certificate assignBooking(Certificate certificate,Long idBooking){
        certificate.setBookingId(idBooking);
        certificate.setAssignedDate(new Date());
        return certificateDAO.saveOrUpdate(certificate);
    }


   /* public String validCertificate(String certificate){

        Optional<Campaign> campaignOptional;
        String certificateOficial="";

        campaignOptional=campaignDAO.findBySlugify(certificate);

        if(campaignOptional.isPresent()){//Certificado generico

            certificateOficial=getFreeGeneric(campaignOptional.get());

        }
        else if(isValidUniqueCertificate(certificate)){
            certificateOficial=certificate;

        }

        return certificateOficial;

    }*/
   public  Optional<Certificate> getCertificateValid(String certificateNumber){
       Optional<Certificate> certificate =certificateDAO.find(certificateNumber);
       return certificate;
   }
    public Certificate validCertificate(String certificateNumber){
        Certificate certificate;
        List<Certificate> certificateLst;

        certificateLst = findFree(certificateNumber);

        if(certificateLst!=null && !certificateLst.isEmpty() && !saleDAO.existsNumcert(certificateNumber)){
            certificate=certificateLst.iterator().next();

        }else{
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Certificate", "Not available.")).build());

        }
        return certificate;
    }

    public boolean isValidUniqueCertificate(String certificate){

        if(!certificateDAO.findFree(certificate).isEmpty()) {

            return !saleDAO.existsNumcert(certificate);
        }
        return false;
    }

    public String getFreeGeneric(Campaign campaign){
        return certificateDAO.findFreeGeneric(campaign);
    }
    public void createBatch(int quantity,BatchCertificates batchCertificates){
        Certificate cert;
        String certNumPrefix= String.valueOf(batchCertificates.getId());
        String certNum;
        //  1- E-CERT   2- FISICOS
       // CertType certType=certT
        for(int i=0;i<quantity;i++){

            int sequ=i+1;
            certNum=certNumPrefix+StringTools.generaToken(8)+sequ;
            cert=new Certificate(certNum,i+1,batchCertificates);
            certificateDAO.saveOrUpdate(cert);
        }


    }
    public List<Certificate>findByLote(long loteId){
        BatchCertificates batchCertificates= batchCertificatesDAO.find(loteId).get();
        return certificateDAO.findByLoteId(batchCertificates);
    }
}
