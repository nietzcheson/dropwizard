package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.sistemas.UserIntranet;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.util.Constants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by desarrollo1 on 30/03/2015.
 */
public class UserIntranetDAO extends GenericDAO<UserIntranet> {
    public UserIntranetDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<UserIntranet> findAll() {
        return currentSession().createCriteria(UserIntranet.class).addOrder(Order.desc("createDate")).list();
    }

    @Override
    public Optional<UserIntranet> find(long id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public UserIntranet saveOrUpdate(UserIntranet entity) {
        this.currentSession().saveOrUpdate(entity);
        return entity;
    }
    public Optional<UserIntranet> find(String userName){
        /*Criteria criteria=this.currentSession().
                createCriteria(UserIntranet.class).
                add(Restrictions.ilike("username", userName));
        */
        return Optional.fromNullable(get(userName.toUpperCase()));
    }

    public Optional<UserIntranet> findByToken(String token) {
        Criteria criteria=this.currentSession().
                createCriteria(UserIntranet.class).
                add(Restrictions.eq("token", token));
        return Optional.fromNullable((UserIntranet)criteria.uniqueResult());
    }

    public Optional<UserIntranet> login(String userName,String password){
//        StringBuilder hql=new StringBuilder();
//
//        hql.append("select u from UserIntranet u where u.username like :name and ");
//        hql.append("u.password like :password");
//        Query query=currentSession().createQuery(hql.toString());
//
//        query.setParameter("name",userName);
//        query.setParameter("password",password);
//        Optional <UserIntranet> user= Optional.fromNullable((UserIntranet)query.uniqueResult());
        Criteria criteria=this.currentSession().
                createCriteria(UserIntranet.class).
                add(Restrictions.ilike("username", userName)).
                add(Restrictions.eq("password",password));
        return Optional.fromNullable((UserIntranet)criteria.uniqueResult());
//        Criteria criteria=criteria().
//                add(Restrictions.ilike("username", userName)).
//                add(Restrictions.ilike("password",password));
        //return user;
    }

    public List<UserIntranet> find(UserIntranet perfilUserIntranet)
    {
        Criteria criteria=this.currentSession().
                createCriteria(UserIntranet.class).
                add(Restrictions.eq("perfilUserIntranet", perfilUserIntranet)).addOrder(Order.desc("createDate"));
        return  criteria.list();
    }

    public PaginationDTO<UserIntranet> findAll(int sizePage,int page) {
        Criteria criteria;
        criteria=criteria().addOrder(Order.asc("userName"));
        return pagination(criteria,sizePage,page);
    }
    public PaginationDTO<UserIntranet> findAll(String orderBy,String dir,int sizePage,int page) {
        Criteria criteria=criteria();

        if(dir.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()) {
            criteria.addOrder(Order.asc(orderBy));
        }else if(dir.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            criteria.addOrder(Order.desc(orderBy));
        }
        return pagination(criteria,sizePage,page);

    }
    public PaginationDTO<UserIntranet> globalSearch(String search,String orderBy,String direction,int sizePage,int page){
        StringBuilder hql=new StringBuilder();
        hql.append("select u from UserCRM u where u.name like :name or ");
        hql.append("u.userName like : userName or ");
        hql.append("u.email like :email or  ");
        hql.append("u.perfilUser.name like :perfil");
        Query query=currentSession().createQuery(hql.toString());
        query.setParameter("name",search);
        query.setParameter("userName",search);
        query.setParameter("email",search);
        query.setParameter("perfil",search);
        if(direction.equalsIgnoreCase(Constants.ORDER_ASC) && !orderBy.isEmpty()){
            hql.append("order by u."+orderBy +" "+Constants.ORDER_ASC);
        }else if(direction.equalsIgnoreCase(Constants.ORDER_DESC) && !orderBy.isEmpty()){
            hql.append("order by u." + orderBy + " " + Constants.ORDER_DESC);
        }
        return pagination(query,sizePage,page);
    }
}
