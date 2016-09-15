package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.CallCenter;
import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.Customer;
import com.m4c.model.entity.Reservation;
import com.m4sg.crm4marketingsunset.core.dto.CustomerDataLongDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.core.dto.PreviewCustomerDTO;
import com.m4sg.crm4marketingsunset.core.dto.ResultbyDateDTO;
import com.m4sg.crm4marketingsunset.util.Constants;
import com.m4sg.crm4marketingsunset.util.Utils;
import org.hibernate.LockOptions;
import org.hibernate.QueryException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.query.spi.sql.NativeSQLQueryReturn;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Fernando on 30/03/2015.
 */
public class CustomerDAO  extends GenericDAO<Customer> {

    private ReservationDAO reservationDAO;
    public CustomerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public Optional<Customer> find(long id) {
        return Optional.fromNullable(get(id));
    }

    public Long findMax(){
        Long max= (Long)criteria().setProjection(Projections.max("id")).uniqueResult();
        return max+1;
    }

    @Override
    @Transactional
    public Customer saveOrUpdate(Customer entity) {
        currentSession().saveOrUpdate(entity);
        currentSession().flush();
        boolean h=true;
        return entity;
    }
    public PaginationDTO<PreviewCustomerDTO> findAll(String orderBy,String direction,int sizePage,int page, List<CallCenter> callCenter, String fromDate, String toDate)throws QueryException{

        return globalSearch("",orderBy,direction,sizePage,page, callCenter,fromDate,toDate);
    }


    public PaginationDTO<PreviewCustomerDTO> globalSearch(String search,String orderBy,String direction,int pageLength, int page, List<CallCenter> callCenter, String fromDate, String toDate)throws QueryException{

        PaginationDTO<PreviewCustomerDTO> paginationDTO;
        StringBuilder queryBuilder=getBaseSQLQuery(getSQLPropertiesPreview());
        long totalElements = count(search,fromDate,toDate);
        int totalPages = (int) Math.ceil(new Double(totalElements) / new Double(pageLength));

        if (page > totalPages) {
            page = totalPages;
        }
        if (page < 1) {
            page = 1;
        }

        int start = ((page -1 ) * pageLength)+1;
        int end = (page * pageLength)>totalElements? (int) totalElements :(page * pageLength);

        search=search.toUpperCase();
        queryBuilder.append(")a ");
            queryBuilder = createConditionNativeQuery(search, queryBuilder,fromDate,toDate);
        queryBuilder=createSQLOrderBy(queryBuilder,direction, orderBy);
        queryBuilder.insert(0, "SELECT * FROM (");
        queryBuilder.append(")customer)where rn between ");

        queryBuilder.append(start).append(" and ").append(end);


        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString());
        if(!search.isEmpty()) {
            setQueryValueCondition(query, search);
        }
        setSQLPropertiesPreview(query);
        query.setResultTransformer(Transformers.aliasToBean(PreviewCustomerDTO.class));

        List<PreviewCustomerDTO>previewCustomerDTOLst=query.list();

        for (int i = 0; i < previewCustomerDTOLst.size(); i++) {
            PreviewCustomerDTO previewCustomerDTO= previewCustomerDTOLst.get(i);
            System.out.println(previewCustomerDTO.getARCHIVO());
            if  ("Cancelada".equals(previewCustomerDTO.getMV_STATUS())){
                previewCustomerDTO.setLINECOLOR("#FF0000");
                previewCustomerDTO.setSTATUS("CANCELADA");

            }else if("Posible cancelacion".equals(previewCustomerDTO.getMV_STATUS())){
                previewCustomerDTO.setLINECOLOR("#FF9900");
                previewCustomerDTO.setSTATUS("POSIBLECANCELADA");
            }else if(previewCustomerDTO.getM_RESERVACION()!=null) {
                System.out.println("Manifesto 11 "+previewCustomerDTO.getM_RESERVACION());
                StringBuilder hql=new StringBuilder();
                hql.append("select numeroManifesto from Reservation where  id="+previewCustomerDTO.getM_RESERVACION());//Buscamos el manifesto en la reservacion
                Long reservation= (Long) currentSession().createQuery(hql.toString()).list().get(0);


                //Reservation reservation=reservationDAO.find(previewCustomerDTO.getM_RESERVACION()).get();
                //if(reservation!=null){
                    System.out.println("Manifesto "+reservation);
                    hql.delete(0,hql.length());
                    List<Integer> lst;
                    try{
                        hql.append("select 1 from ManifestContract m,Contract c where  m.id="+reservation +" and c.id=m.idContract and c.cancelDate  is null");
                        lst =  currentSession().createQuery(hql.toString()).list();
                    }catch (Exception e){
                        lst= new ArrayList<Integer>();
                    }
                System.out.println(" HQL "+hql);

                    System.out.println("idSocio "+ lst.size());
                    if(lst.size()==1){// Si es socio y no esta cancelado el contrato

                        if  (previewCustomerDTO.getIDSTATUSRESERVA()==13){
                            previewCustomerDTO.setLINECOLOR("#B2E338");// Socio C/Reserva Cancelada
                            previewCustomerDTO.setSTATUS("SOCIORESERVACANCELADA");

                        }else{
                            previewCustomerDTO.setLINECOLOR("#4BB52D");// Socio
                            previewCustomerDTO.setSTATUS("SOCIO");
                        }
                    }else{
                        if  (previewCustomerDTO.getIDSTATUSRESERVA()==13){
                            previewCustomerDTO.setLINECOLOR("#BF8200");// Reservado Cancelado
                            previewCustomerDTO.setSTATUS("RESERVACANCELADA");
                        }else{
                            previewCustomerDTO.setLINECOLOR("#00FFFF");  // Reservado
                            previewCustomerDTO.setSTATUS("RESERVADO");
                        }
                    }
                //}



            }else{
                previewCustomerDTO.setLINECOLOR("#ffffff");
            }
        }
        paginationDTO=new PaginationDTO<PreviewCustomerDTO>(page, totalElements, totalPages, previewCustomerDTOLst);

        return paginationDTO;

    }
    public PaginationDTO<CustomerDataLongDTO> globalSearchExtra(String search, String orderBy, String direction, int pageLength, int page, StringBuilder stringBuilder, String fromDate, String toDate, String status)throws QueryException{

        PaginationDTO<CustomerDataLongDTO> paginationDTO;

        StringBuilder queryBuilder=getBaseSQLQueryExtra(new StringBuilder(),stringBuilder,status,fromDate,toDate);
        long totalElements =countExtra(search,stringBuilder,status,fromDate,toDate);
        int totalPages = (int) Math.ceil(new Double(totalElements) / new Double(pageLength));

        if (page > totalPages) {
            page = totalPages;
        }
        if (page < 1) {
            page = 1;
        }

        int start = ((page -1 ) * pageLength)+1;
        int end = (page * pageLength)>totalElements? (int) totalElements :(page * pageLength);

        search=search.toUpperCase();
        //queryBuilder.append(")a ");
        queryBuilder = createConditionNativeQueryExtra(search, queryBuilder);
        //queryBuilder=createSQLOrderBy(queryBuilder,direction, orderBy);
        queryBuilder.insert(0, "SELECT * FROM (");
        queryBuilder.append(" ) ) where rn between ");

        queryBuilder.append(start).append(" and ").append(end);


        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString());
        if(!search.isEmpty()) {
            setQueryValueConditionExtra(query, search);
        }
        setSQLPropertiesPreviewExtra(query);
        query.setResultTransformer(Transformers.aliasToBean(CustomerDataLongDTO.class));

        List<CustomerDataLongDTO>previewCustomerDTOLst=query.list();

        paginationDTO=new PaginationDTO<CustomerDataLongDTO>(page, totalElements, totalPages, previewCustomerDTOLst);

        return paginationDTO;

    }

    public Long count(String search, String fromDate, String toDate){

        StringBuilder queryBuilder=getBaseSQLQuery(getSQLCount());
        queryBuilder.append(")a ");
            search = search.toUpperCase();
            queryBuilder = createConditionNativeQuery(search, queryBuilder,fromDate,toDate);
        queryBuilder.append(") ");
        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString());

        if(!search.isEmpty()) {
            setQueryValueCondition(query, search);
        }
        query.addScalar("count",LongType.INSTANCE);
        return (Long) query.uniqueResult();
    }

    public Long countExtra(String search,StringBuilder stringBuilder,String status,String fromDate,String toDate){

        StringBuilder queryBuilder=getBaseSQLQueryExtra(getSQLCountExtra(),stringBuilder,status,fromDate,toDate);

        search = search.toUpperCase();
        queryBuilder = createConditionNativeQueryExtra(search, queryBuilder);
        queryBuilder.append(" ) ) ");
        //queryBuilder.append(") ");
        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString());

        if(!search.isEmpty()) {
           setQueryValueConditionExtra(query, search);
        }
        query.addScalar("count",LongType.INSTANCE);

        return (Long) query.uniqueResult();
    }


    public StringBuilder createConditionNativeQuery(String search,StringBuilder queryBuilder, String fromDate, String toDate){

        queryBuilder.append("where ");
        queryBuilder.append("idcallcenter IN (201, 1541, 5962,361 )  ");
        if(!search.isEmpty()) {
            queryBuilder.append("and( ");
            if (Utils.isNumber(search)) {
                queryBuilder.append("a.IDBOOKING=:IDBOOKING ");
            } else if (Utils.isMail(search)) {
                queryBuilder.append("upper(a.email) like :EMAIL ");

            } else {
                queryBuilder.append("upper(a.campaign) like :CAMPAIGN ");
                queryBuilder.append("or upper(a.firstName) like :NAME ");
                queryBuilder.append("or upper(a.slug) like :SLUG ");
                queryBuilder.append("or upper(a.NUMCERT) =:CERTIFICATE ");
                queryBuilder.append("or upper(a.IDUSER) like :USER ");
                queryBuilder.append("or upper(a.CALLCENTER_NAME) like :CALLCENTER_NAME ");
            }
            queryBuilder.append(") ");
        }
        if(fromDate!=null){
            queryBuilder.append(" and (A. FECHAVENTA >= To_date('"+fromDate+"', 'yyyy-mm-dd') ");
        }
        if(toDate!=null){
            queryBuilder.append(" AND A. FECHAVENTA <= To_date('"+toDate+"', 'yyyy-mm-dd')+1) ");
        }else if (fromDate!=null){
            queryBuilder.append(" AND A. FECHAVENTA <= CURDATE()+1 ");
        }

        return queryBuilder;
    }

    public StringBuilder createConditionNativeQueryExtra(String search,StringBuilder queryBuilder){



        if(!search.isEmpty()) {
           // queryBuilder.append("where ");
            queryBuilder.append(" where ( ");
            if (Utils.isNumber(search)) {
                queryBuilder.append(" MV_IDBOOKING=:IDBOOKING ");
            } /*else if (Utils.isMail(search)) {
                queryBuilder.append("upper(M.EMAIL) like :EMAIL ");

            }*/ else {
                queryBuilder.append("upper(IDCAMPANIA) like :CAMPAIGN ");
                queryBuilder.append("or upper(M_NOMBRE) like :NAME ");
                queryBuilder.append("or upper(M_APELLIDO) like :NAME ");
                queryBuilder.append("or upper(NUMAUT) =:CERTIFICATE ");
            }
            queryBuilder.append(") ");
        }

        return queryBuilder;
    }
    public StringBuilder createSQLOrderBy(StringBuilder queryBuilder,String direction,String orderBy){
        if(direction.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()){

            if("DATEUPDATED".equalsIgnoreCase(orderBy) || "DATECREATED".equalsIgnoreCase(orderBy) ){

                queryBuilder.append("order by nvl(a." + orderBy + ",TO_DATE('1900-01-01','yyyy-MM-dd')) " + Constants.ORDER_ASC);

            }else {
                queryBuilder.append("order by a." + orderBy + " " + Constants.ORDER_ASC);
            }

        }else if(direction.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){

            if("DATEUPDATED".equalsIgnoreCase(orderBy) || "DATECREATED".equalsIgnoreCase(orderBy) ){

                queryBuilder.append("order by nvl(a." + orderBy + ",TO_DATE('1900-01-01','yyyy-MM-dd')) " + Constants.ORDER_DESC);

            }else {
                queryBuilder.append("order by a." + orderBy + " " + Constants.ORDER_DESC);
            }
        }

        return queryBuilder;
    }
    public void setQueryValueCondition(SQLQuery query,String search){
        if(Utils.isNumber(search)) {
            query.setParameter("IDBOOKING", Long.parseLong(search));
        }else if(Utils.isMail(search)) {
            query.setParameter("EMAIL",  search );
        }else{
            query.setParameter("CAMPAIGN",  "%" + search.toUpperCase() + "%");
            query.setParameter("CERTIFICATE", search.toUpperCase());
            query.setParameter("SLUG", search.toUpperCase());
            query.setParameter("NAME", "%" + search.toUpperCase() + "%");
            query.setParameter("USER", "%" + search.toUpperCase() + "%");
            query.setParameter("CALLCENTER_NAME", "%" + search.toUpperCase() + "%");
        }
    }
    public void setQueryValueConditionExtra(SQLQuery query,String search){
        if(Utils.isNumber(search)) {
            query.setParameter("IDBOOKING", Long.parseLong(search));
        }else if(Utils.isMail(search)) {
            //query.setParameter("EMAIL",  search );
        }else{
            query.setParameter("CAMPAIGN",  "%" + search.toUpperCase() + "%");
            query.setParameter("CERTIFICATE", search.toUpperCase());
            query.setParameter("NAME", "%" + search.toUpperCase() + "%");
        }
    }

    public StringBuilder getBaseSQLQuery(StringBuilder complex){

        StringBuilder queryBuilder=new StringBuilder();

        queryBuilder.append(complex);
        queryBuilder.append("FROM   m4cliente c ");
        queryBuilder.append("Inner join ( ");
        queryBuilder.append("SELECT ca.campania, ");
        queryBuilder.append("v.idbooking, ");
        queryBuilder.append("v.numcert, ");
        queryBuilder.append("v.idcliente, ");
        queryBuilder.append("v.IDUSER, ");
        queryBuilder.append("ca.IDCAMPANIA, ");
        queryBuilder.append("ca.slug, ");
        queryBuilder.append("callcenter.nombre as CALLCENTER_NAME,");
        queryBuilder.append("arch.filename as ARCHIVO,");
        queryBuilder.append("(SELECT RES.\"NAME\" from M4CRESERVATIONGROUP res where RES.\"ID\" = ca.IDRESERVATIONGROUP) as reservationgroup, ");
        queryBuilder.append("ca.OFFER_ID as OFFERID, ");
        queryBuilder.append("ca.TIPO_CERT as TIPOCERT, ");
        queryBuilder.append("v.FECHAVENTA,v.IDSTATUS ");
        queryBuilder.append("FROM   m4cventa v ");
        queryBuilder.append("inner join m4ccatcampana ca ");
        queryBuilder.append("ON ca.idcampania = v.idcampania ");
        queryBuilder.append("INNER JOIN M4CCALLCENTER callcenter on callcenter.IDCALLCENTER= v.IDCALLCENTER ");
        queryBuilder.append("LEFT JOIN RTP_ARCHIVODIGITAL arch ON (v.idbooking = arch.idbooking and arch.TIPO_DOCUMENTO='PL') ");
        queryBuilder.append(")cav ");
        queryBuilder.append("ON c.idcliente = cav.idcliente ");
        return queryBuilder;
    }

    public StringBuilder getBaseSQLQueryExtra(StringBuilder count,StringBuilder campaings,String status, String fromDate, String toDate){

        StringBuilder queryBuilder=new StringBuilder();
        String statusFilter="";
        if (!status.isEmpty()){
            statusFilter+=" where ";
            if(status.equals("RESERVED")){
                statusFilter+=" reserva.idcliente IS NOT NULL";
            }else {
                statusFilter+=" NOTAS.idcliente IS NOT NULL";
            }

        }
        if(statusFilter.isEmpty()){
            if(fromDate!=null){
                statusFilter+=(" where ( cliente.fecha >= To_date('"+fromDate+"', 'yyyy-mm-dd') ");

            }
            if(toDate!=null){
                statusFilter+=(" AND cliente.fecha <= To_date('"+toDate+"', 'yyyy-mm-dd')+1) ");
            }else if (fromDate!=null){
                statusFilter+=(" AND cliente.fecha <= CURDATE()+1) ");
            }
        }else{
            if(fromDate!=null){
                statusFilter+=(" and( cliente.fecha >= To_date('"+fromDate+"', 'yyyy-mm-dd') ");
            }
            if(toDate!=null){
                statusFilter+=(" AND cliente.fecha <= To_date('"+toDate+"', 'yyyy-mm-dd')+1) ");

            }else if (fromDate!=null){
                statusFilter+=(" AND cliente.fecha <= CURDATE()+1) ");
            }
        }
        queryBuilder.append(count);
        queryBuilder.append("SELECT * FROM (SELECT ROW_NUMBER () OVER (ORDER BY M_IDCLIENTE DESC) RN,customer.* FROM (SELECT * FROM (SELECT DISTINCT venta.idbooking AS MV_IDBOOKING, " +
                " cliente.nombre AS M_NOMBRE,cliente.apellido AS M_APELLIDO,cliente.idcliente AS M_IDCLIENTE,NUMCERT AS NUMAUT,INITCAP (venta.IDUSER) AS USERINIT, NVL (reserva.idcliente,  0 ) AS RES,  venta.iduser AS M_IDUSER,  TO_CHAR ( FECHAVENTA,  'mon/dd/yyyy', " +
                "        'NLS_DATE_LANGUAGE=English' ) AS M_FECHA,  venta.idcampania, venta.idstatus AS IDSTATUS, ( SELECT  TO_CHAR (   fecha, 'mon/dd/yyyy', 'NLS_DATE_LANGUAGE=English' " +
                "         ) AS fecha  FROM  m4cclientenotas  WHERE   idtiponota = 6   AND idcliente = cliente.idcliente " +
                "        AND ROWNUM = 1 ) AS FECHA_WELCOME    FROM   m4cliente cliente  INNER JOIN M4Cventa venta ON venta.idcliente = cliente.IDCLIENTE  AND venta.IDCAMPANIA IN ("+campaings+") " +
                "      LEFT JOIN (  SELECT DISTINCT  idcliente   FROM    m4creservacion where status=10 ) reserva ON reserva.idcliente = cliente.IDCLIENTE LEFT JOIN (   SELECT DISTINCT  idcliente " +
                "       FROM  m4cclientenotas  WHERE  idtiponota = 6 ) notas ON notas.idcliente = cliente.idcliente  "+statusFilter+")  ) customer ");

        return queryBuilder;
    }
    public StringBuilder getBaseSQLQueryMinified(StringBuilder campaings){
        StringBuilder queryBuilder=new StringBuilder();

        queryBuilder.append("/* Query busqueda de lead por status */ " +
                "  SELECT distinct venta.idbooking AS MV_IDBOOKING,\n" +
                "  cliente.nombre as  M_IDUSER, \n" +
                "  cliente.apellido AS M_APELLIDO, \n" +
                "  cliente.idcliente AS M_IDCLIENTE, \n" +
                "  venta.iduser AS M_IDUSER, \n" +
                "  venta.idcampania,\n" +
                "  venta.idstatus AS IDSTATUS,\n" +
                "  (SELECT To_char (fecha, 'mon/dd/yyyy', \n" +
                "                       'NLS_DATE_LANGUAGE=English') AS \n" +
                "                       fecha \n" +
                "                FROM   m4cclientenotas \n" +
                "                WHERE  idtiponota = 6 \n" +
                "                       AND idcliente = cliente .idcliente \n" +
                "                       AND ROWNUM = 1) \n" +
                "               AS \n" +
                "                      FECHA_WELCOME\n" +
                "   from m4cliente cliente\n" +
                "   inner join M4Cventa venta\n" +
                "   on venta.idcliente=cliente.IDCLIENTE and venta.IDCAMPANIA in ("+campaings+")\n" +
                "   left JOIN (select distinct idcliente from m4creservacion )reserva\n" +
                "   on reserva.idcliente=cliente.IDCLIENTE\n" +
                "   left JOIN (select distinct idcliente from m4cclientenotas \n" +
                "   where idtiponota=6 ) notas\n" +
                "   on notas.idcliente= cliente.idcliente");
        return queryBuilder;
    }

    public List<ResultbyDateDTO> ReportWelcomeCall(StringBuilder campaings, StringBuilder from,
                                          StringBuilder to, String by){
        StringBuilder queryBuilder=new StringBuilder();
        queryBuilder.append("SELECT *from(SELECT ROWNUM  r,a.* FROM(");
        if(by.equals("WELCOMECALL")){
            /* Opción 2*/
            queryBuilder.append("SELECT (case when notas.idcliente is null then to_char(cliente.FECHA,'-yyyy-mm') "+
                    " else to_char(cliente.FECHA,'yyyy-mm') end) DATEGROUP, count(*) AS total " +
                    "from m4cliente cliente " +
                    "inner join M4Cventa venta " +
                    "on venta.idcliente=cliente.IDCLIENTE and venta.IDCAMPANIA in ("+campaings+") " +
                    "left join ( Select distinct idcliente from M4CCLIENTENOTAS  where " +
                    "IDTIPONOTA in ( 6, 105, 128)  ) notas " +
                    "on notas.IDCLIENTE=cliente.IDCLIENTE ");
            if(from.length() > 0){
                queryBuilder.append("where cliente.FECHA >= TO_DATE('"+from+"', 'yyyy-mm-dd') "+
                        "and cliente.FECHA <= TO_DATE('"+to+"', 'yyyy-mm-dd')+1 ");
            }
            queryBuilder.append(" GROUP BY (case when notas.idcliente is null then  to_char(cliente.FECHA,'-yyyy-mm') "+
            "else to_char(cliente.FECHA,'yyyy-mm') end) order by DATEGROUP desc)a )b\n" +
                    "WHERE\n" +
                    "\tr <= 20  ");

        }else{
            /* Opción 2*/
            queryBuilder.append("SELECT (case when reserva.fecha_reservacion is null then 'Sin Reserva' "+
                    " else to_char(reserva.fecha_reservacion,'yyyy-mm') end) DATEGROUP, count(*) AS total " +
                    "from m4cliente cliente " +
                    "inner join M4Cventa venta " +
                    "on venta.idcliente=cliente.IDCLIENTE and venta.IDCAMPANIA in ("+campaings+") " +
                    " INNER JOIN (\n" +
                    "select inn.* from (\n" +
                    "select reserva1.*, (ROW_NUMBER() OVER (PARTITION BY idcliente order by fecha_reservacion)) as rank\n" +
                    "from m4creservacion reserva1 where reserva1.STATUS=10\n" +
                    ") inn where inn.rank=1\n" +
                    ") reserva on reserva.idcliente=cliente.idcliente");
            if(from.length() > 0){
                queryBuilder.append(" WHERE fecha_reservacion >= To_date('"+from+"', 'yyyy-mm-dd') "+
                        "AND fecha_reservacion <= To_date('"+to+"', 'yyyy-mm-dd')+1 ");
            }
            queryBuilder.append(" group by (case when reserva.fecha_reservacion is null then 'Sin Reserva' "+
                    "else to_char(reserva.fecha_reservacion,'yyyy-mm') end) order by DATEGROUP desc) a )b\n" +
                    "WHERE\n" +
                    "\tr <= 20");
        }

        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString())
                .addScalar("DATEGROUP", StringType.INSTANCE)
                .addScalar("TOTAL", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultbyDateDTO.class));
        List<ResultbyDateDTO> resultbyDateDTOList=query.list();
        return resultbyDateDTOList;
    }

    public StringBuilder getSQLPropertiesPreview(){

        StringBuilder queryBuilder=new StringBuilder();

        queryBuilder.append("SELECT ");
        queryBuilder.append("ROWNUM AS rn,customer.* from ( ");
        queryBuilder.append("SELECT *from(");
        queryBuilder.append("SELECT ");
        queryBuilder.append("c.idcliente,concat(c.nombre,concat(' ',c.apellido)) firstName, ");
        queryBuilder.append("c.email, ");
        queryBuilder.append("cav.campania campaign,");
        queryBuilder.append("cav.slug,");
        queryBuilder.append("cav.idbooking,");
        queryBuilder.append("cav.NUMCERT, ");
        queryBuilder.append("cav.ARCHIVO, ");
        queryBuilder.append("c.FECHAUPDATE dateupdated, ");
        queryBuilder.append("cav.CALLCENTER_NAME,");
       // queryBuilder.append("c.FECHA datecreated, ");
        queryBuilder.append("c.idcallcenter, ");
        queryBuilder.append("cav.IDUSER, ");
        queryBuilder.append("cav.IDCAMPANIA, ");
        queryBuilder.append("cav.OFFERID, ");
        queryBuilder.append("CAV.reservationgroup, ");
        queryBuilder.append("cav.TIPOCERT, ");
        queryBuilder.append("cav.FECHAVENTA, ");
        queryBuilder.append("(select ST.status from m4ccatstatus st where ST.tipo=1 and ST.IDSTATUS=cav.IDSTATUS) as mv_status, ");
        queryBuilder.append("CAV.IDSTATUS, ");
        queryBuilder.append("nvl((select idreservacion from m4creservacion where idcliente=c.idcliente and rownum=1 AND STATUS!=7),'') as M_RESERVACION, ");
        queryBuilder.append("nvl((select STATUS from m4creservacion where idcliente=c.idcliente and rownum=1 AND STATUS!=7),'') as IDSTATUSRESERVA ");

        return queryBuilder;
    }
    public StringBuilder getSQLCount(){

        StringBuilder queryBuilder=new StringBuilder();
        queryBuilder.append("SELECT ");
        queryBuilder.append("count(*) count from( select * from( select ");
        queryBuilder.append("concat(c.nombre,concat(' ',c.apellido)) firstName, ");
        queryBuilder.append("c.email email, ");
        queryBuilder.append("cav.campania campaign,");
        queryBuilder.append("cav.slug,");
        queryBuilder.append("cav.idbooking,");
        queryBuilder.append("cav.NUMCERT,c.idcallcenter, ");
        queryBuilder.append("cav.IDUSER, ");
        queryBuilder.append("cav.IDCAMPANIA, ");
        queryBuilder.append("cav.CALLCENTER_NAME, ");
        queryBuilder.append("cav.OFFERID, ");
        queryBuilder.append("CAV.reservationgroup, ");
        queryBuilder.append("cav.TIPOCERT, ");
        queryBuilder.append("cav.FECHAVENTA, ");
        queryBuilder.append("(select ST.status from m4ccatstatus st where ST.tipo=1 and ST.IDSTATUS=cav.IDSTATUS) as mv_status, ");
        queryBuilder.append("CAV.IDSTATUS, ");
        queryBuilder.append("nvl((select idreservacion from m4creservacion where idcliente=c.idcliente and rownum=1 AND STATUS!=7),'') as M_RESERVACION, ");
        queryBuilder.append("nvl((select STATUS from m4creservacion where idcliente=c.idcliente and rownum=1 AND STATUS!=7),'') as IDSTATUSRESERVA ");


        return  queryBuilder;
    }
    public StringBuilder getSQLCountExtra(){

        StringBuilder queryBuilder=new StringBuilder();
        queryBuilder.append("SELECT ");
        queryBuilder.append("count(*) count from( ");

        return  queryBuilder;
    }
    public void setSQLPropertiesPreview(SQLQuery query){

        query.addScalar("FIRSTNAME", StringType.INSTANCE);
        query.addScalar("IDCLIENTE", LongType.INSTANCE);
        query.addScalar("CAMPAIGN", StringType.INSTANCE);
        query.addScalar("IDBOOKING", LongType.INSTANCE);
        query.addScalar("NUMCERT", StringType.INSTANCE);
        query.addScalar("IDUSER", StringType.INSTANCE);
        query.addScalar("RESERVATIONGROUP", StringType.INSTANCE);
        query.addScalar("DATEUPDATED", DateType.INSTANCE);
        query.addScalar("FECHAVENTA", DateType.INSTANCE);
        query.addScalar("IDCAMPANIA", LongType.INSTANCE);
        query.addScalar("OFFERID", LongType.INSTANCE);
        query.addScalar("TIPOCERT", LongType.INSTANCE);
        query.addScalar("IDSTATUS", LongType.INSTANCE);
        query.addScalar("MV_STATUS", StringType.INSTANCE);
        query.addScalar("M_RESERVACION", LongType.INSTANCE);
        query.addScalar("IDSTATUSRESERVA", LongType.INSTANCE);
        query.addScalar("ARCHIVO", StringType.INSTANCE);

    }
    public void setSQLPropertiesPreviewExtra(SQLQuery query){

        query.addScalar("M_APELLIDO", StringType.INSTANCE);
        query.addScalar("RN", StringType.INSTANCE);
        //query.addScalar("M_EMAIL", StringType.INSTANCE);
        //query.addScalar("M_ESTADO", StringType.INSTANCE);
        query.addScalar("M_FECHA", StringType.INSTANCE);
       // query.addScalar("M_IDCALLCENTER", StringType.INSTANCE);
        query.addScalar("M_IDCLIENTE", LongType.INSTANCE);
        query.addScalar("M_IDUSER", StringType.INSTANCE);
        query.addScalar("M_NOMBRE", StringType.INSTANCE);
       // query.addScalar("CIUDAD", StringType.INSTANCE);
        query.addScalar("MV_IDBOOKING", LongType.INSTANCE);
       // query.addScalar("MV_CONSULTOR", StringType.INSTANCE);
        //query.addScalar("MV_IDSTATUS", LongType.INSTANCE);
        query.addScalar("NUMAUT", StringType.INSTANCE);
        //query.addScalar("STATUSVENTA", StringType.INSTANCE);
        query.addScalar("RES", LongType.INSTANCE);
        query.addScalar("USERINIT", StringType.INSTANCE);
        //query.addScalar("ECERT", StringType.INSTANCE);
        //query.addScalar("VENTA_STATUS", StringType.INSTANCE);
        query.addScalar("IDSTATUS", StringType.INSTANCE);
        query.addScalar("FECHA_WELCOME", StringType.INSTANCE);
        query.addScalar("IDCAMPANIA", LongType.INSTANCE);

    }
    public void  refresh(Customer customer){
        currentSession().refresh(customer);
    }

    public void  flush(){

        currentSession().flush();

    }
    public void commit(){
        currentSession().getTransaction().commit();
    }
    public StringBuilder createSQLOrderByExtra(StringBuilder queryBuilder,String direction,String orderBy){

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
}
