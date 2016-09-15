package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.AppApi;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

/**
 * Created by Usuario on 03/12/2015.
 */
public class AppApiDAO extends GenericDAO<AppApi> {
    public AppApiDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<AppApi> findAll() {
        Criteria criteria=this.currentSession().createCriteria(AppApi.class);

        criteria.add(Restrictions.eq("active",1));

        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    @Override
    public Optional<AppApi> find(long id) {
        return null;
    }

    @Override
    public AppApi saveOrUpdate(AppApi entity) {
       // currentSession().flush();
        currentSession().saveOrUpdate(entity);


        return entity;
    }

    public Long findMax(){
        Long max= (Long)criteria().setProjection(Projections.max("id")).uniqueResult();
        if(max==null)max=0l;
        return max+1;
    }
    /**
     * Obtiene la aplicacion dependiendo el nombre y el token
     * @param name
     * @param token
     * @return
     */
    public  AppApi getByNameAndToken(String name, String token) throws Exception {
        //Busca la aplicacion en base al nombre y que este activa.

        AppApi app = (AppApi) criteria().
                add(Restrictions.
                        eq("name", name)).
                add(Restrictions.
                        eq("active", 1)).
                        uniqueResult();
        if (app != null) {
            //Obtenemos el token de la aplicación
            String app_token = app.generatePublicToken();

            //Verificamos que el token de la aplicación sea igual al parametro token
            if (app_token.equals(token)) {
                return app;
            }
        }
        return null;
    }

}
