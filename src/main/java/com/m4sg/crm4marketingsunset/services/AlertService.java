package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Alert;
import com.m4c.model.entity.Customer;
import com.m4c.model.entity.User;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.CustomerNoteDTO;
import com.m4sg.crm4marketingsunset.dao.AlertDAO;
import com.m4sg.crm4marketingsunset.dao.UserDAO;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Created by desarrollo1 on 17/09/2015.
 */
public class AlertService extends GenericDAOService {
    public AlertService() {
        super(AlertDAO.class, UserDAO.class);
    }

    public Alert createAlert(CustomerNoteDTO customerNoteDTO, Customer customer) {
        Alert alert= new Alert();
        Long idalert= alertDAO.getNewId();
        alert.setId(idalert);
        if (customer != null) {
            alert.setCustomer(customer);
        }
        if (customerNoteDTO.getDescription() != null && !customerNoteDTO.getDescription().isEmpty()) {
            alert.setComments(customerNoteDTO.getDescription());
        }
        if (customerNoteDTO.getAudit().getUsername()!= null && !customerNoteDTO.getAudit().getUsername().isEmpty()) {
            alert.setSourceUser(customerNoteDTO.getAudit().getUsername());
        }
        if(customerNoteDTO.getContactDate() !=null){
            //Crear Alerta
            alert.setDate(customerNoteDTO.getContactDate());
        }
        if(customerNoteDTO.getRemindTOusername()!=null ){
            alert.setUser(customerNoteDTO.getRemindTOusername());
        }
        alert.setCreatedDate(new Date());
        alert.setType("NOTIFICACION");
        alert.setStatus("ABIERTA");
        return alertDAO.saveOrUpdate(alert);
    }

    public List<Alert> findAlertByUser(String user) {
        Optional<User> optionalUser =userDAO.findUser(user.toUpperCase());
        List<Alert>paginationDTO;
        if(optionalUser.isPresent()){
            String access = optionalUser.get().getAccess() != null
                    ? optionalUser.get().getAccess().trim().toUpperCase() : "";
            if(access.equals("S")){
                String group = optionalUser.get().getGroup().trim();
                paginationDTO= alertDAO.findbyGroup(group);
            }else{
                paginationDTO= alertDAO.findbyUsername(user.toUpperCase());
            }
        }else{
            //Regresar error
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("user", "not exist")).build());
        }

        return paginationDTO;
    }
}
