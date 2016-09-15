package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Alert;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import java.util.Calendar;
import java.util.List;

/**
 * Created by desarrollo1 on 17/09/2015.
 */
public class AlertDAO extends GenericDAO<Alert>{
    public AlertDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public Long getNewId(){
        StringBuilder query=new StringBuilder();
        query.append("select MAX(idalerta)+1 as id ");
        query.append("from M4CALERTAS");
        SQLQuery sqlQuery=  currentSession().createSQLQuery(query.toString());

//        sqlQuery.setParameter("FOLIO_CALLCENTER", callCenter.getFolio());
//        sqlQuery.setParameter("CALLCENTER_ID",callCenter.getId());

        return (Long)sqlQuery.addScalar("id", LongType.INSTANCE).uniqueResult();

    }

    @Override
    public List<Alert> findAll() {
        Criteria criteria=this.currentSession().createCriteria(Alert.class);
        criteria.add(Restrictions.eq("active", true));
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    public List<Alert> findbyUsername(String user) {
        Criteria criteria=findActives();
        criteria.add(Restrictions.eq("user", user).ignoreCase());
        return criteria.list();
    }
    public List<Alert> findbyGroup(String group) {
        Criteria criteria=findActives();
        criteria.add(Restrictions.eq("group", group).ignoreCase());
        return criteria.list();
    }

    public Criteria findActives() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, +2);
        System.out.println(cal.getTime());
        Criteria criteria=this.currentSession().createCriteria(Alert.class);
        String[]status= new String[] { "ABIERTA", "ENPROCESO" };
        criteria.add(Restrictions.in("status", status));
        criteria.add(Restrictions.le("date", cal.getTime()));
        criteria.addOrder(Order.asc("date"));
        return criteria;
    }


    @Override
    public Optional<Alert> find(long id) {
        return Optional.fromNullable(get(id));
    }
    @Override
    public Alert saveOrUpdate(Alert entity) {
        this.currentSession().saveOrUpdate(entity);
        this.currentSession().persist(entity);
        return entity;
    }
}
