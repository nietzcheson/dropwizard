package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Payment;
import com.m4sg.crm4marketingsunset.core.dto.PaymentDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaymentServiceResponseDTO;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 21/07/2015.
 */
public class PaymentDAO extends GenericDAO<Payment> {


    public PaymentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Payment> findAll() {
        return null;
    }

    @Override
    public Optional<Payment> find(long id) {
        return null;
    }
    public Optional<Payment> find(long idBooking,long idPago) {

        Payment payment= (Payment) criteria().add(Restrictions.eq("booking", idBooking)).add(Restrictions.eq("payment",idPago)).uniqueResult();

        return Optional.fromNullable(payment);
    }

    public List<Payment> findByIdBooking(long idBooking){

        return criteria().add(Restrictions.eq("pk.idBooking",idBooking)).list();

    }

    public void delete(Payment payment) {

        currentSession().delete(payment);
    }

    public Long findMaxByIdBooking(long idBooking){

        Criteria criteria=currentSession().createCriteria(Payment.class);
        criteria.add(Restrictions.eq("pk.idBooking", idBooking));
        criteria.addOrder(Order.desc("pk.idPayment"));
        criteria.setMaxResults(1);
        Payment payment = (Payment) criteria.uniqueResult();
        Long max=0l;
        if(payment!=null){
            max=payment.getPayment();
        }

        return max+1;
    }

    public PaymentDTO detail(long idBooking,long idPago,String username){


        StringBuilder query=new StringBuilder();
        SQLQuery nativeQuery;
        List<Long> excludeIdServices=new ArrayList<Long>();
//region Query :: Obtiene los servicios incluidos en el  pago
        query.append("SELECT *  ");

        query.append("FROM   (SELECT ps.idpago,  ");
        query.append("               ps.idbooking,  ");
        query.append("               ps.banco,  ");
        query.append("               ps.importe,  ");
        query.append("               ps.moneda,  ");
        query.append("               ps.ntipocambio,  ");
        query.append("               cast(ps.numautorizacion as varchar2(100))numautorizacion,  ");
        query.append("               TO_CHAR(ps.fecha,'yyyy-MM-dd') fecha,  ");
        query.append("               ps.iduser,  ");
        query.append("               ps.idusercomision,  ");
        query.append("               Nvl(tabla.campo7, (SELECT nombre  ");
        query.append("                                  FROM   m4cventa v  ");
        query.append("                                         inner join m4cliente c  ");
        query.append("                                                 ON v.idcliente = c.idcliente  ");
        query.append("                                  WHERE  idbooking = ps.idbooking  ");
        query.append("                                         AND ROWNUM = 1))name,  ");
        query.append("               Nvl(tabla.campo8, (SELECT apellido  ");
        query.append("                                  FROM   m4cventa v  ");
        query.append("                                         inner join m4cliente c  ");
        query.append("                                                 ON v.idcliente = c.idcliente  ");
        query.append("                                  WHERE  idbooking = ps.idbooking  ");
        query.append("                                         AND ROWNUM = 1))lastname,  ");
        query.append("               tabla.campo3 tarjeta,  ");
        query.append("               tabla.campo6 cvv,  ");
        query.append("               tabla.campo5 exp,  ");
        query.append("               tabla.campo4 tipo  ");
        query.append("        FROM   m4c.m4cpagosservicios ps  ");
        query.append("               left join m4ctabla tabla  ");
        query.append("                      ON ps.idbooking = tabla.campo1  ");
        query.append("                         AND ps.idpago = tabla.campo2  ");
        query.append("        WHERE  idreservacion IS NULL  ");
        query.append("               AND idbooking = :IDBOOKING) pago  ");
        query.append("       left join (SELECT sp.idbooking idbookingservicio,  ");
        query.append("                         sp.idpago idpagoservicio,  ");
        query.append("                         sc.idservcontratado,  ");
        query.append("                         ss.adultos,  ");
        query.append("                         sc.importe             costoservicio,  ");
        query.append("                         sp.importe             pagado,  ");
        query.append("                         (SELECT CASE  ");
        query.append("                                   WHEN Nvl(sc1.importe - Nvl(SUM(sp1.importe),  ");
        query.append("                                                          0), 0)  ");
        query.append("                                        <= 0 THEN  ");
        query.append("                                   0  ");
        query.append("                                   ELSE Round(Nvl(sc1.importe -  ");
        query.append("                                                  Nvl(SUM(sp1.importe), 0  ");
        query.append("                                                  ), 0), 2)  ");
        query.append("                                 END  ");
        query.append("                          FROM   m4cservicioscontratados sc1  ");
        query.append("                                 left join m4cserviciospagados sp1  ");
        query.append("                                        ON sc1.idbooking = sp1.idbooking  ");
        query.append("                                           AND sc1.idservcontratado =  ");
        query.append("                                               sp1.idservcontratado  ");
        query.append("                          WHERE  sc1.idbooking = :IDBOOKING  ");
        query.append("                                 AND sc1.idservcontratado = sp.idservcontratado  ");
        query.append("                                 AND sc1.idreservacion IS NULL  ");
        query.append("                          GROUP  BY sc1.idservcontratado,  ");
        query.append("                                    sc1.importe)remaining,  ");
        query.append("                         sc.iduser              usernameservicio,  ");
        query.append("                         sc.descripcion         servicio,  ");
        query.append("                         ss.descripcion         subservicio  ");
        query.append("                  FROM   ( m4cserviciospagados sp  ");
        query.append("                           inner join m4cservicioscontratados sc  ");
        query.append("                                   ON sp.idbooking = sc.idbooking  ");
        query.append("                                      AND sp.idservcontratado =  ");
        query.append("                                          sc.idservcontratado  ");
        query.append("                                      AND sp.idreservacion IS NULL  ");
        query.append("                           inner join m4csubservicios ss  ");
        query.append("                                   ON ss.idsubservicio = sc.idsubservicio ))  ");
        query.append("                 servicios  ");
        query.append("              ON pago.idbooking = servicios.idbookingservicio  ");
        query.append("                 AND pago.idpago = servicios.idpagoservicio  ");
        query.append("WHERE  pago.idpago=:IDPAGO ");

//endregion

        nativeQuery=currentSession().createSQLQuery(query.toString());

        nativeQuery.setParameter("IDPAGO", idPago);
        nativeQuery.setParameter("IDBOOKING", idBooking);
//region Scalar
        nativeQuery.addScalar("IDPAGO", LongType.INSTANCE);
        nativeQuery.addScalar("IDBOOKING", LongType.INSTANCE);
        nativeQuery.addScalar("BANCO", LongType.INSTANCE);
        nativeQuery.addScalar("IMPORTE", DoubleType.INSTANCE);
        nativeQuery.addScalar("MONEDA", LongType.INSTANCE);
        nativeQuery.addScalar("NTIPOCAMBIO", DoubleType.INSTANCE);
        nativeQuery.addScalar("NUMAUTORIZACION", StringType.INSTANCE);
        nativeQuery.addScalar("FECHA", StringType.INSTANCE);
        nativeQuery.addScalar("IDUSER", StringType.INSTANCE);
        nativeQuery.addScalar("IDUSERCOMISION", StringType.INSTANCE);
        nativeQuery.addScalar("NAME", StringType.INSTANCE);
        nativeQuery.addScalar("LASTNAME", StringType.INSTANCE);
        nativeQuery.addScalar("TARJETA", StringType.INSTANCE);
        nativeQuery.addScalar("CVV", StringType.INSTANCE);
        nativeQuery.addScalar("EXP", StringType.INSTANCE);
        nativeQuery.addScalar("TIPO", StringType.INSTANCE);
        nativeQuery.addScalar("IDSERVCONTRATADO", LongType.INSTANCE);
        nativeQuery.addScalar("ADULTOS", IntegerType.INSTANCE);
        nativeQuery.addScalar("COSTOSERVICIO", DoubleType.INSTANCE);
        nativeQuery.addScalar("PAGADO", DoubleType.INSTANCE);
        nativeQuery.addScalar("REMAINING", DoubleType.INSTANCE);
        nativeQuery.addScalar("USERNAMESERVICIO", StringType.INSTANCE);
        nativeQuery.addScalar("SERVICIO", StringType.INSTANCE);
        nativeQuery.addScalar("SUBSERVICIO", StringType.INSTANCE);
//endregion

        List<Object[]> resultLst=nativeQuery.list();
        PaymentDTO paymentDTO=null;

        for(Object[] row:resultLst){

            if(paymentDTO==null){
                paymentDTO=new PaymentDTO(
                        (Long)row[2],(Double)row[3],
                        (Long)row[4],(Double)row[5],
                        (String)row[6],(String)row[7],(String)row[8],
                        (String)row[9],(String)row[10],(String)row[11],
                        (String)row[12],(String)row[13],(String)row[14],
                        (String) row[15],username
                );
            }

            if (row[16]!=null) {
                excludeIdServices.add((Long) row[16]);
                paymentDTO.getProvidedServicesDTO().add(
                        new PaymentServiceResponseDTO(
                                (Long)row[1], (Long)row[16], (Integer) row[17],
                                (Double) row[18], (Double) row[19], (Double) row[20],
                                (String) row[21], (String) row[22], (String) row[23],
                                true));
            }
        }
        if(paymentDTO!=null) {
            paymentDTO.getProvidedServicesDTO().addAll(getPendigPaymentsByIdBooking(idBooking, excludeIdServices));
        }

        return paymentDTO;

    }

    public List<PaymentServiceResponseDTO> getPendigPaymentsByIdBooking(Long idBooking,List<Long>excludeIdServices){

        StringBuilder query=new StringBuilder();
        SQLQuery nativeQuery;
        List<PaymentServiceResponseDTO>paymentServiceResponseDTOLst=new ArrayList<PaymentServiceResponseDTO>();
//region Query :: Obtiene los Servicios con adeudo

        query.append("select *from (");
        query.append("SELECT sc.idservcontratado,  ");
        query.append("       ss.adultos,  ");
        query.append("       sc.importe,  ");
        query.append("       cast(Nvl(SUM(sp.importe), 0 )as number ) pagado,  ");
        query.append("       cast(CASE  ");
        query.append("         WHEN Nvl(sc.importe - Nvl(SUM(sp.importe), 0), 0) <= 0 THEN 0  ");
        query.append("         ELSE Round(Nvl(sc.importe - Nvl(SUM(sp.importe), 0), 0), 2)  ");
        query.append("       END as number)                    remaining,  ");
        query.append("       sc.iduser,  ");
        query.append("       sc.descripcion,  ");
        query.append("       ss.descripcion          subservicio  ");
        query.append("FROM   m4csubservicios ss  ");
        query.append("       inner join m4cservicioscontratados sc  ");
        query.append("               ON ss.idsubservicio = sc.idsubservicio  ");
        query.append("       left join m4cserviciospagados sp  ");
        query.append("              ON sc.idbooking = sp.idbooking  ");
        query.append("                 AND sc.idservcontratado = sp.idservcontratado  ");
        query.append("WHERE  sc.idbooking = :IDBOOKING  ");
        query.append("       AND sc.idreservacion IS NULL  ");

        if(excludeIdServices!=null && excludeIdServices.size()>0) {
            query.append("AND sc.idservcontratado not in( "+StringUtils.join(excludeIdServices,",")+") ");
        }

        query.append("GROUP  BY sc.idservcontratado,  ");
        query.append("          sc.descripcion,  ");
        query.append("          ss.descripcion,  ");
        query.append("          sc.iduser,  ");
        query.append("          ss.adultos,  ");
        query.append("          sc.importe  ");
        query.append(")a where a.remaining > 0");
//endregion

        nativeQuery=currentSession().createSQLQuery(query.toString());
        nativeQuery.setParameter("IDBOOKING",idBooking);
//region Scalar
        nativeQuery.addScalar("IDSERVCONTRATADO", LongType.INSTANCE);
        nativeQuery.addScalar("ADULTOS", IntegerType.INSTANCE);
        nativeQuery.addScalar("IMPORTE", DoubleType.INSTANCE);
        nativeQuery.addScalar("PAGADO", DoubleType.INSTANCE);
        nativeQuery.addScalar("REMAINING", DoubleType.INSTANCE);
        nativeQuery.addScalar("IDUSER", StringType.INSTANCE);
        nativeQuery.addScalar("DESCRIPCION", StringType.INSTANCE);
        nativeQuery.addScalar("SUBSERVICIO", StringType.INSTANCE);
//endregion
        List<Object[]> resultLst=nativeQuery.list();

        for(Object[] row:resultLst){
            paymentServiceResponseDTOLst.add(
                    new PaymentServiceResponseDTO(
                            idBooking, (Long) row[0], (Integer) row[1],
                            (Double) row[2], (Double) row[3], (Double) row[4],
                            (String) row[5], (String) row[6], (String) row[7],
                            null));

        }
        return paymentServiceResponseDTOLst;

    }
    public List<PaymentServiceResponseDTO> getProvidedServices(Long idBooking){

        StringBuilder query=new StringBuilder();
        SQLQuery nativeQuery;
        List<PaymentServiceResponseDTO>paymentServiceResponseDTOLst=new ArrayList<PaymentServiceResponseDTO>();
//region Query :: Obtiene los Servicios con adeudo

        query.append("SELECT sc.idservcontratado,  ");
        query.append("       ss.adultos,  ");
        query.append("       sc.importe,  ");
        query.append("       cast(Nvl(SUM(sp.importe), 0 )as number ) pagado,  ");
        query.append("       cast(CASE  ");
        query.append("         WHEN Nvl(sc.importe - Nvl(SUM(sp.importe), 0), 0) <= 0 THEN 0  ");
        query.append("         ELSE Round(Nvl(sc.importe - Nvl(SUM(sp.importe), 0), 0), 2)  ");
        query.append("       END as number)                    remaining,  ");
        query.append("       sc.iduser,  ");
        query.append("       sc.descripcion,  ");
        query.append("       ss.subservicio          subservicio  ");
        query.append("FROM   m4csubservicios ss  ");
        query.append("       inner join m4cservicioscontratados sc  ");
        query.append("               ON ss.idsubservicio = sc.idsubservicio  ");
        query.append("       left join m4cserviciospagados sp  ");
        query.append("              ON sc.idbooking = sp.idbooking  ");
        query.append("                 AND sc.idservcontratado = sp.idservcontratado  ");
        query.append("WHERE  sc.idbooking = :IDBOOKING  ");
        query.append("       AND sc.idreservacion IS NULL  ");



        query.append("GROUP  BY sc.idservcontratado,  ");
        query.append("          sc.descripcion,  ");
        query.append("          ss.subservicio,  ");
        query.append("          sc.iduser,  ");
        query.append("          ss.adultos,  ");
        query.append("          sc.importe  ");
//endregion

        nativeQuery=currentSession().createSQLQuery(query.toString());
        nativeQuery.setParameter("IDBOOKING",idBooking);
//region Scalar
        nativeQuery.addScalar("IDSERVCONTRATADO", LongType.INSTANCE);
        nativeQuery.addScalar("ADULTOS", IntegerType.INSTANCE);
        nativeQuery.addScalar("IMPORTE", DoubleType.INSTANCE);
        nativeQuery.addScalar("PAGADO", DoubleType.INSTANCE);
        nativeQuery.addScalar("REMAINING", DoubleType.INSTANCE);
        nativeQuery.addScalar("IDUSER", StringType.INSTANCE);
        nativeQuery.addScalar("DESCRIPCION", StringType.INSTANCE);
        nativeQuery.addScalar("SUBSERVICIO", StringType.INSTANCE);
//endregion
        List<Object[]> resultLst=nativeQuery.list();

        for(Object[] row:resultLst){
            paymentServiceResponseDTOLst.add(
                    new PaymentServiceResponseDTO(
                            idBooking, (Long) row[0], (Integer) row[1],
                            (Double) row[2], (Double) row[3], (Double) row[4],
                            (String) row[5], (String) row[6], (String) row[7],
                            null));

        }
        return paymentServiceResponseDTOLst;

    }
    @Override
    public Payment saveOrUpdate(Payment entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }
}
