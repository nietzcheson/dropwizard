package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.google.common.primitives.Booleans;
import com.m4c.model.entity.*;
import com.m4c.model.entity.online.CertCustomer;
import com.m4c.model.entity.online.CertLogin;
import com.m4c.model.entity.online.Certificate;
import com.m4c.model.entity.online.Country;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.*;
import com.m4sg.crm4marketingsunset.dao.*;
import com.m4sg.crm4marketingsunset.util.Constants;
import org.apache.commons.collections.Predicate;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.QueryException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by sistemas on 07/01/2015.
 */
public class CampaignService extends GenericDAOService {


    public CampaignService() {
        super(MerchantDAO.class,
                SegmentDAO.class,
                CampaignDAO.class,
                CountryDAO.class,
                CallCenterDAO.class,
                OfferDAO.class,
                ReservationGroupDAO.class,
                FoliosCertificateDAO.class,
                CertCustomerDAO.class,
                CertLoginDAO.class,
                CertificateDAO.class,
                BatchCertificateFoliosDAO.class,
                BatchCertificatesDAO.class
                );
    }

    public Campaign createCampaign(CampaignDTO campaignDTO) {
        Optional<Campaign> campaignOptional=campaignDAO.find(campaignDTO.getName());
        if(campaignOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("name", "Campaign already exist.")).build());
        }
        Campaign campaign = setParams(campaignDTO, new Campaign());
        createCampaign(campaign);
        setBrokers(campaignDTO,campaign);
        return campaign;
    }

    public void setBrokers(CampaignDTO campaignDTO, Campaign campaign){
        if(campaignDTO.getBrokerId().length>0){
            for (int i = 0; i < campaignDTO.getBrokerId().length; i++) {
                String lng=campaignDTO.getBrokerId()[i];
                System.out.println("lng "+lng);
                CertLogin certLogin = lng != null ? certLoginDAO.find(lng).get() : null;
                if (certLogin != null) {
                    System.out.println("certLogin "+certLogin.getUser());

                    certLogin.setCampaign(campaign);
                    certLoginDAO.saveOrUpdate(certLogin);
                }
            }
        }


    }
    public Campaign setParams(CampaignDTO campaignDTO, Campaign campaign) {

        Segment segment = campaignDTO.getSegment() > 0 ? segmentDAO.find(campaignDTO.getSegment()).get() : null;
        Merchant merchant = campaignDTO.getMerchant() != null ? merchantDAO.findByClave(campaignDTO.getMerchant()).get() : null;
//        UserCRM userCRM = campaignDTO.getUserToken() != null ? userCRMDAO.findByToken(campaignDTO.getUserToken()).get() : null;
//        UserCRM brokerCRM = campaignDTO.getBrokerToken() != null ? userCRMDAO.findByToken(campaignDTO.getBrokerToken()).get() : null;
        Country country = campaignDTO.getCountry() != null ? countryDAO.find(campaignDTO.getCountry()).get() : null;
        CallCenter callCenter = campaignDTO.getCallcenter() != null ? callCenterDAO.find(campaignDTO.getCallcenter()).get() : null;
        ReservationGroup reservationGroup = campaignDTO.getReservationGroup() > 0 ? reservationGroupDAO.find(campaignDTO.getReservationGroup()).get() : null;
        Offer offer = campaignDTO.getOfferId() != null ? offerDAO.find(campaignDTO.getOfferId()).get() : null;

        CertCustomer certCustomer = campaignDTO.getCerCustomerId() != null ? certCustomerDAO.find(campaignDTO.getCerCustomerId()).get() : null;


        //detecta si es "1" generico, crear un folio, y asignarselo
        if (campaignDTO.getName() != null && !campaignDTO.getName().isEmpty()) {
            campaign.setName(campaignDTO.getName());
            //campaign.setSlug(StringTools.slugify(campaignDTO.getName(), true));
        }
        if (campaignDTO.getDescription() != null && !campaignDTO.getDescription().isEmpty()) {
            campaign.setDescription(campaignDTO.getDescription());
        }
        campaign.setSlug(campaignDTO.getSlug());
       /* if (campaignDTO.getCode() != null && !campaignDTO.getCode().isEmpty()) {
            campaign.setCodigo(campaignDTO.getCode());
        }*/



        if(campaign.getId()==null){
            campaign.setId(campaignDAO.findMax());
        }
        campaign.setType(2);
        if (segment != null) {
            campaign.setSegment(segment);
        }
        if (merchant != null) {
            campaign.setMerchant(merchant.getClave());
        }
        if (certCustomer != null) {
            campaign.setCertCustomer(certCustomer);
        }

//        if (brokerCRM != null) {
//            campaign.setBrokerCRM(brokerCRM);
//        }
        if (country != null) {
            campaign.setCountry(country);
        }
        if (callCenter != null) {
            campaign.setCallCenter(callCenter);
        }
        if (reservationGroup != null) {
            campaign.setReservationGroup(reservationGroup);
        }
        if (offer != null) {

            campaign.setOffer(offer);

        }
        campaign.setProgram(new ProgramService().find(4l).get());
        if (campaignDTO.getExpiration() != null && campaignDTO.getExpiration() > 0) {
            campaign.setExpira(campaignDTO.getExpiration());
        }
        if (campaignDTO.getTypeFolio() != null && campaignDTO.getTypeFolio() > 0) {
            campaign.setTypeFolio(campaignDTO.getTypeFolio());
        }
        if (campaignDTO.getTypeCert() != null && campaignDTO.getTypeCert() > 0) {
            campaign.setTypeCertificate(campaignDTO.getTypeCert());
        }
        campaign.setStatus(1);
        campaign.setDateUpdated(new Date());

        List<CertLogin> lstLogi= new ArrayList<CertLogin>();
        /*campaign.setBrokers(lstLogi);*/
         lstLogi.addAll(campaign.getBrokers());
        for (int i = 0; i < lstLogi.size() ; i++) {
            CertLogin certLogin =certLoginDAO.find(lstLogi.get(i).getUser()).get() ;

            certLogin.setCampaign(null);
            certLoginDAO.saveOrUpdate(certLogin);
            System.out.println("Campaign "+certLogin.getCampaign());
        }


        return campaign;

    }

    public boolean createCertificates(CertificateDTO certificateDTO) {
        CertificateService certificateService = new CertificateService();
        Campaign campaign = campaignDAO.find(certificateDTO.getCampaignId()).get();
        FoliosCertificado foliosCertificate = new FoliosCertificado(
                certificateDTO.getQuantityFolios(),
                campaign);

        campaign.getFoliosCertificado().add(foliosCertificate);
        campaignDAO.saveOrUpdate(campaign);
        campaignDAO.flushSession();
        certificateService.create(certificateDTO.getQuantityFolios(), foliosCertificate);
        return true;

    }

    public List<CertificateResultDTO> insertCertificates(InputStream uploadedInputStream,
                                                         Long idCampaign,
                                                         String action)
            throws IOException {
        /*  Servicio para la inserción de nuevos folios de certificados */
        XSSFWorkbook wb = new XSSFWorkbook(uploadedInputStream);
        XSSFSheet worksheet= wb.getSheetAt(0);
        Optional<Campaign> campaignOptional=campaignDAO.find(idCampaign);
        List<CertificateResultDTO> results= new ArrayList<CertificateResultDTO>();
        if(campaignOptional.isPresent()){
            Campaign campaign = campaignOptional.get();
            Certificate certificate;
            Optional<Certificate> certificateOptional;
            List<Certificate> certificateList= new ArrayList<Certificate>();
            /*  Recorrer excel con listado de certificados en columna 1 */
            /*  para obtener certificados a insertar o reasignar */
            for (Row row : worksheet) {
                CertificateResultDTO certificateResultDTO= new CertificateResultDTO();
                String certNum = row.getCell(0)==null ?"":   row.getCell(0).toString();
                if(!certNum.isEmpty()){
                    certificateOptional= certificateDAO.find(certNum);
                    /*  Si se desea crear un nuevo certificado */
                    if(action.toUpperCase().equals("INSERTAR")){
                        /*  Validar que el certificado no exista */
                        if(certificateOptional.isPresent()){
                            String text_camp = certificateOptional.get().getCampaign()!=null ?
                                    certificateOptional.get().getCampaign().toString() : " Ninguna";
                            certificateResultDTO.setResult("Certificado existente, asociado a campaña: "
                                    + text_camp );
                        }else if(findCertificate(certificateList, certNum))
                            certificateResultDTO.setResult("Certificado repetido en excel");
                        else{
                            /*  Crear certificado */
                            certificate= new Certificate(certNum, null, campaign.getId());
                            certificateList.add(certificate);
                            certificateResultDTO.setResult("INSERTADO");
                        }
                    }else{
                        /*  Si se desea reasignar un certificado viejo s/lote s/folio */
                        Long idbooking = certificateOptional.isPresent() ? certificateOptional.get().getBookingId() : null;
                        /*  Validar que el certificado no tenga idbooking */
                        if(idbooking != null)
                            certificateResultDTO.setResult("Certificado con idbooking: " + idbooking );
                        /*  Validar que el certificado exista */
                        else if(!certificateOptional.isPresent())
                            certificateResultDTO.setResult("Certificado no existe" );
                        else if(findCertificate(certificateList, certNum))
                            certificateResultDTO.setResult("Certificado repetido en excel");
                        else{
                            certificate=certificateOptional.get();
                            /*  Cambiar campaña del certificado si no tiene lote o folio */
                            if(certificate.getFolioCertificadoId()==null && certificate.getBatchId()==null){
                                certificate.setCampaign(campaign.getId());
                                certificateList.add(certificate);
                                certificateResultDTO.setResult("REASIGNADO");
                            }else
                                certificateResultDTO.setResult("Certificado con lote" );
                        }
                    }
                    certificateResultDTO.setCertificate(certNum);
                    results.add(certificateResultDTO);
                }
            }
            if(certificateList.size()>0){
                /*  Crear el lote con los certificados creados o reasignados    */
                FoliosCertificado foliosCertificate = new FoliosCertificado(
                        certificateList.size(),campaign);
                foliosCertificateDAO.saveOrUpdate(foliosCertificate);
                saveCertificates(foliosCertificate, certificateList);
            }
        }else{
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Campaign", "Campaign doesn't exist.")).build());
        }
        return results;
    }

    public boolean findCertificate(List<Certificate> certificateList, String certNum){
        for (Certificate certificate: certificateList){
            if(certificate.getNumber().equals(certNum))
                return true;
        }
        return false;
    }

    public boolean saveCertificates(FoliosCertificado foliosCertificado, List<Certificate> certificateList){
        for (Certificate cert: certificateList) {
            System.out.println(cert.getNumber());
            cert.setFolioCertificadoId(foliosCertificado.getId());
            certificateDAO.saveOrUpdate(cert);
        }
        return true;
    }

    public List<CertificateResultDTO> moveCertificates(InputStream uploadedInputStream,
                                                       Long idBatch, Long idCampaign, Long idFolio)
            throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook(uploadedInputStream);
        XSSFSheet worksheet= wb.getSheetAt(0);
        Optional<Campaign> campaignOptional=campaignDAO.find(idCampaign);
        List<CertificateResultDTO> results= new ArrayList<CertificateResultDTO>();
        Optional<FoliosCertificado> foliosCertificadoOptional= foliosCertificateDAO.find(idFolio);
        /*  Validar que exista la campaña   */
        if(!campaignOptional.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Campaign", "Campaign doesn't exist.")).build());
        }else if(!foliosCertificadoOptional.isPresent()){
            /*  Validar que exista lote */
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Batch", "Batch doesn't exist.")).build());
        }else if(campaignOptional.get().getId()==foliosCertificadoOptional.get().getCampaign().getId()){
            /*  Que la campaña a reasignar no sea la misma a la que el lote está asignado   */
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Campaign", "Please, select another campaign.")).build());
        }else{
            Campaign campaign = campaignOptional.get();
            Certificate certificate;
            Optional<Certificate> certificateOptional;
            List<Certificate> certificateList= new ArrayList<Certificate>();
            /*  Recorrer excel con certificados */
            for (Row row : worksheet) {
                CertificateResultDTO certificateResultDTO= new CertificateResultDTO();
                String certNum = row.getCell(0).toString();
                if(!certNum.isEmpty()){
                    certificateOptional= certificateDAO.find(certNum);
                    /*  Validar que exista el certificado   */
                    if(!certificateOptional.isPresent())
                        certificateResultDTO.setResult("Certificate doesn't exist");
                    /*  Validar que el certificado pertenezca al lote   */
                    else if(!certificateOptional.get().getFolioCertificadoId().equals(idFolio))
                        certificateResultDTO.setResult("Certificate doesn't belong to this Batch");
                    /*  Validar que el certificado no tenga idbooking   */
                    else if(certificateOptional.get().getBookingId()!=null)
                        certificateResultDTO.setResult("Certificate has an idbooking");
                    else{
                        /*  Reasignar certificado a la campaña */
                        certificate=certificateOptional.get();
                        certificate.setCampaign(campaign.getId());
                        certificateList.add(certificate);
                        certificateResultDTO.setResult("REASIGNADO");
                    }
                    certificateResultDTO.setCertificate(certNum);
                    results.add(certificateResultDTO);
                }
            }
            if(certificateList.size()>0){
                FoliosCertificado foliosCertificate;
                /*  Si se movera el lote completo   */
                if(foliosCertificadoOptional.get().getQuantity() <= certificateList.size()){
                     foliosCertificate = foliosCertificadoOptional.get();
                     foliosCertificate.setCampaign(campaign);
                    foliosCertificateDAO.saveOrUpdate(foliosCertificate);
                }else{
                    /*  Actualizar la cantidad de certificados en el lote anterior  */
                    foliosCertificate = foliosCertificadoOptional.get();
                    foliosCertificate.setQuantity(foliosCertificate.getQuantity()-certificateList.size());
                    foliosCertificateDAO.saveOrUpdate(foliosCertificate);
                    foliosCertificate = new FoliosCertificado(certificateList.size(),campaign);
                    foliosCertificateDAO.saveOrUpdate(foliosCertificate);
                    /*  Crear nuevo lote */
                    AllocateBatchDTO allocateBatchDTO= new AllocateBatchDTO();
                    allocateBatchDTO.setStart(1L);
                    allocateBatchDTO.setEnd(1L);
                    BatchCertificateFolios batchCertificateFolios = setParamsBatchCertificateFolios(allocateBatchDTO, foliosCertificate.getId(), idBatch, new BatchCertificateFolios());
                    batchCertificateFoliosDAO.saveOrUpdate(batchCertificateFolios);
                }
                saveCertificates(foliosCertificate, certificateList);
            }
        }
        return results;
    }

    public BatchCertificateFolios setParamsBatchCertificateFolios(AllocateBatchDTO allocateBatchDTO, Long idFolios, Long batchId, BatchCertificateFolios batchCertificateFolios) {
        FoliosCertificado foliosCertificado= idFolios> 0 ? foliosCertificateDAO.find(idFolios).get() : null;
        BatchCertificates batchCertificates= batchId > 0 ? batchCertificatesDAO.find(batchId).get() : null;
        if (foliosCertificado != null) {
            batchCertificateFolios.setFoliosCertificado(foliosCertificado);
        }
        if (batchCertificates != null) {
            batchCertificateFolios.setBatchCertificates(batchCertificates);
        }
        if (allocateBatchDTO != null) {
            if (allocateBatchDTO.getStart() != null && allocateBatchDTO.getStart() > 0) {
                batchCertificateFolios.setBegin( (int) (long)allocateBatchDTO.getStart());
            }
            if (allocateBatchDTO.getStart() != null && allocateBatchDTO.getStart() > 0) {
                batchCertificateFolios.setEnd((int) (long)allocateBatchDTO.getEnd());
            }
        }
        batchCertificateFolios.setCreatedDate(new Date());
        return batchCertificateFolios;

    }

    public boolean createBatch(AllocateBatchDTO allocateBatchDTO, Long batchId) {
        BatchCertificates batchCertificates= batchCertificatesDAO.find(batchId).get();
        if(allocateBatchDTO.getEnd() > batchCertificates.getQuantity()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Range", "Range is overflow.")).build());
        }

        List<BatchCertificateFolios> batchCertificateFoliosList = batchCertificateFoliosDAO.findbyrange(allocateBatchDTO, batchCertificates);
        if(!batchCertificateFoliosList.isEmpty()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Range", "Batch range already exist.")).build());
        }
        CertificateService certificateService=new CertificateService();
        Campaign campaign = campaignDAO.find(allocateBatchDTO.getIdCampaign()).get();
        FoliosCertificado foliosCertificado = new FoliosCertificado(allocateBatchDTO.getEnd().intValue() - allocateBatchDTO.getStart().intValue()+1,
                campaign);
        foliosCertificateDAO.saveOrUpdate(foliosCertificado);
        BatchCertificateFolios batchCertificateFolios = setParamsBatchCertificateFolios(allocateBatchDTO, foliosCertificado.getId(), batchId, new BatchCertificateFolios());
        batchCertificateFoliosDAO.saveOrUpdate(batchCertificateFolios);
        Integer i= certificateService.update(foliosCertificado, batchCertificates, allocateBatchDTO, campaign);
        if(i>0){
            return true;
        }else{
            return false;
        }
    }

    public Campaign createCampaign(Campaign campaign) {
        return campaignDAO.saveOrUpdate(campaign);
    }

    public Campaign updateCampaign(CampaignDTO campaignDTO) {
        CampaignService campaignService = new CampaignService();
        Campaign campaign = campaignService.findById(campaignDTO.getId()).get();
        setParams(campaignDTO, campaign);
        setBrokers(campaignDTO,campaign);
        return campaignDAO.saveOrUpdate(campaign);
    }

    public Boolean updateEmailConfig(long idcampaign,EmailCampaignDTO emailCampaignDTO){
        CampaignService campaignService = new CampaignService();
        Campaign campaign = campaignService.findById(idcampaign).get();
        setParamsEmailCampaign(emailCampaignDTO, campaign);
        return true;
    }

    public void setParamsEmailCampaign(EmailCampaignDTO emailCampaignDTO, Campaign campaign){
        if (emailCampaignDTO.getSubject() != null && !emailCampaignDTO.getSubject().isEmpty()) {
            campaign.setSubject(emailCampaignDTO.getSubject());
        }
        if (emailCampaignDTO.getBody() != null && !emailCampaignDTO.getBody().isEmpty()) {
            campaign.setBody(emailCampaignDTO.getBody());
        }
    }

    public Optional<Campaign> findById(long id) {
        return campaignDAO.find(id);
    }

    public Optional<Campaign> findBySlug(String slug) {
        return campaignDAO.findBySlugify(slug);
    }

    public Optional<Campaign> find(String name) {
        return campaignDAO.find(name);
    }

    public PaginationDTO<ResultCampaignDTO> list(int sizePage, int page) {
        PaginationDTO<ResultCampaignDTO>paginationDTO;
        List <CallCenter> callCenterList= callCenterDAO.findByName("Certifi","Travel Club");
        try{
            paginationDTO=campaignDAO.findAll("IDCAMPANIA", Constants.ORDER_DESC,sizePage, page, callCenterList);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }

    public PaginationDTO<ResultCampaignDTO> list(String orderBy,String dir,int sizePage, int page) {
        PaginationDTO<ResultCampaignDTO>paginationDTO;
        try{
            List <CallCenter> callCenterList= callCenterDAO.findByName("Certifi","Travel Club");
            paginationDTO=campaignDAO.findAll(orderBy,dir,sizePage,page, callCenterList);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }

    public PaginationDTO<ResultCampaignDTO> list(String search,String orderBy,String dir,int sizePage, int page) {
        PaginationDTO<ResultCampaignDTO> paginationDTO;
        List <CallCenter> callCenterList= callCenterDAO.findByName("Certifi","Travel Club");
        try{
            paginationDTO = campaignDAO.globalSearch(search, orderBy, dir, sizePage, page, callCenterList);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;

    }

    public PaginationDTO<FoliosCertificado> seriesCertificateList(long campaignId,int sizePage, int page) {

        Optional<Campaign>campaign= campaignDAO.find(campaignId);
        if(!campaign.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(new ErrorRepresentation("campaign","campaign does not exist")).build());
        }

        return foliosCertificateDAO.getSeriesCertificate(campaign.get(),sizePage,page);
    }


    public CertificateCampaignDTO validateCertificate(String certificateNumber){

        Optional<Campaign> campaignOptional;
        CertificateService certificateService=new CertificateService();
        Campaign campaign=null;
        CertificateCampaignDTO certificateCampaignDTO =null;
        campaignOptional=campaignDAO.findBySlugify(certificateNumber);


        if(campaignOptional.isPresent()){//Certificado generico

            certificateNumber=certificateService.getFreeGeneric(campaignOptional.get());
            if(!certificateNumber.isEmpty()){
                campaign=campaignOptional.get();
            }
            else{
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorRepresentation("certificate", "unavailable")).build());
            }

        }else{// Certificado unico

            campaign=getCampaign(certificateNumber);

            }

        if(campaign!=null) {

            certificateCampaignDTO = new CertificateCampaignDTO(certificateNumber, campaign);
        }

        return certificateCampaignDTO;
    }


     public Campaign getCampaign(String certificateNumber){
        Optional<FoliosCertificado> foliosCertificadoOptional;
        CertificateService service=new CertificateService();
        List<Certificate> certificateLst=service.findFree(certificateNumber);
         Campaign campaign= null;
        if(!certificateLst.isEmpty()){
            Certificate certificate=certificateLst.iterator().next();
            campaign= campaignDAO.find(certificate.getCampaign()).get();
//            foliosCertificadoOptional=foliosCertificateDAO.find(certificate.getFolioCertificadoId());

        }else{
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("certificate", "Not available.")).build());
        }
        return campaign;
    }


    public boolean createBatchCertificates(BatchCertificateDTO batchCertificateDTO) {

        CertificateService certificateService = new CertificateService();

        BatchCertificates batchCertificates=new BatchCertificates();
        batchCertificates.setQuantity(batchCertificateDTO.getQuantityFolios());
        batchCertificates.setCreatedDate(new Date());
        batchCertificatesDAO.saveOrUpdate(batchCertificates);
        batchCertificatesDAO.flushSession();
       /* FoliosCertificado foliosCertificate = new FoliosCertificado(
                batchCertificateDTO.getQuantityFolios());

        //campaign.getFoliosCertificado().add(foliosCertificate);
        //campaignDAO.saveOrUpdate(campaign);

        //campaignDAO.flushSession();*/
        certificateService.createBatch(batchCertificateDTO.getQuantityFolios(), batchCertificates);
        return true;

    }

    public List<CommonDTO> getCampaigns(List<Long> callcenter){
        List<CommonDTO> campaigns=campaignDAO.find(callcenter);

        return campaigns;
    }

    public HashMap<String, List<CampaignPackageDTO>> getCampaignsByCallcenter(String callcenter){
        List<CampaignPackageDTO> campaigns=campaignDAO.findCampaignsbyCallcenter(callcenter);
        HashMap<String, List<CampaignPackageDTO>> hashMap = new HashMap<String, List<CampaignPackageDTO>>();
        String id="";
        for(CampaignPackageDTO campaignPackageDTO: campaigns){
            id=campaignPackageDTO.getNOMBRE();
            campaignPackageDTO.setID(null);
            campaignPackageDTO.setNOMBRE(null);
            if (!hashMap.containsKey(id)) {
                List<CampaignPackageDTO> list = new ArrayList<CampaignPackageDTO>();
                list.add(campaignPackageDTO);
                hashMap.put(id, list);
            } else {
                hashMap.get(id).add(campaignPackageDTO);
            }
        }
        return hashMap;
    }

    public List<CertificatesFreeDTO> getFoliosbyActiveCampaigns(){
        Calendar calendar= Calendar.getInstance();
        Date to = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        Date from= calendar.getTime();
        List<CertificatesFreeDTO> certificatesFreeDTOList= certificateDAO.getFoliosbyActiveCampaigns(from, to);
        return certificatesFreeDTOList;
    }

    public List<CountFoliosDTO> countFolios(Long campaignId){
        List<CountFoliosDTO> countFoliosDTOList= certificateDAO.countFolios(campaignId);
        return countFoliosDTOList;
    }




}
