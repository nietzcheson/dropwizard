package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.NoteType;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by desarrollo1 on 08/05/2015.
 */
public class NoteTypeDAO extends GenericDAO<NoteType> {

    public NoteTypeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<NoteType> findAll() {
        return null;
    }
    public List<CommonDTO> findAll(List<Long>ignoreDeptLst) {

        StringBuilder hql=new StringBuilder();
        Query query;

        hql.append("select new com.m4sg.crm4marketingsunset.core.dto.CommonDTO(nt.id,nt.typeEng) from NoteType nt ");

        if(ignoreDeptLst!=null && !ignoreDeptLst.isEmpty()) {
            hql.append("where nt.depto not in(:ignore_depto) ");
        }

        hql.append("order by nt.typeEng");

        query=currentSession().createQuery(hql.toString());

        if(ignoreDeptLst!=null && !ignoreDeptLst.isEmpty()) {
            query.setParameterList("ignore_depto",ignoreDeptLst);
        }

        return query.list();
    }

    @Override
    public Optional<NoteType> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public NoteType saveOrUpdate(NoteType entity) {
        return  null;
    }
}