package com.m4sg.crm4marketingsunset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 04/12/2015.
 */
public class SimplePrincipal {
    private String username;
    private Boolean sendEmail;
    private List<String> roles = new ArrayList<String>();

    public SimplePrincipal(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean isUserInRole(String roleToCheck) {
        return roles.contains(roleToCheck);
    }

    public String getUsername() {
        return username;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }
}

