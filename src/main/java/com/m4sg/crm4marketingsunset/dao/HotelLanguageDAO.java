package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Hotel;
import com.m4c.model.entity.HotelLanguage;
import com.m4c.model.entity.Language;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Juan on 10/03/2015.
 */
public class HotelLanguageDAO extends GenericDAO<HotelLanguage> {
    public HotelLanguageDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<HotelLanguage> findAll() {
        return null;
    }

    @Override
    public Optional<HotelLanguage> find(long id) {
        return null;
    }

    @Override
    public HotelLanguage saveOrUpdate(HotelLanguage hotelLanguage) {
        return persist(hotelLanguage);
    }

    public Optional<HotelLanguage> find(Language language) {
        String hql = "from HotelLanguage where language = :language";
        Query query = currentSession().createQuery(hql);
        query.setEntity("language", language);
        query.setMaxResults(1);
        HotelLanguage hotelLanguage = uniqueResult(query);
        return Optional.fromNullable(hotelLanguage);
    }

    public void deleteHotelLanguage(HotelLanguage hotelLanguage) {
        currentSession().delete(hotelLanguage);
    }

    public void deleteHotelLanguages(Hotel hotel) {
        Query query = currentSession().createQuery("delete from HotelLanguage where hotel = :hotel ");
        query.setParameter("hotel", hotel);
        query.executeUpdate();
        currentSession().flush();
        currentSession().clear();
        hotel.getTranslations().clear();
    }
}
