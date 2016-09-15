package com.m4sg.crm4marketingsunset.core.dto;

import java.util.Date;

/**
 * Created by desarrollo1 on 25/02/2016.
 */
public class CustomerNoteUserDTO {
    private Long IDCLIENTE;
    private Long IDNOTA;
    private String NOTA;
    private Date FECHA;
    private String IDUSER;
    private Long IDTIPONOTA;
    private Long IDESTADO;
    private Date FECHAPCONTACTO;
    private Long NOTA_IDNOTA;
    private String SPECIALREQ;
    private Long IDCONCIERGE;
    private Long IDREGALO;

    public Long getIDCLIENTE() {
        return IDCLIENTE;
    }

    public void setIDCLIENTE(Long IDCLIENTE) {
        this.IDCLIENTE = IDCLIENTE;
    }

    public Long getIDNOTA() {
        return IDNOTA;
    }

    public void setIDNOTA(Long IDNOTA) {
        this.IDNOTA = IDNOTA;
    }

    public String getNOTA() {
        return NOTA;
    }

    public void setNOTA(String NOTA) {
        this.NOTA = NOTA;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public String getIDUSER() {
        return IDUSER;
    }

    public void setIDUSER(String IDUSER) {
        this.IDUSER = IDUSER;
    }

    public Long getIDTIPONOTA() {
        return IDTIPONOTA;
    }

    public void setIDTIPONOTA(Long IDTIPONOTA) {
        this.IDTIPONOTA = IDTIPONOTA;
    }

    public Long getIDESTADO() {
        return IDESTADO;
    }

    public void setIDESTADO(Long IDESTADO) {
        this.IDESTADO = IDESTADO;
    }

    public Date getFECHAPCONTACTO() {
        return FECHAPCONTACTO;
    }

    public void setFECHAPCONTACTO(Date FECHAPCONTACTO) {
        this.FECHAPCONTACTO = FECHAPCONTACTO;
    }

    public Long getNOTA_IDNOTA() {
        return NOTA_IDNOTA;
    }

    public void setNOTA_IDNOTA(Long NOTA_IDNOTA) {
        this.NOTA_IDNOTA = NOTA_IDNOTA;
    }

    public String getSPECIALREQ() {
        return SPECIALREQ;
    }

    public void setSPECIALREQ(String SPECIALREQ) {
        this.SPECIALREQ = SPECIALREQ;
    }

    public Long getIDCONCIERGE() {
        return IDCONCIERGE;
    }

    public void setIDCONCIERGE(Long IDCONCIERGE) {
        this.IDCONCIERGE = IDCONCIERGE;
    }

    public Long getIDREGALO() {
        return IDREGALO;
    }

    public void setIDREGALO(Long IDREGALO) {
        this.IDREGALO = IDREGALO;
    }
}
