package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.*;
import com.m4c.model.entity.online.CertLogin;
import com.m4c.model.entity.online.Certificate;
import com.m4c.model.entity.online.Country;
import com.m4c.model.entity.online.State;
import com.m4c.model.entity.sistemas.UserIntranet;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.*;
import com.m4sg.crm4marketingsunset.dao.*;
import com.m4sg.crm4marketingsunset.util.Constants;
import com.m4sg.crm4marketingsunset.util.Utils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hamcrest.Matchers;
import org.hibernate.QueryException;
import org.hibernate.SQLQuery;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.function.matcher.AndMatcher.and;

/**
 * Created by Fernando on 30/03/2015.
 */
public class CustomerService extends GenericDAOService {

    public CustomerService() {
        super(CustomerDAO.class, CountryDAO.class,StateDAO.class, TitleDAO.class, FeatureDAO.class,CertificateDAO.class,FoliosCertificateDAO.class,EdoCivilDAO.class, CampaignDAO.class, CallCenterDAO.class);
    }

    public PaginationDTO<PreviewCustomerDTO> list(String search,String orderBy,String dir,int sizePage, int page, String fromDate, String toDate){
        List<CallCenter> callCenterList;
        callCenterList=callCenterDAO.findByName("Certifi","Travel Club");
        return customerDAO.globalSearch(search,orderBy,dir,sizePage,page, callCenterList,fromDate,toDate);

    }
    public PaginationDTO<CustomerDataLongDTO> listExtra(String search,String orderBy,String dir,int sizePage, int page,CertLogin user,String fromDate,String toDate,String status){

        StringBuilder stringBuilder=new StringBuilder();

        if(user.getMasterBroker()!=null){

            for (int i = 0; i <user.getMasterBroker().getCampaigns().size() ; i++) {
                stringBuilder.append(user.getMasterBroker().getCampaigns().get(i).getId());
                if(i==(user.getMasterBroker().getCampaigns().size()-1)){

                }else{
                    stringBuilder.append(",");
                }
            }
        }else{

            stringBuilder.append(user.getCampaign().getId());
        }
        return customerDAO.globalSearchExtra(search,orderBy,dir,sizePage,page, stringBuilder,fromDate,toDate,status);

    }
    public PaginationDTO<PreviewCustomerDTO> list(int sizePage, int page, String fromDate, String toDate) {
        PaginationDTO<PreviewCustomerDTO>paginationDTO;
        try{
            paginationDTO=customerDAO.findAll("IDCLIENTE", Constants.ORDER_DESC,sizePage, page, null,fromDate,toDate);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }
    public PaginationDTO<PreviewCustomerDTO> list(String orderBy,String dir,int sizePage, int page, String fromDate, String toDate) {
        PaginationDTO<PreviewCustomerDTO>paginationDTO;
        List<CallCenter> callCenterList;
        callCenterList=callCenterDAO.findByName("Certifi","Travel Club");
//        CallCenter callCenter= callCenterDAO.find(201).get();
        try{
            paginationDTO=customerDAO.findAll(orderBy,dir,sizePage,page, callCenterList,fromDate,toDate);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
        return paginationDTO;
    }
    public Customer createCustomerSale(CustomerDTO customerDTO){
        Customer customer=createCustomer(customerDTO);
        String certificateNumber="";

        if(customerDTO.getSale().getCertificate()==null || customerDTO.getSale().getCertificate().isEmpty()){
            //findFreeGeneric
            CampaignService campaignService=new CampaignService();
            Campaign campaign=campaignDAO.find(customerDTO.getSale().getCampaignId()).get();
            // Verificar callcenter en caso de ser 201, 1541, 5962 es certificados, si no es paquete
            Long[] callCentersIds = Constants.callCentersIds;
            if( Arrays.asList(callCentersIds).contains(campaign.getCallCenter().getId())){
                //Es certificado
                certificateNumber=certificateDAO.findFreeGeneric(campaign);
            }else{
                //Es paquete
                new SaleService().createSale(customerDTO.getSale(), customer, campaign);
                return customer;
            }
        }else{
            certificateNumber=customerDTO.getSale().getCertificate();
        }
        new SaleService().createSale(certificateNumber,customer,
                                     "ATCLIENTES","ATCLIENTES","ATCLIENTES",
                                     0,customer.getUserName(),1l,true,null,null,null,
                                    customerDTO.getSale().getComment(),null);
        return customer;
    }
    public Customer createCustomer(CustomerDTO customerDTO){
        Customer customer=setParams(customerDTO, new Customer());
        return createClient(customer);
    }
    public Customer updateCustomer(CustomerDTO customerDTO,long id){

        Optional<Customer> customerOptional=customerDAO.find(id);
        Utils.isValid(customerOptional,"customer");
        Customer customer=setParams(customerDTO,customerOptional.get());

        return customerDAO.saveOrUpdate(customer);
    }


    public Customer setParams(CustomerDTO customerDTO,Customer customer){
        if(customerDTO.getCountryCode()!=null && !customerDTO.getCountryCode().isEmpty()){

            Optional<Country> countryOptional=countryDAO.find(customerDTO.getCountryCode());
            Utils.isValid(countryOptional,"countryCode");

           customer.setCountry(countryOptional.get());

        }if(customerDTO.getState()!=null && customerDTO.getState()>0l){

            Optional<State> stateOptional=stateDAO.find(customerDTO.getState());
            Utils.isValid(stateOptional,"stateID");
            customer.setState(stateOptional.get());

        }
        if(customer.getState()==null){
            Optional<State> stateOptional=stateDAO.findStateNone(customer.getCountry());
            if(stateOptional.isPresent())
                customer.setState(stateOptional.get());
        }
//        if(customerDTO.getBirthdate()!=null && !customerDTO.getBirthdate().isEmpty()){
//            customer.setBirthdate(DateUtils.stringToDateYYYYMMDD(customerDTO.getBirthdate()));
//        }
        if(customerDTO.getUserName() !=null && !customerDTO.getUserName().isEmpty()){

            Optional<UserIntranet> userIntranetOptional=new UserIntranetService().find(customerDTO.getUserName());
            Optional<CertLogin> certLoginOptional=new CertLoginService().findById(customerDTO.getUserName());
            if(!userIntranetOptional.isPresent()&&!certLoginOptional.isPresent()){
                Utils.isValid(userIntranetOptional,"userName");
            }

            if(userIntranetOptional.isPresent()){
                customer.setUserName(userIntranetOptional.get().getUsername());
            }else{
                customer.setUserName(certLoginOptional.get().getUser());
            }

        }
        if(customerDTO.getOccupation()!=null && !customerDTO.getOccupation().isEmpty()){
            customer.setOccupation(customerDTO.getOccupation());
        }
        if(customerDTO.getStatusMarital()!=null && !customerDTO.getStatusMarital().isEmpty()){

            Optional<EdoCivil> edoCivilOptional=edoCivilDAO.findID(customerDTO.getStatusMarital());
            Utils.isValid(edoCivilOptional,"statusMarital");
            customer.setMaritalStatus(edoCivilOptional.get());
        }
        if(customerDTO.getTitle()!=null && customerDTO.getTitle()>0l){

            Optional<Title> titleOptional=titleDAO.find(customerDTO.getTitle());
            Utils.isValid(titleOptional,"title");
            customer.setTitle(titleOptional.get());
        }
        if(customerDTO.getFeatureLst()!=null && customerDTO.getFeatureLst().length>0){

            List<CustomerFeatures> featuresLstAux=new ArrayList<CustomerFeatures>();

            for(Integer featureId: Arrays.asList(customerDTO.getFeatureLst())){
                Optional<Features> optionalFeatures=featureDAO.find(featureId);
                if(optionalFeatures.isPresent()) {

                    featuresLstAux.add(new CustomerFeatures(customer, optionalFeatures.get()));
                }
            }
            if(customer.getId()==null){
                customer.getFeaturesesLst().addAll(featuresLstAux);
            }else{

                customer.setFeaturesesLst((mergeFeatures(new ArrayList<CustomerFeatures>(customer.getFeaturesesLst()),featuresLstAux)));
            }

        }
        if(customerDTO.getSale()!=null) {
            if (customerDTO.getSale().getCertificate() == null || customerDTO.getSale().getCertificate().isEmpty()) {
                if(customerDTO.getSale().getCertificate()==null|| customerDTO.getSale().getCertificate().isEmpty() ){//findFreeGeneric
                    Optional <Campaign> campaignOptional=campaignDAO.find(customerDTO.getSale().getCampaignId());
                    Integer active= campaignOptional.isPresent()? campaignOptional.get().getActive() :0 ;
                    if(active==0){
                        throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                                .entity(new ErrorRepresentation("Campaign", "Not valid")).build());
                    }
                    Campaign campaign=campaignOptional.get();
                    if(campaign.getCallCenter() != null){
                        customer.setCallCenter(campaign.getCallCenter());
                    }

                    if (customer.getId() == null) {
                        Long id= customerDAO.findMax();
                        customer.setId(id);
                    }
                }else{
                    throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                            .entity(new ErrorRepresentation("Certificate", "Does not exists.")).build());
                }

            } else if (customer.getId() == null) {
                Long id= customerDAO.findMax();
                customer.setId(id);
                List<Certificate> certificateLst = certificateDAO.findByNumber(customerDTO.getSale().getCertificate());

                if (!certificateLst.isEmpty()) {
                    Certificate certificate = certificateLst.iterator().next();
                    Campaign campaign= campaignDAO.find(certificate.getCampaign()).get();
                    if(campaign.getCallCenter() != null){
                        customer.setCallCenter(campaign.getCallCenter());
                    }
//                    Optional<FoliosCertificado> foliosCertificadoOptional = foliosCertificateDAO.find(certificate.getFolioCertificadoId());
//                    Utils.isValid(foliosCertificadoOptional, "Certificate");
//                    if (foliosCertificadoOptional.isPresent()) {
//                        customer.setCallCenter(foliosCertificadoOptional.get().getCampaign().getCallCenter());
//                    }
                }
            }
        }else{
//            Campaign campaign= campaignDAO.find(customerDTO.getC);
        }
        if(customerDTO.getAddress()!=null && !customerDTO.getAddress().isEmpty()){
            customer.setAddress(customerDTO.getAddress());
        }if(customerDTO.getEmail()!=null && !customerDTO.getEmail().isEmpty()){
            customer.setEmail(customerDTO.getEmail());
        }if(customerDTO.getEmail2()!=null && !customerDTO.getEmail2().isEmpty()){
            customer.setEmail2(customerDTO.getEmail2());
        }if(customerDTO.getCity()!=null && !customerDTO.getCity().isEmpty()){
            customer.setCity(customerDTO.getCity());
        }if(customerDTO.getFirstName()!=null && !customerDTO.getFirstName().isEmpty()){
            customer.setFirstName(customerDTO.getFirstName());
        }if(customerDTO.getLastName() !=null && !customerDTO.getLastName().isEmpty()){
            customer.setLastName(customerDTO.getLastName());
        }if(customerDTO.getPhone()!=null && !customerDTO.getPhone().isEmpty()){
            customer.setPhone1(customerDTO.getPhone().replaceAll("[^\\d.]", ""));
        }if(customerDTO.getPhone2()!=null && !customerDTO.getPhone2().isEmpty()){
            customer.setMovil(customerDTO.getPhone2().replaceAll("[^\\d.]", ""));
        }if(customerDTO.getPhone3()!=null && !customerDTO.getPhone3().isEmpty()){
            customer.setPhone2(customerDTO.getPhone3().replaceAll("[^\\d.]", ""));
        }if(customerDTO.getZip()!=null && !customerDTO.getZip().isEmpty()){
            customer.setPostalCode(customerDTO.getZip().split("\\.", 2)[0]);
        }
        if(customerDTO.getdOBYear()!=null ){
            customer.setDOBYear(customerDTO.getdOBYear());
        }if(customerDTO.getdOBMonth()!=null){
            customer.setDOBMonth(customerDTO.getdOBMonth());
        }
        if(customerDTO.getdOBDay()!=null){
            customer.setDOBDay(customerDTO.getdOBDay());
        }
        customer.setUpdateDate(new Date());
        return customer;
    }

    public Customer createClient(Customer customer){
        return customerDAO.saveOrUpdate(customer);
    }

    public List<ResultbyDateDTO> report(ReportDTO reportDTO, Long idcampaign){
        List<ResultbyDateDTO> list = customerDAO.ReportWelcomeCall(
                new StringBuilder().append(idcampaign),
                reportDTO.getFrom()!=null ? new StringBuilder().append(reportDTO.getFrom()): new StringBuilder(""),
                reportDTO.getTo()!=null ? new StringBuilder().append(reportDTO.getTo()): new StringBuilder(""),
                reportDTO.getBy().toUpperCase()
        );
        /*Long total=0L, si=0L, no=0L;
        if(list.size()>0){
            no=new Long(list.get(0));
            if(list.size()>1)
                si=new Long(list.get(1));
            total= si + no;
        }*/
//        ResultReportDTO resultReportDTO=new ResultReportDTO();
//        resultReportDTO.setDone(si);
//        resultReportDTO.setNodone(no);
//        resultReportDTO.setTotal(total);
        return list;
    }

    public Optional<Customer> find(long id){
        return customerDAO.find(id);
    }

    public HashSet<CustomerFeatures> mergeFeatures(List<CustomerFeatures> customerFeaturesLst,List<CustomerFeatures> customerFeaturesesLstRQ){

        List<CustomerFeatures> customerFeaturesLstAux=new ArrayList<CustomerFeatures>(customerFeaturesLst);
        CustomerFeaturesService customerFeaturesService=new CustomerFeaturesService();

            for(CustomerFeatures customerFeatures:customerFeaturesLstAux){

                if(select(customerFeaturesesLstRQ,and(having(on(CustomerFeatures.class).getCustomer(),
                        Matchers.equalTo(customerFeatures.getCustomer())),
                        having(on(CustomerFeatures.class).getFeatures(),Matchers.equalTo(customerFeatures.getFeatures())
                             )))
                        .size()==0){

                    customerFeaturesLst.remove(customerFeatures);
                    customerFeaturesService.delete(customerFeatures.getCustomer(),customerFeatures.getFeatures());
                }
            }

        for(CustomerFeatures customerFeatures:customerFeaturesesLstRQ){

            if(select(customerFeaturesLst,and(having(on(CustomerFeatures.class).getCustomer(),
                            Matchers.equalTo(customerFeatures.getCustomer())),
                    having(on(CustomerFeatures.class).getFeatures(),Matchers.equalTo(customerFeatures.getFeatures())
                    )))
                    .size()==0){

                customerFeaturesLst.add(customerFeatures);
            }

        }
        return new HashSet<CustomerFeatures>(customerFeaturesLst);

    }
    public void refresh(Customer customer){
         customerDAO.refresh(customer);
    }

    public void flush(){
        customerDAO.flush();
    }
    public void commit(){
        customerDAO.commit();
    }
    public void insertCustomerSaleFromExcel(InputStream uploadedInputStream, String userName)throws IOException{

        XSSFWorkbook wb = new XSSFWorkbook(uploadedInputStream);
        XSSFSheet worksheet= wb.getSheetAt(0);
        Customer customer;
        CustomerDTO customerDTO;


        for (Row row : worksheet) {
            customerDTO=new CustomerDTO();
            customerDTO.setUserName("INTRANET");
            String certificate="";
            Long campaignId=0l;
            boolean isOk=true;

            customerDTO.setUserName("INTRANET");
            if(row.getRowNum()>0){

            for (Cell cell : row) {
                //set foreground color here
                String content = cell==null ? "": cell.toString();
                switch (cell.getColumnIndex()){
                    case 0:
                        campaignId = (long)cell.getNumericCellValue();
                        break;
                    case 1:
                        certificate=content;
                        break;
                    case 2:
                        customerDTO.setFirstName(content);
                        break;
                    case 3://INTRANET
                        customerDTO.setLastName(content);
                        break;
                    case 4:
                        customerDTO.setAddress(content);
                        break;
                    case 5:
                        customerDTO.setCity(content);
                        break;
                    case 6:
                        Optional<State> stateOptional=new StateService().getState(content);
                        if(stateOptional.isPresent())
                            customerDTO.setState(stateOptional.get().getId());
                        break;
                    case 7:
                        customerDTO.setZip(content);
                        break;
                    case 8:
                        customerDTO.setPhone(content.replaceAll("[^\\d]", ""));
                        break;
                    case 9:
                        Optional<Country> countryOptional=new CountryService().getCountry(content);
                        if(countryOptional.isPresent())
                            customerDTO.setCountryCode(content);
                        break;
                    case 10:
                        customerDTO.setEmail(content);
                        break;
                }
            }
            if(certificate.equalsIgnoreCase("ASIGNAR") && campaignId > 0l){
                Optional<Campaign> campaignOptional=new CampaignService().findById(campaignId);
                if(campaignOptional.isPresent()){
                    certificate=new CertificateService().getFreeGeneric(campaignOptional.get());
                }else{
                    isOk=false;
                }

            }else if(certificate.isEmpty() || !new CertificateService().isValidUniqueCertificate(certificate)){
                    isOk=false;
                }

            if(isOk) {
                SaleDTO saleDAO =new SaleDTO();
                saleDAO.setCertificate(certificate);
                customerDTO.setSale(saleDAO);
                customer=createCustomer(customerDTO);
                //INTRANET
                new SaleService().createSale(certificate, customer,
                                             "ATCLIENTES","ATCLIENTES","ATCLIENTES",
                                              0,"INTRANET",1l,true,null,null,null,null,null);
            }}

        }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    public CustomerDTO setParamsDTO(Customer customer){
        CustomerDTO customerDTO =new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setSale(new SaleDTO());
        customerDTO.getSale().setBookingId(customer.getSaleSet().iterator().next().getId());
        customerDTO.setTitle(null);
        customerDTO.setState(null);
        customerDTO.setId(customer.getId());
        return customerDTO;
    }
}
