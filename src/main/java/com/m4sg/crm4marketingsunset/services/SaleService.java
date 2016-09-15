package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.*;
import com.m4c.model.entity.online.Certificate;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.CertificateCustomerDTO;
import com.m4sg.crm4marketingsunset.core.dto.CustomerDTO;
import com.m4sg.crm4marketingsunset.core.dto.SaleCertificateDTO;
import com.m4sg.crm4marketingsunset.core.dto.SaleDTO;
import com.m4sg.crm4marketingsunset.dao.*;
import com.m4sg.crm4marketingsunset.util.DateUtils;
import com.m4sg.crm4marketingsunset.util.Utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Fernando on 01/04/2015.
 */
public class SaleService extends GenericDAOService {
    public SaleService() {
        super(SaleDAO.class, ContractServiceDAO.class, CustomerDAO.class,
                SellerDAO.class, ReservationStatusDAO.class,CertificateDAO.class,CampaignDAO.class,UserIntranetDAO.class,LogM4SGDAO.class);
    }

    public Long createSale(String certificateNumber,Customer customer,String consultant,
                           String verifier,String supervisor,Integer monthlyPayments,
                           String userName,Long status,boolean verificationProcess,
                           Long extra,String collectionUser,Date collectionDate,String comments,String record) throws WebApplicationException{
        CampaignService campaignService=new CampaignService();
        Campaign campaign=campaignService.getCampaign(certificateNumber);
        CertificateService certificateService=new CertificateService();


        Certificate certificate;
        if(verificationProcess) {
            certificate =certificateService.validCertificate(certificateNumber);
        }else{
            certificate=certificateService.getCertificate(certificateNumber).iterator().next();
        }

        Long idBooking =getNewBooking(customer.getCallCenter());
        System.out.println("idBooking==>"+idBooking);

        try {
            certificateService.assignBooking(certificate,idBooking);
            certificateService.saveOrUpdate(certificate);
        }catch (org.hibernate.exception.ConstraintViolationException e){
            // certificateService.resolveIdBookingDuplicate(certificate,idBooking);
        }
        Sale sale= new Sale(campaign,customer,idBooking,consultant,verifier,supervisor,monthlyPayments,certificateNumber,userName,status.toString(),new Date(),new Date(),0,
                extra,collectionUser,collectionDate,comments,record);
        saleDAO.saveOrUpdate(sale);
        addTxService(sale, campaign.getOffer());
        saleDAO.flush();
        return idBooking;
    }
    public Long createSale(SaleCertificateDTO saleCertificateDTO,Long idCliente,String username){

        Optional<Customer> optionalCustomer=customerDAO.find(idCliente);
        Optional<Sellers> optionalAgent=sellerDAO.find(saleCertificateDTO.getAgent());
        Optional<Sellers> optionalChecker=
                saleCertificateDTO.getChecker()==null || saleCertificateDTO.getChecker().isEmpty()
                        ?
                        sellerDAO.find("ATCLIENTES")
                        :
                        sellerDAO.find(saleCertificateDTO.getChecker());
        Optional<Campaign> optionalCampaign=Optional.fromNullable(null);
        Optional<Sellers> optionalSupervisor=sellerDAO.find(saleCertificateDTO.getSupervisor());
        Optional<ReservationStatus> reservationStatusOptional=reservationStatusDAO.find(saleCertificateDTO.getStatus());
        String numcert=saleCertificateDTO.getNumCert();
        Optional<ExtraBonus>extraBonusOptional;
        Optional<Sellers>userCollectionsOptional;
        Long extraBonus=null;
        String userCollections=null;
        Date dateCollections=null;
        Long idBooking=null;


        if(saleCertificateDTO.getCollectionDate()!=null && !saleCertificateDTO.getCollectionDate().isEmpty()){
            dateCollections= DateUtils.stringToDateYYYYMMDD(saleCertificateDTO.getCollectionDate());
        }
        if(saleCertificateDTO.getCollectionAgent()!=null && !saleCertificateDTO.getCollectionAgent().isEmpty()){
            userCollectionsOptional=sellerDAO.find(saleCertificateDTO.getCollectionAgent());
            if(!userCollectionsOptional.isPresent()){
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorRepresentation("Collections user", "Does not exists.")).build());
            }
            userCollections=userCollectionsOptional.get().getId();
        }
        if(numcert==null || numcert.isEmpty()){

            if(saleCertificateDTO.getCampaignId()!=null && saleCertificateDTO.getCampaignId()>0){
                optionalCampaign=campaignDAO.find(saleCertificateDTO.getCampaignId());
            }
            if(!optionalCampaign.isPresent()){
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorRepresentation("Campaign", "Does not exists.")).build());
            }
        }
        if(!optionalCustomer.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Customer", "Does not exists.")).build());
        }
        if(saleCertificateDTO.getExtraBonus()!=null) {
            extraBonusOptional=new ExtraBonusService().extraBonus(saleCertificateDTO.getExtraBonus(),optionalCustomer.get().getCallCenter().getId());
            if(!extraBonusOptional.isPresent()){
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorRepresentation("extraBonus", "Does not exists.")).build());
            }
            extraBonus=extraBonusOptional.get().getExtraBonusId().getId();
        }
        if(!optionalAgent.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Agent", "Does not exists.")).build());
        }
        if(!optionalChecker.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Checker", "Does not exists.")).build());
        }if(!optionalSupervisor.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Supervisor", "Does not exists.")).build());

        }if(!reservationStatusOptional.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("status", "Does not exists.")).build());
        }
        if(numcert==null || numcert.isEmpty()){

            Optional<Certificate> optionalCertificate=certificateDAO.findAvailableCertificateByCampaign(optionalCampaign.get(), null);

            if(optionalCertificate.isPresent()){
                numcert=optionalCertificate.get().getNumber();

            }else{
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorRepresentation("certificate", "Certificates unavailable.")).build());
            }
        }
        try {
            idBooking=createSale(numcert, optionalCustomer.get(), optionalAgent.get().getId(),
                    optionalChecker.get().getId(), optionalSupervisor.get().getId(),
                    1, username,
                    reservationStatusOptional.get().getId(), false, extraBonus, userCollections, dateCollections,
                    saleCertificateDTO.getComment(), saleCertificateDTO.getNumRecord()
            );
        }catch (Exception ex){
            return idBooking;
        }
/*
        Long idBooking =createSale(numcert,optionalCustomer.get(),optionalAgent.get().getId(),
                optionalChecker.get().getId(),optionalSupervisor.get().getId(),
                saleCertificateDTO.getMonthlyPayments(),username,
                reservationStatusOptional.get().getId());
        PaymentService paymentService= new PaymentService();

        List<PaymentServiceResponseDTO> lst=paymentService.getProvidedServices(idBooking);

        Double totalServices=0d;
        for (int i = 0; i < lst.size(); i++) {
            PaymentServiceResponseDTO paymentServiceResponseDTO= lst.get(i);
            totalServices+=paymentServiceResponseDTO.getRemaining();

        }
         Double amountParcial=(totalServices-saleCertificateDTO.getDeposit())/saleCertificateDTO.getMonthlyPayments();
        Long max = null;
        for (int i = 0; i <saleCertificateDTO.getMonthlyPayments() ; i++) {
            GregorianCalendar calendar =new GregorianCalendar();

            calendar.add(Calendar.DAY_OF_MONTH,30);
            max= paymentDAO.findMaxByIdBooking(idBooking);
            Payment payment = new Payment(max,
                    1,
                    "",
                    1,
                    max.toString(),
                    amountParcial,
                    saleCertificateDTO.getAgent(),
                    1d,
                    null,
                    idBooking,calendar.getTime(),
                    saleCertificateDTO.getChecker());
            paymentDAO.saveOrUpdate(payment);
        }


        DataCard dataCard=paymentService.setDataCard(Objects.toString(idBooking),saleCertificateDTO.getCreditCardDTO(),new DataCard(),max.toString());

        dataCardDAO.saveOrUpdate(dataCard);*/

        return idBooking;
    }

    //Crear venta para paquete
    public Long createSale(SaleDTO saleDTO, Customer customer, Campaign campaign){
        Long idBooking =getNewBooking(customer.getCallCenter());
        BankService bankService= new BankService();
        //Obtener Banco
        if(saleDTO.getCurrency()==null)
            saleDTO.setCurrency(1);
        if(saleDTO.getDownpayment()==null)
            saleDTO.setDownpayment(0.0);
        if(saleDTO.getNumberOfPayments()==null)
            saleDTO.setNumberOfPayments(0);
        Double downpayment= saleDTO.getDownpayment();

        Optional<Bank> bankOptional ;
        Bank bank=null;
        if(saleDTO.getBank()!=null){
            bankOptional = bankService.find(saleDTO.getBank());
            if(bankOptional.isPresent() && bankOptional.get().getActive()){
                bank=bankOptional.get();
                downpayment=saleDTO.getDownpayment()!=null ? round(saleDTO.getDownpayment()/bank.getExchangeRate(), 2): 0.0;
            }
        }
//        else{
//            //Error banco no exite
//            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
//                    .entity(new ErrorRepresentation("Bank", "Bank does not exist.")).build());
//        }
        //downpayment= saleDTO.getCurrency()== 1 ? saleDTO.getDownpayment() : round(saleDTO.getDownpayment()/1.0, 2);
        Sale sale= new Sale(campaign,customer,idBooking,"OTRO","OTRO","OTRO",saleDTO.getNumberOfPayments(),null,
                customer.getUserName(),"2",new Date(),new Date(),0,
                null,null,null,saleDTO.getComment(),null);
        saleDAO.saveOrUpdate(sale);
        //Agregar servicio
        addTxService(sale, campaign, saleDTO, bank);
        saleDAO.flush();
        return idBooking;
    }
    public void validateData(Optional<Object> objectOptional){

    }

    public Long getNewBooking(CallCenter callcenter){
        return saleDAO.getNewBooking(callcenter);
    }
    //Agregar servicios y pagos para paquetes
    public void addTxService(Sale sale, Campaign campaign, SaleDTO saleDTO, Bank bank){
        Double exchangeRate= saleDTO.getCurrency() == 1 ? 1.0 : bank.getExchangeRate();
        SubServiceService subServiceService= new SubServiceService();
        PaymentService paymentService = new PaymentService();
        Optional<SubService> subServiceOptional=  subServiceService.findByCampaign(campaign.getId());
        Double downpayment = (saleDTO.getCurrency() == 0 && saleDTO.getDownpayment() > 0) ? round(saleDTO.getDownpayment()/exchangeRate, 2) : saleDTO.getDownpayment();
        if(downpayment > subServiceOptional.get().getPrice()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Downpayment", "Downpayment is higher than the package price.")).build());
        }
        //Ingresar Servicio

        Long start=1L;
        if(subServiceOptional.isPresent()) {
            //Falta tipo de cambio
            addService(sale.getId(), 1l ,subServiceOptional.get(),"PAQ 2.2 CANCUN", sale.getUser(),null, subServiceOptional.get().getPrice(), exchangeRate);
            //Ingresar pagos
            downpayment = saleDTO.getDownpayment()!=null ? saleDTO.getDownpayment() : 0.0;
            Calendar cal = Calendar.getInstance();
            if(downpayment>0){
                downpayment= saleDTO.getDownpayment();
                paymentService
                        .savePayment(sale.getId(), saleDTO.getCurrency(),
                                null, saleDTO.getDownpayment(), sale.getUser(),
                                sale.getUser(), exchangeRate, bank, new Date(), null);
                downpayment= round(downpayment/exchangeRate, 2);
                start++;
                if(sale.getNumberOfPayments()>0 && (subServiceOptional.get().getPrice()-downpayment)>0){
                    Double monthly_payment = round((subServiceOptional.get().getPrice()-downpayment)/saleDTO.getNumberOfPayments(),2);
                    for(int i=0; i < saleDTO.getNumberOfPayments(); i++){
                        cal.add(Calendar.MONTH, 1);
                        Date date= cal.getTime();
                        paymentService.savePayment(sale.getId(), 1,
                                null, monthly_payment, sale.getUser(),
                                sale.getUser(), 1.0, null, date, start);
                        start++;
                    }
                }
            }

        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void addTxService(Sale sale,Offer offer){

        Optional<SubService> subServiceOptional;

        if(offer.getActivationFee() > 0){
            subServiceOptional=new SubServiceService().find(24l);
            if(subServiceOptional.isPresent()) {

                addService(sale.getId(),0l,subServiceOptional.get(),"ACTIVACION", sale.getUser(),sale.getCertificateNumber(),offer.getActivationFee(), 1);
            }
        }
        if(offer.getTaxes() > 0){
            subServiceOptional=new SubServiceService().find(35l);
            if(subServiceOptional.isPresent()) {

                addService(sale.getId(),1l,subServiceOptional.get(),"IMPUESTOS HOTELEROS",sale.getUser(), sale.getCertificateNumber(), offer.getTaxes(), 1);
            }
        }
        if(offer.getPrice() > 0) {//Si tiene precio se refiere a un plan AI

            subServiceOptional=new SubServiceService().find(3719l);
            if(subServiceOptional.isPresent()) {

                addService(sale.getId(),offer.getTaxes()>0?2l:1l,
                        subServiceOptional.get(),"ALL INCLUSIVE",sale.getUser(), sale.getCertificateNumber(), offer.getPrice(), 1);
            }
        }
    }

    public ContractService addService(Long idBooking,
                                      Long idServContratado,
                                      SubService subService,
                                      String description,
                                      String username,
                                      String certificate,
                                      double amount,
                                      double exchangeRate){
        ContractService contractService=
                new ContractService(idBooking,
                        idServContratado,
                        subService,
                        description,//subServiceOptional.get().getDescription(),
                        username,certificate,2,2,5,amount);
        return saveOrUpdateContractService(contractService);
    }

    public ContractService saveOrUpdateContractService(ContractService contractService){
        return  contractServiceDAO.saveOrUpdate(contractService);

    }

    public boolean existsNumcert(String certificate){
        boolean existsNumcert=saleDAO.existsNumcert(certificate);
        saleDAO.flush();
        return existsNumcert;
    }

    public SaleCertificateDTO updateSale(SaleCertificateDTO saleCertificateDTO,Long idBooking){
        CertificateService certificateService=new CertificateService();
        Optional<Sale> saleOptional=saleDAO.find(idBooking);
        Sale sale=saleOptional.get();
        Optional<Certificate> certificate=certificateService.getCertificateValid(sale.getCertificateNumber());
        if(!certificate.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("certificate", "Certificate does not exists.")).build());
        }

        certificate.get().setBookingId(null);
        certificateDAO.saveOrUpdate(certificate.get());
        //Se busca el certificado enviado y se le asigna el booking
        Certificate certificate1 =certificateService.getCertificateValid(saleCertificateDTO.getNumCert()).get();
        certificateService.assignBooking(certificate1,idBooking);
        // Se le asignan a la venta los nuevos datos recibidos
        setParams(saleCertificateDTO,sale);
        saleDAO.saveOrUpdate(sale);
        return saleCertificateDTO;
    }
    public SaleCertificateDTO getSale(Long idBooking){

        Optional<Sale> saleOptional=saleDAO.find(idBooking);
        Sale sale=saleOptional.get();
       SaleCertificateDTO saleCertificateDTO= new SaleCertificateDTO(
               sale.getCampaign().getId(),
               sale.getCertificateNumber(),
               sale.getUser(),
               sale.getSupervisor(),
               new Long(sale.getStatus()),
               sale.getRecord(),
               sale.getComments(),
               sale.getExtras(),
               (sale.getCollectionDate()!=null)?sale.getCollectionDate().toString():null,
               sale.getCollectionAgent(),sale.getVerifier()

       );

        return saleCertificateDTO;
    }
    public CertificateCustomerDTO getCertificate(String queryParam){
        CertificateCustomerDTO certificateCustomerDTO=null;
        if(Utils.isNumber(queryParam)) {
            Optional<Sale> saleOptional = saleDAO.find(new Long(queryParam));
            Sale sale = saleOptional.get();
             certificateCustomerDTO=new CertificateCustomerDTO(sale.getCertificateNumber(),sale.getCampaign(),sale.getCustomer());
        }else{
            Optional<Sale> saleOptional = saleDAO.find(queryParam);
            Sale sale = saleOptional.get();
            certificateCustomerDTO=new CertificateCustomerDTO(sale.getCertificateNumber(),sale.getCampaign(),sale.getCustomer());

        }




        return certificateCustomerDTO;
    }

    public Sale setParams(SaleCertificateDTO saleCertificateDTO,Sale sale){

        Optional<Campaign> optionalCampaign=Optional.fromNullable(null);
        Optional<Sellers> optionalSupervisor=sellerDAO.find(saleCertificateDTO.getSupervisor());
        Optional<ReservationStatus> reservationStatusOptional=reservationStatusDAO.find(saleCertificateDTO.getStatus());
        String numcert=saleCertificateDTO.getNumCert();

        if(numcert==null || numcert.isEmpty()){

            if(saleCertificateDTO.getCampaignId()!=null && saleCertificateDTO.getCampaignId()>0){
                optionalCampaign=campaignDAO.find(saleCertificateDTO.getCampaignId());
            }
            if(!optionalCampaign.isPresent()){
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorRepresentation("Campaign", "Does not exists.")).build());
            }
        }

       if(!optionalSupervisor.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("Supervisor", "Does not exists.")).build());

        }if(!reservationStatusOptional.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("status", "Does not exists.")).build());
        }
        if(optionalCampaign.isPresent()){
            sale.setCampaign(optionalCampaign.get());
        }

        sale.setCertificateNumber(numcert);

        sale.setUser(saleCertificateDTO.getAgent());
        sale.setSupervisor(saleCertificateDTO.getSupervisor());
        sale.setStatus(reservationStatusOptional.get().getId().toString());
        sale.setVerifier(saleCertificateDTO.getChecker());
        sale.setRecord(saleCertificateDTO.getNumRecord());
        sale.setComments(saleCertificateDTO.getComment());
        sale.setExtras(saleCertificateDTO.getExtraBonus());
        if(saleCertificateDTO.getCollectionDate()!=null){
            sale.setCollectionDate(DateUtils.stringToDateYYYYMMDD(saleCertificateDTO.getCollectionDate()));
        }
        sale.setCollectionAgent(saleCertificateDTO.getCollectionAgent());


        return sale;

    }


}
