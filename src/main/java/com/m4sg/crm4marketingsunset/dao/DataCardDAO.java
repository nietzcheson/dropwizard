package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.DataCard;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Usuario on 28/07/2015.
 */
public class DataCardDAO extends GenericDAO<DataCard>{

    public DataCardDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<DataCard> findAll() {
        return null;
    }

    @Override
    public Optional<DataCard> find(long id) {
        return null;
    }
    public Optional<DataCard> find(long idBooking,long idPago) {

        DataCard dataCard=(DataCard)criteria().add(Restrictions.eq("dataCardId.idBooking", String.valueOf(idBooking))).add(Restrictions.eq("dataCardId.payment", String.valueOf(idPago))).uniqueResult();

        return Optional.fromNullable(dataCard);
    }

    @Override
    public DataCard saveOrUpdate(DataCard entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }

    public void delete(long idBooking,long idPago) {

        Optional<DataCard>dataCardOptional=find(idBooking,idPago);

        if(dataCardOptional.isPresent()){
            currentSession().delete(dataCardOptional.get());
        }
    }



}
