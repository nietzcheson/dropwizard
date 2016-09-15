package com.m4sg.crm4marketingsunset.services;

import com.m4c.model.entity.User;
import com.m4sg.crm4marketingsunset.dao.UserDAO;

import java.util.List;

/**
 * Created by Usuario on 20/07/2015.
 */
public class UserService   extends GenericDAOService {
    public UserService() {
        super(UserDAO.class);
    }

    public List<User> listTitles(String  group_username, String group,List<String> ignore_username){
        return userDAO.findAllGroup(  group_username,  group, ignore_username);
    }

}
