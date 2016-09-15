package com.m4sg.crm4marketingsunset.core;

import java.io.Serializable;

/**
 * Created by sistemas on 13/01/2015.
 */
public class ErrorRepresentation implements Serializable{
    private final String property;
    private final String message;

    public ErrorRepresentation(String property, String message) {
        this.property = property;
        this.message = message;
    }

    public String getProperty() {
        return property;
    }

    public String getMessage() {
        return message;
    }
}
