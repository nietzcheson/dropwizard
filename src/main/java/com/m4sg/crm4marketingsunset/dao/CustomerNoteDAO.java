package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Customer;
import com.m4c.model.entity.CustomerNote;
import com.m4sg.crm4marketingsunset.core.dto.CustomerNoteDTO;
import com.m4sg.crm4marketingsunset.core.dto.CustomerNoteUserDTO;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.util.List;

/**
 * Created by desarrollo1 on 07/05/2015.
 */
public class CustomerNoteDAO extends GenericDAO<CustomerNote> {

    public CustomerNoteDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    @Override
    public List<CustomerNote> findAll() {
        return null;
    }
    @Override
    public Optional<CustomerNote> find(long id) {
        return Optional.fromNullable(get(id));
    }
    public int find(Customer customer) {
        int cont=1;
        Criteria criteria=criteria();
        criteria.add(Restrictions
                .eq("customer", customer))
                .addOrder(Order.desc("number"))
                .setMaxResults(1);
        if(criteria.list().size()>0){
            CustomerNote customernote=(CustomerNote)criteria.list().get(0);
            cont= customernote.getNumber()+1;
        }
        return cont;
    }

    public List<CustomerNote> findNotes(Customer customer) {

        Criteria criteria=criteria();
        criteria.add(Restrictions
                .eq("customer", customer))
                .addOrder(Order.desc("number"));

        return criteria.list();
    }
    public CustomerNote findNoteById(long idCustomer,int id) {

        Criteria criteria=criteria();
        criteria.add(Restrictions
                .eq("pk.customerId", idCustomer))
                .add(Restrictions
                        .eq("pk.noteId", id))
                .addOrder(Order.desc("number"));

        return (CustomerNote) criteria.setMaxResults(1).uniqueResult();
    }

    @Override
    public CustomerNote saveOrUpdate(CustomerNote entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }

    public List<CustomerNoteUserDTO> findNotesbyUser(String user) {
        StringBuilder hql=new StringBuilder();
        hql.append("Select IDCLIENTE, IDNOTA, NOTA, FECHA, IDUSER, " );
        hql.append("IDTIPONOTA, IDESTADO, FECHAPCONTACTO, NOTA_IDNOTA, ");
        hql.append("SPECIALREQ, IDCONCIERGE, IDREGALO ");
        hql.append("FROM M4CCLIENTENOTAS M where  FECHAPCONTACTO>=sysdate ");
        hql.append("AND 1=(case when (idnota in(select nota_idnota from M4CCLIENTENOTAS ");
        hql.append("where idcliente=M.idcliente)) then 0 else 1 end)");
        hql.append("and IDUSER='"+user+"'");//Buscamos el manifesto en la reservacion
        SQLQuery query=currentSession().createSQLQuery(hql.toString())
                .addScalar("IDUSER", StringType.INSTANCE)
                .addScalar("IDNOTA", LongType.INSTANCE)
                .addScalar("NOTA", StringType.INSTANCE)
                .addScalar("FECHA", DateType.INSTANCE)
                .addScalar("IDUSER", StringType.INSTANCE)
                .addScalar("IDTIPONOTA", LongType.INSTANCE)
                .addScalar("IDESTADO", LongType.INSTANCE)
                .addScalar("FECHAPCONTACTO", DateType.INSTANCE)
                .addScalar("NOTA_IDNOTA", LongType.INSTANCE)
                .addScalar("SPECIALREQ", StringType.INSTANCE)
                .addScalar("IDCONCIERGE", LongType.INSTANCE)
                .addScalar("IDREGALO", LongType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(CustomerNoteUserDTO.class));
        List<CustomerNoteUserDTO>lst=query.list();
//        List<CustomerNote> lst =  currentSession().createQuery(hql.toString()).list();
        return lst;
    }

//    public Optional<CustomerNote> getNextNumber(long idcliente) {
//        Criteria criteria=criteria();
//        criteria.add(Restrictions
//                .eq("customer", idcliente))
//                .addOrder(Order.asc("number"))
//                .uniqueResult();
//        return Optional.fromNullable((CustomerNote)criteria);
//    }

}
