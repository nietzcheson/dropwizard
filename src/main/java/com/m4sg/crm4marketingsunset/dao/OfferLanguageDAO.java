package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Offer;
import com.m4c.model.entity.OfferLanguage;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Juan on 09/03/2015.
 */
public class OfferLanguageDAO extends GenericDAO<OfferLanguage> {

    public OfferLanguageDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<OfferLanguage> findAll() {
        return list(namedQuery("com.m4c.model.entity.OfferLanguage.findAll"));
    }

    public List<OfferLanguage> findAll(Offer offer) {
        String hql = "SELECT o FROM OfferLanguage o where o.offer like :offer";
        Query query = currentSession().createQuery(hql);
        query.setParameter("offer", offer);
        return list(query);
    }

    @Override
    public Optional<OfferLanguage> find(long id) {
        return null;
    }

    @Override
    public OfferLanguage saveOrUpdate(OfferLanguage offerLanguage) {
        return persist(offerLanguage);
    }

    public void deleteOfferLanguage(OfferLanguage offerLanguage) {
        currentSession().delete(offerLanguage);
    }

    public void deleteOfferLanguages(Offer offer) {
        Query query = currentSession().createQuery("delete from OfferLanguage where offer = :offer ");
        query.setParameter("offer", offer);
        query.executeUpdate();
        currentSession().flush();
        currentSession().clear();
        offer.getTranslations().clear();
    }
}
