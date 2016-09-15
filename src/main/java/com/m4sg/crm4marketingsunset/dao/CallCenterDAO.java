package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.CallCenter;
import com.m4c.model.entity.Segment;
import com.m4sg.crm4marketingsunset.core.dto.CallCenterDTO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import javax.ws.rs.DefaultValue;
import java.util.List;

/**
 * Created by sistemas on 06/01/2015.
 */
public class CallCenterDAO extends GenericDAO<CallCenter> {

    public CallCenterDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    public List<CallCenter> findAll() {
        return null;
    }
    @Override
    public Optional<CallCenter> find(long id){
        System.out.println("buscando callcenter===>"+id);

        return Optional.fromNullable(get(id));

        }

    @Override
    public CallCenter saveOrUpdate(CallCenter entity) {
        return null;
    }

    public List<CallCenter> findByName(String name,String name2){
        Criteria criteria=this.currentSession().createCriteria(CallCenter.class);
        Long[] callCentersIds = { 201l, 1541l,5962l,361l };
        Criterion cond1=Restrictions.in("id",callCentersIds);
        criteria.add(cond1);
        System.out.println("criteria list "+criteria.list().size());
        return criteria.list();
    }

    public List<CallCenterDTO> findBySegment(Boolean isOTA){

        StringBuilder query = new StringBuilder();

        query.append("SELECT idcallcenter id, ");
        query.append("nombre name ");
        query.append("FROM M4CCALLCENTER callcenter ");

        if(isOTA == true){
            query.append("WHERE callcenter.segmento IN(4, 26) ");
        }

        query.append("ORDER BY name ASC ");

        SQLQuery sqlQuery = currentSession().createSQLQuery(query.toString());
        sqlQuery.addScalar("ID", LongType.INSTANCE);
        sqlQuery.addScalar("NAME", StringType.INSTANCE);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(CallCenterDTO.class));

        List<CallCenterDTO> resultado = sqlQuery.list();

        return resultado;

    }

}
