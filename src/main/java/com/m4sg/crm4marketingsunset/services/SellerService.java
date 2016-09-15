package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.m4c.model.entity.Sellers;
import com.m4sg.crm4marketingsunset.core.dto.CommonDTO;
import com.m4sg.crm4marketingsunset.dao.SellerDAO;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Fernando on 12/08/2015.
 */
public class SellerService extends GenericDAOService{

    public SellerService() {
        super(SellerDAO.class);
    }

    public List<Sellers> findAll(List<Long> callCenterLst,List<String>typeLst){

    return sellerDAO.findAll(callCenterLst,typeLst);
    }

    public List<CommonDTO> findAllTransformCommon(List<Long> callCenterLst,List<String>typeLst){

        List<Sellers> sellerLst=findAll(callCenterLst,typeLst);

        return   Lists.transform(sellerLst, new Function<Sellers, CommonDTO>() {
            @Nullable
            @Override
            public CommonDTO apply(Sellers sellers) {
                return new CommonDTO(sellers.getId(),sellers.getName());
            }
        });
    }
}
