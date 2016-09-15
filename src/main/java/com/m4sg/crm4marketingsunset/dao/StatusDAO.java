package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Status;
import com.m4sg.crm4marketingsunset.core.dto.MoodDTO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by desarrollo1 on 08/05/2015.
 */
public class StatusDAO  extends GenericDAO<Status> {

    public StatusDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Status> findAll() {
        return null;
    }
    public List<MoodDTO> list() {

        StringBuilder hql=new StringBuilder();
        Query q;
        hql.append("select new com.m4sg.crm4marketingsunset.core.dto.MoodDTO(s.id,s.statusEng,s.keyword) from Status s order by s.statusEng ");
        q=currentSession().createQuery(hql.toString());
        return q.list();
    }

    @Override
    public Optional<Status> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public Status saveOrUpdate(Status entity) {
        return  null;
    }
}

