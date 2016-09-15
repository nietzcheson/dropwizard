package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.CallCenter;
import com.m4c.model.entity.Campaign;
import com.m4sg.crm4marketingsunset.core.dto.CampaignPackageDTO;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.core.dto.ResultCampaignDTO;
import com.m4sg.crm4marketingsunset.util.Constants;
import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.util.Iterator;
import java.util.List;


/**
 * Created by sistemas on 05/01/2015.
 */
public class CampaignDAO extends GenericDAO<Campaign> {
    public CampaignDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public PaginationDTO<ResultCampaignDTO> findAll(String orderBy,String dir,int sizePage,int page, List<CallCenter> callCenterList) {
        PaginationDTO<ResultCampaignDTO> paginationDTO;
        StringBuilder idList=new StringBuilder();
        Iterator<CallCenter> it = callCenterList.iterator();
        while(it.hasNext()) {

            CallCenter myClass = it.next();
            idList.append(myClass.getId().toString());
            if(it.hasNext()){
                idList.append(",");
            }
            // do something with myClass
        }
        long totalElements =getCountSQLQuery(callCenterList,"");
        int totalPages = (int) Math.ceil(new Double(totalElements) / new Double(sizePage));

        if (page > totalPages) {
            page = totalPages;
        }
        if (page < 1) {
            page = 1;
        }
        int start = ((page -1 ) * sizePage)+1;
        int end = (page * sizePage)>totalElements? (int) totalElements :(page * sizePage);

        System.out.println("start "+start +" end "+end);
        StringBuilder hql=new StringBuilder();
        SQLQuery query;

        //hql.append("select c from Campaign c left join c.offer o left join c.certCustomer ct left join c.callCenter  clc ");
        //hql.append(" where c.callCenter in (:callCenterList) ");
        hql.append("SELECT  * from (SELECT row_number() over (");
        if(dir.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()){

            hql.append("order by c." + orderBy + " " + Constants.ORDER_ASC);

        }else if(dir.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            hql.append("order by c." + orderBy + " " + Constants.ORDER_DESC);
        }
        hql.append(" ) rn," +
                "\tc.IDCAMPANIA AS ID,\n" +
                "C.CAMPANIA AS NAME,\n" +
                "\tC.DATE_UPDATED AS DATEUPDATED,\n" +
                "\tCUST.COMPANIA AS CERTCUSTOMER,\n" +
                "\tO.name AS OFFER \n" +
                "FROM\n" +
                "\tM4CCATCAMPANA c\n" +
                "LEFT JOIN M4CCERTSCLIENTES@M4CPUBLIC cust ON c.IDCLIENTE = CUST. ID\n" +
                "LEFT JOIN m4coffer o on c.OFFER_ID = o.id LEFT JOIN M4CCALLCENTER clc on c.IDCALLCENTER = clc.IDCALLCENTER");
        hql.append(" WHERE c.IDCALLCENTER in ("+idList+") ");


        hql.append(" ) where rn BETWEEN ");
        hql.append(start).append(" and ").append(end);
        System.out.println("hql "+hql.toString());




        query=  this.currentSession().createSQLQuery(hql.toString());

        //criteria.add(Restrictions.in("callCenter", callCenterList));
        //return pagination(criteria,sizePage,page);

        //query.setParameterList("callCenterList",callCenterList);

        query.setResultTransformer(Transformers.aliasToBean(ResultCampaignDTO.class));
        query.addScalar("NAME", StringType.INSTANCE);
        query.addScalar("DATEUPDATED", DateType.INSTANCE);
        query.addScalar("OFFER", StringType.INSTANCE);
        query.addScalar("ID", LongType.INSTANCE);

        query.addScalar("CERTCUSTOMER", StringType.INSTANCE);
        List<ResultCampaignDTO>previewCustomerDTOLst=query.list();

        System.out.println("Size "+previewCustomerDTOLst.size());

        paginationDTO=new PaginationDTO<ResultCampaignDTO>(page, totalElements, totalPages, previewCustomerDTOLst);
        return paginationDTO;

    }

    public PaginationDTO<ResultCampaignDTO> globalSearch(String search,String orderBy,String dir,int sizePage,int page, List<CallCenter> callCenterList) throws QueryException {


        StringBuilder idList=new StringBuilder();
        Iterator<CallCenter> it = callCenterList.iterator();
        while(it.hasNext()) {

            CallCenter myClass = it.next();
            idList.append(myClass.getId().toString());
            if(it.hasNext()){
                idList.append(",");
            }
            // do something with myClass
        }

        //System.out.println("Lista Callcenters "+idList);
        PaginationDTO<ResultCampaignDTO> paginationDTO;
        long totalElements =getCountSQLQuery(callCenterList,search);
        int totalPages = (int) Math.ceil(new Double(totalElements) / new Double(sizePage));

        if (page > totalPages) {
            page = totalPages;
        }
        if (page < 1) {
            page = 1;
        }
        int start = ((page -1 ) * sizePage)+1;
        int end = (page * sizePage)>totalElements? (int) totalElements :(page * sizePage);

       System.out.println("start "+start +" end "+end +" dir "+dir);
        StringBuilder hql=new StringBuilder();
        SQLQuery query;
        hql.append("SELECT  * from (SELECT row_number() over (");
        if(dir.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()){

            hql.append("order by c." + orderBy + " " + Constants.ORDER_ASC);

        }else if(dir.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            hql.append("order by c." + orderBy + " " + Constants.ORDER_DESC);
        }
        else{
            hql.append("order by c.CAMPANIA");
        }
        hql.append(" ) rn," +
                "\tc.IDCAMPANIA AS ID,\n" +
                "C.CAMPANIA AS NAME,\n" +
                "\tC.DATE_UPDATED AS DATEUPDATED,\n" +
                "\tCUST.COMPANIA AS CERTCUSTOMER,\n" +
                "\tO.name AS OFFER \n" +
                "FROM\n" +
                "\tM4CCATCAMPANA c\n" +
                "LEFT JOIN M4CCERTSCLIENTES@M4CPUBLIC cust ON c.IDCLIENTE = CUST. ID\n" +
                "LEFT JOIN m4coffer o on c.OFFER_ID = o.id LEFT JOIN M4CCALLCENTER clc on c.IDCALLCENTER = clc.IDCALLCENTER");
        hql.append(" WHERE c.IDCALLCENTER in ("+idList+") AND (");
        hql.append(" lower(c.CAMPANIA) like :name or ");
        hql.append(" lower(c.CODIGO) like :codigo or ");
        hql.append(" lower(o.NAME) like :offerName or ");
        hql.append(" lower(c.SLUG) like :name or ");
        hql.append(" lower(CUST.COMPANIA) like :brokerCRM ) ");


        hql.append(" ) where rn BETWEEN ");
        hql.append(start).append(" and ").append(end);
       // System.out.println("hql "+hql.toString());




        query=  this.currentSession().createSQLQuery(hql.toString());

        search="%".concat(search.trim().toLowerCase()).concat("%");

        query.setParameter("name",search);
        query.setParameter("codigo",search);
        query.setParameter("offerName",search);
        query.setParameter("brokerCRM",search);

        query.setResultTransformer(Transformers.aliasToBean(ResultCampaignDTO.class));
        query.addScalar("NAME", StringType.INSTANCE);
        query.addScalar("DATEUPDATED", DateType.INSTANCE);
        query.addScalar("OFFER", StringType.INSTANCE);
        query.addScalar("ID", LongType.INSTANCE);

        query.addScalar("CERTCUSTOMER", StringType.INSTANCE);
        List<ResultCampaignDTO>previewCustomerDTOLst=query.list();

        System.out.println("Size "+previewCustomerDTOLst.size());

        paginationDTO=new PaginationDTO<ResultCampaignDTO>(page, totalElements, totalPages, previewCustomerDTOLst);
        return paginationDTO;
    }



    @Override
    public List<Campaign> findAll() {
        return currentSession().createCriteria(Campaign.class).addOrder(Order.desc("id")).list();
    }
    public List<CommonDTO> find(List<Long>callcenterLst) {
        List<CommonDTO>campaigns;
        if(!callcenterLst.isEmpty()){
            Query query=currentSession().createQuery("select new com.m4sg.crm4marketingsunset.core.dto.CommonDTO(c.id,c.name) from Campaign c where c.callCenter.id in (:callCenters) "+" order by name");
            query.setParameterList("callCenters",callcenterLst);

            campaigns=query.list();
        }else{
            Query query=currentSession().createQuery("select new com.m4sg.crm4marketingsunset.core.dto.CommonDTO(c.id,c.name) from Campaign c order by name");

            campaigns=query.list();
        }
        return campaigns;
    }

    @Override
    public Optional<Campaign> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public Optional<Campaign> find(String name){
        return Optional.fromNullable((Campaign)criteria().add(Restrictions.ilike("name",name)).setMaxResults(1).uniqueResult());
    }

    public Optional<Campaign>findBySlugify(String slugify){
        slugify =slugify == null ? "": slugify.toUpperCase();
        return
                Optional.fromNullable((Campaign)criteria().add(Restrictions.ilike("slug", slugify)).setMaxResults(1).uniqueResult());

    }

    @Override
    public Campaign saveOrUpdate(Campaign entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }
    public void flushSession(){
        currentSession().flush();
    }

    public Long findMax(){
        Long max= (Long)criteria().setProjection(Projections.max("id")).uniqueResult();
        return max+1;
    }

    public Long getCountSQLQuery( List<CallCenter> callCenterList,String search ){

        StringBuilder idList=new StringBuilder();

        Iterator<CallCenter> it = callCenterList.iterator();
        while(it.hasNext()) {

            CallCenter myClass = it.next();
            idList.append(myClass.getId().toString());
            if(it.hasNext()){
                idList.append(",");
            }
            // do something with myClass
        }

        StringBuilder queryBuilder=new StringBuilder();

        queryBuilder.append("SELECT\n" +
                "\t count(*) as count \n" +
                "FROM\n" +
                "\tM4CCATCAMPANA c\n" +
                "LEFT JOIN M4CCERTSCLIENTES@M4CPUBLIC cust ON c.IDCLIENTE = CUST. ID\n" +
                "LEFT JOIN m4coffer o on c.OFFER_ID = o.id LEFT JOIN M4CCALLCENTER clc on c.IDCALLCENTER = clc.IDCALLCENTER");
        queryBuilder.append(" WHERE c.IDCALLCENTER in ("+idList+") ");

        if(!search.isEmpty()) {
            queryBuilder.append(" AND ( lower(c.CAMPANIA) like :name or ");
            queryBuilder.append(" lower(c.CODIGO) like :codigo or ");
            queryBuilder.append(" lower(o.NAME) like :offerName or ");
            queryBuilder.append(" lower(c.SLUG) like :name or ");
            queryBuilder.append(" lower(CUST.COMPANIA) like :brokerCRM ) ");

        }

        System.out.println("hql "+queryBuilder.toString());
        System.out.println("search  "+search);
        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString());

        if(!search.isEmpty()) {
            search="%".concat(search.trim().toLowerCase()).concat("%");
            query.setParameter("name", search);
            query.setParameter("codigo", search);
            query.setParameter("offerName", search);
            query.setParameter("brokerCRM", search);
        }
        query.addScalar("count",LongType.INSTANCE);


        return (Long) query.uniqueResult();

    }

    public List<CampaignPackageDTO> findCampaignsbyCallcenter(String callCenters){
        StringBuilder queryBuilder=new StringBuilder();
        queryBuilder.append("select callcenter.idcallcenter as ID, callcenter.NOMBRE, ");
        queryBuilder.append("camp.CAMPANIA, camp.IDCAMPANIA, ");
        queryBuilder.append("sub.IMPORTE ");
        queryBuilder.append("from M4CCALLCENTER callcenter ");
        queryBuilder.append("Inner join m4ccatcampana camp ");
        queryBuilder.append("on camp.IDCALLCENTER=callcenter.IDCALLCENTER ");
        queryBuilder.append("inner join m4csubservicios sub ");
        queryBuilder.append("on sub.idcampania=camp.IDCAMPANIA ");
        queryBuilder.append("where camp.activo=1 ");
        if(callCenters==null || !callCenters.isEmpty())
            queryBuilder.append("and  callcenter.idcallcenter in ("+callCenters+") ");
        queryBuilder.append("order by callcenter.idcallcenter ");
        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("ID",LongType.INSTANCE);
        query.addScalar("NOMBRE", StringType.INSTANCE);
        query.addScalar("IMPORTE", DoubleType.INSTANCE);
        query.addScalar("CAMPANIA", StringType.INSTANCE);
        query.addScalar("IDCAMPANIA", LongType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(CampaignPackageDTO.class));
        List<CampaignPackageDTO>campaignPackageDTOList=query.list();
        return campaignPackageDTOList;
    }
}
