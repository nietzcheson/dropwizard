package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4sg.crm4marketingsunset.core.CommisionReportBean;
import com.m4sg.crm4marketingsunset.core.dto.ResultbyDateDTO;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by Fernando on 12/09/2016.
 */
public class CommisionReportDAO extends GenericDAO{


    public  List<CommisionReportBean> reportCobranza(Date beginning,Date ending,String username){

        StringBuilder query=new StringBuilder();

        query.append("SELECT   (CASE ");
        query.append("             WHEN (   UPPER (m.iduser) IN (:IDUSER) ");
        query.append("                   OR UPPER (m.idusercomision) IN (:IDUSER) ");
        query.append("                  ) ");
        query.append("                THEN 'COBRANZA' ");
        query.append("             WHEN (m.importe < 0 AND UPPER (m.numautorizacion) != 'TEF') ");
        query.append("                THEN 'CXL' ");
        query.append("             WHEN (m.importe < 0 AND UPPER (m.numautorizacion) = 'TEF') ");
        query.append("                THEN 'TEFS' ");
        query.append("             WHEN (m.idpago = 1 OR m.iduser IN (SELECT iduser ");
        query.append("                                                  FROM m4cusuarios_deptos ");
        query.append("                                                 WHERE iddepto = ',26,27,')) ");
        query.append("                THEN 'VENTAS' ");
        query.append("             ELSE 'VARIOS' ");
        query.append("          END ");
        query.append("         ) AS depto, ");
        query.append("         trunc(m.fecha) AS fecha, ");
        query.append("         to_number(m.idbooking) AS idbooking, ");
        query.append("         (SELECT CASE ");
        query.append("                    WHEN (m1.idcallcenter = 341 AND idcampania != 39 ");
        query.append("                         ) ");
        query.append("                       THEN 'VACATION UNLIMITED MEX' ");
        query.append("                    WHEN (m1.idcallcenter = 341 AND idcampania = 39) ");
        query.append("                       THEN 'VACATION UNLIMITED MEX SPA' ");
        query.append("                    ELSE nombre ");
        query.append("                 END ");
        query.append("            FROM m4c.m4ccallcenter ");
        query.append("           WHERE idcallcenter = m1.idcallcenter) AS callcenter, ");
        query.append("         (CASE ");
        query.append("             WHEN (2 = 2) ");
        query.append("                THEN CASE ");
        query.append("                       WHEN (m.idusercomision IS NOT NULL) ");
        query.append("                          THEN m.idusercomision ");
        query.append("                       ELSE m.iduser ");
        query.append("                    END ");
        query.append("             ELSE m.iduser ");
        query.append("          END ");
        query.append("         ) AS iduser, ");

        query.append("         case when m.moneda =0 then  round(m.importe/ntipocambio) else m.importe end as importe, ");
        query.append("         m1.idcallcenter AS idcallcenter ");
        query.append("    FROM m4c.m4cpagosservicios m, m4c.m4cventa m2, m4c.m4cliente m1 ");
        query.append("   WHERE m.idbooking = m2.idbooking ");
        query.append("     AND m2.idcliente = m1.idcliente ");
        query.append("     AND numautorizacion IS NOT NULL ");
        query.append("     AND (   m1.idcallcenter IN (SELECT idcallcenter ");
        query.append("                                   FROM m4ccallcenter ");
        query.append("                                  WHERE ingresos = 1) ");
        query.append("          OR (    m1.idcallcenter IN (SELECT idcallcenter ");
        query.append("                                        FROM m4ccallcenter ");
        query.append("                                       WHERE ingresos = 0) ");
        query.append("              AND m.iduser NOT IN (SELECT iduser ");
        query.append("                                     FROM m4ccertslogin@m4cpublic) ");
        query.append("             ) ");
        query.append("         ) ");
        query.append("     AND trunc(m.fecha) between :FECHA_INICIO and :FECHA_FIN ");

        query.append("     AND (   m.iduser IN (:IDUSER) ");
        query.append("          OR m.idusercomision IN (:IDUSER) ");
        query.append("         ) ");
        query.append("UNION ");
        query.append("SELECT   (CASE ");
        query.append("             WHEN (   UPPER (m.iduser) IN (:IDUSER) ");
        query.append("                   OR UPPER (m.idusercomision) IN (:IDUSER) ");
        query.append("                  ) ");
        query.append("                THEN 'COBRANZA' ");
        query.append("             WHEN (m.importe < 0 AND UPPER (m.numautorizacion) != 'TEF') ");
        query.append("                THEN 'CXL' ");
        query.append("             WHEN (m.importe < 0 AND UPPER (m.numautorizacion) = 'TEF') ");
        query.append("                THEN 'TEFS' ");
        query.append("             ELSE 'RESERVAS' ");
        query.append("          END ");
        query.append("         ) AS depto, ");
        query.append("         trunc(m.fecha) AS fecha, ");
        query.append("         to_number(m2.idreservacion) AS idbooking, ");
        query.append("         (SELECT nombre ");
        query.append("            FROM m4c.m4ccallcenter ");
        query.append("           WHERE idcallcenter = m1.idcallcenter) AS callcenter, ");
        query.append("         (CASE ");
        query.append("             WHEN (2 = 2) ");
        query.append("                THEN m.idusercomision ");
        query.append("             ELSE m.iduser ");
        query.append("          END) AS iduser,  ");
        query.append("case when m.moneda =0 then  round(m.importe/ntipocambio) else m.importe end as importe, ");
        query.append("         m1.idcallcenter AS idcallcenter ");
        query.append("    FROM m4c.m4crespagos m, m4c.m4creservacion m2, m4c.m4cliente m1 ");
        query.append("   WHERE m.idreservacion = m2.idreservacion ");
        query.append("     AND m2.idcliente = m1.idcliente ");
        query.append("     AND numautorizacion IS NOT NULL ");
        query.append("     AND trunc(m.fecha) between  :FECHA_INICIO and :FECHA_FIN ");

        query.append("     AND (   m.iduser IN (:IDUSER) ");
        query.append("          OR m.idusercomision IN (:IDUSER) ");
        query.append("         ) ");
        query.append("ORDER BY 5, 2 DESC, 4, 1 ");


        SQLQuery q=this.currentSession().createSQLQuery(query.toString());

        q.setResultTransformer(Transformers.aliasToBean(CommisionReportBean.class));

        q.setParameter("FECHA_INICIO",beginning);
        q.setParameter("FECHA_FIN",ending);
        q.setParameter("IDUSER",username);

        q.addScalar("IDBOOKING", IntegerType.INSTANCE);
        q.addScalar("DEPTO", StringType.INSTANCE);
        q.addScalar("FECHA", DateType.INSTANCE);
        q.addScalar("CALLCENTER", StringType.INSTANCE);
        q.addScalar("IDCALLCENTER", LongType.INSTANCE);
        q.addScalar("IDUSER", StringType.INSTANCE);
        q.addScalar("IMPORTE", DoubleType.INSTANCE);

        List<CommisionReportBean> commisionReportBeanLst=q.list();


        return commisionReportBeanLst;
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public Optional find(long id) {
        return null;
    }

    @Override
    public Object saveOrUpdate(Object entity) {
        return null;
    }

    public CommisionReportDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


}
