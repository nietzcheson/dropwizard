package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.AssistCard;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Fernando on 06/10/2015.
 */
public class AssistCardDAO  extends GenericDAO<AssistCard>  {
    public AssistCardDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<AssistCard> findAll() {
        return null;
    }

    @Override
    public Optional<AssistCard> find(long id) {
        return null;
    }


    public Optional<AssistCard> findBySubservice(long id) {

        return Optional.fromNullable((AssistCard)
                criteria().
                        add(Restrictions.
                        eq("idSubService",id)).
                        uniqueResult());
    }
    @Override
    public AssistCard saveOrUpdate(AssistCard entity) {
        return null;
    }
}
