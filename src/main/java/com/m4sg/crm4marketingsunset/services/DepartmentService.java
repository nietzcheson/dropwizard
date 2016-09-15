package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Department;
import com.m4sg.crm4marketingsunset.core.dto.MoodDTO;
import com.m4sg.crm4marketingsunset.dao.DepartmentDAO;

import java.util.List;

/**
 * Created by Usuario on 11/09/2015.
 */
public class DepartmentService extends GenericDAOService {

    public DepartmentService() {
        super(DepartmentDAO.class);
    }

    public List<MoodDTO> list(){

        return departmentDAO.findAllByCallcenter();
    }

    public Optional<Department> findById(long id){
        return departmentDAO.find(id);
    }
}
