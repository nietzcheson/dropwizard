package com.m4sg.crm4marketingsunset.services;

import com.m4c.model.entity.Customer;
import com.m4c.model.entity.Features;
import com.m4sg.crm4marketingsunset.dao.CustomerFeaturesDAO;

/**
 * Created by Fernando on 15/04/2015.
 */
public class CustomerFeaturesService extends GenericDAOService {

    public CustomerFeaturesService() {
        super(CustomerFeaturesDAO.class);
    }

    public boolean delete(Customer customer,Features features){
        return customerFeaturesDAO.delete(customer,features);
    }
}
