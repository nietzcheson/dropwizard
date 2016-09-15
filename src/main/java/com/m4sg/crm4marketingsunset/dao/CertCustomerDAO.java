package com.m4sg.crm4marketingsunset.dao;

import ch.lambdaj.group.Group;
import com.google.common.base.Optional;
import com.m4c.model.entity.online.CertCustomer;
import com.m4sg.crm4marketingsunset.core.dto.MasterBrokerDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.core.dto.ResultSearchMBDTO;
import com.m4sg.crm4marketingsunset.util.Constants;
import org.hibernate.QueryException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.util.List;

import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.group.Groups.by;

/**
 * Created by Usuario on 26/03/2015.
 */
public class CertCustomerDAO extends GenericDAO<CertCustomer> {
    public CertCustomerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    @Override
    public List<CertCustomer> findAll() {
        return currentSession().createCriteria(CertCustomer.class).addOrder(Order.desc("id")).list();
    }

    @Override
    public Optional<CertCustomer> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public CertCustomer saveOrUpdate(CertCustomer entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }
/*    public PaginationDTO<CertCustomer> findAll(int sizePage,int page) {

        Criteria criteria;
        criteria=criteria().addOrder(Order.asc("id"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return pagination(criteria,sizePage,page);

    }*/

    public StringBuilder getBaseSQLQuery(){
        StringBuilder query=new StringBuilder();

        query.append("SELECT client_login.id, ");
        query.append("compania COMPANY, ");
        query.append("responsablename RESPONSABLENAME, ");
        query.append("email, ");
        query.append("iduser USERNAME, ");
        query.append("cast(Xmlagg(XMLELEMENT(e, campania||',')).EXTRACT('//text()') as varchar2(1000)) CAMPAIGN  ");
        query.append("FROM   (SELECT cliente.id, ");
        query.append("login.email, ");
        query.append("login.iduser, ");
        query.append("cliente.responsablename, ");
        query.append("cliente.compania ");
        query.append("FROM   m4ccertsclientes@m4cpublic cliente ");
        query.append("INNER JOIN m4ccertslogin@m4cpublic login ");
        query.append(" ON cliente.id = login.id_masterbroker)client_login ");
        query.append("LEFT JOIN m4ccatcampana campania ");
        query.append("ON campania.IDCLIENTE = client_login.id ");

        return query;
    }


    public long count(String search){

        StringBuilder queryBuilder=getSQLCount(getBaseSQLQuery());

        if(!search.isEmpty()) {
            queryBuilder = createConditionNativeQuery(search, queryBuilder);
        }
        queryBuilder.append("group by client_login.id,email,iduser,responsablename,compania) ");

        System.out.println("query:::"+queryBuilder.toString());
        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString());

        if(!search.isEmpty()) {
            setQueryValueCondition(query, search);
        }

        query.addScalar("count",LongType.INSTANCE);


        return (Long) query.uniqueResult();
    }

    public void setQueryValueCondition(SQLQuery query,String search){

            query.setParameter("SEARCH","%" + search.trim().toUpperCase()+"%");



    }
    public StringBuilder createConditionNativeQuery(String search,StringBuilder queryBuilder){

        if(!search.isEmpty()) {
       queryBuilder.append("where ");
       queryBuilder.append("upper(compania) like :SEARCH OR ");
       queryBuilder.append("upper(campania) like :SEARCH OR ");
       queryBuilder.append("upper(responsablename) like :SEARCH OR ");
       queryBuilder.append("upper(email) like :SEARCH OR ");
       queryBuilder.append("upper(iduser) like :SEARCH ");

        }

        return queryBuilder;
    }

    public StringBuilder getSQLCount(StringBuilder baseQuery){

        StringBuilder countQuery=new StringBuilder("select count(*) count from (");
//        baseQuery.append("group by iduser");
        countQuery.append(baseQuery);

        return countQuery;
    }
    public PaginationDTO<MasterBrokerDTO> globalSearch(String search,String orderBy,String direction,int pageLength, int page)throws QueryException {

        PaginationDTO<MasterBrokerDTO> paginationDTO;
        StringBuilder queryBuilder=getBaseSQLQuery();
        long totalElements =count(search);
        int totalPages = (int) Math.ceil(new Double(totalElements) / new Double(pageLength));

        if (page > totalPages) {
            page = totalPages;
        }
        if (page < 1) {
            page = 1;
        }

        int start = ((page -1 ) * pageLength)+1;
        int end = (page * pageLength)>totalElements? (int) totalElements :(page * pageLength);


        queryBuilder = createConditionNativeQuery(search, queryBuilder);


        queryBuilder=createSQLOrderBy(queryBuilder,direction, orderBy);

        queryBuilder.insert(0, "SELECT * FROM (");
        queryBuilder.append(" group by client_login.id,email,iduser,responsablename,compania ");
        queryBuilder.append(")mb where rn between ");

        queryBuilder.append(start).append(" and ").append(end);


        System.out.println("query:::"+queryBuilder.toString());
        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString());



        if(!search.isEmpty()) {
            setQueryValueCondition(query, search);
        }
        setSQLPropertiesPreview(query);

        query.setResultTransformer(Transformers.aliasToBean(MasterBrokerDTO.class));

        List<MasterBrokerDTO>masterBrokerDTOLst=query.list();

        groupMasterByCampaign(masterBrokerDTOLst);


        paginationDTO=new PaginationDTO<MasterBrokerDTO>(page, totalElements, totalPages, masterBrokerDTOLst);

        return paginationDTO;

    }

    public void groupMasterByCampaign(List<MasterBrokerDTO> masterBrokerDTOLst){

        Group<MasterBrokerDTO> masterGroup=group(masterBrokerDTOLst,by(on(MasterBrokerDTO.class).getCOMPANY()));

        System.out.println("MasterBroker==>"+masterGroup.key());
        System.out.println("campañas==>"+masterGroup.subgroups().size());


    }

    public StringBuilder createSQLOrderBy(StringBuilder queryBuilder,String direction,String orderBy){

        StringBuilder orderBuilder=new StringBuilder();
        if(direction.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()){

            orderBuilder.append("order by " + orderBy + " " + Constants.ORDER_ASC);
        }
        else if(direction.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            orderBuilder.append("order by " + orderBy + " " + Constants.ORDER_DESC);

        }else{

            orderBuilder.append("order by id desc ");
        }

        queryBuilder.insert(7, "row_number() over( " + orderBuilder.toString() + ")rn, ");
        return queryBuilder;
    }

    public void setSQLPropertiesPreview(SQLQuery query){

        query.addScalar("ID", LongType.INSTANCE);
        query.addScalar("COMPANY", StringType.INSTANCE);
        query.addScalar("RESPONSABLENAME", StringType.INSTANCE);
        query.addScalar("EMAIL", StringType.INSTANCE);
        query.addScalar("USERNAME", StringType.INSTANCE);
        query.addScalar("CAMPAIGN", StringType.INSTANCE);



    }

    /*public PaginationDTO<CertCustomer> findAll(String orderBy,String dir,int sizePage,int page) {

        Criteria criteria=this.currentSession().createCriteria(CertCustomer.class);

        if(dir.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()) {
            criteria.addOrder(Order.asc(orderBy));
        }else if(dir.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            criteria.addOrder(Order.desc(orderBy));
        }
        criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        return pagination(criteria,sizePage,page);

    }*/
    /*public PaginationDTO<CertCustomer> globalSearch(String search,String orderBy,String direction,int sizePage,int page){

        StringBuilder hql=new StringBuilder();

        hql.append("select c from CertCustomer c,CertLogin cl where c.user.user=cl.user and ( LOWER(c.companyName) like :name or ");
        hql.append("LOWER(cl.user) like :userName or ");
        hql.append("LOWER(cl.email) like :email or  ");
        hql.append("LOWER(c.responsableName) like :perfil) ");

        Query query=currentSession().createQuery(hql.toString());

        query.setParameter("name","%"+search.toLowerCase()+"%");
        query.setParameter("userName","%"+search.toLowerCase()+"%");
        query.setParameter("email","%"+search.toLowerCase()+"%");
        query.setParameter("perfil","%"+search.toLowerCase()+"%");

        if(direction.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()){

            hql.append(" order by u."+orderBy +" "+Constants.ORDER_ASC);

        }else if(direction.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            hql.append(" order by u." + orderBy + " " + Constants.ORDER_DESC);
        }

        return pagination(query,sizePage,page);
    }*/

    public List<ResultSearchMBDTO>searchMB(String term){

        StringBuilder query=new StringBuilder();
        term=term.toUpperCase();
        query.append("SELECT c.id ID, ");
        query.append("c.compania COMPANY, ");
        query.append("cl.iduser USERNAME, ");
        query.append("'master_broker' CATEGORY ");
        query.append("FROM   m4ccertsclientes@M4CPUBLIC c ");
        query.append("inner join m4ccertslogin@M4CPUBLIC cl ");
        query.append("ON c.id = cl.id_masterBroker ");
        query.append("WHERE  upper(c.compania) LIKE :TERM ");
        query.append("AND ROWNUM <= 5 ");
        query.append("UNION ");
        query.append("SELECT c.id ID, ");
        query.append("c.compania COMPANY, ");
        query.append("cl.iduser USERNAME, ");
        query.append("'user' CATEGORY ");
        query.append("FROM   m4ccertsclientes@M4CPUBLIC c ");
        query.append("inner join m4ccertslogin@M4CPUBLIC cl ");
        query.append("ON c.id = cl.id_masterBroker ");
        query.append("WHERE  upper(cl.iduser) LIKE :TERM AND ROWNUM <= 5 order by CATEGORY,COMPANY,USERNAME ");

        SQLQuery q=currentSession().createSQLQuery(query.toString());

        q.setParameter("TERM", "%" + term + "%");

        q.addScalar("ID", LongType.INSTANCE);
        q.addScalar("COMPANY", StringType.INSTANCE);
        q.addScalar("USERNAME", StringType.INSTANCE);
        q.addScalar("CATEGORY", StringType.INSTANCE);

        q.setResultTransformer(Transformers.aliasToBean(ResultSearchMBDTO.class));

        return q.list();
    }
    public Long findMax(){
        Long max= (Long)criteria().setProjection(Projections.max("id")).uniqueResult();
        return max+1;
    }
}
