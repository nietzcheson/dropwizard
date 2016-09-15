package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Features;
import com.m4sg.crm4marketingsunset.dao.FeatureDAO;

import java.util.List;

/*import com.sun.org.apache.xalan.internal.utils.FeatureManager;*/

/**
 * Created by desarrollo1 on 07/04/2015.
 */
public class FeatureService extends GenericDAOService {
    public FeatureService() {
        super(FeatureDAO.class);
    }

    public Optional<Features> find(Long id)
    {
        return featureDAO.find(id);
    }
    public Optional<Features> getFeature(Long id){
        return featureDAO.find(id);
    }
    public List<Features> listFeatures(){
        return featureDAO.findAll();
    }
}
