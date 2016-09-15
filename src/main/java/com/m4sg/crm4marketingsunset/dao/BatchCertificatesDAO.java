package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.BatchCertificates;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.util.Constants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * Created by jhon on 16/06/15.
 */
public class BatchCertificatesDAO extends GenericDAO<BatchCertificates>  {

    public BatchCertificatesDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public PaginationDTO<BatchCertificates> findAll(String orderBy,String dir,int sizePage,int page) {

        StringBuilder orderBuilder=new StringBuilder();

        orderBuilder.append("select cl from BatchCertificates cl  ");



        /*if (dir.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()) {
            criteria.addOrder(Order.asc(orderBy));
        } else if (dir.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()) {
            criteria.addOrder(Order.desc(orderBy));
        }*/

        if(dir.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()){

            orderBuilder.append(" order by " + orderBy + " " + Constants.ORDER_ASC);
        }
        else if(dir.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            orderBuilder.append(" order by " + orderBy + " " + Constants.ORDER_DESC);

        }else{

            orderBuilder.append(" order by id desc ");
        }

        Query query=currentSession().createQuery(orderBuilder.toString());
        return pagination(query, sizePage, page);
    }

    @Override
    public List<BatchCertificates> findAll() {
        return null;
    }

    @Override
    public Optional<BatchCertificates> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public BatchCertificates saveOrUpdate(BatchCertificates entity) {

        this.currentSession().saveOrUpdate(entity);
        return entity;
    }

    public void flushSession(){
        currentSession().flush();
    }
}
