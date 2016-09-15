package com.m4sg.crm4marketingsunset.core.dto;

import org.hibernate.validator.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


/**
 * Created by Fernando on 20/07/2015.
 */
public class AuditDTO {

    @NotBlank
    @Length(min = 2, max = 255)
    private final String username;
    @NotNull
    private final Long idBooking;
    @NotNull
    @javax.validation.constraints.Pattern(regexp="^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]).){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$",message = " invalid" )
    private final String ip;
    @NotBlank
    @Length(min = 2, max = 255)
    private final String modulo;
    @NotBlank
    @Length(min = 2, max = 255)
    private final String ventana;
    @NotBlank
    @Length(min = 2, max = 20)
    private final String accion;
    @NotBlank
    @Length(min = 2, max = 255)
    private final String detalle;


    public AuditDTO() {
        this.username = null;
        this.idBooking = null;
        this.ip = null;
        this.modulo = null;
        this.ventana = null;
        this.accion = null;
        this.detalle = null;
    }


    public String getUsername() {
        return username;

    }

    public Long getIdBooking() {
        return idBooking;
    }

    public String getIp() {
        return ip;
    }

    public String getModulo() {
        return modulo;
    }

    public String getVentana() {
        return ventana;
    }

    public String getAccion() {
        return accion;
    }

    public String getDetalle() {
        return detalle;
    }
}
