package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.User;
import com.m4c.model.entity.sistemas.UserIntranet;
import com.m4sg.crm4marketingsunset.core.UserSecuritySunsetDTO;
import com.m4sg.crm4marketingsunset.core.dto.UserIntranetDTO;
import com.m4sg.crm4marketingsunset.dao.UserDAO;
import com.m4sg.crm4marketingsunset.dao.UserIntranetDAO;
import com.m4sg.crm4marketingsunset.util.Constants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalToIgnoringCase;

/**
 * Created by desarrollo1 on 30/03/2015.
 */
public class UserIntranetService extends GenericDAOService {
    public UserIntranetService() {
        super(UserIntranetDAO.class, UserDAO.class);
    }

    public Optional<UserIntranet> findByToken(String token)
    {
        return userIntranetDAO.findByToken(token);
    }
    public Optional<UserIntranet> find(String userName)
    {
        return userIntranetDAO.find(userName);
    }
    public Optional<UserIntranet> find(long id)
    {
        return userIntranetDAO.find(id);
    }
    public Optional<User> findUser(String id)
    {
        return userDAO.findUser(id);
    }

    public UserIntranet setParams(UserIntranetDTO userDTO,UserIntranet userIntranetIntranet){

        if(userDTO.getAccessLevel()!=null && userDTO.getAccessLevel() >0){
            userIntranetIntranet.setAccessLevel(userDTO.getAccessLevel());
        }
        if(userDTO.getName()!=null && !userDTO.getName().isEmpty()){
            userIntranetIntranet.setName(userDTO.getName());
        }
        if(userDTO.getPassword()!=null && !userDTO.getPassword().isEmpty()){
            userIntranetIntranet.setPassword(userDTO.getPassword());
        }

        return userIntranetIntranet;
    }
    public List<UserIntranet> findAll(){
        return userIntranetDAO.findAll();
    }



    public UserSecuritySunsetDTO login(String userName,String password){

        Client client = Client.create();
        WebResource invokeLogin = client.resource(Constants.IP_SECURITY_SUNSET.concat(Constants.PATH_SECURITY_SUNSET_LOGIN));


        MultivaluedMap<String,String> queryParams = new MultivaluedMapImpl();

        queryParams.add("usuarioid", userName);
        queryParams.add("numerohotel", "30");
        queryParams.add("password", password);

        UserSecuritySunsetDTO userSecuritySunsetDTO=invokeLogin.post(UserSecuritySunsetDTO.class, queryParams);
        if(userSecuritySunsetDTO.ERROR==null) {
            //Obtener extensión
            Optional<User> user =findUser(userName);
            if(user.isPresent()){
                userSecuritySunsetDTO.setUserIntranet(true);
                userSecuritySunsetDTO.setExtension(user.get().getExtension());
                userSecuritySunsetDTO.setDeptoid(String.valueOf(user.get().getIddepartment()));
                userSecuritySunsetDTO.setAcceso(user.get().getAccess());
                userSecuritySunsetDTO.setGrupo(user.get().getGroup());
            }else
                userSecuritySunsetDTO.setUserIntranet(false);
            userSecuritySunsetDTO.setRoleLst(getPermissionLst(userName));
        }

        return userSecuritySunsetDTO;
    }

    public List<String> getPermissionLst(String userName){

        MultivaluedMap<String,String> queryParams = new MultivaluedMapImpl();
        queryParams.add("usuarioid", userName);
        queryParams.add("numerohotel", "30");
        queryParams.add("nombrearea", "CrmM4SG");

        Client client = Client.create();
        WebResource invokeRoles = client.resource(Constants.IP_SECURITY_SUNSET.concat(Constants.PATH_SECURITY_SUNSET_ROLES));

        Map roles = invokeRoles.post(Map.class, queryParams);

        return  new ArrayList(roles.keySet());
    }

    public boolean hasPermission(String permission,String userName){

        List<String> permissionLst=getPermissionLst(userName);

        return !select(permissionLst,having(on(String.class),equalToIgnoringCase(permission))).isEmpty();
    }



}
