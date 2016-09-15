package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.online.CertLogin;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.core.dto.PreviewBrokerDTO;
import com.m4sg.crm4marketingsunset.util.Constants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.util.List;

/**
 * Created by Usuario on 30/03/2015.
 */
public class CertLoginDAO extends GenericDAO<CertLogin>{
    public CertLoginDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<CertLogin> findAll() {
        return currentSession().createCriteria(CertLogin.class).addOrder(Order.desc("user")).list();
    }

    @Override
    public Optional<CertLogin> find(long id) {
        return  Optional.fromNullable(get(id));
    }

    public Optional<CertLogin> find(String user) {

        return Optional.fromNullable((CertLogin)criteria().add(Restrictions.eq("user", user)).uniqueResult());
    }
    public Optional<CertLogin> auth(String user,String password) {

        return Optional.fromNullable((CertLogin) criteria().add(Restrictions.eq("user", user)).add(Restrictions.eq("password",password)).uniqueResult());
    }

    @Override
    public CertLogin saveOrUpdate(CertLogin entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }
    public PaginationDTO<CertLogin> findAll(int sizePage,int page) {

        Criteria criteria;
        criteria=criteria().addOrder(Order.asc("user"));
        return pagination(criteria,sizePage,page);

    }

    public PaginationDTO<CertLogin> findAllMb(int sizePage,int page,Long certCustomerId) {

        Criteria criteria;
        criteria=criteria().add(Restrictions.eq("certCustomer.id", certCustomerId)).addOrder(Order.asc("user"));
        return pagination(criteria,sizePage,page);

    }

    public PaginationDTO<PreviewBrokerDTO> findAllMbNt(int sizePage,int page,Long certCustomerId){
        PaginationDTO<PreviewBrokerDTO> paginationDTO;
        StringBuilder hql=new StringBuilder();

        hql.append("SELECT B.NOMBRE,b.IDUSER,camp.IDCAMPANIA,CAMP.CAMPANIA FROM (SELECT B.IDUSER,B.NOMBRE,B.IDCAMPANA FROM M4CCERTSCLIENTES@M4CPUBLIC MB INNER JOIN M4CCERTSLOGIN@M4CPUBLIC B ON MB. ID = B.IDCLIENTE AND MB. ID = :idCertCustomer )B LEFT JOIN M4CCATCAMPANA camp ON B.IDCAMPANA = CAMP.IDCAMPANIA");




        SQLQuery query=currentSession().createSQLQuery(hql.toString());

        query.addScalar("NOMBRE", StringType.INSTANCE);
        query.addScalar("IDUSER", StringType.INSTANCE);
        query.addScalar("IDCAMPANIA", LongType.INSTANCE);
        query.addScalar("CAMPANIA", StringType.INSTANCE);
        query.setParameter("idCertCustomer", certCustomerId);
        query.setResultTransformer(Transformers.aliasToBean(PreviewBrokerDTO.class));
        List<PreviewBrokerDTO>previewCustomerDTOLst=query.list();

System.out.println("Size "+previewCustomerDTOLst.size());
        paginationDTO=new PaginationDTO<PreviewBrokerDTO>(page, previewCustomerDTOLst.size(), 1, previewCustomerDTOLst);
        return paginationDTO;


    }
    public PaginationDTO<CertLogin> findAll(String orderBy,String dir,int sizePage,int page) {

        Criteria criteria=criteria();

        if(dir.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()) {
            criteria.addOrder(Order.asc(orderBy));
        }else if(dir.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            criteria.addOrder(Order.desc(orderBy));
        }
        return pagination(criteria,sizePage,page);

    }
    public PaginationDTO<CertLogin> globalSearch(String search,String orderBy,String direction,int sizePage,int page){

        StringBuilder hql=new StringBuilder();

        hql.append("select cl from CertLogin cl where  ");
        hql.append("cl.user like :userName or ");
        hql.append("cl.email like :email  ");

        Query query=currentSession().createQuery(hql.toString());


        query.setParameter("userName","%"+search+"%");
        query.setParameter("email","%"+search+"%");


        if(direction.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()){

            hql.append(" order by u."+orderBy +" "+Constants.ORDER_ASC);

        }else if(direction.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            hql.append(" order by u." + orderBy + " " + Constants.ORDER_DESC);
        }

        return pagination(query,sizePage,page);



    }
}
