package com.m4sg.crm4marketingsunset.services;

import com.m4c.model.entity.CardType;
import com.m4sg.crm4marketingsunset.dao.CardTypeDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 21/07/2015.
 */
public class CardTypeService  extends GenericDAOService {
    public CardTypeService() {
        super(CardTypeDAO.class);
    }

    public List<CardType> list(){

        List<CardType> cardTypeList=new ArrayList<CardType>();

        cardTypeList=cardTypeDAO.findAll();
        return cardTypeList;

    }
}
