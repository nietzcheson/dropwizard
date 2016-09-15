package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.ReservationStatus;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Usuario on 17/08/2015.
 */
public class ReservationStatusDAO  extends GenericDAO<ReservationStatus> {
    public ReservationStatusDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<ReservationStatus> findAll() {
        return null;
    }

    @Override
    public Optional<ReservationStatus> find(long id) {
        return Optional.fromNullable((ReservationStatus)criteria().add(Restrictions.eq("id",id)).uniqueResult());
    }

    @Override
    public ReservationStatus saveOrUpdate(ReservationStatus entity) {
        return null;
    }

    public List<CommonDTO> findAllByCallcenter(List<Long>typeLst) {
        List<CommonDTO>campaigns;
        if(!typeLst.isEmpty()){
            Query query=currentSession().createQuery("select new com.m4sg.crm4marketingsunset.core.dto.CommonDTO(c.id,c.status) from ReservationStatus c where c.tipo in (:callCenters) and c.isActive=true "+" order by status");
            query.setParameterList("callCenters",typeLst);

            campaigns=query.list();
        }else{
            Query query=currentSession().createQuery("select new com.m4sg.crm4marketingsunset.core.dto.CommonDTO(c.id,c.status) from ReservationStatus c where c.isActive=true order by status");

            campaigns=query.list();
        }
        return campaigns;

    }
}
