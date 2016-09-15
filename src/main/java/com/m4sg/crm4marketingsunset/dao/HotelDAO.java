package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Destination;
import com.m4c.model.entity.Hotel;
import com.m4c.model.entity.Offer;
import com.m4c.model.entity.OfferDestination;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

/**
 * Created by Juan on 1/26/2015.
 */
public class HotelDAO extends GenericDAO<Hotel> {

    public HotelDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Hotel> findAll() {
        return list(namedQuery("com.m4c.model.entity.Hotel.findAll"));
    }

    @Override
    public Optional<Hotel> find(long id) {
        return Optional.fromNullable(get(id));
    }
//    public Optional<Hotel> findbyOffer(Array ids) {
//        return Optional.fromNullable(get(id));
//    }

    public List<Hotel> findByOffer(Offer offer) {
        StringBuilder list= new StringBuilder();
//        StringBuilder list= new StringBuilder();
        Object[] object=extract(offer.getDestinations(), on(OfferDestination.class).getDestination().getId()).toArray();
        List<Long> destinations= new ArrayList<Long>();
        for (Object result : object) {
            Long destinationId =(Long) result;
            destinations.add(destinationId);
            list.append(destinationId).append(",");
        }
        list.deleteCharAt(list.length()-1);

        StringBuilder hql=new StringBuilder();
        hql.append("select h from Hotel h where ");
        hql.append("h.destination.id in ("+list+")");
//        hql.append("h.destination.id in (:destinationLst)");
        Query query=this.currentSession().createQuery(hql.toString());
//        query.setParameter("destinationLst", destinations.toArray());
        return query.list();

        /*return criteria().add(
                                Restrictions.in("destination.id",
                                extract(offer.getDestinations(),
                                on(OfferDestination.class).getDestination().getId()))).
                        list();*/

    }

    @Override
    public Hotel saveOrUpdate(Hotel hotel) {
        return persist(hotel);
    }

    public Optional<Hotel> find(String name) {
        String hql = "from Hotel where name = :name";
        Query query = currentSession().createQuery(hql);
        query.setString("name", name);
        query.setMaxResults(1);
        Hotel hotel = uniqueResult(query);
        return Optional.fromNullable(hotel);
    }

    public List<Hotel> findAll(String name) {
        String hql = "from Hotel where name = :name";
        Query query = currentSession().createQuery(hql);
        query.setString("name", name);
        return list(query);
    }

    public List<Hotel>findByDestinationLst(List<Destination> destinationLst){

        return criteria().add(Restrictions.in("destination",destinationLst)).list();
    }
}
