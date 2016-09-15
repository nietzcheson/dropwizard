package com.m4sg.crm4marketingsunset.core;

import java.util.Date;

/**
 * Created by Fernando on 12/09/2016.
 */
public class CommisionReportBean {

    private String DEPTO;
    private Date FECHA;
    private Integer IDBOOKING;
    private String CALLCENTER;
    private Long IDCALLCENTER;
    private String IDUSER;
    private Double IMPORTE;

    public String getDEPTO() {
        return DEPTO;
    }

    public void setDEPTO(String DEPTO) {
        this.DEPTO = DEPTO;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public Integer getIDBOOKING() {
        return IDBOOKING;
    }

    public void setIDBOOKING(Integer IDBOOKING) {
        this.IDBOOKING = IDBOOKING;
    }

    public String getCALLCENTER() {
        return CALLCENTER;
    }

    public void setCALLCENTER(String CALLCENTER) {
        this.CALLCENTER = CALLCENTER;
    }

    public Long getIDCALLCENTER() {
        return IDCALLCENTER;
    }

    public void setIDCALLCENTER(Long IDCALLCENTER) {
        this.IDCALLCENTER = IDCALLCENTER;
    }

    public String getIDUSER() {
        return IDUSER;
    }

    public void setIDUSER(String IDUSER) {
        this.IDUSER = IDUSER;
    }

    public Double getIMPORTE() {
        return IMPORTE;
    }

    public void setIMPORTE(Double IMPORTE) {
        this.IMPORTE = IMPORTE;
    }

}
