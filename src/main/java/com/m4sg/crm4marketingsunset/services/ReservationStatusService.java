package com.m4sg.crm4marketingsunset.services;

import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import com.m4sg.crm4marketingsunset.dao.ReservationStatusDAO;

import java.util.List;

/**
 * Created by Usuario on 17/08/2015.
 */
public class ReservationStatusService extends GenericDAOService {

    public ReservationStatusService() {
        super(ReservationStatusDAO.class);
    }

    public List<CommonDTO> list(List<Long>typeLst){
        return reservationStatusDAO.findAllByCallcenter(typeLst);
    }
}
