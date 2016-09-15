package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.ReservationGroup;
import com.m4sg.crm4marketingsunset.dao.ReservationGroupDAO;

import java.util.List;

/**
 * Created by Jhon on 30/01/2015.
 */
public class ReservationGroupService extends GenericDAOService {

    public ReservationGroupService() {
        super( ReservationGroupDAO.class);
    }

    public Optional<ReservationGroup> findById(long id){
        return reservationGroupDAO.find(id);
    }
    public List<ReservationGroup> findAll(){
        return reservationGroupDAO.findAll();
    }
}
