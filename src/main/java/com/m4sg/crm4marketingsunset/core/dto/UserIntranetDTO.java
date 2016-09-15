package com.m4sg.crm4marketingsunset.core.dto;

import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by desarrollo1 on 30/03/2015.
 */
public class UserIntranetDTO extends ValidateConstraint {
    private final String id;
    @NotNull
    private final String user;
    @NotNull
    private final String password;
    private final String name;

    private final Integer accessLevel;
    public UserIntranetDTO() {
        this.id=null;
        this.password=null;
        this.name=null;
        this.accessLevel=null;
        this.user=null;
    }

    public String getId() {
        return id;
    }
    public String getUser() {
        return user;
    }

    public String getPassword(){ return password; }

    public String getName(){ return name;}
    public Integer getAccessLevel(){return accessLevel;}

    @Override
    public List<ErrorRepresentation> validate() {
        return null;
    }
}
