package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Department;
import com.m4sg.crm4marketingsunset.core.dto.MoodDTO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Usuario on 11/09/2015.
 */
public class DepartmentDAO extends GenericDAO<Department> {


    public DepartmentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Department> findAll() {
        return null;
    }

    @Override
    public Optional<Department> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public Department saveOrUpdate(Department entity) {
        return null;
    }

    public List<MoodDTO> findAllByCallcenter() {
        List<MoodDTO>campaigns;
            Query query=currentSession().createQuery("select new com.m4sg.crm4marketingsunset.core.dto.MoodDTO(c.id,c.department,c.clave) from Department c where c.clave in ('R','RI','RE','CI','CE','RYH','RR','OE','VE','CC','CCH','CCO','AF','RCR') "+" order by department");

            campaigns=query.list();


        return campaigns;

    }
}
