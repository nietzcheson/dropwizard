package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.CallCenter;
import com.m4c.model.entity.Sale;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;

import java.util.List;

/**
 * Created by Fernando on 01/04/2015.
 */
public class SaleDAO extends GenericDAO<Sale> {
    @Override
    public List<Sale> findAll() {
        return null;
    }

    @Override
    public Optional<Sale> find(long id) {
        return Optional.fromNullable((Sale)criteria().add(Restrictions.eq("id", id)).uniqueResult());
       // return null;
    }

    @Override
    public Sale saveOrUpdate(Sale entity) {

            currentSession().saveOrUpdate(entity);


        return entity;
    }

    public Optional<Sale> find(String numCert){
        return Optional.fromNullable((Sale)criteria().add(Restrictions.eq("certificateNumber", numCert)).uniqueResult());
    }
    public SaleDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public Long getNewBooking(CallCenter callCenter){

        StringBuilder query=new StringBuilder();

        query.append("select nvl(MAX(idbooking)+1,:FOLIO_CALLCENTER) as booking ");
        query.append("from M4CVENTA ve ");
        query.append("inner join ");
        query.append("M4CLIENTE cl ");
        query.append("on ve.idcliente = cl.idcliente and cl.idcallcenter = :CALLCENTER_ID ");

        SQLQuery sqlQuery=  currentSession().createSQLQuery(query.toString());

        sqlQuery.setParameter("FOLIO_CALLCENTER", callCenter.getFolio());
        sqlQuery.setParameter("CALLCENTER_ID",callCenter.getId());

        return (Long)sqlQuery.addScalar("booking", LongType.INSTANCE).uniqueResult();

    }
    public void flush(){
        this.currentSession().flush();
    }

    public boolean existsNumcert(String numcert){

        StringBuilder query=new StringBuilder();

        query.append("select count(*)from M4CVENTA ");
        query.append("where REGEXP_SUBSTR(numcert,'([a-z]*[A-Z]*[0-9]*)+')=:NUMCERT and rownum=1 ");


        return ((Number)(currentSession().createSQLQuery(query.toString()).setString("NUMCERT", numcert)).uniqueResult()).longValue()>0?true:false;


    }
}
