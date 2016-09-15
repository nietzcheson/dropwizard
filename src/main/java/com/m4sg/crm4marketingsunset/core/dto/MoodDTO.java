package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by Usuario on 03/09/2015.
 */
public class MoodDTO extends CommonDTO {
    public String code;


    public MoodDTO(Long id, String name,String code) {
        super(id, name);
        this.code=code;
    }
}
