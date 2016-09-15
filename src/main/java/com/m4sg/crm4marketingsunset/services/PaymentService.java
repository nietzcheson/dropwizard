package com.m4sg.crm4marketingsunset.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.m4c.model.entity.*;
import com.m4sg.crm4marketingsunset.core.BankResponseBean;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.CreditCardDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaymentDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaymentServiceResponseDTO;
import com.m4sg.crm4marketingsunset.core.dto.ResponseRegisterPaymentDTO;
import com.m4sg.crm4marketingsunset.dao.*;
import com.m4sg.crm4marketingsunset.util.Constants;
import com.m4sg.crm4marketingsunset.util.DateUtils;

import javax.annotation.Nullable;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Fernando on 21/07/2015.
 */
public class PaymentService extends GenericDAOService{

    public PaymentService() {
        super(PaymentDAO.class,
                PaymentServiceDAO.class,
                BancosDAO.class,
                UserIntranetDAO.class,
                LogM4SGDAO.class,
                ContractServiceDAO.class,
                DataCardDAO.class

            );
    }

    public PaymentDTO getPaymentDetail(long idBooking,long idPago,String username){
       return paymentDAO.detail(idBooking,idPago,username);
    }



    public List<PaymentServiceResponseDTO>getPaymentsResponse(long idBooking){

        List<Payment> paymentLst=getPayments(idBooking);

        return Lists.transform(paymentLst, new Function<Payment, PaymentServiceResponseDTO>() {
            @Override
            public PaymentServiceResponseDTO apply(@Nullable Payment payment) {
                return new PaymentServiceResponseDTO(payment.getPayment(),
                        Long.parseLong(payment.getNumPayment()),
                        payment.getBank()==null ? "SIN NOMBRE" : payment.getBank().getName(),
                        payment.getAmount(),
                        payment.getDescCurrency(),
                        payment.getInsertDate(),
                        payment.getUserPayment());
            }
        });
    }
    public List<Payment>getPayments(long idBooking){

        return paymentDAO.findByIdBooking(idBooking);

    }
    public boolean savePayment(Long idBooking,Integer currency,String numAutorizacion,
                               Double amount,String username,String usernameCommission,Double exchangeRate,
                               Bank bank,Date paymentDate, Long max){
        if(max==null)
            max = paymentDAO.findMaxByIdBooking(idBooking);
        Payment payment = new Payment(max,
                currency,
                numAutorizacion,
                1,
                max.toString(),
                amount,
                username,
                exchangeRate,
                bank,
                idBooking,
                paymentDate,
                usernameCommission);

        try {
            paymentDAO.saveOrUpdate(payment);

        }catch (Exception e) {
        return false;
        }
        return true;
    }


    public ResponseRegisterPaymentDTO createPayment(long idBooking, PaymentDTO paymentDTO) {

        ResponseRegisterPaymentDTO responseRegisterPaymentDTO = new ResponseRegisterPaymentDTO();

        String result="";
        Long max = paymentDAO.findMaxByIdBooking(idBooking);
        Random rnd = new Random();
        int numrandom= (int)(rnd.nextDouble() * 1000);
        String referencia=Objects.toString(idBooking)+"-"+max+"-"+ Objects.toString(numrandom);
        Optional<Bank> bank = bancosDAO.find(paymentDTO.getTerminal());
        if(bank.get().getId()==13 ){


            result=Utilerias.UtilsPagos.pagoSantander("YHM",
                    referencia,
                    paymentDTO.getCreditCardDTO().getName(),
                    paymentDTO.getCreditCardDTO().getLastName(),
                    paymentDTO.getCreditCardDTO().getTypeCard(),
                    paymentDTO.getCreditCardDTO().getnCard(),
                    paymentDTO.getCreditCardDTO().getExp(),
                    paymentDTO.getCreditCardDTO().getCvv(),
                    paymentDTO.getAmount().toString(),
                    paymentDTO.getCurrency().toString()
            );
            System.out.println("Result "+result );
            ObjectMapper mapper= new ObjectMapper();
            try {
               BankResponseBean bankResponseBean= mapper.readValue(result, BankResponseBean.class);
                System.out.println("bankResponseBean "+bankResponseBean );
                if (bankResponseBean.status.equals("approved")){
                    registerPayment(idBooking,paymentDTO,bank.get(),max,bankResponseBean.autorizacion);

                }
                responseRegisterPaymentDTO.setDescription(bankResponseBean.mensaje);
                responseRegisterPaymentDTO.setStatus(bankResponseBean.status);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return responseRegisterPaymentDTO;

        }else if(bank.get().getId()==19){

            result=Utilerias.UtilsPagos.pagoBancomer("YHM",
                    referencia,
                    paymentDTO.getCreditCardDTO().getName(),
                    paymentDTO.getCreditCardDTO().getLastName(),
                    paymentDTO.getCreditCardDTO().getTypeCard(),
                    paymentDTO.getCreditCardDTO().getnCard(),
                    paymentDTO.getCreditCardDTO().getExp(),
                    paymentDTO.getCreditCardDTO().getCvv(),
                    paymentDTO.getAmount().toString(),
                    paymentDTO.getCurrency().toString()
            );

            System.out.println("Result "+result );
            ObjectMapper mapper= new ObjectMapper();
            try {
                BankResponseBean bankResponseBean= mapper.readValue(result, BankResponseBean.class);
                System.out.println("bankResponseBean "+bankResponseBean );
                if (bankResponseBean.status.equals("approved")){
                    registerPayment(idBooking,paymentDTO,bank.get(),max,bankResponseBean.autorizacion);

                }
                responseRegisterPaymentDTO.setDescription(bankResponseBean.mensaje);
                responseRegisterPaymentDTO.setStatus(bankResponseBean.status);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseRegisterPaymentDTO;

        }else{
            registerPayment(idBooking,paymentDTO,bank.get(), max,paymentDTO.getAuthorizationNumber());
        }

        responseRegisterPaymentDTO.setDescription("REF "+paymentDTO.getAuthorizationNumber());
        responseRegisterPaymentDTO.setStatus("approved");
        return responseRegisterPaymentDTO;
    }

    private void registerPayment(long idBooking, PaymentDTO paymentDTO,Bank bank,Long max,String numAutorizacion){


        Payment payment = new Payment(max,
                paymentDTO.getCurrency().intValue(),
                numAutorizacion,
                1,
                max.toString(),
                paymentDTO.getAmount(),
                paymentDTO.getUserPayment(),
                paymentDTO.getExchangeRate(),
                bank,
                idBooking,new Date(),
                paymentDTO.getUserCommission());
        paymentDAO.saveOrUpdate(payment);

        DataCard dataCard=setDataCard(Objects.toString(idBooking),paymentDTO.getCreditCardDTO(),new DataCard(),max.toString());

        dataCardDAO.saveOrUpdate(dataCard);

        for (int i = 0; i < paymentDTO.getProvidedServicesDTO().size(); i++) {

            PaymentServiceResponseDTO paymentServiceResponseDTO = paymentDTO.getProvidedServicesDTO().get(i);
            Long maxService = paymentServiceDAO.findMax();
            ContractService contractServiceOptional = contractServiceDAO.find(idBooking,paymentServiceResponseDTO.getId());
            com.m4c.model.entity.PaymentService paymentService = new com.m4c.model.entity.PaymentService(maxService,
                    idBooking,
                    max,
                    contractServiceOptional,
                    paymentServiceResponseDTO.getAmount(),
                    paymentDTO.getCurrency().intValue(),
                    paymentDTO.getExchangeRate()

            );

            paymentServiceDAO.saveOrUpdate(paymentService);
        }

    }
     public ResponseRegisterPaymentDTO updatePayment(long idBooking, PaymentDTO paymentDTO,Long idPayment){

         ResponseRegisterPaymentDTO responseRegisterPaymentDTO = new ResponseRegisterPaymentDTO();


         Optional<Payment> paymentOptional=paymentDAO.find(idBooking,idPayment);
         Payment payment=paymentOptional.get();


         Boolean isOnline=false;
         if(payment.getBank().getId()==13 || payment.getBank().getId()==19){

             isOnline=true;
         }else{
             Optional<DataCard>  dataCardOptional= dataCardDAO.find(payment.getBooking(), payment.getPayment());

             DataCard dataCard= setDataCard(payment.getBooking().toString(),paymentDTO.getCreditCardDTO(),dataCardOptional.get(),payment.getPayment().toString());

             dataCardDAO.saveOrUpdate(dataCard);

         }

         payment=setParams(paymentDTO,payment,isOnline);
         paymentDAO.saveOrUpdate(payment);



         for (int i = 0; i < paymentDTO.getProvidedServicesDTO().size(); i++) {
             PaymentServiceResponseDTO paymentServiceResponseDTO = paymentDTO.getProvidedServicesDTO().get(i);
             ContractService contractServiceOptional = contractServiceDAO.find(idBooking,paymentServiceResponseDTO.getId());

             com.m4c.model.entity.PaymentService paymentServiceOptional=paymentServiceDAO.findById(idBooking, idPayment,contractServiceOptional);
             if(paymentServiceOptional!=null){//Si existe el pago del servicio
                 if(paymentServiceResponseDTO.getAmount().intValue()==0){//Sino tiene importe lo eliminamos
                     paymentServiceDAO.deleteByService(idBooking, idPayment,contractServiceOptional);
                 }else{//Si tiene importe actualizamos los datos
                     paymentServiceOptional.setAmount(paymentServiceResponseDTO.getAmount());
                     paymentServiceOptional.setCurrency(paymentDTO.getCurrency().intValue());
                     paymentServiceOptional.setExchangeRate(paymentDTO.getExchangeRate());
                     paymentServiceDAO.saveOrUpdate(paymentServiceOptional);
                 }
             }else {//Agregar nuevo pago de servicio
                 Long maxService = paymentServiceDAO.findMax();
                 com.m4c.model.entity.PaymentService paymentService = new com.m4c.model.entity.PaymentService(maxService,
                         idBooking,
                         idPayment,
                         contractServiceOptional,
                         paymentServiceResponseDTO.getAmount(),
                         paymentDTO.getCurrency().intValue(),
                         paymentDTO.getExchangeRate()

                 );

                 paymentServiceDAO.saveOrUpdate(paymentService);
             }



         }
         responseRegisterPaymentDTO.setDescription("REF "+paymentDTO.getAuthorizationNumber());
         responseRegisterPaymentDTO.setStatus("approved");

         return responseRegisterPaymentDTO;
    }

    private Payment setParams(PaymentDTO paymentDTO,Payment payment,Boolean isOnline){
        if(isOnline){
            if(paymentDTO.getUserCommission()!=null){
                payment.setUserComission(paymentDTO.getUserCommission());
            }
            if(paymentDTO.getUserPayment()!=null){
                payment.setUserPayment(paymentDTO.getUserPayment());
            }
        }else{
            if(paymentDTO.getTerminal()!=null){
                Optional<Bank> bank = bancosDAO.find(paymentDTO.getTerminal());
                payment.setBank(bank.get());
            }
            if(paymentDTO.getAmount()!=null){
                payment.setAmount(new Double(paymentDTO.getAmount()));
            }
            if(paymentDTO.getCurrency()!=null){
                payment.setCurrency(new Integer(paymentDTO.getCurrency().toString()));
            }
            if(paymentDTO.getAuthorizationNumber()!=null){
                payment.setAutorization(paymentDTO.getAuthorizationNumber());
            }
            if(paymentDTO.getPaymentDate()!=null){
                payment.setReleasedDate(DateUtils.stringToDateYYYYMMDD(paymentDTO.getPaymentDate()));
            }
            if(paymentDTO.getExchangeRate()!=null){
                payment.setExchangeRate(paymentDTO.getExchangeRate());
            }
        }





        return payment;

    }

    public DataCard setDataCard(String idBooking,CreditCardDTO varCreditCardDTO, DataCard dataCard,String payment){

        CreditCardDTO creditCardDTO=encryptCreditCard(varCreditCardDTO);
        dataCard.setCvv( creditCardDTO.getCvv());
        dataCard.setExp(creditCardDTO.getExp());
        dataCard.setLastName(creditCardDTO.getLastName());
        dataCard.setName(creditCardDTO.getName());
        dataCard.setnCard(creditCardDTO.getnCard());
        dataCard.setTipoCard( creditCardDTO.getTypeCard());
        dataCard.setDataCardId( new DataCardId(payment,idBooking));

        return dataCard;
    }


    public CreditCardDTO decryptCreditCard(CreditCardDTO creditCardDTO,String username){

        UserIntranetService userIntranetService=new UserIntranetService();
        boolean hasPermission=userIntranetService.hasPermission(Constants.PERMISSION_DECRYPT_CREDIT_CARD,username);
        try {
        String nCard = Utilerias.UtilsPagos.Desencriptar(Constants.KEY_ENCRYPT_DECRYPT, creditCardDTO.getnCard());
        String exp = Utilerias.UtilsPagos.Desencriptar(Constants.KEY_ENCRYPT_DECRYPT, creditCardDTO.getExp());
        String cvv = "XXX";


        if(hasPermission){
            cvv=Utilerias.UtilsPagos.Desencriptar(Constants.KEY_ENCRYPT_DECRYPT, creditCardDTO.getCvv());

                creditCardDTO=new CreditCardDTO(creditCardDTO.getName(), creditCardDTO.getLastName(),
                                                nCard,cvv,
                                                exp,creditCardDTO.getTypeCard());
        }else{
            nCard="XXXX-XXXX-XXXX-"+nCard.substring(nCard.length()-4,nCard.length());

            creditCardDTO=new CreditCardDTO(creditCardDTO.getName(), creditCardDTO.getLastName(),
                                            nCard,cvv,exp,creditCardDTO.getTypeCard());
        }
        }catch (Exception e){

        }

        return creditCardDTO;

    }
    public CreditCardDTO encryptCreditCard(CreditCardDTO creditCardDTO){

       
        try {
        String nCard = Utilerias.UtilsPagos.Encriptar(Constants.KEY_ENCRYPT_DECRYPT, creditCardDTO.getnCard());
        String exp = Utilerias.UtilsPagos.Encriptar(Constants.KEY_ENCRYPT_DECRYPT, creditCardDTO.getExp());
        String cvv = "XXX";


       
            cvv=Utilerias.UtilsPagos.Encriptar(Constants.KEY_ENCRYPT_DECRYPT, creditCardDTO.getCvv());

                creditCardDTO=new CreditCardDTO(creditCardDTO.getName(), creditCardDTO.getLastName(),
                                                nCard,cvv,
                                                exp,creditCardDTO.getTypeCard());
       
        }catch (Exception e){

        }

        return creditCardDTO;

    }

    public boolean deletePayment(long idBooking,long idPago){

           Optional<Payment> paymentOptional=paymentDAO.find(idBooking, idPago);

        if(paymentOptional.isPresent() && paymentOptional.get().getAutorization()!=null && !paymentOptional.get().getAutorization().isEmpty()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("payment", "Imposible delete payment, it has an authorization number")).build());
        }
        if(paymentOptional.isPresent()) {
            paymentDAO.delete(paymentOptional.get());
            dataCardDAO.delete(idBooking,idPago);
            paymentServiceDAO.delete(idBooking,idPago);
        }

            return true;
    }

    public List<PaymentServiceResponseDTO>  getProvidedServices(long idBooking){
        return paymentDAO.getProvidedServices(idBooking);
    }

    public List<com.m4c.model.entity.PaymentService> getPaymentsByIdContractService(long idBooking,ContractService contractService){
       return paymentServiceDAO.findPaymentsByIdContractService( idBooking, contractService);
    }
}
