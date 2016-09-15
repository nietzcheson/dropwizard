package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Hotel;
import com.m4c.model.entity.HotelImage;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Juan on 10/03/2015.
 */
public class HotelImageDAO extends GenericDAO<HotelImage> {
    public HotelImageDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<HotelImage> findAll() {
        return null;
    }

    @Override
    public Optional<HotelImage> find(long id) {
        return null;
    }
    public Optional<HotelImage> find(Hotel hotel,String tokenImage){

        Criteria criteria=criteria().add(Restrictions.and(Restrictions.eq("hotel", hotel), Restrictions.eq("token", tokenImage)));

        return Optional.fromNullable((HotelImage) criteria.uniqueResult());

    }

    @Override
    public HotelImage saveOrUpdate(HotelImage entity) {
        return null;
    }

    public Optional<HotelImage> find(String name) {
        String hql = "from HotelImage where name = :name";
        Query query = currentSession().createQuery(hql);
        query.setString("name", name);
        query.setMaxResults(1);
        HotelImage hotelImage = uniqueResult(query);
        return Optional.fromNullable(hotelImage);
    }

    public void deleteHotelImage(HotelImage hotelImage) {
        currentSession().delete(hotelImage);
    }
}
