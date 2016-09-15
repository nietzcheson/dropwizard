package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by Fernando on 11/08/2015.
 */
public class CommonDTO {

    private String id;
    private String name;

    public CommonDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public CommonDTO(Long id, String name) {
        this.id = id.toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
