package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.ContractService;
import com.m4c.model.entity.PaymentService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Usuario on 23/07/2015.
 */
public class PaymentServiceDAO extends GenericDAO<PaymentService> {
    public PaymentServiceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<PaymentService> findAll() {
        return null;
    }

    @Override
    public Optional<PaymentService> find(long id) {
        return null;
    }


    public List<PaymentService> find(long idBooking,long idPago) {

        return criteria().add(Restrictions.eq("idBooking",idBooking)).add(Restrictions.eq("payment",idPago)).list();
    }
    public List<PaymentService> findPaymentsByIdContractService(long idBooking,ContractService contractService) {

        //return criteria().add(Restrictions.eq("idBooking",idBooking)).add(Restrictions.eq("contractService",contractService)).list();

        StringBuilder hql=new StringBuilder();

        hql.append("select cl from PaymentService cl where  ");
        hql.append("cl.idBooking = :idBooking and ");

        hql.append("cl.contractService = :idService  ");

        Query query=currentSession().createQuery(hql.toString());


        query.setParameter("idBooking",idBooking);

        query.setParameter("idService",contractService);

        return (List<PaymentService>) query.list();
    }

    public PaymentService findById(long idBooking,long idPago,ContractService contractService) {

//        return (PaymentService) criteria().add(Restrictions.eq("idBooking",idBooking)).add(Restrictions.eq("payment",idPago)).setMaxResults(1).uniqueResult();
        StringBuilder hql=new StringBuilder();

        hql.append("select cl from PaymentService cl where  ");
        hql.append("cl.idBooking = :idBooking and ");
        hql.append("cl.payment = :payment  and ");
        hql.append("cl.contractService = :idService  ");

        Query query=currentSession().createQuery(hql.toString());


        query.setParameter("idBooking",idBooking);
        query.setParameter("payment",idPago);
        query.setParameter("idService",contractService);

        return (PaymentService) query.setMaxResults(1).uniqueResult();
    }


    @Override
    public PaymentService saveOrUpdate(PaymentService entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }

    public Long findMax(){
        Long max= (Long)criteria().setProjection(Projections.max("id")).uniqueResult();
        return max+1;
    }

    public void delete(long idBooking,long idPago) {

        StringBuilder hql=new StringBuilder();

        hql.append("delete PaymentService where idBooking=:IDBOOKING and payment=:IDPAGO ");
        Query q=currentSession().createQuery(hql.toString());

        q.setParameter("IDBOOKING",idBooking);
        q.setParameter("IDPAGO",idPago);

        q.executeUpdate();

    }
    public void deleteByService(long idBooking,long idPago,ContractService contractService) {

        StringBuilder hql=new StringBuilder();

        hql.append("delete PaymentService where idBooking=:IDBOOKING and payment=:IDPAGO and contractService=:IDSERVICIO ");
        Query q=currentSession().createQuery(hql.toString());

        q.setParameter("IDBOOKING",idBooking);
        q.setParameter("IDPAGO",idPago);
        q.setParameter("IDSERVICIO",contractService);

        q.executeUpdate();

    }
}
