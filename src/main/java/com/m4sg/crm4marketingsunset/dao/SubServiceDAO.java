package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.SubService;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 06/04/2015.
 */
public class SubServiceDAO extends GenericDAO<SubService> {
    public SubServiceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<SubService> findAll() {
        return null;
    }

    @Override
    public Optional<SubService> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public Optional<SubService> findByCampaign(long CampaingId) {
       return Optional.fromNullable((SubService)criteria().add(Restrictions.eq("idCampaign", CampaingId)).setMaxResults(1).uniqueResult());
    }


    public List<SubService> find(boolean isCertificateArea,List<Long> callCenterLst) {

        List<SubService> subServiceLst;
        if(isCertificateArea){
            List<Long> idServiceLst=new ArrayList();
            if(callCenterLst==null || callCenterLst.isEmpty()){
                callCenterLst= new ArrayList<Long>();
            }
            callCenterLst.add(7l);

            idServiceLst.add(1l);
            idServiceLst.add(2l);
            idServiceLst.add(6l);
            idServiceLst.add(7l);
            idServiceLst.add(9l);
            idServiceLst.add(10l);
            idServiceLst.add(29l);
            idServiceLst.add(30l);
            idServiceLst.add(31l);
            idServiceLst.add(36l);
            idServiceLst.add(39l);

            subServiceLst=criteria().add(Restrictions.in("service.id", idServiceLst)).add(Restrictions.in("callCenter.id",callCenterLst)).addOrder(Order.asc("name")).list();

        }else if(callCenterLst!=null && !callCenterLst.isEmpty()){
            subServiceLst=criteria().add(Restrictions.in("callCenter.id", callCenterLst)).addOrder(Order.asc("name")).list();
        }else{
            Query query=currentSession().createQuery("select ss from SubService ss order by ss.name ");
            subServiceLst=query.list();

        }

        return subServiceLst;
    }

    @Override
    public SubService saveOrUpdate(SubService entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }


}
