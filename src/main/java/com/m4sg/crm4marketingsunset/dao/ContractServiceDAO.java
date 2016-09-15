package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.ContractService;
import com.m4c.model.entity.ContractServicePk;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Fernando on 06/04/2015.
 */
public class ContractServiceDAO extends GenericDAO<ContractService>{
    public ContractServiceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<ContractService> findAll() {
        return null;
    }

    @Override
    public Optional<ContractService> find(long id) {
        return null;
    }


    public Long findMaxByIdBooking(long idBooking){


        /*Criteria criteria=currentSession().createCriteria(ContractService.class);
        criteria.add(Restrictions.eq("pk.idBooking", idBooking));
        criteria.addOrder(Order.desc("pk.idService"));
        criteria.setMaxResults(1);
        ContractService payment = (ContractService) criteria.uniqueResult();*/

        StringBuilder hql=new StringBuilder();

        hql.append("select cl from ContractService cl where  ");
        hql.append("cl.idBooking = :idBooking  ");
        hql.append("order by cl.idContractService desc  ");

        Query query=currentSession().createQuery(hql.toString());


        query.setParameter("idBooking",idBooking);

        ContractService payment=(ContractService) query.setMaxResults(1).uniqueResult();
        Long max=0l;
        if(payment!=null){
            max=payment.getIdContractService();
        }

        return max+1;
    }


    public ContractService find(long idBooking,long idContractService) {
        //Criteria criteria=currentSession().createCriteria(ContractService.class);
/*
        return Optional.fromNullable((ContractService) criteria().add(Restrictions.eq("idBooking", idBooking)).
                add(Restrictions.eq("idContractService", idContractService)).setMaxResults(1).uniqueResult());
                */


        return this.get(new ContractServicePk(idContractService,idBooking));

        /*StringBuilder hql=new StringBuilder();

        hql.append("select cl from ContractService cl where  ");

        hql.append("cl.pk.idService=:idContractService and cl.pk.idBooking=:idBooking");


        Query query=currentSession().createQuery(hql.toString());


        query.setParameter("idBooking",idBooking);
        query.setParameter("idContractService",idContractService);
        return (ContractService) query.setMaxResults(1).uniqueResult();*/
    }
    public ContractService findByIdReservationAndIdContract(long idReservation,long idContractService) {


        StringBuilder query=new StringBuilder();

        query.append("select {sc.*} from M4CSERVICIOSCONTRATADOS sc ");
        query.append("where idservcontratado=:IDCONTRACTSERVICE and idreservacion=:IDRESERVACION ");

        Query q=currentSession().createSQLQuery(query.toString()).addEntity("sc",ContractService.class);

        q.setParameter("IDCONTRACTSERVICE",idContractService);
        q.setParameter("IDRESERVACION",idReservation);


        return (ContractService) q.uniqueResult();



    }

    @Override
    public ContractService saveOrUpdate(ContractService entity) {
         currentSession().saveOrUpdate(entity);
        return entity;
    }

    public void delete(long idBooking,long idContractService) {

        StringBuilder hql=new StringBuilder();

        hql.append("delete ContractService where idBooking=:IDBOOKING and idContractService=:IDSERVICE ");
        Query q=currentSession().createQuery(hql.toString());

        q.setParameter("IDBOOKING",idBooking);
        q.setParameter("IDSERVICE",idContractService);

        q.executeUpdate();

    }
}
