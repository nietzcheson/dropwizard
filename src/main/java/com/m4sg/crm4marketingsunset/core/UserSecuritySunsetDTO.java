package com.m4sg.crm4marketingsunset.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

/**
 * Created by Fernando on 20/07/2015.
 */
public class UserSecuritySunsetDTO {
    private String depto;
//    @JsonIgnore
    private String deptoid;
    private Boolean userIntranet;
    /* Depto de base de datos de Marketing */
    private String email;
    private String nombre;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String publico;
    @JsonIgnore
    private String puesto;
    private String telefono;
    @JsonIgnore
    private String telefono2;
    @JsonIgnore
    private String tipousuario;
    private String usuario;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String ERROR;
    @JsonProperty("permisos")
    private List<String> roleLst;
    private String extension;
    private String acceso;
    private String grupo;

    public Boolean getUserIntranet() {
        return userIntranet;
    }

    public void setUserIntranet(Boolean userIntranet) {
        this.userIntranet = userIntranet;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getDeptoid() {
        return deptoid;
    }

    public void setDeptoid(String deptoid) {
        this.deptoid = deptoid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPublico() {
        return publico;
    }

    public void setPublico(String publico) {
        this.publico = publico;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getTipousuario() {
        return tipousuario;
    }

    public void setTipousuario(String tipousuario) {
        this.tipousuario = tipousuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<String> getRoleLst() {
        return roleLst;
    }

    public void setRoleLst(List<String> roleLst) {
        this.roleLst = roleLst;
    }
}



