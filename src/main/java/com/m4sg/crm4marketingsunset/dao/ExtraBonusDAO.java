package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.ExtraBonus;
import com.m4c.model.entity.ExtraBonusId;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Usuario on 17/08/2015.
 */

public class ExtraBonusDAO extends GenericDAO<ExtraBonus> {
    public ExtraBonusDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<ExtraBonus> findAll() {

        return null;
    }
    public List<CommonDTO> findAllByCallcenter(List<Long> callCenterLst) {
        List<CommonDTO>campaigns;
        if(!callCenterLst.isEmpty()){
            Query query=currentSession().createQuery("select new com.m4sg.crm4marketingsunset.core.dto.CommonDTO(c.extraBonusId.id,c.nameEng) from ExtraBonus c where c.extraBonusId.idCallCenter in (:callCenters) "+" order by nameEng");
            query.setParameterList("callCenters",callCenterLst);

            campaigns=query.list();
        }else{
            Query query=currentSession().createQuery("select new com.m4sg.crm4marketingsunset.core.dto.CommonDTO(c.extraBonusId.id,c.nameEng) from ExtraBonus c order by nameEng");

            campaigns=query.list();
        }
        return campaigns;

    }



    @Override
    public Optional<ExtraBonus> find(long id) {
        return null;
    }

    public Optional<ExtraBonus> find(long id,long idCallcenter) {
        ExtraBonusId extraBonusId=new ExtraBonusId(id,idCallcenter);

        return  Optional.fromNullable((ExtraBonus)criteria().add(Restrictions.eq("id",extraBonusId)).uniqueResult());

    }

    @Override
    public ExtraBonus saveOrUpdate(ExtraBonus entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }
}
