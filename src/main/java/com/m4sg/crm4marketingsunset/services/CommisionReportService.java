package com.m4sg.crm4marketingsunset.services;

import com.m4sg.crm4marketingsunset.core.CommisionReportBean;
import com.m4sg.crm4marketingsunset.dao.CommisionReportDAO;

import java.util.Date;
import java.util.List;

/**
 * Created by Fernando on 12/09/2016.
 */
public class CommisionReportService  extends GenericDAOService   {


    public CommisionReportService() {
        super(CommisionReportDAO.class);
    }

    public List<CommisionReportBean> commisionReportCobranza(Date beginning, Date ending, String username){
        return commisionReportDAO.reportCobranza(beginning,ending,username);
    }
}
