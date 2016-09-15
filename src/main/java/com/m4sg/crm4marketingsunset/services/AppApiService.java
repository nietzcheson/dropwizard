package com.m4sg.crm4marketingsunset.services;

import com.m4c.model.entity.AppApi;
import com.m4sg.crm4marketingsunset.dao.AppApiDAO;

import java.util.List;

/**
 * Created by Usuario on 03/12/2015.
 */
public class AppApiService extends GenericDAOService {

    public AppApiService() {
        super(AppApiDAO.class);
    }
    public AppApi createAppApi(String name){
        AppApi appApi=new AppApi();
        appApi.name=name;
        return appApiDAO.saveOrUpdate(appApi);
    }
    public AppApi getByNameAndToken(String name, String token) throws Exception {
        AppApi appApi=appApiDAO.getByNameAndToken(name,token);

        if(appApi!=null){
            String app_token = appApi.generatePublicToken();
            //Verificamos que el token de la aplicación sea igual al parametro token
            if (app_token.equals(token)) {
                return appApi;
            }
        }
        return null;
    }
    public List<AppApi> list(){
        return appApiDAO.findAll();
    }
}
