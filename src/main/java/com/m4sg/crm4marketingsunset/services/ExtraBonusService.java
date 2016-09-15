package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.ExtraBonus;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import com.m4sg.crm4marketingsunset.dao.ExtraBonusDAO;

import java.util.List;

/**
 * Created by Usuario on 17/08/2015.
 */
public class ExtraBonusService extends GenericDAOService  {
    public ExtraBonusService() {
        super(ExtraBonusDAO.class);
    }

    public List<CommonDTO> list(List<Long> callCenterLst){
        return extraBonusDAO.findAllByCallcenter(callCenterLst);
    }

    public Optional<ExtraBonus> extraBonus(Long id,Long idCallcenter){
        return extraBonusDAO.find(id,idCallcenter);
    }
}
