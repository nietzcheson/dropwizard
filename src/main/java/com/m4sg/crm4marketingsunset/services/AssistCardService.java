package com.m4sg.crm4marketingsunset.services;

import assistcard.ws._36._208._104._190.ws.services.AssistCardService.AssistCardServiceServiceLocator;
import assistcard.ws._36._208._104._190.ws.services.AssistCardService.AssistCardServiceSoapBindingStub;
import assistcard.ws.beans.Emision;
import assistcard.ws.beans.InformacionEmisionType;
import assistcard.ws.beans.Vouchers;
import com.google.common.base.Optional;
import com.javeros.anonimos.code.Rfc;
import com.javeros.anonimos.code.dto.PersonaRfcDto;
import com.m4c.model.entity.*;
import com.m4c.model.entity.sistemas.UserIntranet;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.dao.*;
import com.m4sg.crm4marketingsunset.util.FileIO;
import com.m4sg.crm4marketingsunset.util.StringTools;
import org.apache.axis.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

/**
 * Created by Fernando on 06/10/2015.
 */
public class AssistCardService  extends GenericDAOService {


    public AssistCardService() {
        super(AssistCardDAO.class, ReservationDAO.class, PaxDAO.class, ContractServiceDAO.class, UserIntranetDAO.class);
    }

    public boolean emitir(Long idReservation,Long idProvidedService,String lang){

        Optional<Reservation> reservationOptional=reservationDAO.find(idReservation);
        ContractService contractService=contractServiceDAO.findByIdReservationAndIdContract(idReservation, idProvidedService);
        boolean isOK=false;
        Optional<AssistCard>assistCardOptional;
        //List<String>polizas=new ArrayList<String>();

        if(!reservationOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("idReservacion", "Reservation does not exists.")).build());
        }
        if(contractService== null) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("name", "Provided service does not exists.")).build());
        }
        assistCardOptional=assistCardDAO.findBySubservice(contractService.getSubService().getId());

        if(!assistCardOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("assistcard", "Reservation does not exists.")).build());
        }
//        List<Pax> paxList=paxDAO.findByReservation(reservationOptional.get());
        List<Pax> paxList= new ArrayList<Pax>( new HashSet<Pax>(reservationOptional.get().getPaxes()));


        Optional<UserIntranet> agentOptional=userIntranetDAO.find(contractService.getUser());
        int numPolizas=contractService.getPax();

        HashMap<String,List> divisionPax=divisionPaxes(paxList);

        List<List<Pax>>allocation=allocationInsurange(
                divisionPax.get("minor"),
                divisionPax.get("adult"),
                divisionPax.get("nn"),numPolizas,new ArrayList<List<Pax>>()
        );

        for(List<Pax> alloc:allocation){

            isOK=sendToAssistCard(assistCardOptional.get(),
                    reservationOptional.get().getHotel().getDestination().getCountry().getCode(),
                    alloc,
                    reservationOptional.get(),agentOptional.get(),lang
            );

           /* if(voucherCode.isEmpty()){
                hasError=true;
            }else {
                polizas.add(voucherCode);
            }*/

        }

        Reservation reservation=reservationOptional.get();
        reservationDAO.saveOrUpdate(reservation);

        /*if(reservation.getPolizaAssistCard()!=null) {
            String []insuranceArray=reservation.getPolizaAssistCard().split(",");
            if(insuranceArray!=null && insuranceArray.length>0){
                polizas.addAll(Arrays.asList(insuranceArray));
            }
        }

        if(!polizas.isEmpty()) {
            //reservation.setPolizaAssistCard(StringUtils.join(polizas, ","));
            reservationDAO.saveOrUpdate(reservation);
        }*/


        return isOK;
    }

    public List<List<Pax>> allocationInsurange(List<Pax>paxMinorLst,List<Pax>paxAdultLst,List<Pax>nnLst,
                                               int insuranges,List<List<Pax>> allocation){

        int paxesxInsurange=(int)Math.ceil(new Double(paxMinorLst.size()+paxAdultLst.size()+nnLst.size())/insuranges);

        List<Pax> auxAllocationLst=new ArrayList<Pax>();
        List<Pax> auxMinorLst=new ArrayList<Pax>();
        List<Pax> auxAdultLst=new ArrayList<Pax>();
        List<Pax> auxnnLst;

        if(!paxMinorLst.isEmpty() ){

            int responsable=0;
            if(!paxAdultLst.isEmpty() || !nnLst.isEmpty()){
                responsable++;
            }

            auxMinorLst=new ArrayList<Pax>(paxMinorLst.subList(0, (paxesxInsurange - responsable) > paxMinorLst.size()?paxMinorLst.size():(paxesxInsurange - responsable)));
            paxMinorLst.removeAll(auxMinorLst);
            auxAllocationLst.addAll(auxMinorLst);
        }

        if(!paxAdultLst.isEmpty() && auxAllocationLst.size() < paxesxInsurange){

            auxAdultLst=new ArrayList<Pax>(paxAdultLst.subList(0,(paxesxInsurange-auxAllocationLst.size()) > paxAdultLst.size()?paxAdultLst.size():(paxesxInsurange-auxAllocationLst.size())));
            paxAdultLst.removeAll(auxAdultLst);
            auxAllocationLst.addAll(auxAdultLst);

        }

        if( !nnLst.isEmpty() && auxAllocationLst.size() < paxesxInsurange){

            auxnnLst=new ArrayList<Pax>(nnLst.subList(0,paxesxInsurange-auxAllocationLst.size()));
            nnLst.removeAll(auxnnLst);

            for(Pax p:auxnnLst){

                if(auxAdultLst.size()>0 &&auxAdultLst.size() <= 2){//Que sean menores

                    p.setBirthDate(new DateTime().minusYears(12).toDate());

                    auxMinorLst.add(p);
                }else {//Que sean adultos

                    p.setBirthDate(new DateTime().minusYears(30).toDate());
                    auxAdultLst.add(p);

                }
                auxAllocationLst.add(p);
            }
        }


Collections.sort(auxAllocationLst, new Comparator<Pax>() {
    @Override
    public int compare(Pax o1, Pax o2) {
        return o1.getRelation().compareToIgnoreCase(o2.getRelation());
    }
});
        allocation.add(auxAllocationLst);
        insuranges--;
        if(insuranges>0){
            allocationInsurange(paxMinorLst,paxAdultLst,nnLst,insuranges,allocation);
        }

        return allocation;
    }

    public HashMap<String,List> divisionPaxes(List<Pax>paxes){


        List<Pax>paxMinorLst=new ArrayList<Pax>();
        List<Pax>paxAdultLst=new ArrayList<Pax>();
        List<Pax>nnLst=new ArrayList<Pax>();
        HashMap paxesMap=new HashMap<String,List<Pax>>();

        for(Pax pax:paxes){

            if(pax.getBirthDate()!=null){

                Years age=Years.yearsBetween(new LocalDate(pax.getBirthDate()),new LocalDate(new DateTime()));

                if(age.getYears()>18){// Es Adulto

                    paxAdultLst.add(pax);

                }else{//Es Menor

                    paxMinorLst.add(pax);
                }
            }else{//Inventar fecha
                nnLst.add(pax);
            }
        }

        paxesMap.put("adult",paxAdultLst);
        paxesMap.put("minor",paxMinorLst);
        paxesMap.put("nn",nnLst);

        return paxesMap;
    }




    public boolean sendToAssistCard(AssistCard assistCard,
                                   String countryCodeDestination,
                                   List<Pax>paxes,
                                   Reservation reservation,UserIntranet agent,String lang){

        try {

            String endPoint= FileIO.readProperties().getProperty("endpoint-ws-assistcard");
            String sucursal=FileIO.readProperties().getProperty(lang.equalsIgnoreCase("es")?"sucursal-es":"sucursal-en");
            String agencyProperty="agencyCode-assistcard";
            String agencyCode= FileIO.readProperties().getProperty(agencyProperty);
            String countryCode= FileIO.readProperties().getProperty("countryCode-assistcard");
            String userAssistCard= FileIO.readProperties().getProperty("ws-user-assistcard");
            String passAssistCard= FileIO.readProperties().getProperty("ws-password-assistcard");

            URL url=new URL(endPoint);
            AssistCardServiceServiceLocator locator=new AssistCardServiceServiceLocator();
            AssistCardServiceSoapBindingStub stub;
            DateTime dateStart=new DateTime();
            Date dateEnd;
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            stub = new AssistCardServiceSoapBindingStub(url,locator );


            Emision emision=new Emision();
            emision.setPais(Integer.parseInt(countryCode));
            emision.setCodigoAgencia(agencyCode);
            emision.setNumeroSucursal(Integer.parseInt(sucursal));
            emision.setCodigoCounter("");


            emision.setCodigoProducto(assistCard.getProductCode());
            emision.setCodigoTarifa(assistCard.getRateCode());
            emision.setCantidadDias(assistCard.getEffectiveDays());

            if(assistCard.getDateType().equalsIgnoreCase("REGISTRO")){

                emision.setFechaInicioVigencia(simpleDateFormat.format(dateStart.toDate()));
            }else if(assistCard.getDateType().equalsIgnoreCase("CHECKIN")){

                dateStart=new DateTime(reservation.getInitialDate());

                emision.setFechaInicioVigencia(simpleDateFormat.format(dateStart.toDate()));
            }



            dateEnd=new DateTime(dateStart).plusDays(assistCard.getEffectiveDays()).toDate();

            dateEnd=new DateTime(dateEnd).minusDays(1).toDate();
            /*try {
                if(dateStart.year().isLeap() && dateStart.toDate().before(simpleDateFormat.parse("29/02/"+dateStart.year().getAsString()))) {
                    dateEnd=new DateTime(dateEnd).minusDays(1).toDate();
                }else{
                    System.out.println("Quita 1");

                }
            } catch (ParseException e) {

                e.printStackTrace();
            }*/

            emision.setFechaFinVigencia(simpleDateFormat.format(dateEnd));

            emision.setPlanFamiliar(true);

            emision.setAreaDestino(countryCodeDestination.equalsIgnoreCase("USA")?"01":"03");
            emision.setPagoEfectivo(true);
            emision.setEnviaEvoucher("true");

            Emision.Pasajeros pasajeros=new Emision.Pasajeros();

            for(Pax pax:paxes) {


                Emision.Pasajeros.Pasajero pasajero = new Emision.Pasajeros.Pasajero();

                pasajero.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").format(pax.getBirthDate()));
                pasajero.setApellido(StringTools.replaceEspecialCharacterByHyphen(pax.getLastName()));
                //pasajero.setApellido(pax.getLastName());
                //pasajero.setNombre(pax.getFirstName());
                pasajero.setNombre(StringTools.replaceEspecialCharacterByHyphen(pax.getFirstName()));
                pasajero.setTipoDocumento(1);


                String aPaterno="";
                String aMaterno="";
                if(pax.getLastName()!=null) {
                    String[] combineLastName = pax.getLastName().split(" ");
                    if(combineLastName.length>1) {
                        aMaterno=combineLastName[1];
                    }
                    aPaterno=combineLastName[0];
                }

                pasajero.setNumeroDocumento(StringTools.generateRFC(pax.getFirstName(),aPaterno,aMaterno,pax.getBirthDate()));

                if(paxes.indexOf(pax)==0) {//Marcar como principal

                    pasajero.setEmail(StringTools.extractFirstEmail(reservation.getCustomer().getEmail()));
                    pasajero.setTelefono(reservation.getCustomer().getPhone1());
                    pasajero.setDomicilio(reservation.getCustomer().getAddress());
                    pasajero.setCodigoPostal(reservation.getCustomer().getPostalCode());
                    pasajero.setCiudad(reservation.getCustomer().getCity());


                }


                pasajero.setPais(520);
                pasajero.setContacto(agent.getName());
                pasajero.setUpgrades("");
                pasajeros.getPasajero().add(pasajero);
            }
            emision.setPasajeros(pasajeros);
            emision.setCodigoUsuario(userAssistCard);
            emision.setTipoUsuario((byte) 2);
            emision.setTipoCarga("X");

            try {
                java.io.StringWriter sw = new StringWriter();
                JAXBContext context= JAXBContext.newInstance(Emision.class);

                JAXBContext jaxbContext = JAXBContext.newInstance(assistcard.ws.beans.Response.class);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();


                Marshaller jaxbMarshaller = context.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
                jaxbMarshaller.marshal(emision,sw);
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<< Emision AssistCard >>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println("RQ:: "+sw.toString());
                String response=stub.emitir(sw.toString(),userAssistCard,passAssistCard);
                System.out.println("RS::"+response);
                assistcard.ws.beans.Response voucherRS=(assistcard.ws.beans.Response)jaxbUnmarshaller.unmarshal(new StringReader(response));

                /*System.out.println("CodigoCompletoVoucher==>"+
                        vaucher.getVouchers().getInformacionEmision().getCodigoCompletoVoucher());*/
                for(InformacionEmisionType voucher:voucherRS.getVouchers().getInformacionEmision()){

                    Pax pax= select(paxes, having(on(Pax.class).getFirstNameNoEspecialCharacter(),
                                         Matchers.equalToIgnoringCase(voucher.getCliente().getNombre()))
                                    .and(having(on(Pax.class).getLastNameNoEspecialCharacter(),
                                            Matchers.equalToIgnoringCase(voucher.getCliente().getApellido())))).get(0);

                    if(pax.getPolizas()!=null && !pax.getPolizas().isEmpty()){

                        List<String> aux=new ArrayList<String>(Arrays.asList(pax.getPolizas().split(",")));

                        aux.add(voucher.getCodigoCompletoVoucher());

                        pax.setPolizas(StringUtils.join(aux, ","));

                    }else {

                        pax.setPolizas(voucher.getCodigoCompletoVoucher());
                    }

                }


                return true;
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return false;

    }

}
