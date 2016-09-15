package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.FoliosCertificado;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by sistemas on 12/01/2015.
 */
public class FoliosCertificateDAO extends GenericDAO<FoliosCertificado> {
    public FoliosCertificateDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    public List<FoliosCertificado> findAll() {
        return null;
    }

    @Override
    public Optional<FoliosCertificado> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public FoliosCertificado saveOrUpdate(FoliosCertificado entity) {
        this.currentSession().persist(entity);
        return entity;
    }
    public PaginationDTO<FoliosCertificado> getSeriesCertificate(Campaign campaign,int sizePage,int page) {

        Criteria criteria=currentSession().createCriteria(FoliosCertificado.class);
        criteria.add(Restrictions.eq("campaign", campaign));
        criteria.addOrder(Order.desc("fechaCreacion"));

        return pagination(criteria,sizePage,page);

    }

}
