package com.m4sg.crm4marketingsunset.dao;

import com.google.common.base.Optional;
import com.m4c.model.entity.User;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Usuario on 20/07/2015.
 */
public class UserDAO extends GenericDAO<User>  {


    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }



    @Override
    public List<User> findAll() {
        return null;
    }

    public List<User> findAllGroup(String  group_username, String group,List<String> ignore_username) {
        //Criteria criteria=this.currentSession().createCriteria(User.class);
        /*criteria.add(Restrictions.eq("active", 1) );
        if(!StringUtils.isEmpty(group_username)){

            User user=findByUser(group_username.toUpperCase());
            if(user!=null){
                criteria.add(Restrictions.eq("group", user.getGroup().toUpperCase()) );
            }else{
                criteria.add(Restrictions.eq("group", "") );
            }

        }else if(!StringUtils.isEmpty(group)){
            criteria.add(Restrictions.eq("group", group.toUpperCase()));
        }
        if (ignore_username.size()>0){
            criteria.add(Restrictions.not(Restrictions.in("id",ignore_username)));
        }

        criteria.addOrder(Order.asc("id"));*/

        return this.currentSession().createCriteria(User.class).list();
    }

    @Override
    public Optional<User> find(long id) {
        return Optional.fromNullable(get(id));
    }
    public Optional<User> findUser(String id) {
        return Optional.fromNullable(get(id));
    }

    @Override
    public User saveOrUpdate(User entity) {
        return null;
    }
    public User findByUser(String user){
        Criteria criteria=this.currentSession().createCriteria(User.class);


            criteria.add(Restrictions.eq("id", user) );

        return (User) criteria.uniqueResult();
    }
}
