package com.m4sg.crm4marketingsunset.core.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by desarrollo1 on 16/02/2016.
 */
public class DepartmentDTO {

    private Long id;
    @Size(min = 5)
    private final String deparment;

    @Size(min = 5)
    private String clave;

    @Size(min = 5)
    private String contexto;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDeparment() {
        return deparment;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public DepartmentDTO(){
        id=null;
        deparment=null;
        clave= null;
        contexto=null;
    }
}
