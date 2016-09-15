package com.m4sg.crm4marketingsunset.services;

import com.m4c.model.entity.*;
import com.m4sg.crm4marketingsunset.core.dto.AuditDTO;
import com.m4sg.crm4marketingsunset.core.dto.CustomerDTO;
import com.m4sg.crm4marketingsunset.core.dto.CustomerNoteDTO;
import com.m4sg.crm4marketingsunset.core.dto.CustomerNoteUserDTO;
import com.m4sg.crm4marketingsunset.dao.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by desarrollo1 on 07/05/2015.
 */
public class CustomerNoteService  extends GenericDAOService{

    public CustomerNoteService() {
        super(CustomerNoteDAO.class, CustomerDAO.class, CustomerNotePkDAO.class, StatusDAO.class, NoteTypeDAO.class, AlertDAO.class);
    }

    public CustomerNoteDTO createCustomerNote(CustomerNoteDTO customerNoteDTO,long idClient) {
        CustomerNote customerNote = setParams(customerNoteDTO, new CustomerNote(),idClient);
        createCustomerNote(customerNote);
        customerNoteDTO=setCustomer(customerNote);
        return customerNoteDTO;
    }
    public void  createCustomerNotefromSale(Customer customer,CustomerDTO customerDTO){
        CustomerNoteDTO customerNoteDTO=new CustomerNoteDTO();
        customerNoteDTO.setUsername(customerDTO.getUserName());

        customerNoteDTO.setIdCliente(customer.getId());


            customerNoteDTO.setMood("1");
            //customerNoteDTO.setStatus("1");


        customerNoteDTO.setType("1");
        customerNoteDTO.setDate(new Date());
        customerNoteDTO.setDescription(customerDTO.getSale().getComment());

        AuditDTO auditDTO=new AuditDTO();

        CustomerNote customerNote = setParams(customerNoteDTO, new CustomerNote(),customer.getId());
        customerNote.setUser(customerDTO.getUserName());
        createCustomerNote(customerNote);
        customerNoteDTO = setCustomer(customerNote);
        //return customerNoteDTO;
    }

    public CustomerNote setParams(CustomerNoteDTO customerNoteDTO, CustomerNote customerNote,long idCliente) {
        Customer customer=idCliente > 0 ? customerDAO.find(idCliente).get() : null;
        CustomerNotePk customerNotePk= new CustomerNotePk();
        NoteType noteType = Long.valueOf(customerNoteDTO.getType()) > 0 ? noteTypeDAO.find(Long.valueOf(customerNoteDTO.getType())).get() : null;
        Status status = Long.valueOf(customerNoteDTO.getMood())>0 ?  statusDAO.find(Long.valueOf(customerNoteDTO.getMood())).get():null;
        if (customer != null) {
            customerNote.setCustomer(customer);
        }
        if(noteType !=null){
            customerNote.setNoteType(noteType);
        }
        if(status !=null){
            customerNote.setStatus(status);
        }
        if (customerNoteDTO.getDescription() != null) {
            customerNote.setNote(customerNoteDTO.getDescription());
        }
        if(customerNoteDTO.getAudit()!=null){
            if (customerNoteDTO.getAudit().getUsername()!= null && !customerNoteDTO.getAudit().getUsername().isEmpty()) {
                customerNote.setUser(customerNoteDTO.getAudit().getUsername());
            }
        }
        if (customerNoteDTO.getSpecialReq() != null && !customerNoteDTO.getSpecialReq().isEmpty()) {
            customerNote.setSpecialReq(customerNoteDTO.getSpecialReq());
        }
        if(customerNoteDTO.getContactDate() !=null && customerNoteDTO.getRemindTOusername()!=null ){
            new AlertService().createAlert(customerNoteDTO, customer);
            customerNote.setContactDate(customerNoteDTO.getContactDate());
        }
        customerNote.setNumber(customerNoteDAO.find(customer));
        customerNotePk.setNoteId(customerNoteDAO.find(customer));
        customerNotePk.setCustomerId(customer.getId());
        customerNote.setPk(customerNotePk);
        customerNote.setDate(new Date());
        return customerNote;
    }

    public CustomerNote createCustomerNote(CustomerNote customerNote) {
        return customerNoteDAO.saveOrUpdate(customerNote);
    }

    public CustomerNoteDTO find(long idCustomer,int id){
        CustomerNote customerNote = customerNoteDAO.findNoteById(idCustomer,id);
        CustomerNoteDTO customerNoteDTO=setCustomer(customerNote);

        return customerNoteDTO;
    }

    public List<CustomerNoteDTO> findAll(Customer customer){
        List<CustomerNote> customerNoteList=customerNoteDAO.findNotes(customer);
        List<CustomerNoteDTO> customerNoteDTOList= new ArrayList<CustomerNoteDTO>();
        for (int i = 0; i < customerNoteList.size(); i++) {
            CustomerNote customerNote =customerNoteList.get(i);
            CustomerNoteDTO customerNoteDTO= setCustomer(customerNote);
            customerNoteDTOList.add(customerNoteDTO);
        }

        return customerNoteDTOList;
    }

    public CustomerNoteDTO setCustomer(CustomerNote customerNote){
        CustomerNoteDTO customerNoteDTO=new CustomerNoteDTO();
        customerNoteDTO.setUsername(customerNote.getUser());
        customerNoteDTO.setId(Long.valueOf(customerNote.getPk().getNoteId()));
        customerNoteDTO.setIdCliente(Long.valueOf(customerNote.getPk().getCustomerId()));
        customerNoteDTO.setContactDate(customerNote.getContactDate());
        customerNoteDTO.setSpecialReq(customerNote.getSpecialReq());
        if(customerNote.getStatus()!=null){
            customerNoteDTO.setMood(customerNote.getStatus().getStatus());
            customerNoteDTO.setStatus(customerNote.getStatus().getKeyword());
        }

        customerNoteDTO.setType(customerNote.getNoteType().getType());
        customerNoteDTO.setDate(customerNote.getDate());
        customerNoteDTO.setDescription(customerNote.getNote());
        return customerNoteDTO;
    }

    public List<CustomerNoteUserDTO> findCustomerNotebyUser(String user){
        List<CustomerNoteUserDTO> customerNoteList = customerNoteDAO.findNotesbyUser(user);
        return customerNoteList;
    }
}
