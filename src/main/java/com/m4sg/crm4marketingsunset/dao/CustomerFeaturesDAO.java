package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.Customer;
import com.m4c.model.entity.CustomerFeatures;
import com.m4c.model.entity.Features;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Fernando on 15/04/2015.
 */
public class CustomerFeaturesDAO extends GenericDAO<CustomerFeatures> {

    public CustomerFeaturesDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    @Override
    public List<CustomerFeatures> findAll() {
        return null;
    }

    @Override
    public Optional<CustomerFeatures> find(long id) {
        return null;
    }

    @Override
    public CustomerFeatures saveOrUpdate(CustomerFeatures entity) {
        return null;
    }




    public boolean delete(Customer customer,Features features){

        StringBuilder query=new StringBuilder();
        query.append("delete from CustomerFeatures cf where ");
        query.append("cf.customerFeaturesId.customer=:CUSTOMER ");
        query.append("and cf.customerFeaturesId.features=:FEATURE ");

        Query q=currentSession().createQuery(query.toString());
        q.setParameter("CUSTOMER",customer);
        q.setParameter("FEATURE",features);

        return q.executeUpdate()>0?true:false ;

    }
}
