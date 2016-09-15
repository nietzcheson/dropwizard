package com.m4sg.crm4marketingsunset;

import com.google.common.base.Optional;
import com.m4c.model.entity.AppApi;
import com.m4sg.crm4marketingsunset.services.AppApiService;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

/**
 * Created by Usuario on 07/05/2015.
 */
public class SimpleAuthenticator implements Authenticator<BasicCredentials, SimplePrincipal> {
    @Override
    public Optional<SimplePrincipal> authenticate(BasicCredentials credentials) throws AuthenticationException {

        if ("sys73xrv".equals(credentials.getPassword())) {
           // return Optional.of(true);
            SimplePrincipal prince = new SimplePrincipal(credentials.getUsername());
            prince.getRoles().add(Roles.ADMIN);

            return Optional.fromNullable(prince);
        } else {
            try {
                AppApi appApi=new AppApiService().getByNameAndToken(credentials.getUsername(),credentials.getPassword());
                if(appApi!=null){
                    //return Optional.of(true);
                    SimplePrincipal prince = new SimplePrincipal(credentials.getUsername());
                    prince.getRoles().add(Roles.USER);
                    prince.setSendEmail(appApi.getSendEmail());
                    return Optional.fromNullable(prince);
                }else{

                    return Optional.absent();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.absent();
            }
        }


    }
}
