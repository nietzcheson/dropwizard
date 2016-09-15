package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.BatchCertificates;
import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.FoliosCertificado;
import com.m4c.model.entity.online.Certificate;
import com.m4sg.crm4marketingsunset.core.dto.AllocateBatchDTO;
import com.m4sg.crm4marketingsunset.core.dto.CertificatesFreeDTO;
import com.m4sg.crm4marketingsunset.core.dto.CountFoliosDTO;
import com.m4sg.crm4marketingsunset.services.SaleService;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

/**
 * Created by sistemas on 09/01/2015.
 */
public class CertificateDAO extends GenericDAO<Certificate> {
    public CertificateDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    public List<Certificate> findAll() {
        return null;
    }

    @Override
    public Optional<Certificate> find(long id) {

        return null;
    }
    public Optional<Certificate> find(String id) {
        return Optional.fromNullable(get(id));
    }
    public List<Certificate> findByNumber(String number){
        return criteria().add(Restrictions.eq("number",number)).list();
    }
    public List<Certificate> findByCert(String number, Long[] campaignList, int limit){
        Criteria criteria=criteria().add(Restrictions.and(Restrictions.ilike("number", "%"+number+"%"),Restrictions.isNull("bookingId")));
        if(campaignList.length > 0 )
            criteria.add(Restrictions.in("campaign", campaignList));
        criteria.setMaxResults(limit);
        return criteria.list();
    }
    public Optional<Certificate> findByIdBooking(Long idBooking){
        return Optional.fromNullable((Certificate) criteria().add(Restrictions.eq("bookingId", idBooking)).uniqueResult());
    }
    public Optional<Certificate> findbySequenceBatch(BatchCertificates batchCertificates, Integer sequence){
        return Optional.fromNullable((Certificate) criteria().add(Restrictions.and(Restrictions.eq("batchId", batchCertificates), Restrictions.eq("sequenceBatch", sequence))).uniqueResult());
    }
    public Integer updateFoliosCertificado(Long lote_id, AllocateBatchDTO allocateBatchDTO, Long folio_id, Long campaign){

        StringBuilder query=new StringBuilder();
        query.append("UPDATE m4ccertspublic@M4CPUBLIC SET FOLIOSCERTIFICADO_ID=:FOLIO_ID, ");
        query.append(" IDCAMP=:CAMP_ID ");
        query.append("WHERE LOTE_ID =:LOTE_ID ");
        query.append("AND CONSECUTIVO_LOTE BETWEEN :INICIO ");
        query.append("AND :FIN");

        SQLQuery sqlQuery = currentSession().createSQLQuery(query.toString());
        sqlQuery.setParameter("FOLIO_ID",folio_id);
        sqlQuery.setParameter("CAMP_ID",campaign);
        sqlQuery.setParameter("LOTE_ID",lote_id);
        sqlQuery.setParameter("INICIO",allocateBatchDTO.getStart());
        sqlQuery.setParameter("FIN",allocateBatchDTO.getEnd());
        Integer i = sqlQuery.executeUpdate();
        return i;
    }
    public List<Certificate> findFree(String number){

        return criteria().add(Restrictions.ilike("number",number)).add(Restrictions.isNull("bookingId")).list();
    }
    @Override
    public Certificate saveOrUpdate(Certificate entity) {
        this.currentSession().saveOrUpdate(entity);
        this.currentSession().flush();
        return entity;
    }

    public  List<Certificate> findByIdFolioCertificado(Long idFolioCertificado){

        List<Certificate> certificateList=criteria().add(Restrictions.eq("folioCertificadoId", idFolioCertificado)).list();

        return certificateList;
    }

    public String findFreeGeneric(Campaign campaign){

        List<Long> folioCertificateIdLst;
        String freeCertificate="";

        StringBuilder query=new StringBuilder();
        SQLQuery q;
        Certificate certificate;
        folioCertificateIdLst=extract(campaign.getFoliosCertificado(), on(FoliosCertificado.class).getId());


        query.append("SELECT * ");//Toma certificados con Fecha de asignación pasados  10 min o con Fecha de asignación nulas
        query.append("FROM m4ccertspublic\\@M4CPUBLIC WHERE ");
        //query.append("FOLIOSCERTIFICADO_ID in(:FOLIOSCERTIFICADO_ID) ");
//        query.append("FOLIOSCERTIFICADO_ID in(").append(StringUtils.join(folioCertificateIdLst,",")).append(")");
        query.append("idcamp = ").append(campaign.getId());
        query.append(" AND idbooking IS NULL ");
        query.append("AND (SYSDATE - 10 / 1440 >= fechaasignacion OR fechaasignacion IS NULL) ");
        query.append("AND ROWNUM =1 ");

        q=currentSession().createSQLQuery(query.toString());


        q.addEntity(Certificate.class);
        certificate =(Certificate)q.uniqueResult();

        if(certificate!=null) {
            updateAssignationDate(certificate);
            freeCertificate=certificate.getNumber();
        }
        return freeCertificate;

    }

    public void updateAssignationDate(Certificate certificate){

        StringBuilder query=new StringBuilder();

        query.append("update M4CCERTSPUBLIC\\@M4CPUBLIC set FECHAASIGNACION=sysdate where NUMCERT=:NUMCERT");

        SQLQuery q=this.currentSession().createSQLQuery(query.toString());
        q.setParameter("NUMCERT",certificate.getNumber());
        q.executeUpdate();

    }
    public Optional<Certificate> findAvailableCertificateByCampaign(Campaign campaign,List<String>ignoreCerts){


        StringBuilder hql=new StringBuilder();
        hql.append("select c from Certificate c ");
        hql.append("where c.campaign=:IDCAMPAIGN and idbooking is null ");

        if(ignoreCerts!=null && !ignoreCerts.isEmpty()){
        hql.append("and number not in (:CERTS)");
        }

        org.hibernate.Query query=currentSession().createQuery(hql.toString());
        query.setParameter("IDCAMPAIGN",campaign.getId());

        if(ignoreCerts!=null && !ignoreCerts.isEmpty()) {
            query.setParameterList("CERTS", ignoreCerts);
        }

        query.setMaxResults(1);

        Certificate certificate= (Certificate) query.uniqueResult();

        if(certificate!=null && new SaleService().existsNumcert(certificate.getNumber())){

            if(ignoreCerts==null){ignoreCerts=new ArrayList<String>();}

            ignoreCerts.add(certificate.getNumber());
            findAvailableCertificateByCampaign(campaign,ignoreCerts);
        }

        return Optional.fromNullable(certificate);


    }
    public  List<Certificate> findByLoteId(BatchCertificates loteId){

        List<Certificate> certificateList=criteria().add(Restrictions.eq("batchId", loteId)).list();

        return certificateList;
    }

    public List<CountFoliosDTO> countFolios(Long idcampania){
        StringBuilder queryBuilder=new StringBuilder();
        queryBuilder.append("select (case when idbooking is not null ");
        queryBuilder.append(" then 'UNAVAILABLE' else 'AVAILABLE' end) ");
        queryBuilder.append(" as status, count(*) as counter ");
        queryBuilder.append(" from m4ccertspublic@M4CPUBLIC ");
        queryBuilder.append(" where idcamp= ");
        queryBuilder.append(idcampania);
        queryBuilder.append(" group by (case when idbooking ");
        queryBuilder.append(" is not null then 'UNAVAILABLE'  ");
        queryBuilder.append(" else 'AVAILABLE' end)  ");
        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString())
                .addScalar("status", StringType.INSTANCE)
                .addScalar("counter", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(CountFoliosDTO.class));
        List<CountFoliosDTO> countFoliosDTOList=query.list();
        return countFoliosDTOList;
    }
    public List<CertificatesFreeDTO> getFoliosbyActiveCampaigns(Date from, Date to){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder queryBuilder=new StringBuilder();
        queryBuilder.append("select CAMPANIA_ID,campania.campania as CAMPANIA_NAME, COUNTER from( ");
        queryBuilder.append("select certs.idcamp as CAMPANIA_ID, ");
        queryBuilder.append("count(*) as COUNTER from M4CVENTA venta ");
        queryBuilder.append("inner join m4ccertspublic@M4CPUBLIC certs ");
        queryBuilder.append("on certs.idcamp=venta.IDCAMPANIA ");
        queryBuilder.append("where venta.FECHA > TO_DATE('" );
        queryBuilder.append(format.format(from));
        queryBuilder.append("', 'yyyy-mm-dd') ");
        queryBuilder.append("and venta.FECHA <  TO_DATE('"+format.format(to)+"', 'yyyy-mm-dd') ");
        queryBuilder.append("and certs.idbooking is null ");
        queryBuilder.append("group by certs.idcamp ");
        queryBuilder.append("order by count(*)) ");
        queryBuilder.append("inner join m4ccatcampana campania ");
        queryBuilder.append("on campania.IDCAMPANIA = CAMPANIA_ID ");
        queryBuilder.append("where counter < 50 ");
        SQLQuery query=currentSession().createSQLQuery(queryBuilder.toString())
                .addScalar("CAMPANIA_ID", LongType.INSTANCE)
                .addScalar("CAMPANIA_NAME", StringType.INSTANCE)
                .addScalar("COUNTER", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(CertificatesFreeDTO.class));
        List<CertificatesFreeDTO> certificatesFreeDTOList=query.list();
        return certificatesFreeDTOList;
    }
    public void flushSession(){
        currentSession().flush();
    }

}
